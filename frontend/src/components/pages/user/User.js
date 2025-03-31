import { Route, Routes } from "react-router";
import UserSidebar from "../../layouts/UserSidebar";
import AdminDashboard from "./admin/AdminDashboard";
import ProductManagement from "./admin/ProductManagement";
import ProductAdmin from "./admin/ProductAdmin";

export default function UserComponent(){
    return(
        <div id="user-dashboard">
            <UserSidebar/>
            <main className="window-main">
                {/* <UserHeader/> */}
                <div className="window-main-body h-100">
                    <Routes>
                        <Route path="*" element={<AdminDashboard/>}/>
                        <Route path="/products" element={<ProductManagement/>} />
                        <Route path="/product" element={<ProductAdmin/>} />
                    </Routes>;
                    
                </div>
            </main>
            {/* <div className="window">
                <div className="col-md-3" style={{height:"100vh"}}>
                </div>
                <div className="col-md-9" style={{height:"100vh"}}>
                </div>
            </div> */}
        </div>
    )
}