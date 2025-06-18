import { useCallback, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { addReservationItem, cancelReservationItem, createReservation, fetchReservationById, fetchReservations, processReservation, updateReservation, updateReservationItemQuantity, updateReservationItemStatus } from "../api/reservationApi";
import useSWR from "swr";
import { SORTING_DIRECTIONS } from "../constants/webConstant";
import { RESERVATIONS_ENDPOINT } from "../constants/api";

// Hook for fetching a single reservation
export const useReservation = ({ reservationId } = {}) => {
  const { token } = useAuth();
  const fetcher = useCallback(() => {
    if (!token) return null;
    if (!reservationId) return null;
    return fetchReservationById({ token, reservationId });
  }, [token, reservationId]);

  return useSWR(reservationId ? `reservation-${reservationId}` : null, fetcher, { keepPreviousData: true });
};

// Hook for fetching multiple reservations
export const useReservations = ({ page = 0, size = 10, sortBy = "date", direction = SORTING_DIRECTIONS.ASC, status, startDate, endDate, search } = {}) => {
  const { token } = useAuth();
  const fetcher = useCallback(() => {
    if (!token) return null;
    return fetchReservations({
      token: token,
      page: page,
      size: size,
      field: sortBy,
      direction: direction,
      status: status,
      startDate: startDate,
      endDate: endDate,
      search: search
    });
  }, [token, page, size, status, sortBy, direction, startDate, endDate, search]);

  return useSWR([RESERVATIONS_ENDPOINT, page, size, status, sortBy, direction, startDate, endDate, search], fetcher, { keepPreviousData: true });
};

export const useReservationActions = () => {
  const { token, error: authError } = useAuth();

  // --- State for createReservation ---
  const [createLoading, setCreateLoading] = useState(false);
  const [createError, setCreateError] = useState(null);
  const [createSuccess, setCreateSuccess] = useState(null);
  const resetCreate = useCallback(() => {
    setCreateError(null);
    setCreateSuccess(null);
  }, []);

  // --- State for processReservation ---
  const [processLoading, setProcessLoading] = useState(false);
  const [processError, setProcessError] = useState(null);
  const [processSuccess, setProcessSuccess] = useState(null);
  const resetProcess = useCallback(() => {
    setProcessError(null);
    setProcessSuccess(null);
  }, []);

  // --- State for updateReservation ---
  const [updateLoading, setUpdateLoading] = useState(false);
  const [updateError, setUpdateError] = useState(null);
  const [updateSuccess, setUpdateSuccess] = useState(null);
  const resetUpdate = useCallback(() => {
    setUpdateError(null);
    setUpdateSuccess(null);
  }, []);

  // --- Action Handlers ---
  const handleCreateReservation = useCallback(
    async (requestReservationDTO) => {
      if (!token) {
        setCreateError(authError?.message || "Authentication required");
        return null;
      }
      setCreateLoading(true);
      setCreateError(null);
      setCreateSuccess(null);
      try {
        const response = await createReservation({
          token: token,
          requestReservationDTO: requestReservationDTO,
        });
        setCreateSuccess(response?.message || "Reservation placed successfully");
        return response;
      } catch (err) {
        setCreateError(err);
        return null;
      } finally {
        setCreateLoading(false);
      }
    },
    [token, authError]
  );

  const handleProcessReservation = useCallback(
    async (reservationId, action) => {
      if (!token) {
        setProcessError(authError?.message || "Authentication required");
        return null;
      }
      setProcessLoading(true);
      setProcessError(null);
      setProcessSuccess(null);

      try {
        const response = await processReservation({ token, reservationId, action });
        setProcessSuccess(response?.message || "Reservation processed successfully");
        return response;
      } catch (error) {
        setProcessError(error);
        return null;
      } finally {
        setProcessLoading(false);
      }
    },
    [token, authError]
  );

  const handleUpdateReservation = useCallback(
    async (id, requestReservationDTO) => {
      if (!token) {
        setUpdateError(authError?.message || "Authentication required");
        return null;
      }
      setUpdateLoading(true);
      setUpdateError(null);
      setUpdateSuccess(null);

      try {
        const response = await updateReservation({
          token,
          id,
          requestReservationDTO,
        });
        setUpdateSuccess(response?.message || "Reservation updated successfully");
        return response;
      } catch (error) {
        setUpdateError(error);
        return null;
      } finally {
        setUpdateLoading(false);
      }
    },
    [token, authError]
  );

  return {
    // Create
    handleCreateReservation,
    createError,
    createLoading,
    createSuccess,
    resetCreate,

    // Process
    handleProcessReservation,
    processError,
    processLoading,
    processSuccess,
    resetProcess,

    // Update
    handleUpdateReservation,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate,
  };
};

export const useReservationItemActions = () => {
  const { token, error: authError } = useAuth();

  // --- State for addReservationItem ---
  const [addLoading, setAddLoading] = useState(false);
  const [addError, setAddError] = useState(null);
  const [addSuccess, setAddSuccess] = useState(null);
  const resetAdd = useCallback(() => {
    setAddError(null);
    setAddSuccess(null);
  }, []);

  // --- State for updateReservationItemQuantity ---
  const [updateQuantityLoading, setUpdateQuantityLoading] = useState(false);
  const [updateQuantityError, setUpdateQuantityError] = useState(null);
  const [updateQuantitySuccess, setUpdateQuantitySuccess] = useState(null);
  const resetUpdateQuantity = useCallback(() => {
    setUpdateQuantityError(null);
    setUpdateQuantitySuccess(null);
  }, []);

  // --- State for updateReservationItemStatus ---
  const [updateStatusLoading, setUpdateStatusLoading] = useState(false);
  const [updateStatusError, setUpdateStatusError] = useState(null);
  const [updateStatusSuccess, setUpdateStatusSuccess] = useState(null);
  const resetUpdateStatus = useCallback(() => {
    setUpdateStatusError(null);
    setUpdateStatusSuccess(null);
  }, []);

  // --- State for cancelReservationItem ---
  const [cancelLoading, setCancelLoading] = useState(false);
  const [cancelError, setCancelError] = useState(null);
  const [cancelSuccess, setCancelSuccess] = useState(null);
  const resetCancel = useCallback(() => {
    setCancelError(null);
    setCancelSuccess(null);
  }, []);

  const handleAddReservationItem = useCallback(
    async ({ reservationId, productId, quantity, specialInstruction }) => {
      if (!token) {
        setAddError(authError?.message || 'Authentication required');
        return null;
      }
      setAddLoading(true);
      setAddError(null);
      setAddSuccess(null);

      try {
        const response = await addReservationItem({
          token,
          reservationId,
          productId,
          quantity,
          specialInstruction,
        });
        setAddSuccess(response?.message || 'Reservation item added successfully');
        return response;
      } catch (err) {
        setAddError(err);
        return null;
      } finally {
        setAddLoading(false);
      }
    },
    [token, authError]
  );

  const handleUpdateReservationItemQuantity = useCallback(
    async ({ reservationId, itemId, productId, quantity, specialInstruction }) => {
      if (!token) {
        setUpdateQuantityError(authError?.message || 'Authentication required');
        return null;
      }
      setUpdateQuantityLoading(true);
      setUpdateQuantityError(null);
      setUpdateQuantitySuccess(null);

      try {
        const response = await updateReservationItemQuantity({
          token,
          reservationId,
          itemId,
          productId,
          quantity,
          specialInstruction,
        });
        setUpdateQuantitySuccess(response?.message || 'Reservation item quantity updated successfully');
        return response;
      } catch (err) {
        setUpdateQuantityError(err);
        return null;
      } finally {
        setUpdateQuantityLoading(false);
      }
    },
    [token, authError]
  );

  const handleUpdateReservationItemStatus = useCallback(
    async ({ reservationId, itemId, status }) => {
      if (!token) {
        setUpdateStatusError(authError?.message || 'Authentication required');
        return null;
      }
      setUpdateStatusLoading(true);
      setUpdateStatusError(null);
      setUpdateStatusSuccess(null);

      try {
        const response = await updateReservationItemStatus({
          token,
          reservationId,
          itemId,
          status,
        });
        setUpdateStatusSuccess(response?.message || 'Reservation item status updated successfully');
        return response;
      } catch (err) {
        setUpdateStatusError(err);
        return null;
      } finally {
        setUpdateStatusLoading(false);
      }
    },
    [token, authError]
  );

  const handleCancelReservationItem = useCallback(
    async ({ reservationId, itemId }) => {
      if (!token) {
        setCancelError(authError?.message || 'Authentication required');
        return null;
      }
      setCancelLoading(true);
      setCancelError(null);
      setCancelSuccess(null);

      try {
        const response = await cancelReservationItem({
          token,
          reservationId,
          itemId,
        });
        setCancelSuccess(response?.message || 'Reservation item cancelled successfully');
        return response;
      } catch (err) {
        setCancelError(err);
        return null;
      } finally {
        setCancelLoading(false);
      }
    },
    [token, authError]
  );

  return {
    // Add
    handleAddReservationItem,
    addError,
    addLoading,
    addSuccess,
    resetAdd,

    // Update quantity
    handleUpdateReservationItemQuantity,
    updateQuantityError,
    updateQuantityLoading,
    updateQuantitySuccess,
    resetUpdateQuantity,

    // Update status
    handleUpdateReservationItemStatus,
    updateStatusError,
    updateStatusLoading,
    updateStatusSuccess,
    resetUpdateStatus,

    // Cancel
    handleCancelReservationItem,
    cancelError,
    cancelLoading,
    cancelSuccess,
    resetCancel,
  };
};