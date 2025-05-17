import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUtensils,
  faCircle,
  faSignOutAlt,
  faUser,
  faCartShopping,
  faTable,
  faBell,
} from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";
import {
  EMPLOYEE_PLACE_ORDERS_URI,
  EMPLOYEE_PROFILE_URI,
  HOME_URI,
  EMPLOYEE_KITCHEN_URI,
  EMPLOYEE_TABLES_URI,
  EMPLOYEE_NOTIFICATIONS_URI,
} from "../../constants/routes";
import { useAuth } from "../../context/AuthContext";
import { useModal } from "../../context/ModalContext";
import { useCallback } from "react";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import styles from "./Sidebar.module.css";

export const EmpoyeeSidebar = () => {
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
          {hasRole(user, ROLES.EMPLOYEE) && (
            <>
              <Link
                to={EMPLOYEE_PROFILE_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === EMPLOYEE_PROFILE_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon icon={faUser} className={styles["nav_icon"]} />
                <span className={styles["nav_name"]}>Profile</span>
              </Link>
              <Link
                to={EMPLOYEE_PLACE_ORDERS_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === EMPLOYEE_PLACE_ORDERS_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faCartShopping}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Place&nbsp;Order</span>
              </Link>
              <Link
                to={EMPLOYEE_KITCHEN_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === EMPLOYEE_KITCHEN_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faUtensils}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Kitchen</span>
              </Link>
              <Link
                to={EMPLOYEE_TABLES_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === EMPLOYEE_TABLES_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faTable}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Tables</span>
              </Link>
              <Link
                to={EMPLOYEE_NOTIFICATIONS_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === EMPLOYEE_NOTIFICATIONS_URI
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
