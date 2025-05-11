import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { LOGIN_URI } from "../../constants/routes";
import Loading from "../Loading/Loading";

const PrivateRoute = () => {
  const { user, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return <Loading />;
  }

  return user ? (
    <Outlet />
  ) : (
    <Navigate to={LOGIN_URI} state={{ from: location }} replace />
  );
};
export default PrivateRoute;
