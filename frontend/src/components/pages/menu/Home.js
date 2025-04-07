import { Route, Routes } from "react-router";

import { Footer } from "../../layouts/Footer";
import Header from "../../layouts/Header";
import MenuPage from "./Menu";
import { DefaultHomePage } from "./DefaultHome";
import {
  CART_URI,
  CHECKOUT_ORDER_URI,
  MENU_URI,
  RESERVATION_URI,
} from "../../../constants/WebPageURI";
import ReservationPage from "./Reservation";
import ProductPage from "./Product";
import CartPage from "./Cart";
import CheckoutOrderPage from "./Checkout";

export default function HomePage() {
  return (
    <>
      {/* <div id="loader">
                <div id="status"></div>
            </div> */}
      <Header />

      <Routes>
        <Route path="*" element={<DefaultHomePage />} />
        <Route path={RESERVATION_URI} element={<ReservationPage />} />
        <Route path={MENU_URI} element={<MenuPage />} />
        <Route path="/foods" element={<ProductPage />} />
        <Route path={CART_URI} element={<CartPage />} />
        <Route path={CHECKOUT_ORDER_URI} element={<CheckoutOrderPage />} />
      </Routes>

      <Footer />
    </>
  );
}
