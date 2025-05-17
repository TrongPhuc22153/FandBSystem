import useSWR from "swr";
import { useCallback, useState } from "react";
import { EMPLOYEE_PROFILE_ENDPOINT } from "../constants/api";
import {
  fetchEmployeeProfile,
  updateEmployeeProfile,
} from "../api/employeeApi";
import { useAuth } from "../context/AuthContext";

export const useEmployeeProfile = () => {
  const { token } = useAuth();

  const fetcher = async () => {
    if (!token) {
      return null;
    }
    return await fetchEmployeeProfile(token);
  };

  return useSWR(token ? EMPLOYEE_PROFILE_ENDPOINT : null, fetcher);
};

export const useEmployeeProfileActions = () => {
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
        const { message, data } = await updateEmployeeProfile(
          token,
          requestData
        );
        setUpdateSuccess(message || "Your profile updated successfully");
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