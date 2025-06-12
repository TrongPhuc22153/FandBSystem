import useSWR from "swr";
import {
  fetchUsers,
  createUser,
  deleteUser,
  changePassword,
} from "../api/usersApi";
import { useCallback, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { USERS_ENDPOINT } from "../constants/api";

export const useUsers = ({
  username,
  email,
  role,
  search,
  page = 0,
  size = 10,
} = {}) => {
  const { token } = useAuth();
  return useSWR([USERS_ENDPOINT, username, email, role, page, size, search], () =>
    fetchUsers({ username, email, role, page, size, search, token }),
    { keepPreviousData: true }
  );
};

export const useUserActions = () => {
  const { token, user } = useAuth();
  const [createError, setCreateError] = useState(null);
  const [deleteError, setDeleteError] = useState(null);
  const [changePasswordError, setChangePasswordError] = useState(null);

  const [createLoading, setCreateLoading] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [loadingChangePassword, setLoadingChangePassword] = useState(false);

  const [createSuccess, setCreateSuccess] = useState(null);
  const [deleteSuccess, setDeleteSuccess] = useState(null);
  const [changePasswordSuccess, setChangePasswordSuccess] = useState(null);

  const handleChangePassword = useCallback(
    async (passwordData) => {
      setLoadingChangePassword(true);
      setChangePasswordError(null);
      setChangePasswordSuccess(null);
      try {
        const response = await changePassword({
          userId: user.userId,
          oldPassword: passwordData.oldPassword,
          newPassword: passwordData.newPassword,
          token: token,
        });
        setChangePasswordSuccess(
          response?.message || "Password updated successfully"
        );
        return response;
      } catch (error) {
        setChangePasswordError(error);
        return null;
      } finally {
        setLoadingChangePassword(false);
      }
    },
    [token, user.userId]
  );

  const handleCreateUser = useCallback(
    async (requestData) => {
      setCreateError(null);
      setCreateSuccess(null);
      setCreateLoading(true);
      try {
        const response = await createUser({
          requestUserDTO: requestData,
          token: token,
        });
        setCreateSuccess(response.message);
        return response.data;
      } catch (error) {
        setCreateError(error);
        return null;
      } finally {
        setCreateLoading(false);
      }
    },
    [token]
  );

  const handleDeleteUser = useCallback(
    async (userId, enabled) => {
      setDeleteError(null);
      setDeleteSuccess(null);
      setDeleteLoading(true);
      try {
        const response = await deleteUser({
          id: userId,
          enabled: enabled,
          token: token,
        });
        setDeleteSuccess(response.message);
        return response.message;
      } catch (error) {
        setDeleteError(error);
        return null;
      } finally {
        setDeleteLoading(false);
      }
    },
    [token]
  );

  return {
    handleChangePassword,
    loadingChangePassword,
    changePasswordError,
    changePasswordSuccess,
    resetChangePassword: useCallback(() => {
      setChangePasswordError(null);
      setChangePasswordSuccess(null);
    }, []),

    handleCreateUser,
    createError,
    createLoading,
    createSuccess,
    resetCreate: useCallback(() => {
      setCreateError(null);
      setCreateSuccess(null);
    }, []),

    handleDeleteUser,
    deleteError,
    deleteLoading,
    deleteSuccess,
    resetDelete: useCallback(() => {
      setDeleteError(null);
      setDeleteSuccess(null);
    }, []),
  };
};
