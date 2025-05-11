"use client";

import styles from "./OrderSummary.module.css";

export default function OrderSummary({ items, onUpdateQuantity }) {
  const total = items.reduce(
    (sum, item) => sum + item.food.unitPrice * item.quantity,
    0
  );

  if (items.length === 0) {
    return (
      <div className={styles.emptyOrder}>
        <p>No items in order yet</p>
      </div>
    );
  }

  return (
    <div>
      <div className={styles.orderList}>
        {items.map((item) => (
          <div key={item.food.productId} className={styles.orderItem}>
            <div className={styles.orderItemHeader}>
              <div>
                <h6 className={styles.itemName}>{item.food.name}</h6>
                <small className={styles.itemPrice}>
                  ${item.food.unitPrice.toFixed(2)} each
                </small>
              </div>
              <div className={styles.itemTotal}>
                <div>${(item.food.unitPrice * item.quantity).toFixed(2)}</div>
              </div>
            </div>
            <div className={styles.quantityControls}>
              <div className={styles.quantityInput}>
                <button
                  className={styles.quantityButton}
                  onClick={() =>
                    onUpdateQuantity(item.food.id, item.quantity - 1)
                  }
                >
                  -
                </button>
                <input
                  type="text"
                  className={styles.quantityValue}
                  value={item.quantity}
                  readOnly
                />
                <button
                  className={styles.quantityButton}
                  onClick={() =>
                    onUpdateQuantity(item.food.id, item.quantity + 1)
                  }
                >
                  +
                </button>
              </div>
              <button
                className={styles.removeButton}
                onClick={() => onUpdateQuantity(item.food.id, 0)}
              >
                Remove
              </button>
            </div>
          </div>
        ))}
      </div>

      <div className={styles.orderTotal}>
        <span>Total:</span>
        <span>${total.toFixed(2)}</span>
      </div>
    </div>
  );
}
