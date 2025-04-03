import { Route, Routes } from "react-router";
import UserSidebar from "../../layouts/UserSidebar";
import AdminComponent from "./admin/Admin";
import CustomerComponent from "./customer/Customer";

export default function UserComponent() {
  return (
    <div id="user-dashboard">
      <UserSidebar />
      <main className="window-main">
        {/* <UserHeader/> */}
        <div className="window-main-body h-100">
          <Routes>
            <Route path="/admin/*" element={<AdminComponent />} />
            <Route path="/customer/*" element={<CustomerComponent />} />
            {/* <Route path="/merchant/*" element={<MerchantComponent />} /> */}
            {/* <Route path="/delivery/*" element={<DeliveryComponent />} /> */}
            {/* Add other user roles here */}
          </Routes>
        </div>
      </main>
    </div>
  );
}
