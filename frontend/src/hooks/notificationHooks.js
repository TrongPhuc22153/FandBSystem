import useSWR from "swr";
import { USER_NOTIFICATIONS_ENDPOINT } from "../shared/constants/api";
import { useAuth } from "../shared/context/AuthContext";
import { useCallback, useState } from "react";
import {
  fetchNotifications,
  updateNotificationIsReadStatus,
} from "../api/notificationApi";
import { SORTING_DIRECTIONS } from "../shared/constants/webConstant";
import {useSelector} from "react-redux";
import useSignedUser from "../features/users/hooks/useSignedUser";

export const useNotifications = ({
  page = 0,
  size = 10,
  field = "createdAt",
  isRead,
  direction = SORTING_DIRECTIONS.DESC,
} = {}) => {
    const { user } = useSignedUser();
  const {token} = useSelector((state) => state.auth);
  return useSWR(
    token
      ? [
          USER_NOTIFICATIONS_ENDPOINT,
          page,
          size,
          field,
          direction,
          isRead,
          token,
          user.username,
        ]
      : null,
    () => fetchNotifications({ page, size, field, direction, isRead, token })
  );
};

export const useNotificationActions = () => {
  const { token } = useAuth();
  const [updateError, setUpdateError] = useState(null);
  const [updateLoading, setUpdateLoading] = useState(false);
  const [updateSuccess, setUpdateSuccess] = useState(null);

  const handleUpdateIsReadStatus = useCallback(
    async (notificationId, isRead) => {
      if (!token) {
        return null;
      }
      setUpdateError(null);
      setUpdateSuccess(null);
      setUpdateLoading(true);
      try {
        const responseData = await updateNotificationIsReadStatus({
          notificationId,
          isRead,
          token,
        });
        setUpdateSuccess(
          responseData?.message || "Notification updated successfully"
        );
        return responseData.data;
      } catch (error) {
        setUpdateError(error);
        return null;
      } finally {
        setUpdateLoading(false);
      }
    },
    [token]
  );

  const resetUpdate = useCallback(() => {
    setUpdateError(null);
    setUpdateSuccess(null);
  }, []);

  return {
    handleUpdateIsReadStatus,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate,
  };
};
