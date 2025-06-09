import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faTachometerAlt,
  faUsers,
  faTags,
  faCircle,
  faSignOutAlt,
  faCalendarCheck,
  faTable,
  faBowlFood,
  faUtensils,
} from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";
import {
  ADMIN_CATEGORIES_URI,
  ADMIN_DASHBOARD_URI,
  ADMIN_ORDERS_URI,
  ADMIN_PRODUCTS_URI,
  ADMIN_RESERVATIONS_URI,
  ADMIN_TABLES_URI,
  ADMIN_USERS_URI,
  HOME_URI,
} from "../../constants/routes";
import { useAuth } from "../../context/AuthContext";
import { useModal } from "../../context/ModalContext";
import { useCallback } from "react";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import styles from "./Sidebar.module.css";

export const AdminSidebar = () => {
  const { logoutAction, user } = useAuth();
  const { onOpen } = useModal();

  const showConfirmModal = useCallback(() => {
    onOpen({
      title: "Logout",
      message: "Do you want to logout?",
      onYes: logoutAction,
    });
  }, [logoutAction, onOpen]);

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
          {hasRole(user, ROLES.ADMIN) && (
            <>
              <Link
                to={ADMIN_DASHBOARD_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === ADMIN_DASHBOARD_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faTachometerAlt}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Dashboard</span>
              </Link>
              <Link
                to={ADMIN_USERS_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === ADMIN_USERS_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faUsers}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Users</span>
              </Link>
              <Link
                to={ADMIN_CATEGORIES_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === ADMIN_CATEGORIES_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon icon={faTags} className={styles["nav_icon"]} />
                <span className={styles["nav_name"]}>Categories</span>
              </Link>
              <Link
                to={ADMIN_PRODUCTS_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === ADMIN_PRODUCTS_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faBowlFood}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Products</span>
              </Link>
              <Link
                to={ADMIN_ORDERS_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === ADMIN_ORDERS_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faUtensils}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Orders</span>
              </Link>
              <Link
                to={ADMIN_RESERVATIONS_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === ADMIN_RESERVATIONS_URI
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
                to={ADMIN_TABLES_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === ADMIN_TABLES_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faTable}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Reservation Table</span>
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
