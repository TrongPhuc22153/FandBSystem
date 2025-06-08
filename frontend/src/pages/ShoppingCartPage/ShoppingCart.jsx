import { useCallback, useState, useEffect } from "react";
import { useCart, useCartActions } from "../../hooks/cartHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { getImageSrc } from "../../utils/imageUtils";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { useModal } from "../../context/ModalContext";
import { useNavigate } from "react-router-dom";
import { CHECKOUT_URI } from "../../constants/routes";
import styles from "./ShoppingCart.module.css";
import { useAlert } from "../../context/AlertContext";
import { CHECKOUT_ITEMS } from "../../constants/webConstant";

const shippingCost = 0;

const ShoppingCart = () => {
  const {
    data: cartDataResult,
    isLoading: loadingCartItems,
    error: cartItemsError,
    mutate: mutateCart,
  } = useCart();

  const {
    handleUpdateCartItemQuantity,
    handleRemoveItemFromCart,
    handleClearCart,
    clearLoading,
    clearError,
  } = useCartActions();

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();
  const navigate = useNavigate();

  // State to manage selected items
  const [selectedItems, setSelectedItems] = useState([]);

  // State to manage "Select All" checkbox
  const [selectAll, setSelectAll] = useState(false);

  // Modal confirmations

  const handleRemoveConfirm = useCallback(
    async (idToRemove) => {
      if (idToRemove) {
        const success = await handleRemoveItemFromCart(idToRemove);
        if (success) {
          mutateCart(
            (prevData) => ({
              ...prevData,
              cartItems: prevData?.cartItems?.filter(
                (cartItem) => cartItem.product.productId !== idToRemove
              ),
            }),
            false
          );
          setSelectedItems((prevSelected) =>
            prevSelected.filter((id) => id !== idToRemove)
          );
        }
      }
    },
    [handleRemoveItemFromCart, mutateCart]
  );

  const showRemoveConfirmModal = useCallback(
    (productId) => {
      onOpen({
        title: "Remove Item",
        message: "Do you want to remove this item?",
        onYes: () => handleRemoveConfirm(productId),
      });
    },
    [onOpen, handleRemoveConfirm]
  );

  const handleClear = useCallback(async () => {
    const response = await handleClearCart();
    if (response) {
      mutateCart();
      setSelectedItems([]);
      setSelectAll(false);
    }
  }, [handleClearCart, mutateCart]);

  const showClearConfirmModal = useCallback(() => {
    onOpen({
      title: "Clear Items",
      message: "Do you want to remove all items?",
      onYes: handleClear,
    });
  }, [onOpen, handleClear]);

  // Cart data
  const [localCart, setLocalCart] = useState(cartDataResult?.cartItems || []);

  // Update local cart when fetched data changes
  useEffect(() => {
    setLocalCart(cartDataResult?.cartItems || []);
    // Reset selected items and "Select All" when cart data changes
    setSelectedItems([]);
    setSelectAll(false);
  }, [cartDataResult?.cartItems]);

  // Handle "Select All" checkbox change
  const handleSelectAll = (event) => {
    const isChecked = event.target.checked;
    setSelectAll(isChecked);
    if (isChecked) {
      const allProductIds = localCart.map((item) => item.product.productId);
      setSelectedItems(allProductIds);
    } else {
      setSelectedItems([]);
    }
  };

  // Handle selection of a single item
  const handleSelectItem = (event, productId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [...prevSelected, productId]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== productId)
      );
    }
  };

  // Update "Select All" state based on individual item selections
  useEffect(() => {
    if (localCart.length > 0 && selectedItems.length === localCart.length) {
      setSelectAll(true);
    } else {
      setSelectAll(false);
    }
  }, [selectedItems, localCart.length]);

  const handleIncrement = (product) => {
    const cartItem = localCart.find((item) => item.product.productId === product.productId);
    const currentQuantity = cartItem ? cartItem.quantity : 0;

    if (product.unitsInStock <= currentQuantity) {
      showNewAlert({
        message: "Exceed the maximun in stock of product",
        variant: "warning",
      });
      return;
    }

    const updatedCart = localCart.map((item) =>
      item.product.productId === product.productId
        ? { ...item, quantity: item.quantity + 1 }
        : item
    );
    setLocalCart(updatedCart);
    const data = {
      productId: product.productId,
      quantity:
        updatedCart.find((item) => item.product.productId === product.productId)?.quantity ||
        0,
    };
    handleUpdateCartItemQuantity(data);
  };

  const handleDecrement = (productId) => {
    const itemToUpdate = localCart.find(
      (item) => item.product.productId === productId
    );
    if (itemToUpdate && itemToUpdate.quantity > 1) {
      const updatedCart = localCart.map((item) =>
        item.product.productId === productId
          ? { ...item, quantity: item.quantity - 1 }
          : item
      );
      setLocalCart(updatedCart);
      const data = {
        productId: productId,
        quantity:
          updatedCart.find((item) => item.product.productId === productId)?.quantity ||
          0,
      };
      handleUpdateCartItemQuantity(data);
    }
  };

  const totalItems = localCart.reduce((sum, item) => sum + item.quantity, 0);

  // Calculate total price based on selected items and their quantities
  const totalPrice = localCart.reduce((sum, item) => {
    if (selectedItems.includes(item.product.productId)) {
      sum += item.unitPrice * item.quantity;
    }
    return sum;
  }, 0);

  const total = totalPrice + shippingCost;

  const handleClickCheckout = (e) =>{
    e.preventDefault();
    const selectedItemsWithQuantity = selectedItems.map((productId) => {
      const cartItem = localCart.find((item) => item.product.productId === productId);
      return {
        productId: productId,
        quantity: cartItem ? cartItem.quantity : 0,
      };
    });
    const serializedItems = JSON.stringify(selectedItemsWithQuantity);
    localStorage.setItem(CHECKOUT_ITEMS, serializedItems);
    navigate(CHECKOUT_URI)
  }

  if (loadingCartItems || clearLoading) return <Loading />;

  if (cartItemsError?.message || clearError?.message) {
    return (
      <ErrorDisplay message={cartItemsError?.message || clearError?.message} />
    );
  }

  return (
    <div id="shopping-cart" className={styles["card"]}>
      <div className="row m-0">
        <div className={`col-md-8 ${styles["cart"]}`}>
          <div className="mb-3">
            <div className="row d-flex justify-content-between align-items-center">
              <div className="col-6">
                <h4>
                  <b>Shopping Cart</b>
                </h4>
              </div>
              <div className="col-auto text-right text-muted">
                {totalItems} items
              </div>
            </div>
          </div>
          {localCart.length > 0 && (
            <div className="row px-3 d-flex justify-content-center align-items-center mb-2">
              <div className="col-1">
                <div className="form-check">
                  <input
                    className="form-check-input"
                    style={{ width: "20px", height: "20px", padding: "0" }}
                    type="checkbox"
                    onChange={handleSelectAll}
                    checked={selectAll}
                  />
                </div>
              </div>
              <div className="col-lg-5 col-sm-4">
                <h6 className="heading">Your Item</h6>
              </div>
              <div className="col-lg-6 col-sm-7">
                <div className="row text-right align-items-center">
                  <div className="col-4">
                    <h6 className="mt-2">Quantity</h6>
                  </div>
                  <div className="col-4">
                    <h6 className="mt-2">Price</h6>
                  </div>
                  <div className="col-2">
                    <button
                      className="btn btn-sm text-danger"
                      onClick={showClearConfirmModal}
                      disabled={clearLoading}
                    >
                      <FontAwesomeIcon icon={faTrash} />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}
          {localCart.length === 0 ? (
            <div className="alert alert-info">Your cart is empty.</div>
          ) : (
            localCart.map((item) => (
              <div
                key={item.product.productId}
                className="row border-top border-bottom"
              >
                <div className="row w-100 m-0 px-3 py-4 align-items-center">
                  <div className="col-1">
                    <div className="form-check">
                      <input
                        className="form-check-input"
                        style={{ width: "20px", height: "20px", padding: "0" }}
                        type="checkbox"
                        value={item.product.productId}
                        checked={selectedItems.includes(item.product.productId)}
                        onChange={(e) => handleSelectItem(e, item.product.productId)}
                      />
                    </div>
                  </div>
                  <div className="col-2">
                    <img
                      className="img-icon rounded-2"
                      src={getImageSrc(item.product.picture)}
                      alt={item.product.productName}
                    />
                  </div>
                  <div className="col-lg-3 col-sm-3">
                    <div className="row text-muted">
                      {item.product.productName}
                    </div>
                    <div className="row">{item.description}</div>
                  </div>
                  <div className="col-lg-6 col-sm-7">
                    <div className="row align-items-center">
                      <div className="col-4 d-flex align-items-center">
                        <button
                          onClick={() => handleDecrement(item.product.productId)}
                          className="btn btn-sm"
                          disabled={clearLoading}
                        >
                          -
                        </button>
                        <span className="border px-2">{item.quantity}</span>
                        <button
                          onClick={() => handleIncrement(item.product)}
                          className="btn btn-sm"
                          disabled={clearLoading}
                        >
                          +
                        </button>
                      </div>
                      <div className="col-4 text-right">
                        $ {item.unitPrice.toFixed(2)}
                      </div>
                      <div className="col-2">
                        <button
                          onClick={() =>
                            showRemoveConfirmModal(item.product.productId)
                          }
                          className="btn btn-sm text-danger"
                          disabled={clearLoading}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
        <div className={`col-md-4 ${styles[`summary`]}`}>
          <div className="mb-5">
            <h4>
              <b>Summary</b>
            </h4>
          </div>
          <hr />
          <div className="row">
            <div className="col" style={{ paddingLeft: "0" }}>
              ITEMS {selectedItems.length}
            </div>
            <div className="col text-right">$ {totalPrice.toFixed(2)}</div>
          </div>
          <div
            className="row"
            style={{ borderTop: "1px solid rgba(0,0,0,.1)", padding: "2vh 0" }}
          >
            <div className="col">TOTAL PRICE</div>
            <div className="col text-right">$ {total.toFixed(2)}</div>
          </div>
          <button className="btn btn-dark" onClick={handleClickCheckout} disabled={clearLoading || selectedItems.length === 0}>
            CHECKOUT
          </button>
        </div>
      </div>
    </div>
  );
};

export default ShoppingCart;