import { useEffect, useRef } from "react";
import { Link, useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { 
    GALLERY_URI, 
    HOME_URI, 
    MENU_URI, 
    RESERVATION_URI 
} from "../../constants/WebPageURI";
import Logo from "./Logo";

export default function Header() {
    const headerRef = useRef();
    const location = useLocation();

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
                                            <a className="text-white btn login-btn">
                                                <FontAwesomeIcon icon={faUser} className="text-white" />&nbsp;Login
                                            </a>
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