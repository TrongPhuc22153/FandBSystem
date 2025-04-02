import { useEffect, useState } from "react";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { getProductsCart } from "../../../api/CartAPI";
import { Link } from "react-router";
import { CHECKOUT_ORDER_URI } from "../../../constants/WebPageURI";

export default function CartComponent() {
  const [cartItems, setCartItems] = useState([]);

  useEffect(() => {
    const fetchProductsInCart = async () => {
      try {
        const data = await getProductsCart();
        setCartItems(data);
      } catch (error) {
        console.error("Failed to fetch cart items:", error);
      }
    };
    fetchProductsInCart();
  }, []);

  const handleRemove = (id) => {
    setCartItems(cartItems.filter((item) => item.id !== id));
  };

  const subtotal = cartItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );
  const shipping = 2.99;
  const total = subtotal + shipping;

  const handleIncreaseQuantity = (id) => {
    setCartItems(
      cartItems.map((item) =>
        item.id === id ? { ...item, quantity: item.quantity + 1 } : item
      )
    );
  };

  const handleDecreaseQuantity = (id) => {
    setCartItems(
      cartItems.map((item) =>
        item.id === id && item.quantity > 1
          ? { ...item, quantity: item.quantity - 1 }
          : item
      )
    );
  };

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
              <button className="btn" onClick={() => setCartItems([])}>
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
          key={item.id}
          className="row d-flex justify-content-center border-top"
        >
          <div className="col-5">
            <div className="d-flex">
              <div className="book">
                <img src={item.image} className="book-img" alt="Item" />
              </div>
              <div className="my-auto flex-column d-flex pad-left">
                <h6 className="mob-text">{item.name}</h6>
              </div>
            </div>
          </div>
          <div className="my-auto col-7">
            <div className="row text-right">
              <div className="col-3">
                <p className="mob-text d-flex align-items-center">
                  {item.category}
                </p>
              </div>
              <div className="col-3">
                <div className="h-100 d-flex justify-content-end px-3">
                  <p className="mb-0 me-2 d-flex align-items-center ">
                    {item.quantity}
                  </p>
                  <div className="d-flex flex-column plus-minus">
                    <span
                      className="vsm-text plus btn"
                      onClick={() => handleIncreaseQuantity(item.id)}
                    >
                      +
                    </span>
                    <span
                      className="vsm-text minus btn"
                      onClick={() => handleDecreaseQuantity(item.id)}
                    >
                      -
                    </span>
                  </div>
                </div>
              </div>
              <div className="col-4">
                <h6 className="mob-text">${item.price.toFixed(2)}</h6>
              </div>
              <div className="col-2">
                <button className="btn" onClick={() => handleRemove(item.id)}>
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
                  <Link className="btn-block btn-blue" to={CHECKOUT_ORDER_URI}>
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
