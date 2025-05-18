import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCartShopping, faUser } from "@fortawesome/free-solid-svg-icons";
import Logo from "../Logo/Logo";
import { getImageSrc } from "../../utils/imageUtils";
import { useAuth } from "../../context/AuthContext";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import {
  CART_URI,
  HOME_URI,
  LOGIN_URI,
  SHOP_URI,
  ADMIN_DASHBOARD_URI,
  REGISTER_URI,
  RESERVATION_URI,
  USER_PROFILE_URI,
  EMPLOYEE_PROFILE_URI,
} from "../../constants/routes";
import { Nav, NavDropdown, Navbar } from "react-bootstrap";
import { useModal } from "../../context/ModalContext";
import { useCallback } from "react";
import styles from "./Header.module.css";
import NotificationDropdown from "../NotificationDropdown/NotificationDropdown";
import SearchBar from "../Searchbar/SearchBar";

export default function Header() {
  const { user, logoutAction } = useAuth();
  const { onOpen } = useModal();

  const showConfirmModal = useCallback(() => {
    onOpen({
      title: "Logout",
      message: "Do you want to logout?",
      onYes: logoutAction,
    });
  }, [logoutAction, onOpen]);

  return (
    <Navbar expand="lg" id="mainNav" className={styles["main-nav"]}>
      <div className="container-fluid">
        <Navbar.Brand as={Link} to={HOME_URI} className={styles["logo-header"]}>
          <Logo />
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="nav-content" />
        <Navbar.Collapse id="nav-content" className="ms-5">
          <Nav className="me-auto">
            <Nav.Item>
              <Nav.Link
                as={Link}
                to={HOME_URI}
                className={`${styles["nav-link"]} ${styles["nav-link-home"]}`}
              >
                Home
              </Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link
                as={Link}
                to={SHOP_URI}
                className={`${styles["nav-link"]} ${styles["nav-link-shop"]}`}
              >
                Shop
              </Nav.Link>
            </Nav.Item>
            {hasRole(user, ROLES.CUSTOMER) && (
              <Nav.Item>
                <Nav.Link
                  as={Link}
                  to={RESERVATION_URI}
                  className={`${styles["nav-link"]} ${styles["nav-link-shop"]}`}
                >
                  Reservation
                </Nav.Link>
              </Nav.Item>
            )}
          </Nav>
          <SearchBar />
          <Nav>
            {user ? (
              <>
                <NotificationDropdown/>
                <NavDropdown
                  title={
                    <div className={`d-flex align-items-center`}>
                      <span className={styles["image"]}>
                        <img
                          src={user?.image || getImageSrc()}
                          className={styles["user-image"]}
                          alt={user?.username || "User"}
                        />
                      </span>
                      <span className={`ms-2`}>{user?.username || "User"}</span>
                    </div>
                  }
                  id={styles["user-dropdown"]}
                  className={`mx-2`}
                >
                  {hasRole(user, ROLES.CUSTOMER) && (
                    <NavDropdown.Item
                      as={Link}
                      to={USER_PROFILE_URI}
                      className={`${styles["dropdown-item"]} ${styles["dropdown-item-profile"]}`}
                    >
                      Profile
                    </NavDropdown.Item>
                  )}
                  {hasRole(user, ROLES.EMPLOYEE) && (
                    <NavDropdown.Item
                      as={Link}
                      to={EMPLOYEE_PROFILE_URI}
                      className={`${styles["dropdown-item"]} ${styles["dropdown-item-profile"]}`}
                    >
                      Profile
                    </NavDropdown.Item>
                  )}
                  {hasRole(user, ROLES.ADMIN) && (
                    <NavDropdown.Item
                      as={Link}
                      to={ADMIN_DASHBOARD_URI}
                      className={`${styles["dropdown-item"]} ${styles["dropdown-item-dashboard"]}`}
                    >
                      Dashboard
                    </NavDropdown.Item>
                  )}
                  <NavDropdown.Divider />
                  <NavDropdown.Item
                    as="button"
                    onClick={showConfirmModal}
                    className={`${styles["dropdown-item"]} ${styles["dropdown-item-logout"]}`}
                  >
                    Logout
                  </NavDropdown.Item>
                </NavDropdown>
                {hasRole(user, ROLES.CUSTOMER) && (
                  <Nav.Item>
                    <Nav.Link
                      as={Link}
                      to={CART_URI}
                      className={`d-flex align-items-center`}
                    >
                      <FontAwesomeIcon icon={faCartShopping} />
                      &nbsp;Cart
                    </Nav.Link>
                  </Nav.Item>
                )}
              </>
            ) : (
              <>
                <Nav.Item>
                  <Nav.Link as={Link} to={LOGIN_URI}>
                    <FontAwesomeIcon icon={faUser} />
                    &nbsp;Sign&nbsp;in
                  </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                  <Nav.Link as={Link} to={REGISTER_URI}>
                    Sign&nbsp;up
                  </Nav.Link>
                </Nav.Item>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </div>
    </Navbar>
  );
}
