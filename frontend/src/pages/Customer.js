import CustomerSidebar from "../components/CustomerSidebar";
import UserHeader from "../components/UserHeader";
import { Outlet } from "react-router-dom";

export default function CustomerPage() {
  return (
    <>
      <CustomerSidebar/>
      <main className="window-main">
        <UserHeader/>
        <div className="window-main-body h-100">
          <Outlet/>
          {/* <Routes>
            <Route path="*" element={<CustomerDashboard />} />
            <Route path="/orders" element={<OrdersCustomer />} />
            <Route path="/order" element={<OrderDetailsCustomer />} />
            <Route path="/notifications" element={<NotificationsCustomer />} />
          </Routes> */}
        </div>
      </main>
    </>
  );
}