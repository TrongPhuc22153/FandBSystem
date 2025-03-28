import { useContext, useEffect, useRef } from "react";
import { Link, useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCartShopping, faUser } from "@fortawesome/free-solid-svg-icons";
import { 
    GALLERY_URI, 
    HOME_URI, 
    MENU_URI, 
    RESERVATION_URI 
} from "../../constants/WebPageURI";
import Logo from "./Logo";
import AuthContext from "../../context/AuthProvider";
import { getDefaultUser } from "../../services/ImageService";

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
                                        <Logo/>
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
                                            <li className={`nav-item ${isActive(GALLERY_URI) ? "active" : ""}`}>
                                                <Link className="nav-link" to={GALLERY_URI}>Gallery</Link>
                                            </li>
                                            <li className={`nav-item ${isActive(RESERVATION_URI) ? "active" : ""}`}>
                                                <Link className="nav-link" to={RESERVATION_URI}>Reservation</Link>
                                            </li>
                                        </ul>
                                        <div className="navbar-text d-flex align-items-center">
                                            {userInfo.isAuth ?
                                                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                                                    <li className={`nav-item d-flex ${isActive(HOME_URI) ? "active" : ""}`}>
                                                        <img src={userInfo.info.image || getDefaultUser()} class="user-image" alt={userInfo.username}/>
                                                        <span className="nav-link" to={HOME_URI}>
                                                            {userInfo.info.username}
                                                        </span>
                                                    </li>
                                                    <li className={`nav-item ${isActive(MENU_URI) ? "active" : ""}`}>
                                                        <Link className="nav-link" to={MENU_URI}>
                                                            <FontAwesomeIcon icon={faCartShopping} className="text-white" />&nbsp;Cart
                                                        </Link>
                                                    </li>
                                                </ul>:
                                                <a className="text-white btn login-btn">
                                                    <FontAwesomeIcon icon={faUser} className="text-white" />&nbsp;Login
                                                </a>
                                            }
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