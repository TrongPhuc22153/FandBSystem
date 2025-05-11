import React, { useCallback, useState, useEffect } from "react";
import { useCart, useCartActions } from "../../hooks/cartHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { getPrimaryProductImage } from "../../utils/imageUtils";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { useModal } from "../../context/ModalContext";
import { Link } from "react-router-dom";
import { CHECKOUT_URI } from "../../constants/routes";
import styles from "./ShoppingCart.module.css";

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

  // Modal confirmations

  const handleRemoveConfirm = useCallback(
    async (idToRemove) => {
      // Receive productId as argument
      if (idToRemove) {
        const success = await handleRemoveItemFromCart(idToRemove);
        if (success) {
          mutateCart((prevData) => {
            if (!prevData?.cartItems) return prevData;
            return {
              ...prevData,
              cartItems: prevData.cartItems.filter(
                (cartItem) => cartItem.product.productId !== idToRemove
              ),
            };
          }, false);
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
  }, [cartDataResult?.cartItems]);

  // Increase/Decrease product quantity
  const handleIncrement = (productId) => {
    const updatedCart = localCart.map((item) =>
      item.product.productId === productId
        ? { ...item, quantity: item.quantity + 1 }
        : item
    );
    setLocalCart(updatedCart);
    const data = {
      productId: productId,
      quantity:
        updatedCart.find((item) => item.product.productId === productId)
          ?.quantity || 0,
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
          updatedCart.find((item) => item.product.productId === productId)
            ?.quantity || 0,
      };
      handleUpdateCartItemQuantity(data);
    }
  };

  const totalItems = localCart.reduce((sum, item) => sum + item.quantity, 0);
  const total = (cartDataResult?.totalPrice || 0) + shippingCost;

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
              <div className="col-5">
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
              <div className="col-lg-6 col-sm-5">
                <h6 className="heading">Your Item</h6>
              </div>
              <div className="col-lg-6 col-sm-7">
                <div className="row text-right align-items-center">
                  <div className="col-5">
                    <h6 className="mt-2">Quantity</h6>
                  </div>
                  <div className="col-5">
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
                  <div className="col-2">
                    <img
                      className="img-fluid"
                      src={getPrimaryProductImage(item.product.images)}
                      alt={item.product.productName}
                    />
                  </div>
                  <div className="col-lg-4 col-sm-3">
                    <div className="row text-muted">
                      {item.product.productName}
                    </div>
                    <div className="row">{item.description}</div>
                  </div>
                  <div className="col-lg-6 col-sm-7">
                    <div className="row">
                      <div className="col-5 d-flex align-items-center">
                        <button
                          onClick={() =>
                            handleDecrement(item.product.productId)
                          }
                          className="btn btn-sm"
                          disabled={clearLoading}
                        >
                          -
                        </button>
                        <span className="border px-2">{item.quantity}</span>
                        <button
                          onClick={() =>
                            handleIncrement(item.product.productId)
                          }
                          className="btn btn-sm"
                          disabled={clearLoading}
                        >
                          +
                        </button>
                      </div>
                      <div className="col-5 text-right">
                        &euro; {item.unitPrice.toFixed(2)}{" "}
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
              ITEMS {totalItems}
            </div>
            <div className="col text-right">&euro; {total.toFixed(2)}</div>
          </div>
          <div
            className="row"
            style={{ borderTop: "1px solid rgba(0,0,0,.1)", padding: "2vh 0" }}
          >
            <div className="col">TOTAL PRICE</div>
            <div className="col text-right">&euro; {total.toFixed(2)}</div>
          </div>
          <Link
            to={CHECKOUT_URI}
            className="btn btn-dark"
            disabled={clearLoading}
          >
            CHECKOUT
          </Link>
        </div>
      </div>
    </div>
  );
};

export default ShoppingCart;
