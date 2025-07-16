import { Navigate, Outlet, useLocation } from "react-router-dom";
import { LOGIN_URI } from "../../shared/constants/routes";
import Loading from "../../shared/components/Loading/Loading";
import useSignedUser from "../../features/users/hooks/useSignedUser";

const PrivateRoute = () => {
  const { user, isLoading } = useSignedUser();
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
