import { Link } from "react-router-dom";
import styles from "./NotificationItem.module.css";

export default function NotificationItem({ notification }) {
  return (
    <Link
      className={`${styles.notificationItem} dropdown-item ${
        !notification.isRead ? styles.unread : ""
      }`}
    >
      <div className={styles.notificationContent}>
        <div className={styles.senderInfo}>
          <span className={styles.sender}>{notification.sender?.username || "System"}</span>
          {!notification.isRead && <span className={styles.dot}></span>}
        </div>
        <p className={styles.message}>{notification.notification.message}</p>
        <span className={styles.time}>{notification.createdAt}</span>
      </div>
    </Link>
  );
}
