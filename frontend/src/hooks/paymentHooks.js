import { useCallback, useState } from "react";
import useSWR from "swr";
import { useAuth } from "../context/AuthContext";
import {
  fetchPaymentById,
  fetchPayments,
  processPayment,
} from "../api/paymentApi";
import { PAYMENTS_ENDPOINT } from "../constants/api";
import { PAYMENT_STATUSES, SORTING_DIRECTIONS } from "../constants/webConstant";

export const usePayments = ({
  page = 0,
  size = 10,
  direction = SORTING_DIRECTIONS.ASC,
  field = "createdAt",
  orderId,
  reservationId,
  phone,
  tableNumber,
  contactName,
  search,
  status = PAYMENT_STATUSES.PENDING,
}) => {
  const { token } = useAuth();

  const fetcher = useCallback(async () => {
    if (!token) {
      return null;
    }
    return fetchPayments({
      page,
      size,
      direction,
      field,
      search,
      phone,
      tableNumber,
      contactName,
      orderId,
      reservationId,
      status,
      token,
    });
  }, [
    page,
    size,
    direction,
    field,
    search,
    phone,
    tableNumber,
    contactName,
    orderId,
    reservationId,
    status,
    token,
  ]);
  const swrKey = token
    ? [
        PAYMENTS_ENDPOINT,
        page,
        size,
        direction,
        field,
        search,
        phone,
        tableNumber,
        contactName,
        orderId,
        reservationId,
        status,
        token,
      ]
    : null;

  return useSWR(swrKey, fetcher);
};

export const usePayment = (id) => {
  const { token } = useAuth();

  const fetcher = useCallback(async () => {
    if (!token || !id) {
      return null;
    }
    return fetchPaymentById(id, token);
  }, [id, token]);

  const swrKey = token && id ? [PAYMENTS_ENDPOINT, id] : null;

  return useSWR(swrKey, fetcher);
};

export const usePaymentActions = () => {
  const { token } = useAuth();
  const [paymentError, setPaymentError] = useState(null);
  const [paymentLoading, setPaymentLoading] = useState(false);
  const [paymentSuccess, setPaymentSuccess] = useState(null);

  const handleProcessPayment = useCallback(
    async ({
      id,
      returnUrl,
      cancelUrl,
      paymentMethod,
      orderId,
      reservationId,
    }) => {
      setPaymentError(null);
      setPaymentSuccess(null);
      setPaymentLoading(true);
      try {
        const response = await processPayment({
          id,
          returnUrl,
          cancelUrl,
          paymentMethod,
          orderId,
          reservationId,
          token,
        });
        setPaymentSuccess(
          response?.message || "Payment processed successfully"
        );
        return response;
      } catch (error) {
        setPaymentError(error);
        return null;
      } finally {
        setPaymentLoading(false);
      }
    },
    [token]
  );

  return {
    handleProcessPayment,
    paymentError,
    paymentLoading,
    paymentSuccess,
    resetPayment: useCallback(() => {
      setPaymentError(null);
      setPaymentSuccess(null);
    }, []),
  };
};
