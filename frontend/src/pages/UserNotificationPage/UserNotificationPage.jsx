import { useCallback, useState } from "react";
import NotificationHeader from "../../components/Notification/NotificationHeader";
import NotificationList from "../../components/Notification/NotificationList";
import styles from "./NotificationPage.module.css";
import {
  useNotificationActions,
  useNotifications,
} from "../../hooks/notificationHooks";

export default function UserNotificationPage() {
  const { data: notificationsData, isLoading: loadingNotifications } =
    useNotifications();
  const notifications = notificationsData?.content || [];

  const {
    handleUpdateIsReadStatus,
  } = useNotificationActions();
  const [filter, setFilter] = useState("all");

  const filteredNotifications =
    filter === "all"
      ? notifications
      : filter === "unread"
      ? notifications.filter((notif) => !notif.isRead)
      : notifications.filter((notif) => notif.type === filter);

  const markAllAsRead = useCallback(() => {}, []);

  const markAsRead = useCallback(
    async (id) => {
      await handleUpdateIsReadStatus(id, true);
    },
    [handleUpdateIsReadStatus]
  );

  return (
    <div className={styles.notificationPage}>
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
