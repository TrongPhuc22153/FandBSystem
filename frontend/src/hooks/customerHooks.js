import useSWR from "swr";
import { useCallback, useState } from "react";
import { CUSTOMER_PROFILE_ENDPOINT } from "../shared/constants/api";
import {
  updateCustomerProfile,
  fetchCustomerProfile,
} from "../api/customerApi";
import { useAuth } from "../shared/context/AuthContext";

export const useCustomerProfile = () => {
  const { token } = useAuth();

  const fetcher = async () => {
    if (!token) {
      return null;
    }
    return await fetchCustomerProfile(token);
  };

  return useSWR(token ? CUSTOMER_PROFILE_ENDPOINT : null, fetcher);
};

export const useCustomerProfileActions = () => {
  const { token } = useAuth();
  const [updateError, setUpdateError] = useState(null);
  const [updateLoading, setUpdateLoading] = useState(false);
  const [updateSuccess, setUpdateSuccess] = useState(null);

  const handleUpdateProfile = useCallback(
    async (requestData) => {
      setUpdateError(null);
      setUpdateSuccess(null);
      setUpdateLoading(true);
      try {
        const { message, data } = await updateCustomerProfile(
          token,
          requestData
        );
        setUpdateSuccess(message || "Profile updated successfully");
        return data;
      } catch (error) {
        setUpdateError(error);
        return null;
      } finally {
        setUpdateLoading(false);
      }
    },
    [token]
  );

  return {
    handleUpdateProfile,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate: useCallback(() => {
      setUpdateError(null);
      setUpdateSuccess(null);
    }, []),
  };
};
