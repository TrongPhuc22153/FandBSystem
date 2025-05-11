import { Navbar } from "react-bootstrap";
import { Link, Outlet } from "react-router-dom";
import { HOME_URI } from "../../constants/routes";
import Logo from "../../components/Logo/Logo";

const AuthLayoutPage = () => {
  return (
    <div id="auth-layout">
      <Navbar className="bg-white">
        <div className="container-fluid">
          <Navbar.Brand as={Link} to={HOME_URI} className="logo-header">
            <Logo />
          </Navbar.Brand>
        </div>
      </Navbar>
      <Outlet />
    </div>
  );
};
export default AuthLayoutPage;
