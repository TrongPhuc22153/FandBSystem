import CustomerSidebar from "../../../layouts/Sidebar/CustomerSidebar";
import UserHeader from "../../../layouts/UserHeader";
import CustomerDashboard from "./CustomerDashboard";
import NotificationsCustomer from "./notification/NotificationsCustomer";
import OrderDetailsCustomer from "./order/OrderDetailsCustomer";
import OrdersCustomer from "./order/OrdersCustomer";
import { Route, Routes } from "react-router-dom";

export default function CustomerPage() {
  return (
    <>
      <CustomerSidebar/>
      <main className="window-main">
        <UserHeader/>
        <div className="window-main-body h-100">
          <Routes>
            <Route path="*" element={<CustomerDashboard />} />
            <Route path="/orders" element={<OrdersCustomer />} />
            <Route path="/order" element={<OrderDetailsCustomer />} />
            <Route path="/notifications" element={<NotificationsCustomer />} />
          </Routes>
        </div>
      </main>
    </>
  );
}