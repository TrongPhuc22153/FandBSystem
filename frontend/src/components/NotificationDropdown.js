import { faBell } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router";
import { USER_NOTIFICATION_URI } from "../constants/WebPageURI";
import { useEffect, useRef, useState } from "react";
import { getNotifications } from "../api/NotificationAPI";

export default function NotificationDropdown() {
  const [notifications, setNotifications] = useState([]);
  const notificationDropdownRef = useRef();

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const data = await getNotifications();
        setNotifications(data);
      } catch (error) {
        console.error("Failed to fetch notifications:", error);
      }
    };
    fetchNotifications();
  }, []);

  const toggleDropdown = () => {
    if (notificationDropdownRef.current) {
      notificationDropdownRef.current.classList.toggle("show");
    }
  };

  return (
    <div className="nav-item dropdown notification-ui show">
      <button
        className="nav-link dropdown-toggle notification-btn notification-ui_icon"
        id="navbarDropdown"
        role="button"
        data-toggle="dropdown"
        aria-haspopup="true"
        aria-expanded="false"
        onClick={toggleDropdown}
      >
        <FontAwesomeIcon icon={faBell} />
        <span className="unread-notification"></span>
      </button>
      <div
        className="dropdown-menu notification-ui_dd"
        aria-labelledby="navbarDropdown"
        ref={notificationDropdownRef}
      >
        <div className="notification-ui_dd-content">
          {notifications.map((notification, index) => (
            <div
              key={index}
              className={`notification-list ${
                !notification.isRead && "notification-list--unread"
              }`}
            >
              <div className="notification-list_img">
                <img src={notification.user.imageUrl} alt="user" />
              </div>
              <div className="notification-list_detail">
                <p>
                  <b>{notification.user.name}</b> {notification.action}{" "}
                  <b>{notification.target}</b>
                </p>
                <p>
                  <small>{notification.timestamp} mins ago</small>
                </p>
              </div>
            </div>
          ))}
        </div>
        <div className="notification-ui_dd-footer text-center mt-2">
          <Link
            to={USER_NOTIFICATION_URI}
            className="btn btn-secondary btn-block w-75 text-white"
          >
            View All
          </Link>
        </div>
      </div>
    </div>
  );
}
