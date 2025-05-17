import React, { useCallback, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars, faX } from "@fortawesome/free-solid-svg-icons";
import { getImageSrc } from "../../utils/imageUtils";
import { Outlet } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import styles from "./ProfileLayout.module.css";
import { EmpoyeeSidebar } from "../../components/Sidebar/EmployeeSidebar";
import { useStompSubscription } from "../../hooks/websocketHooks";
import { TOPIC_EMPLOYEE } from "../../constants/webSocketEnpoint";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";

function EmployeeLayout() {
  const { user } = useAuth();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const handleMessage = useCallback((message) => {
    console.log(message)
  }, [])

  useStompSubscription({
    topic: TOPIC_EMPLOYEE,
    onMessage: handleMessage,
    shouldSubscribe: hasRole(user, ROLES.EMPLOYEE),
  });

  const handleToggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  const sidebarClass = `${isSidebarOpen ? styles["show"] : ""}`;
  const headerClass = isSidebarOpen ? styles["body-pd"] : "";
  const contentAreaStyle = {
    paddingLeft: isSidebarOpen
      ? "calc(var(--nav-width) + 1rem + 250px)"
      : "calc(var(--nav-width) + 1rem",
    transition: "padding-left 0.5s ease",
  };

  return (
    <>
      <header
        className={`shadow-sm bg-white ${styles["header"]} ${headerClass}`}
        id="header"
      >
        <div className={styles["header_toggle"]} onClick={handleToggleSidebar}>
          <FontAwesomeIcon
            icon={isSidebarOpen ? faX : faBars}
            id="header-toggle"
          />
        </div>
        <div className="d-flex align-items-center">
          <span className="image">
            <img
              src={user?.image || getImageSrc()}
              className={styles["header_img"]}
              alt={user?.username || "User"}
            />
          </span>
          <span className="ms-2">{user?.username || "User"}</span>
        </div>
      </header>
      <div
        className={`${styles["sidebar"]} ${styles["l-navbar"]} ${sidebarClass}`}
        id="nav-bar"
      >
        <EmpoyeeSidebar />
      </div>

      <div
        className={`${styles["content-area"]} h-100 bg-light`}
        style={contentAreaStyle}
      >
        <Outlet />
      </div>
    </>
  );
}

export default EmployeeLayout;
