import styles from "./OrderSummary.module.css";

const OrderSummary = ({ order, totalPrice }) => {
  return (
    <div className={`card mb-4 ${styles.orderSummaryCard}`}>
      <div className={`card-header ${styles.cardHeader}`}>
        <h5 className="mb-0">Order Summary</h5>
      </div>
      <div className="card-body">
        <ul className="list-group list-group-flush">
          {order.orderDetails.map((item) => (
            <li
              key={item.id}
              className="list-group-item d-flex justify-content-between align-items-center"
            >
              <span>
                {item.quantity} x {item.product.productName}
              </span>
              <span>${(item.quantity * item.unitPrice).toFixed(2)}</span>
            </li>
          ))}
        </ul>
        <hr />
        <h4
          className={`d-flex justify-content-between align-items-center mt-3 ${styles.totalAmount}`}
        >
          <span>Total:</span>
          <span>${totalPrice.toFixed(2)}</span>
        </h4>
      </div>
    </div>
  );
};

export default OrderSummary;
