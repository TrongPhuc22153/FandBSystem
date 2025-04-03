import CustomerDashboard from "./CustomerDashboard";
import NotificationsCustomer from "./notification/NotificationsCustomer";
import OrderDetailsCustomer from "./order/OrderDetailsCustomer";
import OrdersCustomer from "./order/OrdersCustomer";
import { Route, Routes } from "react-router-dom";

export default function CustomerComponent() {
  return (
    <Routes>
      <Route path="*" element={<CustomerDashboard />} />
      <Route path="/orders" element={<OrdersCustomer />} />
      <Route path="/order" element={<OrderDetailsCustomer />} />
      <Route path="/notifications" element={<NotificationsCustomer />} />
    </Routes>
  );
}