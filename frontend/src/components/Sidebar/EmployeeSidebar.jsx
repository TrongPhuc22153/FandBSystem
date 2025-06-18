import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUtensils,
  faCircle,
  faSignOutAlt,
  faUser,
  faCartShopping,
  faTable,
  faBell,
  faCog,
  faKey,
  faCaretDown,
  faReceipt,
  faTruck,
  faKitchenSet,
  faCalendarCheck,
} from "@fortawesome/free-solid-svg-icons";
import { Link, useLocation } from "react-router-dom";
import {
  EMPLOYEE_PLACE_ORDERS_URI,
  EMPLOYEE_PROFILE_URI,
  HOME_URI,
  EMPLOYEE_KITCHEN_URI,
  EMPLOYEE_TABLES_URI,
  EMPLOYEE_NOTIFICATIONS_URI,
  EMPLOYEE_CHANGE_PASSWORD_URI,
  PAYMENT_CHECKOUT_URI,
  EMPLOYEE_ORDERS_URI,
  EMPLOYEE_RESERVATIONS_URI,
} from "../../constants/routes";
import { useAuth } from "../../context/AuthContext";
import { useModal } from "../../context/ModalContext";
import { useCallback, useState } from "react";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import styles from "./Sidebar.module.css";

export const EmpoyeeSidebar = () => {
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
          {hasRole(user, ROLES.EMPLOYEE) && (
            <>
              <Link
                to={EMPLOYEE_PROFILE_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === EMPLOYEE_PROFILE_URI
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
                  location.pathname === EMPLOYEE_PLACE_ORDERS_URI
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
                  location.pathname === EMPLOYEE_KITCHEN_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faKitchenSet}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Kitchen</span>
              </Link>
              <Link
                to={EMPLOYEE_TABLES_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === EMPLOYEE_TABLES_URI
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
                to={PAYMENT_CHECKOUT_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === PAYMENT_CHECKOUT_URI
                    ? styles["active"]
                    : ""
                }`}
              >
                <FontAwesomeIcon
                  icon={faReceipt}
                  className={styles["nav_icon"]}
                />
                <span className={styles["nav_name"]}>Payments</span>
              </Link>
              <Link
                to={EMPLOYEE_ORDERS_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === EMPLOYEE_ORDERS_URI
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
                to={EMPLOYEE_RESERVATIONS_URI}
                className={`${styles["nav_link"]} ${
                  window.location.pathname === EMPLOYEE_RESERVATIONS_URI
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
                to={EMPLOYEE_NOTIFICATIONS_URI}
                className={`${styles["nav_link"]} ${
                  location.pathname === EMPLOYEE_NOTIFICATIONS_URI
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
                      to={EMPLOYEE_CHANGE_PASSWORD_URI}
                      className={`${styles["nav_dropdown-link"]} ${
                        location.pathname === EMPLOYEE_CHANGE_PASSWORD_URI
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