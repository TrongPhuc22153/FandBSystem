import React from "react";
import { Link } from "react-router-dom";
import { getPrimaryProductImage } from "../../utils/imageUtils";
import { SHOP_URI } from "../../constants/routes";

const CheckoutSummary = ({
  cartItems,
  totalPrice,
  shippingCost,
  finalTotalPrice,
}) => {
  return (
    <div className="ms-lg-4 mt-4 mt-lg-0" style={{ maxWidth: "320px" }}>
      <h6 className="mb-3">Summary</h6>
      <div className="d-flex justify-content-between">
        <p className="mb-2">Total price:</p>
        <p className="mb-2">${totalPrice.toFixed(2)}</p>
      </div>
      <div className="d-flex justify-content-between">
        <p className="mb-2">Shipping cost: </p>
        <p className="mb-2">+ ${shippingCost.toFixed(2)}</p>
      </div>
      <hr />
      <div className="d-flex justify-content-between">
        <p className="mb-2">Total price:</p>
        <p className="mb-2 fw-bold">${finalTotalPrice.toFixed(2)}</p>
      </div>

      <hr />
      <h6 className="text-dark my-4">Items in cart</h6>
      {cartItems.map((item) => (
        <div className="d-flex align-items-center mb-4" key={item.product.productId}>
          <div className="me-3 position-relative">
            <span className="text-black text-bg-light position-absolute top-0 start-100 translate-middle badge rounded-pill badge-dark">
              {item.quantity}
            </span>
            <img
              src={getPrimaryProductImage(item.product.images)}
              style={{ height: "96px", width: "96px" }}
              className="img-sm rounded border"
              alt={item.product.productName}
            />
          </div>
          <div className="">
            <Link
              to={`${SHOP_URI}/${item.product.productName}?id=${item.product.productId}`}
              className="nav-link"
            >
              <b>{item.product.productName}</b> <br />
              {item.product.category.categoryName && (
                <span>{item.product.category.categoryName}</span>
              )}
            </Link>
            <div className="price text-muted">
              Total: ${(item.unitPrice * item.quantity).toFixed(2)}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default CheckoutSummary;
