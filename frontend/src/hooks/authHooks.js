import { register } from "../api/authenticatoinApi"
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
        resetRegister: () => {
            setRegisterError(null);
            setRegisterSuccess(null);
        }
    }

}