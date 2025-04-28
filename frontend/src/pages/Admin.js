import { Outlet } from "react-router";
import AdminSidebar from "../components/AdminSidebar";
import UserHeader from "../components/UserHeader";

export default function AdminPage() {
  return (
    <>
      <AdminSidebar />
      <main className="window-main">
        <UserHeader/>
        <div className="window-main-body h-100">
          <Outlet/>
        </div>
      </main>
    
    </>
  );
}
