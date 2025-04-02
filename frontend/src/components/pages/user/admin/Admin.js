import { Route, Routes } from "react-router";
import AdminDashboard from "./AdminDashboard";
import OrdersAdmin from "./order/OrdersAdmin";
import ProductManagement from "./product/ProductManagement";
import ProductAdmin from "./product/ProductAdmin";
import OrderDetailsAdmin from "./order/OrderDetailsAdmin";
import CustomersAdmin from "./customer/CustomersAdmin";

export default function AdminComponent() {
  return (
    <Routes>
      <Route path="*" element={<AdminDashboard />} />
      <Route path="/orders" element={<OrdersAdmin />} />
      <Route path="/order" element={<OrderDetailsAdmin />} />
      <Route path="/products" element={<ProductManagement />} />
      <Route path="/product" element={<ProductAdmin />} />
      <Route path="/customers" element={<CustomersAdmin />} />
    </Routes>
  );
}
