import { useCallback, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { createReservation, fetchReservationById, fetchReservations, processReservation } from "../api/reservationApi";
import useSWR from "swr";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

// Hook for fetching a single reservation
export const useReservation = ({ reservationId } = {}) => {
  const { token } = useAuth();
  const fetcher = useCallback(() => {
    if (!token) return null;
    if (!reservationId) return null;
    return fetchReservationById({ token, reservationId });
  }, [token, reservationId]);

  return useSWR(reservationId ? `reservation-${reservationId}` : null, fetcher);
};

// Hook for fetching multiple reservations
export const useReservations = ({ page = 0, size = 10, sortBy = "startTime", direction = SORTING_DIRECTIONS.ASC, status, startDate, endDate } = {}) => {
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
      endDate: endDate
    });
  }, [token, page, size, status, sortBy, direction, startDate, endDate ]);

  return useSWR(['reservations', page, size, status, sortBy, direction], fetcher);
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

  // --- State for updateReservationStatus ---
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
        setUpdateError(authError?.message || "Authentication required");
        return null;
      }
      setUpdateLoading(true);
      setUpdateError(null);
      setUpdateSuccess(null);

      try {
        const response = await processReservation({ token, reservationId, action });
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
    handleCreateReservation,
    createError,
    createLoading,
    createSuccess,
    resetCreate,
    handleProcessReservation,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate,
  };
};