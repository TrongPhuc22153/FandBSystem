import { useCallback, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { TABLE_OCCUPANCIES_ENDPOINT } from "../constants/api";
import { SORTING_DIRECTIONS } from "../constants/webConstant";
import useSWR from "swr";
import { createTableOccupanncy, fetchTableOccupancies, fetchTableOccupancy, updateTableOccupancy, updateTableOccupancyStatus } from "../api/tableOccupanciesApi";

export const useTableOccupancies = ({
  page = 0,
  size = 10,
  direction = SORTING_DIRECTIONS.ASC,
  field = "createdAt",
  status,
} = {}) => {
  const { token } = useAuth();

  return useSWR(
    token ? [TABLE_OCCUPANCIES_ENDPOINT, page, size, direction, field, status, token] : null,
    () =>
      fetchTableOccupancies({
        page,
        size,
        direction,
        field,
        status,
        token,
      }),
    { keepPreviousData: true }
  );
};

export const useTableOccupancy = (id) => {
  const { token } = useAuth();

  return useSWR(id && token ? [TABLE_OCCUPANCIES_ENDPOINT, id, token] : null, () =>
    fetchTableOccupancy({ id, token })
  );
};

export const useTableOccupancyActions = () => {
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

  const handleCreateTableOccupancy = useCallback(
    async ({ contactName, phone, partySize, notes, type, reservationId }) => {
      setCreateError(null);
      setCreateSuccess(null);
      setCreateLoading(true);
      try {
        const response = await createTableOccupanncy({ 
          contactName, 
          phone, 
          partySize, 
          notes, 
          reservationId,
          type, 
          token 
        });
        setCreateSuccess(response?.message || "Table occupancy created successfully");
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

  const handleUpdateTableOccupancy = useCallback(
    async (id, requestData) => {
      setUpdateError(null);
      setUpdateSuccess(null);
      setUpdateLoading(true);
      try {
        const response = await updateTableOccupancy({ id, data: requestData, token });
        setUpdateSuccess(response?.message || "Table occupancy updated successfully");
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

  const handleUpdateTableOccupancyStatus = useCallback(
    async (id, { status, tableId }) => {
      setUpdateStatusError(null);
      setUpdateStatusSuccess(null);
      setUpdateStatusLoading(true);
      try {
        const response = await updateTableOccupancyStatus({ id, status, tableId, token });
        setUpdateStatusSuccess(response?.message || "Table occupancy status updated successfully");
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
    handleCreateTableOccupancy,
    createError,
    createLoading,
    createSuccess,
    resetCreate: useCallback(() => {
      setCreateError(null);
      setCreateSuccess(null);
    }, []),

    handleUpdateTableOccupancy,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate: useCallback(() => {
      setUpdateError(null);
      setUpdateSuccess(null);
    }, []),

    handleUpdateTableOccupancyStatus,
    updateStatusError,
    updateStatusLoading,
    updateStatusSuccess,
    resetUpdateStatus: useCallback(() => {
      setUpdateStatusError(null);
      setUpdateStatusSuccess(null);
    }, [])

  };
};