import { Route, Routes } from "react-router";
import UserSidebar from "../../layouts/UserSidebar";
import AdminComponent from "./admin/Admin";

export default function UserComponent() {
  return (
    <div id="user-dashboard">
      <UserSidebar />
      <main className="window-main">
        {/* <UserHeader/> */}
        <div className="window-main-body h-100">
          <Routes>
            <Route path="/admin/*" element={<AdminComponent />} />
          </Routes>
        </div>
      </main>
    </div>
  );
}
