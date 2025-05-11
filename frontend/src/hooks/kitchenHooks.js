import { useCallback, useState } from "react";
import useSWR from "swr";
import { useAuth } from "../context/AuthContext";
import {
  fetchOrdersForPreparation,
  fetchReservationsForPreparation,
  markOrderAsPrepared,
  markReservationAsPrepared,
} from "../api/kitchenApi";

// Hook for fetching orders for preparation
export const useOrdersForPreparation = ({ page = 0, size = 10 } = {}) => {
  const { token } = useAuth();

  const fetcher = useCallback(() => {
    if (!token) return null;
    return fetchOrdersForPreparation({ token, page, size });
  }, [token, page, size]);

  return useSWR(["kitchen-orders", page, size], fetcher);
};

// Hook for fetching reservations for preparation
export const useReservationsForPreparation = ({ page = 0, size = 10 } = {}) => {
  const { token } = useAuth();

  const fetcher = useCallback(() => {
    if (!token) return null;
    return fetchReservationsForPreparation({ token, page, size });
  }, [token, page, size]);

  return useSWR(["kitchen-reservations", page, size], fetcher);
};

// Hook for marking orders and reservations as prepared
export const usePreparationActions = () => {
  const { token, error: authError } = useAuth();

  // --- State for markOrderAsPrepared ---
  const [markOrderLoading, setMarkOrderLoading] = useState(false);
  const [markOrderError, setMarkOrderError] = useState(null);
  const [markOrderSuccess, setMarkOrderSuccess] = useState(null);
  const resetMarkOrder = useCallback(() => {
    setMarkOrderError(null);
    setMarkOrderSuccess(null);
  }, []);

  // --- State for markReservationAsPrepared ---
  const [markReservationLoading, setMarkReservationLoading] = useState(false);
  const [markReservationError, setMarkReservationError] = useState(null);
  const [markReservationSuccess, setMarkReservationSuccess] = useState(null);
  const resetMarkReservation = useCallback(() => {
    setMarkReservationError(null);
    setMarkReservationSuccess(null);
  }, []);

  // --- Action Handlers ---
  const handleMarkOrderAsPrepared = useCallback(
    async (orderId) => {
      if (!token) {
        setMarkOrderError(authError?.message || "Authentication required");
        return null;
      }
      setMarkOrderLoading(true);
      setMarkOrderError(null);
      setMarkOrderSuccess(null);
      try {
        const response = await markOrderAsPrepared({ token, orderId });
        setMarkOrderSuccess(
          response?.message || "Order marked as prepared successfully"
        );
        return response;
      } catch (err) {
        setMarkOrderError(err);
        return null;
      } finally {
        setMarkOrderLoading(false);
      }
    },
    [token, authError]
  );

  const handleMarkReservationAsPrepared = useCallback(
    async (reservationId) => {
      if (!token) {
        setMarkReservationError(
          authError?.message || "Authentication required"
        );
        return null;
      }
      setMarkReservationLoading(true);
      setMarkReservationError(null);
      setMarkReservationSuccess(null);
      try {
        const response = await markReservationAsPrepared({
          token,
          reservationId,
        });
        setMarkReservationSuccess(
          response?.message || "Reservation marked as prepared successfully"
        );
        return response;
      } catch (error) {
        setMarkReservationError(error);
        return null;
      } finally {
        setMarkReservationLoading(false);
      }
    },
    [token, authError]
  );

  return {
    handleMarkOrderAsPrepared,
    markOrderError,
    markOrderLoading,
    markOrderSuccess,
    resetMarkOrder,
    handleMarkReservationAsPrepared,
    markReservationError,
    markReservationLoading,
    markReservationSuccess,
    resetMarkReservation,
  };
};
