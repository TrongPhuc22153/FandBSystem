import { REFUND_ORDER_PREVIEW_ENDPOINT, REFUND_RESERVATION_PREVIEW_ENDPOINT } from "../constants/api";

// Fetcher function for order refund preview
export const fetchOrderRefundPreview = async (orderId, token) => {
  const response = await fetch(REFUND_ORDER_PREVIEW_ENDPOINT(orderId), {
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Fetcher function for reservation refund preview
export const fetchReservationRefundPreview = async (reservationId, token) => {
  const response = await fetch(REFUND_RESERVATION_PREVIEW_ENDPOINT(reservationId), {
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};