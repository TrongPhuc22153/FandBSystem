import { useState } from "react";
import { Bell } from "lucide-react";
import NotificationItem from "../NotificationItem/NotificationItem";
import { useNotifications } from "../../hooks/notificationHooks";
import styles from "./NotificationDropdown.module.css";
import { Link } from "react-router-dom";
import { hasRole } from "../../utils/authUtils";
import { useAuth } from "../../context/AuthContext";
import { ROLES } from "../../constants/roles";
import {
  EMPLOYEE_NOTIFICATIONS_URI,
  USER_NOTIFICATIONS_URI,
} from "../../constants/routes";

export default function NotificationDropdown() {
  const [isOpen, setIsOpen] = useState(false);
  const { user } = useAuth();

  const { data: notificationsData } = useNotifications();
  const notifications = notificationsData?.content || [];

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const unreadCount = notifications.filter(
    (notification) => !notification.isRead
  ).length;

  return (
    <div className={`${styles.notificationContainer} dropdown`}>
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
          {notifications.length > 0 ? (
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
