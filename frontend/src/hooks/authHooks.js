import { forgotPassword, register, resetPassword, validateResetToken } from "../api/authenticatoinApi"
import { useCallback, useState } from "react"

export const useAuthActions = () => {
    const [registerError, setRegisterError] = useState(null);
    const [loadingRegister, setLoadingRegister] = useState(false);
    const [registerSuccess, setRegisterSuccess] = useState(null);

    const handleRegisterUser = useCallback(async (registerData) => {
        setLoadingRegister(true)
        setRegisterError(null)
        setRegisterSuccess(null)
        try {
            const response = await register({
                username: registerData.username,
                email: registerData.email,
                password: registerData.password,
                firstName: registerData.firstName,
                lastName: registerData.lastName
            })
            setRegisterSuccess(response?.message || "Register successfully");
            return response.data
        } catch (error) {
            setRegisterError(error)
            return null;
        } finally {
            setLoadingRegister(false);
        }
    }, [])

    return {
        handleRegisterUser,
        loadingRegister,
        registerError,
        registerSuccess,
        resetRegister: useCallback(() => {
            setRegisterError(null);
            setRegisterSuccess(null);
        }, []),
    }
}

export const useForgotPasswordActions = () => {
    const [forgotPasswordError, setForgotPasswordError] = useState(null);
    const [loadingForgotPassword, setLoadingForgotPassword] = useState(false);
    const [forgotPasswordSuccess, setForgotPasswordSuccess] = useState(null);

    const [validateTokenError, setValidateTokenError] = useState(null);
    const [loadingValidateToken, setLoadingValidateToken] = useState(false);
    const [validateTokenSuccess, setValidateTokenSuccess] = useState(null);

    const [resetPasswordError, setResetPasswordError] = useState(null);
    const [loadingResetPassword, setLoadingResetPassword] = useState(false);
    const [resetPasswordSuccess, setResetPasswordSuccess] = useState(null);

    const handleForgotPassword = useCallback(async (email) => {
        setLoadingForgotPassword(true);
        setForgotPasswordError(null);
        setForgotPasswordSuccess(null);
        try {
            const response = await forgotPassword({ email });
            setForgotPasswordSuccess(response?.message || "Password reset link sent successfully.");
            return response;
        } catch (error) {
            setForgotPasswordError(error);
            return null;
        } finally {
            setLoadingForgotPassword(false);
        }
    }, []);

    const handleValidateToken = useCallback(async (token) => {
        setLoadingValidateToken(true);
        setValidateTokenError(null);
        setValidateTokenSuccess(null);
        try {
            const response = await validateResetToken({ token });
            setValidateTokenSuccess(response?.message || "Token is valid.");
            return response;
        } catch (error) {
            setValidateTokenError(error);
            return null;
        } finally {
            setLoadingValidateToken(false);
        }
    }, []);

    const handleResetPassword = useCallback(async (resetData) => {
        setLoadingResetPassword(true);
        setResetPasswordError(null);
        setResetPasswordSuccess(null);
        try {
            const response = await resetPassword({
                token: resetData.token,
                newPassword: resetData.newPassword,
            });
            setResetPasswordSuccess(response?.message || "Password reset successfully.");
            return response;
        } catch (error) {
            setResetPasswordError(error);
            return null;
        } finally {
            setLoadingResetPassword(false);
        }
    }, []);

    return {
        handleForgotPassword,
        loadingForgotPassword,
        forgotPasswordError,
        forgotPasswordSuccess,
        resetForgotPasswordState: useCallback(() => {
            setForgotPasswordError(null);
            setForgotPasswordSuccess(null);
        }, []),

        handleValidateToken,
        loadingValidateToken,
        validateTokenError,
        validateTokenSuccess,
        resetValidateTokenState: useCallback(() => {
            setValidateTokenError(null);
            setValidateTokenSuccess(null);
        }, []),

        handleResetPassword,
        loadingResetPassword,
        resetPasswordError,
        resetPasswordSuccess,
        resetResetPasswordState: useCallback(() => {
            setResetPasswordError(null);
            setResetPasswordSuccess(null);
        }, []),
    };
};