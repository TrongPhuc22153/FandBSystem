import { changePassword, register } from "../api/authenticatoinApi"
import { useCallback, useState } from "react"
import { useAuth } from "../context/AuthContext";

export const useAuthActions = () => {
    const { token, user } = useAuth();
    const [registerError, setRegisterError] = useState(null);
    const [loadingRegister, setLoadingRegister] = useState(false);
    const [registerSuccess, setRegisterSuccess] = useState(null);

    const [changePasswordError, setChangePasswordError] = useState(null);
    const [loadingChangePassword, setLoadingChangePassword] = useState(false);
    const [changePasswordSuccess, setChangePasswordSuccess] = useState(null);

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
    }, [register])

    const handleChangePassword = useCallback(async (passwordData) => {
        setLoadingChangePassword(true);
        setChangePasswordError(null);
        setChangePasswordSuccess(null);
        try {
            const response = await changePassword({
                userId: user.userId,
                oldPassword: passwordData.oldPassword,
                newPassword: passwordData.newPassword,
                token: token
            });
            setChangePasswordSuccess(response?.message || "Password updated successfully");
            return response;
        } catch (error) {
            setChangePasswordError(error);
            return null;
        } finally {
            setLoadingChangePassword(false);
        }
    }, [changePassword]);

    return {
        handleRegisterUser,
        loadingRegister,
        registerError,
        registerSuccess,
        resetRegister: useCallback(() => {
            setRegisterError(null);
            setRegisterSuccess(null);
        }, []),
        
        handleChangePassword,
        loadingChangePassword,
        changePasswordError,
        changePasswordSuccess,
        resetChangePassword: useCallback(() => {
            setChangePasswordError(null);
            setChangePasswordSuccess(null);
        }, []),
    }

}