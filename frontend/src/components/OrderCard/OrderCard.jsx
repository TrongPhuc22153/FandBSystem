import styles from "./OrderCard.module.css";

const OrderCard = ({ order, isSelected, onSelect }) => {
  return (
    <div
      className={`${styles.container} ${
        isSelected ? styles.container__selected : ""
      }`}
      onClick={() => onSelect(order.id)}
    >
      <h3 className={styles.title}>Order #{order.id}</h3>
      <p className={styles.text}>Customer: {order.customer}</p>
      <p className={styles.text}>
        <span
          className={`${styles.status} ${
            styles[`status--${order.status.toLowerCase()}`]
          }`}
        >
          {order.status}
        </span>
      </p>
      <p className={styles.text}>Time: {order.time}</p>
    </div>
  );
};
export default OrderCard;
