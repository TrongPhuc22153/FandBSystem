import React from "react";
import { Navigate, useLocation, Outlet } from "react-router-dom";
import Unauthorized from "../components/UnauthorizedPage";
import { useAuth } from "../hooks/AuthContext";
import { LOGIN_URI } from "../constants/WebPageURI";
const Authorization = ({ roles }) => {
    const { user } = useAuth();
    const location = useLocation();
    if (user.username) {
        const userrole = user.roles;
        const isAllowed = roles.some((allowed) => userrole.includes(allowed));
        return isAllowed ? <Outlet /> : <Unauthorized />;
    }
    return <Navigate to={LOGIN_URI} state={{ path: location.pathname }} replace />;
};
export default Authorization;