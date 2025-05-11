import NotificationItem from "./NotificationItem";
import styles from "./notification-list.module.css";

export default function NotificationList({ notifications, markAsRead }) {
  if (notifications.length === 0) {
    return (
      <div className={styles.emptyState}>
        <p>No notifications to display</p>
      </div>
    );
  }

  return (
    <div className={styles.list}>
      {notifications.map((notification) => (
        <NotificationItem
          key={notification.id}
          notification={notification}
          markAsRead={markAsRead}
        />
      ))}
    </div>
  );
}
