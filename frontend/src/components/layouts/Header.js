import { useContext, useEffect, useRef } from "react";
import { Link, useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCartShopping, faUser } from "@fortawesome/free-solid-svg-icons";
import { 
    CART_URI, 
    HOME_URI, 
    LOGOUT_URI, 
    MENU_URI, 
    RESERVATION_URI,
    USER_DASHBOARD_URI, 
} from "../../constants/WebPageURI";
import Logo from "./Logo";
import AuthContext from "../../context/AuthProvider";
import { getDefaultUser } from "../../services/ImageService";
import NotificationDropdown from "./NotificationDropdown";

export default function Header() {
    const headerRef = useRef();
    const location = useLocation();
    const userInfo = useContext(AuthContext);

    useEffect(() => {
        document.addEventListener('scroll', onScroll);
        return () => {
            document.removeEventListener('scroll', onScroll);
        };
    }, []);

    const onScroll = () => {
        if (headerRef.current) {
            if (window.scrollY > 0) {
                headerRef.current.classList.add("fixed-menu");
            } else {
                headerRef.current.classList.remove("fixed-menu");
            }
        }
    };

    const isActive = (path) => location.pathname === path;

    return (
        <div id="site-header">
            <header id="header" className="header-block-top" ref={headerRef}>
                <div className="container">
                    <div className="row">
                        <div className="main-menu">
                            <nav className="navbar navbar-expand-lg" id="mainNav">
                                <div className="container-fluid">
                                    <Link className="navbar-brand js-scroll-trigger logo-header" to={HOME_URI}>
                                        <Logo />
                                    </Link>
                                    <button
                                        className="navbar-toggler"
                                        type="button"
                                        data-bs-toggle="collapse"
                                        data-bs-target="#navbarText"
                                        aria-controls="navbarText"
                                        aria-expanded="false"
                                        aria-label="Toggle navigation"
                                    >
                                        <span className="navbar-toggler-icon"></span>
                                    </button>
                                    <div className="ms-5 collapse navbar-collapse" id="navbarText">
                                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                                            <li className={`nav-item ${isActive(HOME_URI) ? "active" : ""}`}>
                                                <Link className="nav-link" to={HOME_URI}>Home</Link>
                                            </li>
                                            <li className={`nav-item ${isActive(MENU_URI) ? "active" : ""}`}>
                                                <Link className="nav-link" to={MENU_URI}>Menu</Link>
                                            </li>
                                            <li className={`nav-item ${isActive(RESERVATION_URI) ? "active" : ""}`}>
                                                <Link className="nav-link" to={RESERVATION_URI}>Reservation</Link>
                                            </li>
                                        </ul>
                                        <div className="navbar-text d-flex align-items-center">
                                            {userInfo.isAuth ? (
                                                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                                                    <li className={`nav-item position-relative me-2 ${isActive(HOME_URI) ? "active" : ""}`}>
                                                        <NotificationDropdown/>
                                                    </li>
                                                    <li className={`nav-item mx-2 d-flex ${isActive(HOME_URI) ? "active" : ""}`}>
                                                        <div className="btn-group">
                                                            <button className="user-btn dropdown-toggle btn"
                                                                data-bs-toggle="dropdown"
                                                                aria-haspopup="true"
                                                                aria-expanded="false"
                                                            >
                                                                <span className="image">
                                                                    <img
                                                                        src={userInfo.info.image || getDefaultUser()}
                                                                        className="user-image"
                                                                        alt={userInfo.username}
                                                                    />
                                                                </span>
                                                                <span className="text-white ms-2 username">
                                                                    {userInfo.info.username}
                                                                </span>
                                                            </button>
                                                            <div className="dropdown-menu">
                                                                <Link className="dropdown-item" to={USER_DASHBOARD_URI}>Dashboard</Link>
                                                                <Link className="dropdown-item" to={USER_DASHBOARD_URI}>Another action</Link>
                                                                <Link className="dropdown-item" to={USER_DASHBOARD_URI}>Something else here</Link>
                                                                <div className="dropdown-divider"></div>
                                                                <a className="dropdown-item" href={LOGOUT_URI}>Logout</a>
                                                            </div>
                                                        </div>
                                                    </li>
                                                    <li className={`ms-2 cart-item d-flex align-items-center nav-item ${isActive(CART_URI) ? "active" : ""}`}>
                                                        <Link className="nav-link" to={CART_URI}>
                                                            <FontAwesomeIcon icon={faCartShopping} className="text-white" />
                                                            &nbsp;Cart
                                                        </Link>
                                                    </li>
                                                </ul>
                                            ) : (
                                                <button className="btn login-btn text-white">
                                                    <FontAwesomeIcon icon={faUser} className="text-white" />
                                                    &nbsp;Login
                                                </button>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            </nav>
                        </div>
                    </div>
                </div>
            </header>
        </div>
    );
}