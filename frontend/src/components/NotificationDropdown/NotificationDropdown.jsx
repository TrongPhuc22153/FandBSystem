import { useCallback, useEffect, useMemo, useState, useRef } from "react";
import { Bell } from "lucide-react";
import NotificationItem from "../NotificationItem/NotificationItem";
import { useNotifications } from "../../hooks/notificationHooks";
import styles from "./NotificationDropdown.module.css";
import { Link } from "react-router-dom";
import { hasRole } from "../../utils/authUtils";
import { useAuth } from "../../context/AuthContext";
import { ROLES } from "../../constants/roles";
import { useStompSubscription } from "../../hooks/websocketHooks";
import {
  EMPLOYEE_NOTIFICATIONS_URI,
  USER_NOTIFICATIONS_URI,
} from "../../constants/routes";
import { TOPIC_EMPLOYEE } from "../../constants/webSocketEnpoint";

export default function NotificationDropdown() {
  const [isOpen, setIsOpen] = useState(false);
  const { user } = useAuth();
  const dropdownRef = useRef(null);

  const { data: notificationsData, isLoading } = useNotifications();
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
      console.error("Error processing notification:", error);
    }
  }, []);

  useStompSubscription({
    topic: TOPIC_EMPLOYEE,
    onMessage: handleMessage,
    shouldSubscribe: hasRole(user, ROLES.EMPLOYEE),
  });

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const unreadCount = useMemo(
    () => notifications.filter((notification) => !notification.isRead).length,
    [notifications]
  );

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        isOpen &&
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target)
      ) {
        setIsOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isOpen]);

  return (
    <div className={`${styles.notificationContainer} dropdown`} ref={dropdownRef}>
      <button
        className={`${styles.notificationButton} btn btn-light position-relative`}
        onClick={toggleDropdown}
        aria-expanded={isOpen}
      >
        <Bell className={styles.bellIcon} />
        {unreadCount > 0 && (
          <span
            className={`${styles.badge} position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger`}
          >
            {unreadCount}
            <span className="visually-hidden">unread notifications</span>
          </span>
        )}
      </button>

      <div
        className={`${styles.dropdownMenu} dropdown-menu ${
          isOpen ? "show" : ""
        }`}
      >
        <div className={styles.notificationHeader}>
          <h6 className="dropdown-header">Notifications</h6>
          {unreadCount > 0 && (
            <span className={styles.unreadCount}>{unreadCount} new</span>
          )}
        </div>

        <div className={styles.notificationList}>
          {isLoading ? (
            <div className={styles.loadingState}>Loading...</div>
          ) : notifications.length > 0 ? (
            notifications.map((notification) => (
              <NotificationItem
                key={notification.id}
                notification={notification}
              />
            ))
          ) : (
            <div className={styles.emptyState}>No notifications</div>
          )}
        </div>

        <div className="dropdown-divider"></div>
        {hasRole(user, ROLES.CUSTOMER) && (
          <Link
            className="dropdown-item text-center text-primary"
            to={USER_NOTIFICATIONS_URI}
          >
            View all notifications
          </Link>
        )}
        {hasRole(user, ROLES.EMPLOYEE) && (
          <Link
            className="dropdown-item text-center text-primary"
            to={EMPLOYEE_NOTIFICATIONS_URI}
          >
            View all notifications
          </Link>
        )}
      </div>
    </div>
  );
}