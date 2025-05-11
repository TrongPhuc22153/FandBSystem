import styles from "./OrderList.module.css";

const OrderList = ({
  orders,
  selectedItemId,
  selectedItemType,
  onOrderSelect,
  onMarkPrepared,
}) => {
  const formatDate = (date) => {
    return new Date(date).toLocaleDateString();
  };

  return (
    <div className={styles.listGroup}>
      <h3 className="text-lg font-semibold mb-2">Orders</h3>
      {orders.length === 0 ? (
        <p className={styles.textMuted}>No orders found.</p>
      ) : (
        orders.map((order) => (
          <button
            key={order.id}
            type="button"
            className={`${styles.listItem} ${
              selectedItemId === order.id && selectedItemType === "order"
                ? styles.active
                : ""
            }`}
            onClick={() => onOrderSelect(order)}
          >
            <span>
              Order #{order.id} -{" "}
              {order.orderDate ? formatDate(order.orderDate) : "N/A"}
            </span>
            {order.status === "Pending" && (
              <button
                className={styles.button}
                onClick={(e) => {
                  e.stopPropagation();
                  onMarkPrepared(order.id);
                }}
              >
                Mark as Prepared
              </button>
            )}
          </button>
        ))
      )}
    </div>
  );
};

export default OrderList;
