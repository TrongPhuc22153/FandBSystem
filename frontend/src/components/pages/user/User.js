import UserContent from "../../layouts/UserContent";
import UserHeader from "../../layouts/UserHeader";
import UserSidebar from "../../layouts/UserSidebar";

export default function UserComponent(){
    return(
        <div id="user-dashboard" className="window">
            <UserSidebar/>
            <main className="window-main">
                <UserHeader/>
                <div className="window-main-body">
                    <UserContent/>
                </div>
            </main>
        </div>
    )
}