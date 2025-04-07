import { Route, Routes } from "react-router";
import AdminPage from "./admin/Admin";
import CustomerPage from "./customer/Customer";
import { EmployeePage } from "./employee/Employee";

export default function UserPage() {
  return (
    <div id="user-dashboard">
      <Routes>
        <Route path="/admin/*" element={<AdminPage />} />
        <Route path="/customer/*" element={<CustomerPage />} />
        <Route path="/employee/*" element={<EmployeePage/>}/>
        {/* Add other user roles here */}
      </Routes>
    </div>
  );
}
