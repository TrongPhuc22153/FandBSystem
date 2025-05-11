import { Navigate, useLocation, Outlet } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { LOGIN_URI } from "../../constants/routes";
import { Forbidden } from "../Forbidden/Forbidden";
const Authorization = ({ roles }) => {
  const { user } = useAuth();
  const location = useLocation();

  if (user.username) {
    const userrole = user.roles;
    const isAllowed = roles.some((allowed) => userrole.includes(allowed));
    return isAllowed ? <Outlet /> : <Forbidden />;
  }
  return (
    <Navigate to={LOGIN_URI} state={{ path: location.pathname }} replace />
  );
};
export default Authorization;
