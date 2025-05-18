import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faListAlt,
  faCircle,
  faUser,
  faSignOutAlt,
  faMapMarkedAlt,
  faCalendarCheck,
  faBell,
  faCog,
  faKey,
  faCaretDown,
} from "@fortawesome/free-solid-svg-icons";
import { Link, useLocation } from "react-router-dom";
import {
  USER_RESERVATIONS_URI,
  HOME_URI,
  USER_ADDRESSES_URI,
  USER_ORDERS_URI,
  USER_PROFILE_URI,
  USER_NOTIFICATIONS_URI,
  USER_CHANGE_PASSWORD_URI
} from "../../constants/routes";
import { useAuth } from "../../context/AuthContext";
import { useModal } from "../../context/ModalContext";
import { useCallback, useState } from "react";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import styles from "./Sidebar.module.css";

export const CustomerSidebar = () => {
  const { logoutAction, user } = useAuth();
  const { onOpen } = useModal();
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const location = useLocation();

  const showConfirmModal = useCallback(() => {
    onOpen({
      title: "Logout",
      message: "Do you want to logout?",
      onYes: logoutAction,
    });
  }, [logoutAction, onOpen]);

  const toggleSettings = useCallback(() => {
    setIsSettingsOpen((prevState) => !prevState);
  }, []);

  return (
    <nav className={styles["nav"]}>
      <div>
        <Link to={HOME_URI} className={styles["nav_logo"]}>
          <FontAwesomeIcon
            icon={faCircle}
            className={styles["nav_logo-icon"]}
          />
          <span className={styles["nav_logo-name"]}>Phucy</span>
        </Link>
        <div className={styles["nav_list"]}>
          {hasRole(user, ROLES.CUSTOMER) && (
            <>
              <Link
                to={USER_PROFILE_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === USER_PROFILE_URI ? styles["active"] : ""
                }`}
              >
                <FontAwesomeIcon icon={faUser} className={styles["nav_icon"]} />
                <span className={styles["nav_name"]}>Profile</span>
              </Link>
              <Link
                to={USER_ADDRESSES_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === USER_ADDRESSES_URI ? styles["active"] : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faMapMarkedAlt}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Address</span>
              </Link>
              <Link
                to={USER_ORDERS_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === USER_ORDERS_URI ? styles["active"] : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faListAlt}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Orders</span>
              </Link>
              <Link
                to={USER_RESERVATIONS_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === USER_RESERVATIONS_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faCalendarCheck}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Reservations</span>
              </Link>
              <Link
                to={USER_NOTIFICATIONS_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === USER_NOTIFICATIONS_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon icon={faBell} className={styles["nav_icon"]} />
                <span className={styles["nav_name"]}>Notifications</span>
              </Link>

              <div className={styles["nav_item"]}>
                <div
                  className={`${styles["nav_link"]} ${
                    isSettingsOpen ? styles["active"] : ""
                  }`}
                  onClick={toggleSettings}
                >
                  <FontAwesomeIcon icon={faCog} className={styles["nav_icon"]} />
                  <span className={styles["nav_name"]}>Settings</span>
                  <FontAwesomeIcon
                    icon={faCaretDown}
                    className={styles["nav_caret"]}
                  />
                </div>
                {isSettingsOpen && (
                  <div className={styles["nav_dropdown"]}>
                    <Link
                      to={USER_CHANGE_PASSWORD_URI}
                      className={`${styles["nav_dropdown-link"]} ${
                        location.pathname === USER_CHANGE_PASSWORD_URI
                          ? styles["active"]
                          : ""
                      }`}
                    >
                      <FontAwesomeIcon
                        icon={faKey}
                        className={styles["nav_dropdown-icon"]}
                      />
                      Change Password
                    </Link>
                  </div>
                )}
              </div>
            </>
          )}
        </div>
      </div>
      <button
        className={`${styles["nav_link"]} btn`}
        onClick={showConfirmModal}
      >
        <FontAwesomeIcon icon={faSignOutAlt} className={styles["nav_icon"]} />
        <span className={styles["nav_name"]}>SignOut</span>
      </button>
    </nav>
  );
};