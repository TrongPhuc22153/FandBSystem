import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import { login, logout, resetPassword } from "../api/authenticatoinApi";
import useSWR, { useSWRConfig } from "swr";
import { USER_ENDPOINT } from "../constants/api";
import { fetchUser } from "../api/usersApi";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import { ADMIN_DASHBOARD_URI, HOME_URI, LOGIN_URI } from "../constants/routes";
import { hasRole } from "../utils/authUtils";
import { ROLES } from "../constants/roles";
import { PASSWORD_RESET_REQUIRED_ERROR } from "../constants/error";

const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {
  const [loginError, setLoginError] = useState(null);
  const [loginLoading, setLoginLoading] = useState(false);
  const [loginSuccess, setLoginSuccess] = useState(null);
  const resetLogin = useCallback(() => {
    setLoginError(null);
    setLoginSuccess(null);
  }, []);

  const [isResetPassword, setIsResetPassword] = useState(
    Cookies.get("reset_password_required") === "true" || false
  );
  const [resetPasswordSuccess, setResetPasswordSuccess] = useState(null);
  const [resetPasswordError, setResetPasswordError] = useState(null);
  const [resetPasswordLoading, setResetPasswordLoading] = useState(false);
  const resetResetPassword = useCallback(() => {
    setResetPasswordError(null);
    setResetPasswordSuccess(null);
  }, []);

  const [token, setToken] = useState(Cookies.get("access_token") || null);
  const { mutate } = useSWRConfig();
  const navigate = useNavigate();

  useEffect(() => {
    const validateResetRequirement = async () => {
      if (token && isResetPassword) {
        try {
          await fetchUser({ token });
        } catch (error) {
          if (error?.error === PASSWORD_RESET_REQUIRED_ERROR) {
            setIsResetPassword(true);
            Cookies.set("reset_password_required", "true", {
              expires: 7,
              sameSite: "Strict",
              secure: true,
            });
          } else {
            setIsResetPassword(false);
            Cookies.remove("reset_password_required", { path: "/" });
          }
        }
      }
    };
    validateResetRequirement();
  }, [token, isResetPassword]);

  const {
    data: user,
    error,
    isLoading,
  } = useSWR(token ? USER_ENDPOINT : null, () => fetchUser({ token: token }), {
    revalidateIfStale: false,
    revalidateOnFocus: false,
    revalidateOnReconnect: false,
    revalidateOnMount: true,
    onError: (err) => {
      if (err.status === 401) {
        setToken(null);
        Cookies.remove("access_token", { path: "/" });
        Cookies.remove("reset_password_required", { path: "/" });
        setIsResetPassword(false);
        navigate(LOGIN_URI);
      }
    },
  });

  const handleResetPassword = useCallback(
    async (newPassword) => {
      setResetPasswordError(null);
      setResetPasswordLoading(true);
      setResetPasswordSuccess(null);
      try {
        const { message, data } = await resetPassword({
          token,
          newPassword,
        });
        setResetPasswordSuccess(message || "Your password reset successfully");
        const accessToken = data.accessToken;
        Cookies.set("access_token", accessToken, {
          expires: 7,
          sameSite: "Strict",
        });
        setToken(accessToken);
        const userData = await fetchUser({ token: accessToken });
        await mutate(USER_ENDPOINT, userData, { revalidate: false });
        setIsResetPassword(false);
        Cookies.remove("reset_password_required", { path: "/" });
      } catch (error) {
        setResetPasswordError(error);
      } finally {
        setResetPasswordLoading(false);
      }
    },
    [token, mutate]
  );

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
        if (error?.error === PASSWORD_RESET_REQUIRED_ERROR) {
          setIsResetPassword(true);
          Cookies.set("reset_password_required", "true", {
            expires: 7,
            sameSite: "Strict",
            secure: true,
          });
        }
        setLoginError(error);
        return;
      } finally {
        setLoginLoading(false);
      }
    },
    [mutate, navigate]
  );

  const logoutAction = useCallback(async () => {
    setLoginError(null);
    setLoginSuccess(null);
    try {
      const currentToken = Cookies.get("access_token");
      if (currentToken) {
        await logout({ token: currentToken });
      }
      setToken(null);
      Cookies.remove("access_token", { path: "/" });
      Cookies.remove("reset_password_required", { path: "/" });
      setIsResetPassword(false);
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
        resetLogin,
        logoutAction,
        isResetPassword,
        handleResetPassword,
        resetPasswordError,
        resetPasswordSuccess,
        resetPasswordLoading,
        resetResetPassword,
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
