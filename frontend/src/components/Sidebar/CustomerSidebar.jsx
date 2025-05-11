import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faListAlt,
  faCircle,
  faUser,
  faSignOutAlt,
  faMapMarkedAlt,
  faCalendarCheck,
  faBell,
} from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";
import {
  USER_RESERVATIONS_URI,
  HOME_URI,
  USER_ADDRESSES_URI,
  USER_ORDERS_URI,
  USER_PROFILE_URI,
  USER_NOTIFICATIONS_URI,
} from "../../constants/routes";
import { useAuth } from "../../context/AuthContext";
import { useModal } from "../../context/ModalContext";
import { useCallback } from "react";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import styles from "./Sidebar.module.css";

export const CustomerSidebar = () => {
  const { logoutAction, user } = useAuth();
  const { onOpen } = useModal();

  const showConfirmModal = useCallback(() => {
    onOpen({
      title: "Logout",
      message: "Do you want to logout?",
      onYes: logoutAction,
    });
  }, [logoutAction, user]);

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
                  window.location.pathname === USER_PROFILE_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon icon={faUser} className={styles["nav_icon"]} />
                <span className={styles["nav_name"]}>Profile</span>
              </Link>
              <Link
                to={USER_ADDRESSES_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === USER_ADDRESSES_URI
                    ? styles["active"]
                    : ""
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
                  window.location.pathname === USER_ORDERS_URI
                    ? styles["active"]
                    : ""
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
                  window.location.pathname === USER_RESERVATIONS_URI
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
                  window.location.pathname === USER_NOTIFICATIONS_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faBell}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Notifications</span>
              </Link>
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
