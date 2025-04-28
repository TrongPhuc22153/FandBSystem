import { useEffect, useState } from "react";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router";
import { useAuth } from "../hooks/AuthContext";
import axios from "axios";
import { UPDATE_ITEMS_CART_URL, USER_CART_URL } from "../constants/ApiEndpoints";
import { getDefaultFood } from "../services/ImageService";

export default function CartPage() {
  const auth = useAuth();
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProductsInCart = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await axios.get(USER_CART_URL, {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${auth.token}`,
          },
        });
        // Assuming the cart items are in response.data.cartItems
        setCartItems(response.data.cartItems || []);
        setLoading(false);
      } catch (error) {
        console.error("Failed to fetch cart items:", error);
        setError("Failed to fetch cart items.");
        setLoading(false);
      }
    };

    if (auth.token) {
      fetchProductsInCart();
    } else {
      setLoading(false);
      // Optionally set an empty cart or a message indicating no user logged in
      setCartItems([]);
    }
  }, [auth.token]);

  const handleRemove = async (productId) => {
    try {
      await axios.delete(`${UPDATE_ITEMS_CART_URL}/${productId}`, {
        headers: {
          Authorization: `Bearer ${auth.token}`,
        },
      });
      // Optimistically update the UI by removing the item locally
      setCartItems(cartItems.filter((item) => item.product.productId !== productId));
      // Optionally, you could refetch the cart to ensure data consistency
      // fetchProductsInCart();
    } catch (error) {
      console.error("Failed to remove item from cart:", error);
      setError("Failed to remove item from cart.");
    }
  };

  const handleClearCart = async () => {
    try {
      await axios.delete(USER_CART_URL, {
        headers: {
          Authorization: `Bearer ${auth.token}`,
        },
      });
      // Optimistically update the UI by clearing the local cart
      setCartItems([]);
      // Optionally, you could refetch the cart to ensure data consistency
      // fetchProductsInCart();
    } catch (error) {
      console.error("Failed to clear cart:", error);
      setError("Failed to clear cart.");
    }
  };

  const updateCartItemQuantity = async (productId, newQuantity) => {
    try {
      await axios.put(
        UPDATE_ITEMS_CART_URL,
        JSON.stringify({ productId: productId, quantity: newQuantity }),// Adjust payload based on your API
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${auth.token}`,
          },
        }
      );
      // Optimistically update the local state
      setCartItems(
        cartItems.map((item) =>
          item.product.productId === productId ? { ...item, quantity: newQuantity } : item
        )
      );
    } catch (error) {
      console.error("Failed to update cart item quantity:", error);
      setError("Failed to update cart item quantity.");
      // Optionally, you might want to revert the local state change on error
    }
  };

  const handleIncreaseQuantity = (productId) => {
    const itemToUpdate = cartItems.find((item) => item.product.productId === productId);
    if (itemToUpdate) {
      const newQuantity = itemToUpdate.quantity + 1;
      updateCartItemQuantity(productId, newQuantity);
    }
  };

  const handleDecreaseQuantity = (productId) => {
    const itemToUpdate = cartItems.find((item) => item.product.productId === productId);
    if (itemToUpdate && itemToUpdate.quantity > 1) {
      const newQuantity = itemToUpdate.quantity - 1;
      updateCartItemQuantity(productId, newQuantity);
    }
  };



  if (loading) {
    return <p>Loading your cart...</p>;
  }

  if (error) {
    return <p className="text-danger">{error}</p>;
  }

  const subtotal = cartItems.reduce(
    (sum, item) => sum + item.unitPrice * item.quantity,
    0
  );
  const shipping = 2.99;
  const total = subtotal + shipping;

  if (cartItems.length === 0) {
    return <div className="container px-4 py-5 panel-margin-top">Your cart is empty. <Link to="/products">Browse products</Link></div>;
  }

  return (
    <div className="container px-4 py-5 panel-margin-top">
      <div className="row d-flex justify-content-center">
        <div className="col-5">
          <h4 className="heading">Your Order</h4>
        </div>
        <div className="col-7">
          <div className="row text-right">
            <div className="col-3">
              <h6 className="mt-2">Category</h6>
            </div>
            <div className="col-3">
              <h6 className="mt-2">Quantity</h6>
            </div>
            <div className="col-4">
              <h6 className="mt-2">Price</h6>
            </div>
            <div className="col-2">
              <button className="btn" onClick={handleClearCart}>
                <span className="mt-2">
                  <FontAwesomeIcon icon={faTrash} />
                </span>
              </button>
            </div>
          </div>
        </div>
      </div>

      {cartItems.map((item) => (
        <div
          key={item.product.productId}
          className="row d-flex justify-content-center border-top"
        >
          <div className="col-5">
            <div className="d-flex">
              <div className="book">
                <img src={item.product.picture || getDefaultFood()} className="book-img" alt={item.product.productName} />
              </div>
              <div className="my-auto flex-column d-flex pad-left">
                <h6 className="mob-text">{item.product.productName}</h6>
              </div>
            </div>
          </div>
          <div className="my-auto col-7">
            <div className="row text-right">
              <div className="col-3">
                <p className="mob-text d-flex align-items-center">
                  {item.product.category.categoryName}
                </p>
              </div>
              <div className="col-3">
                <div className="h-100 d-flex px-3">
                  <p className="mb-0 me-2 d-flex align-items-center ">
                    {item.quantity}
                  </p>
                  <div className="d-flex flex-column plus-minus">
                    <span
                      className="vsm-text plus btn"
                      onClick={() => handleIncreaseQuantity(item.product.productId)}
                    >
                      +
                    </span>
                    <span
                      className="vsm-text minus btn"
                      onClick={() => handleDecreaseQuantity(item.product.productId)}
                    >
                      -
                    </span>
                  </div>
                </div>
              </div>
              <div className="col-4">
                <h6 className="mob-text">${(item.unitPrice * item.quantity).toFixed(2)}</h6> {/* Display total price per item */}
              </div>
              <div className="col-2">
                <button className="btn" onClick={() => handleRemove(item.product.productId)}>
                  <span className="mt-2">
                    <FontAwesomeIcon icon={faTrash} />
                  </span>
                </button>
              </div>
            </div>
          </div>
        </div>
      ))}

      <div className="row justify-content-center">
        <div className="col-lg-12">
          <div className="card">
            <div className="row">
              <div className="col-lg-12 mt-2">
                <div className="d-flex justify-content-between px-4">
                  <p className="mb-1 text-left">Subtotal</p>
                  <h6 className="mb-1 text-right">${subtotal.toFixed(2)}</h6>
                </div>
                <div className="d-flex justify-content-between px-4">
                  <p className="mb-1 text-left">Shipping</p>
                  <h6 className="mb-1 text-right">${shipping.toFixed(2)}</h6>
                </div>
                <div className="d-flex justify-content-between px-4" id="tax">
                  <p className="mb-1 text-left">Total</p>
                  <h6 className="mb-1 text-right">${total.toFixed(2)}</h6>
                </div>
                <div className="d-flex justify-content-end px-4">
                  <Link className="btn-block btn-blue" to="/checkout"> {/* Assuming /checkout is your checkout route */}
                    <span>
                      <span id="checkout" className="me-2">
                        Checkout
                      </span>
                      <span id="check-amt">${total.toFixed(2)}</span>
                    </span>
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
