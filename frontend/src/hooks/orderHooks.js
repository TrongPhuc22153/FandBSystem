import useSWR from "swr";
import { useCallback, useState } from "react";
import {
  addOrderItem,
  fetchOrders,
  fetchUserOrder,
  placeOrder,
  processOrder,
  updateOrder,
  updateOrderItemQuantity,
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

  // --- State for updateOrder ---
  const [updateLoading, setUpdateLoading] = useState(false);
  const [updateError, setUpdateError] = useState(null);
  const [updateSuccess, setUpdateSuccess] = useState(null);
  const resetUpdate = useCallback(() => {
    setUpdateError(null);
    setUpdateSuccess(null);
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

  const handleUpdateOrder = useCallback(
    async ({ orderId, type, waitingListId, orderDetails }) => {
      if (!token) {
        setUpdateError(authError?.message || "Authentication required");
        return null;
      }
      setUpdateLoading(true);
      setUpdateError(null);
      setUpdateSuccess(null);
      try {
        const response = await updateOrder({
          orderId,
          type,
          waitingListId,
          orderDetails,
          token
        });
        setUpdateSuccess(response?.message || "Order updated successfully");
        return response;
      } catch (err) {
        setUpdateError(err);
        return null;
      } finally {
        setUpdateLoading(false);
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

    handleUpdateOrder,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate,
  };
};

export const useOrderItemActions = () => {
  const { token, error: authError } = useAuth();

  // --- State for addOrderItem ---
  const [addLoading, setAddLoading] = useState(false);
  const [addError, setAddError] = useState(null);
  const [addSuccess, setAddSuccess] = useState(null);
  const resetAdd = useCallback(() => {
    setAddError(null);
    setAddSuccess(null);
  }, []);

  // --- State for updateOrderItemQuantity ---
  const [updateQuantityLoading, setUpdateQuantityLoading] = useState(false);
  const [updateQuantityError, setUpdateQuantityError] = useState(null);
  const [updateQuantitySuccess, setUpdateQuantitySuccess] = useState(null);
  const resetUpdateQuantity = useCallback(() => {
    setUpdateQuantityError(null);
    setUpdateQuantitySuccess(null);
  }, []);

  const handleAddOrderItem = useCallback(
    async ({ orderId, productId, quantity, specialInstruction }) => {
      if (!token) {
        setAddError(authError?.message || "Authentication required");
        return null;
      }
      setAddLoading(true);
      setAddError(null);
      setAddSuccess(null);

      try {
        const response = await addOrderItem({ token, orderId, productId, quantity, specialInstruction });
        setAddSuccess(response?.message || "Order item added successfully");
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

  const handleUpdateOrderItemQuantity = useCallback(
    async ({ orderId, orderItemId, productId, quantity, specialInstruction }) => {
      if (!token) {
        setUpdateQuantityError(authError?.message || "Authentication required");
        return null;
      }
      setUpdateQuantityLoading(true);
      setUpdateQuantityError(null);
      setUpdateQuantitySuccess(null);

      try {
        const response = await updateOrderItemQuantity({ token, orderId, orderItemId, productId, quantity, specialInstruction });
        setUpdateQuantitySuccess(response?.message || "Order item quantity updated successfully");
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

  return {
    handleAddOrderItem,
    addError,
    addLoading,
    addSuccess,
    resetAdd,

    handleUpdateOrderItemQuantity,
    updateQuantityError,
    updateQuantityLoading,
    updateQuantitySuccess,
    resetUpdateQuantity,
  };
};


