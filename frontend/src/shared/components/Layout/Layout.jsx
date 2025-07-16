import { Outlet } from "react-router-dom";
import Header from "../Header/Header";
import Footer from "../Footer/Footer";

const Layout = () => {
  return (
    <>
      <Header />
      <div className="container-fluid px-5" style={{ minHeight: "90vh" }}>
        <Outlet />
      </div>
      <Footer />
    </>
  );
};
export default Layout;
