import EmployeeSidebar from "../../../layouts/Sidebar/EmployeeSidebar";
import UserHeader from "../../../layouts/UserHeader";
import EmployeeDashboard from "./EmployeeDashboard";
import NotificationsEmployee from "./notification/NotificationsEmployee";
import OrderDetailsEmployee from "./order/OrderDetailsEmployee";
import OrdersEmployee from "./order/OrdersEmployee";
import { Route, Routes } from "react-router-dom";


export function EmployeePage() {
    return (
        <>
            <EmployeeSidebar />
            <main className="window-main">
                <UserHeader />
                <div className="window-main-body h-100">
                    <Routes>
                        <Route path="*" element={<EmployeeDashboard />} />
                        <Route path="/orders" element={<OrdersEmployee />} />
                        <Route path="/order" element={<OrderDetailsEmployee />} />
                        <Route path="/notifications" element={<NotificationsEmployee />} />
                    </Routes>
                </div>
            </main>
        </>
    )
}