import useSWR from "swr";
import { fetchAuthenticatedUserProfile, fetchUserProfile, updateUserProfile } from "../api/userProfileApi";
import { useAuth } from "../context/AuthContext";
import { USER_PROFILES_ENDPOINT, USER_PROFILE_ENDPOINT } from "../constants/api";
import { useCallback, useState } from "react";

export const useProfile = (userId) => {
    const { token } = useAuth();
    return useSWR(userId ? `${USER_PROFILES_ENDPOINT}/${userId}` : null, () => fetchUserProfile({ userId, token }));
};

export const useAuthenticatedProfile = () => {
    const { token } = useAuth();
    return useSWR(token ? USER_PROFILE_ENDPOINT : null, () => fetchAuthenticatedUserProfile({ token }));
};

export const useAuthProfileActions = () => {
    const [updateError, setUpdateError] = useState(null);
    const [loadingUpdate, setLoadingUpdate] = useState(false);
    const [updateSuccess, setUpdateSuccess] = useState(null);

    const { token } = useAuth()

    const handleUpdateUserAuthenticatedProfile = useCallback(async (requestData) => {
        setLoadingUpdate(true)
        setUpdateError(null)
        setUpdateSuccess(null)
        try {
            const response = await updateUserProfile(token, requestData)
            setUpdateSuccess(response?.message || "Register successfully");
            return response.data
        } catch (error) {
            setUpdateError(error)
            return null;
        } finally {
            setLoadingUpdate(false);
        }
    }, [token])

    return {
        handleUpdateUserAuthenticatedProfile,
        loadingUpdate,
        updateError,
        updateSuccess,
        resetUpdateUserAuthProfile: () => {
            setUpdateError(null);
            setUpdateSuccess(null);
        }
    }
}