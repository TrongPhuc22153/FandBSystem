import { useState } from "react";
import styles from "./notification-item.module.css";

export default function NotificationItem({ notification, markAsRead }) {
  const [expanded, setExpanded] = useState(false);

  const handleClick = () => {
    setExpanded(!expanded);
    if (!notification.isRead) {
      markAsRead(notification.id);
    }
  };

  const getTypeIcon = (type) => {
    switch (type) {
      case "message":
        return "✉️";
      case "system":
        return "🔧";
      case "reminder":
        return "⏰";
      case "feature":
        return "✨";
      case "payment":
        return "💰";
      default:
        return "📌";
    }
  };

  return (
    <div
      className={`${styles.item} ${!notification.isRead ? styles.unread : ""}`}
      onClick={handleClick}
    >
      <div className={styles.icon}>
        {getTypeIcon(notification.notification.topic.topicName)}
      </div>
      <div className={styles.content}>
        <div className={styles.header}>
          <h3 className={styles.title}>{notification.notification.title}</h3>
          <span className={styles.time}>
            {notification.notification.createdAt}
          </span>
        </div>
        <p className={`${styles.message} ${expanded ? styles.expanded : ""}`}>
          {notification.notification.message}
        </p>
      </div>
    </div>
  );
}
