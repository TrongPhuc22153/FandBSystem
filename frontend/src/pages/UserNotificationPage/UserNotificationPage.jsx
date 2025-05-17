import { useCallback, useEffect, useState } from "react";
import Notification from "../../components/Notification/Notification";
import {
  useNotificationActions,
  useNotifications,
} from "../../hooks/notificationHooks";
import { useAuth } from "../../context/AuthContext";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import { useStompSubscription } from "../../hooks/websocketHooks";
import { USER_NOTIFICATION_MESSAGE } from "../../constants/webSocketEnpoint";

export default function UserNotificationPage() {
  const { data: notificationsData } = useNotifications();
  const { handleUpdateIsReadStatus } = useNotificationActions();
  const { user } = useAuth();

  const [notifications, setNotifications] = useState(
    notificationsData?.content || []
  );

  useEffect(() => {
    if (notificationsData?.content) {
      setNotifications(notificationsData.content);
    }
  }, [notificationsData]);

  const handleMessage = useCallback((message) => {
    try {
      const newNotification =
        typeof message === "string" ? JSON.parse(message) : message;

      if (!newNotification?.id) {
        return;
      }

      setNotifications((prevNotifications) => {
        const exists = prevNotifications.some(
          (n) => n.id === newNotification.id
        );
        if (exists) {
          return prevNotifications.map((n) =>
            n.id === newNotification.id ? { ...n, ...newNotification } : n
          );
        }
        return [newNotification, ...prevNotifications];
      });
    } catch (error) {}
  }, []);

  useStompSubscription({
    topic: USER_NOTIFICATION_MESSAGE,
    onMessage: handleMessage,
    shouldSubscribe: hasRole(user, ROLES.CUSTOMER),
  });

  const markAllAsRead = useCallback(async () => {
    try {
      const unreadIds = notifications
        .filter((notification) => !notification.isRead)
        .map((notification) => notification.id);

      if (unreadIds.length === 0) return;

      await Promise.all(
        unreadIds.map((id) => handleUpdateIsReadStatus(id, true))
      );

      setNotifications((prevNotifications) =>
        prevNotifications.map((notification) =>
          unreadIds.includes(notification.id)
            ? { ...notification, isRead: true }
            : notification
        )
      );
    } catch (error) {}
  }, [notifications, handleUpdateIsReadStatus]);

  const markAsRead = useCallback(
    async (id) => {
      try {
        await handleUpdateIsReadStatus(id, true);
        setNotifications((prevNotifications) =>
          prevNotifications.map((notification) =>
            notification.id === id
              ? { ...notification, isRead: true }
              : notification
          )
        );
      } catch (error) {
        console.error("Error marking notification as read:", error);
      }
    },
    [handleUpdateIsReadStatus]
  );

  return (
    <Notification
      notifications={notifications}
      markAllAsRead={markAllAsRead}
      markAsRead={markAsRead}
    />
  );
}
