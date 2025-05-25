import { useCallback, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { createWaitList, fetchWaitListById, fetchWaitLists, updateWaitList, updateWaitListStatus } from "../api/waitingListApi";
import { WAITLISTS_ENDPOINT } from "../constants/api";
import { SORTING_DIRECTIONS } from "../constants/webConstant";
import useSWR from "swr";

export const useWaitingLists = ({
  page = 0,
  size = 10,
  direction = SORTING_DIRECTIONS.ASC,
  field = "createdAt",
  status,
} = {}) => {
  const { token } = useAuth();

  return useSWR(
    token ? [WAITLISTS_ENDPOINT, page, size, direction, field, status, token] : null,
    () =>
      fetchWaitLists({
        page,
        size,
        direction,
        field,
        status,
        token,
      })
  );
};

export const useWaitingList = ({ id }) => {
  const { token } = useAuth();

  return useSWR(id && token ? [WAITLISTS_ENDPOINT, id, token] : null, () =>
    fetchWaitListById({ id, token })
  );
};

export const useWaitingListActions = () => {
  const { token } = useAuth();

  const [createError, setCreateError] = useState(null);
  const [createLoading, setCreateLoading] = useState(false);
  const [createSuccess, setCreateSuccess] = useState(null);

  const [updateError, setUpdateError] = useState(null);
  const [updateLoading, setUpdateLoading] = useState(false);
  const [updateSuccess, setUpdateSuccess] = useState(null);

  const [updateStatusError, setUpdateStatusError] = useState(null);
  const [updateStatusLoading, setUpdateStatusLoading] = useState(false);
  const [updateStatusSuccess, setUpdateStatusSuccess] = useState(null);

  const handleCreateWaitingList = useCallback(
    async ({ contactName, phone, partySize, notes }) => {
      setCreateError(null);
      setCreateSuccess(null);
      setCreateLoading(true);
      try {
        const response = await createWaitList({ contactName, phone, partySize, notes, token });
        setCreateSuccess(response?.message || "WaitingList created successfully");
        return response;
      } catch (error) {
        setCreateError(error);
        return null;
      } finally {
        setCreateLoading(false);
      }
    },
    [token]
  );

  const handleUpdateWaitingList = useCallback(
    async (id, requestData) => {
      setUpdateError(null);
      setUpdateSuccess(null);
      setUpdateLoading(true);
      try {
        const response = await updateWaitList({ id, requestWaitListDTO: requestData, token });
        setUpdateSuccess(response?.message || "WaitingList updated successfully");
        return response;
      } catch (error) {
        setUpdateError(error);
        return null;
      } finally {
        setUpdateLoading(false);
      }
    },
    [token]
  );

  const handleUpdateWaitingListStatus = useCallback(
    async (id, { status, tableId }) => {
      setUpdateStatusError(null);
      setUpdateStatusSuccess(null);
      setUpdateStatusLoading(true);
      try {
        const response = await updateWaitListStatus({ id, status, tableId, token });
        setUpdateStatusSuccess(response?.message || "WaitingList status updated successfully");
        return response;
      } catch (error) {
        setUpdateStatusError(error);
        return null;
      } finally {
        setUpdateStatusLoading(false);
      }
    },
    [token]
  );

  return {
    handleCreateWaitingList,
    createError,
    createLoading,
    createSuccess,
    resetCreate: useCallback(() => {
      setCreateError(null);
      setCreateSuccess(null);
    }, []),

    handleUpdateWaitingList,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate: useCallback(() => {
      setUpdateError(null);
      setUpdateSuccess(null);
    }, []),

    handleUpdateWaitingListStatus,
    updateStatusError,
    updateStatusLoading,
    updateStatusSuccess,
    resetUpdateStatus: useCallback(() => {
      setUpdateStatusError(null);
      setUpdateStatusSuccess(null);
    }, [])

  };
};