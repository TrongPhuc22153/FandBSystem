import useSWR from "swr";
import { useCallback, useState } from "react";
import {
  fetchOrders,
  fetchUserOrder,
  placeOrder,
  processOrder,
} from "../api/orderApi";
import { useAuth } from "../context/AuthContext";
import { ORDERS_ENDPOINT } from "../constants/api";

export const useOrders = ({ page = 0, size = 10, sortField = "orderDate", sortDirection = "DESC", type, status } = {}) => {
  const { token } = useAuth();
  return useSWR([ORDERS_ENDPOINT, token, page, size, sortField, sortDirection, type, status, token],
    () => fetchOrders({
      token: token,
      page: page,
      size: size,
      field: sortField,
      direction: sortDirection,
      type: type,
      status: status
    })
  );
};

// Hook for fetching a single order
export const useOrder = ({ orderId, isRated } = {}) => {
  const { token } = useAuth();

  const fetcher = useCallback(() => {
    if (!token) return null;
    if (!orderId) return null; // Add check for orderId
    return fetchUserOrder({ token, orderId, isRated });
  }, [token, orderId]);

  return useSWR(orderId ? `order-${orderId}` : null, fetcher);
};


export const useOrderActions = () => {
  const { token, error: authError } = useAuth();

  // --- State for placeOrder ---
  const [placeLoading, setPlaceLoading] = useState(false);
  const [placeError, setPlaceError] = useState(null);
  const [placeSuccess, setPlaceSuccess] = useState(null);
  const resetPlace = useCallback(() => {
    setPlaceError(null);
    setPlaceSuccess(null);
  }, []);

  // --- State for processOrder ---
  const [processLoading, setProcessLoading] = useState(false);
  const [processError, setProcessError] = useState(null);
  const [processSuccess, setProcessSuccess] = useState(null);
  const resetProcess = useCallback(() => {
    setProcessError(null);
    setProcessSuccess(null);
  }, []);

  // --- Action Handlers ---
  const handlePlaceOrder = useCallback(
    async (requestOrderDTO, type) => {
      if (!token) {
        setPlaceError(authError?.message || "Authentication required");
        return null;
      }
      setPlaceLoading(true);
      setPlaceError(null);
      setPlaceSuccess(null);
      try {
        const response = await placeOrder({
          token: token,
          requestOrderDTO: { ...requestOrderDTO, type: type },
        });
        setPlaceSuccess(response?.message || "Order placed successfully");
        return response;
      } catch (err) {
        setPlaceError(err);
        return null;
      } finally {
        setPlaceLoading(false);
      }
    },
    [token, authError]
  );

  const handleProcessOrder = useCallback(
    async (orderId, action, type) => {
      if (!token) {
        setProcessError(authError?.message || "Authentication required");
        return null;
      }
      setProcessLoading(true);
      setProcessError(null);
      setProcessSuccess(null);

      try {
        const response = await processOrder({ token, orderId, action, type });
        setProcessSuccess(response?.message || "Order processed successfully");
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

  return {
    handlePlaceOrder,
    placeError,
    placeLoading,
    placeSuccess,
    resetPlace,
    handleProcessOrder,
    processError,
    processLoading,
    processSuccess,
    resetProcess,
  };
};
