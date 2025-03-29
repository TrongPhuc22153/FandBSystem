import { Route, Routes } from "react-router";

import { Footer } from "../../layouts/Footer";
import Header from "../../layouts/Header";
import MenuComponent from "./Menu";
import { DefaultHomeComponent } from "./DefaultHome";
import { CART_URI, CHECKOUT_ORDER_URI, MENU_URI, RESERVATION_URI } from "../../../constants/WebPageURI";
import ReservationComponent from "./Reservation";
import ProductComponent from "./Product";
import CartComponent from "./Cart";
import CheckoutOrderComponent from "./Checkout";

export default function HomeComponent(){


    return(
        <>
            {/* <div id="loader">
                <div id="status"></div>
            </div> */}
            <Header/>

            <Routes>
                <Route path="*" element={<DefaultHomeComponent/>}/>
                <Route path={RESERVATION_URI} element={<ReservationComponent/>}/>
                <Route path={MENU_URI} element={<MenuComponent/>}/>
                <Route path="/foods" element={<ProductComponent/>}/>
                <Route path={CART_URI} element={<CartComponent/>}/>
                <Route path={CHECKOUT_ORDER_URI} element={<CheckoutOrderComponent/>}/>
            </Routes>

            <Footer/>
        </>
    )
}