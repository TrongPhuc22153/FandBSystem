import useSWR from "swr";
import { useCallback, useState } from "react";
import { useAuth } from "../context/AuthContext";
import {
  fetchReservationTables,
  fetchReservationTableById,
  updateReservationTable,
  createReservationTable,
  createReservationTablesBulk,
  updateReservationTableStatus,
  fetchAvailableTables,
} from "../api/tableApi";
import { RESERVATION_TABLES_AVAILABILITY_ENDPOINT, RESERVATION_TABLES_ENDPOINT } from "../constants/api";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

// Hook to fetch a single reservation table by ID
export const useReservationTable = ({ id }) => {
  return useSWR(id ? `reservation-table-${id}` : null, () =>
    fetchReservationTableById({ id })
  );
};

export const useAvailableTables = ({ date, time, search, tableNumber }) => {
  return useSWR([RESERVATION_TABLES_AVAILABILITY_ENDPOINT, date, time, search, tableNumber], () =>
    fetchAvailableTables({ date, time, search, tableNumber })
  );
}

// Hook to fetch all reservation tables with pagination
export const useReservationTables = ({
  page = 0,
  size = 20,
  direction = SORTING_DIRECTIONS.ASC,
  field = "tableNumber",
  isDeleted = false,
  search,
  status,
  tableNumber,
} = {}) => {
  return useSWR(
    [
      RESERVATION_TABLES_ENDPOINT,
      page,
      size,
      direction,
      field,
      isDeleted,
      search,
      status,
      tableNumber,
    ],
    () =>
      fetchReservationTables({
        page,
        size,
        direction,
        field,
        isDeleted,
        search,
        status,
        tableNumber,
      })
  );
};

// Hook for reservation table actions (create, update, delete)
export const useReservationTableActions = () => {
  const { token } = useAuth();
  const [updateError, setUpdateError] = useState(null);
  const [updateStatusError, setUpdateStatusError] = useState(null);
  const [createError, setCreateError] = useState(null);
  const [createBulkError, setCreateBulkError] = useState(null);

  const [updateLoading, setUpdateLoading] = useState(false);
  const [updateStatusLoading, setUpdateStatusLoading] = useState(false);
  const [createLoading, setCreateLoading] = useState(false);
  const [createBulkLoading, setCreateBulkLoading] = useState(false);

  const [updateSuccess, setUpdateSuccess] = useState(null);
  const [updateStatusSuccess, setUpdateStatusSuccess] = useState(null);
  const [createSuccess, setCreateSuccess] = useState(null);
  const [createBulkSuccess, setCreateBulkSuccess] = useState(null);

  const handleUpdateReservationTable = useCallback(
    async (id, requestData) => {
      setUpdateError(null);
      setUpdateSuccess(null);
      setUpdateLoading(true);
      try {
        const response = await updateReservationTable({
          id,
          tableData: requestData,
          token,
        });
        setUpdateSuccess(
          response?.message || "Reservation table updated successfully"
        );
        return response.data;
      } catch (error) {
        setUpdateError(error);
        return null;
      } finally {
        setUpdateLoading(false);
      }
    },
    [token]
  );

  const handleUpdateReservationTableStatus = useCallback(
    async ({ id, status, isDeleted }) => {
      setUpdateStatusError(null);
      setUpdateStatusSuccess(null);
      setUpdateStatusLoading(true);
      try {
        const response = await updateReservationTableStatus({
          id,
          status,
          isDeleted,
          token,
        });
        setUpdateStatusSuccess(
          response?.message || "Reservation table updated successfully"
        );
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

  const handleCreateReservationTable = useCallback(
    async (requestData) => {
      setCreateError(null);
      setCreateSuccess(null);
      setCreateLoading(true);
      try {
        const response = await createReservationTable({
          tableData: requestData,
          token,
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

  const handleCreateReservationTablesBulk = useCallback(
    async (requestData) => {
      setCreateBulkError(null);
      setCreateBulkSuccess(null);
      setCreateBulkLoading(true);
      try {
        const response = await createReservationTablesBulk({
          tablesData: requestData,
          token,
        });
        setCreateBulkSuccess(response.message);
        return response.data;
      } catch (error) {
        setCreateBulkError(error);
        return null;
      } finally {
        setCreateBulkLoading(false);
      }
    },
    [token]
  );

  return {
    handleUpdateReservationTable,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate: useCallback(() => {
      setUpdateError(null);
      setUpdateSuccess(null);
    }, []),

    handleUpdateReservationTableStatus,
    updateStatusError,
    updateStatusLoading,
    updateStatusSuccess,
    resetUpdateStatus: useCallback(() => {
      setUpdateStatusError(null);
      setUpdateStatusSuccess(null);
    }, []),

    handleCreateReservationTable,
    createError,
    createLoading,
    createSuccess,
    resetCreate: useCallback(() => {
      setCreateError(null);
      setCreateSuccess(null);
    }, []),

    handleCreateReservationTablesBulk,
    createBulkError,
    createBulkLoading,
    createBulkSuccess,
    resetCreateBulk: useCallback(() => {
      setCreateBulkError(null);
      setCreateBulkSuccess(null);
    }, []),
  };
};
