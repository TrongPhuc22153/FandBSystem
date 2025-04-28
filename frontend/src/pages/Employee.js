import EmployeeSidebar from "../components/EmployeeSidebar";
import UserHeader from "../components/UserHeader";
import { Outlet } from "react-router-dom";


export default function EmployeePage() {
    return (
        <>
            <EmployeeSidebar />
            <main className="window-main">
                <UserHeader />
                <div className="window-main-body h-100">
                    <Outlet/>
                </div>
            </main>
        </>
    )
}