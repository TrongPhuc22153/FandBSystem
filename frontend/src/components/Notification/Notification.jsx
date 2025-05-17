import { useState } from "react";
import NotificationHeader from "../../components/Notification/NotificationHeader";
import NotificationList from "../../components/Notification/NotificationList";
import styles from "./NotificationPage.module.css";

export default function NotificationPage({
  notifications,
  markAllAsRead,
  markAsRead,
}) {
  const [filter, setFilter] = useState("all");

  const filteredNotifications =
    filter === "all"
      ? notifications
      : filter === "unread"
      ? notifications.filter((notif) => !notif.isRead)
      : notifications.filter((notif) => notif.type === filter);

  return (
    <div className={`${styles.notificationPage} me-3`}>
      <NotificationHeader
        filter={filter}
        setFilter={setFilter}
        markAllAsRead={markAllAsRead}
        unreadCount={notifications.filter((n) => !n.isRead).length}
      />
      <NotificationList
        notifications={filteredNotifications}
        markAsRead={markAsRead}
      />
    </div>
  );
}
