import { createContext, useCallback, useContext, useState } from "react";
import { login, logout } from "../api/authenticatoinApi";
import useSWR, { useSWRConfig } from "swr";
import { USER_ENDPOINT } from "../constants/api";
import { fetchUser } from "../api/usersApi";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import { ADMIN_DASHBOARD_URI, HOME_URI, LOGIN_URI } from "../constants/routes";
import { hasRole } from "../utils/authUtils";
import { ROLES } from "../constants/roles";

const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {
  const [loginError, setLoginError] = useState(null);
  const [loginLoading, setLoginLoading] = useState(false);
  const [loginSuccess, setLoginSuccess] = useState(null);

  const [token, setToken] = useState(Cookies.get("access_token") || null);
  const { mutate } = useSWRConfig();
  const navigate = useNavigate();

  const {
    data: user,
    error,
    isLoading,
  } = useSWR(token ? USER_ENDPOINT : null, () => fetchUser({ token: token }), {
    revalidateIfStale: false,
    revalidateOnFocus: false,
    revalidateOnReconnect: false,
    revalidateOnMount: true,
  });

  const loginAction = useCallback(
    async (credential) => {
      setLoginError(null);
      setLoginSuccess(null);
      setLoginLoading(true);
      try {
        const { message, data } = await login({
          username: credential.username,
          password: credential.password,
        });

        Cookies.set("access_token", data.accessToken, {
          expires: 7,
          sameSite: "Strict",
        });
        setToken(data.accessToken);

        const userData = await fetchUser({ token: data.accessToken });

        await mutate(USER_ENDPOINT, userData, { revalidate: false });
        setLoginSuccess(message || "Logged in successfully");

        if (hasRole(userData, ROLES.ADMIN)) {
          navigate(ADMIN_DASHBOARD_URI);
        } else {
          navigate(HOME_URI);
        }
        return;
      } catch (error) {
        setLoginError(error);
        return;
      } finally {
        setLoginLoading(false);
      }
    },
    [mutate, navigate]
  );

  const logoutAction = useCallback(async () => {
    try {
      const currentToken = Cookies.get("access_token");
      if (currentToken) {
        await logout({ token: currentToken });
      }
      setToken(null);
      Cookies.remove("access_token", { path: "/" });

      await mutate(USER_ENDPOINT, null, { revalidate: false });
      navigate(LOGIN_URI);
      return;
    } catch (error) {
      return;
    }
  }, [mutate, navigate]);

  return (
    <AuthContext.Provider
      value={{
        token,
        user,
        isLoading,
        error,
        loginAction,
        loginSuccess,
        loginError,
        loginLoading,
        logoutAction,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;

export const useAuth = () => {
  return useContext(AuthContext);
};
