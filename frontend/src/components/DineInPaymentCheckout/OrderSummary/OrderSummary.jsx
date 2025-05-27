// src/components/OrderSummary/OrderSummary.js
import React from "react";
import styles from "./OrderSummary.module.css";

const OrderSummary = ({ order, totals }) => {
  return (
    <div className={`card mb-4 ${styles.orderSummaryCard}`}>
      <div className="card-header ${styles.cardHeader}">
        <h5 className="mb-0">Order Summary</h5>
      </div>
      <div className="card-body">
        <ul className="list-group list-group-flush">
          {order.items.map((item) => (
            <li
              key={item.id}
              className="list-group-item d-flex justify-content-between align-items-center"
            >
              <span>
                {item.quantity} x {item.name}
              </span>
              <span>${(item.quantity * item.price).toFixed(2)}</span>
            </li>
          ))}
        </ul>
        <hr />
        <div className="d-flex justify-content-between align-items-center mb-1">
          <span>Subtotal:</span>
          <span>${totals.subtotal.toFixed(2)}</span>
        </div>
        <div className="d-flex justify-content-between align-items-center mb-1">
          <span>Tax ({order.taxRate * 100}%):</span>
          <span>${totals.tax.toFixed(2)}</span>
        </div>
        <div className="d-flex justify-content-between align-items-center mb-1">
          <span>Service Charge ({order.serviceChargeRate * 100}%):</span>
          <span>${totals.serviceCharge.toFixed(2)}</span>
        </div>
        {totals.tipAmount > 0 && (
          <div className="d-flex justify-content-between align-items-center mb-1">
            <span>Tip:</span>
            <span>${totals.tipAmount.toFixed(2)}</span>
          </div>
        )}
        <h4
          className={`d-flex justify-content-between align-items-center mt-3 ${styles.totalAmount}`}
        >
          <span>Total:</span>
          <span>${totals.total.toFixed(2)}</span>
        </h4>
      </div>
    </div>
  );
};

export default OrderSummary;
