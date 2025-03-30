import AdminDashboard from "../../layouts/AdminDashboard";
import UserHeader from "../../layouts/UserHeader";
import UserSidebar from "../../layouts/UserSidebar";

export default function UserComponent(){
    return(
        <div id="user-dashboard">
            <div className="row">
                <div className="col-md-3">
                    <UserSidebar/>
                </div>
                <div className="col-md-9">
                    <main className="window-main">
                        <UserHeader/>
                        <div className="window-main-body">
                            {/* <UserContent/> */}
                            <AdminDashboard/>
                        </div>
                    </main>
                </div>
            </div>
        </div>
    )
}