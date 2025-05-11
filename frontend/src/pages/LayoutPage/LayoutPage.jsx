import { Outlet } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const LayoutPage = () => {
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
export default LayoutPage;
