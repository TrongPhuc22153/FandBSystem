import { useCallback, useEffect, useState } from "react";
import Notification from "../../components/Notification/Notification";
import {
  useNotificationActions,
  useNotifications,
} from "../../hooks/notificationHooks";
import {
  TOPIC_EMPLOYEE,
  USER_NOTIFICATION_MESSAGE,
} from "../../constants/webSocketEnpoint";
import { useAuth } from "../../context/AuthContext";
import { useStompSubscription } from "../../hooks/websocketHooks";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import Pagination from "../../components/Pagination/Pagination";
import { useSearchParams } from "react-router-dom";

export default function EmployeeNotificationPage() {
  const [searchParams] = useSearchParams();
  const currentPage = parseInt(searchParams.get("page") || "0");

  const { data: notificationsData } = useNotifications({
    page: currentPage,
    size: 30
  });
  const totalPages = notificationsData?.totalPages || 0;

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

  const handleMessage = useCallback((newNotification) => {
    try {
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
    } catch (error) {
      console.error("Error processing WebSocket message:", error);
    }
  }, []);

  useStompSubscription({
    topic: USER_NOTIFICATION_MESSAGE,
    onMessage: handleMessage,
    shouldSubscribe: !!user,
  });

  useStompSubscription({
    topic: TOPIC_EMPLOYEE,
    onMessage: handleMessage,
    shouldSubscribe: hasRole(user, ROLES.EMPLOYEE),
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
    } catch (error) {
      console.error("Error marking all notifications as read:", error);
    }
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
    <>
      <Notification
        notifications={notifications}
        markAllAsRead={markAllAsRead}
        markAsRead={markAsRead}
      />
      {totalPages > 1 &&
        <Pagination currentPage={currentPage + 1} totalPages={totalPages} />
      }
    </>
  );
}
