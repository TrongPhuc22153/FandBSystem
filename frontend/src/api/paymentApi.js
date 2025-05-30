import { PAYMENTS_ENDPOINT } from "../constants/api";
import { PAYMENT_STATUSES, SORTING_DIRECTIONS } from "../constants/webConstant";

// GET all payments (paginated)
export const fetchPayments = async ({
  page = 0,
  size = 10,
  direction = SORTING_DIRECTIONS.ASC,
  field = "createdAt",
  search,
  phone,
  tableNumber,
  contactName,
  orderId,
  reservationId,
  status = PAYMENT_STATUSES.PENDING,
  token,
}) => {
  const params = new URLSearchParams();
  params.append("page", page.toString());
  params.append("size", size.toString());
  params.append("direction", direction.toString());
  params.append("field", field.toString());

  if (search != null && search != undefined) {
    params.append("search", search.toString());
  }
  if (status != null && status != undefined) {
    params.append("status", status.toString());
  }
  if (phone != null && phone != undefined) {
    params.append("phone", phone.toString());
  }
  if (contactName != null && contactName != undefined) {
    params.append("contactName", contactName.toString());
  }
  if (orderId != null && orderId != undefined) {
    params.append("orderId", orderId.toString());
  }
  if (reservationId != null && reservationId != undefined) {
    params.append("reservationId", reservationId.toString());
  }
  if (tableNumber != null && tableNumber != undefined) {
    params.append("tableNumber", tableNumber.toString());
  }

  const response = await fetch(`${PAYMENTS_ENDPOINT}?${params.toString()}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// GET payment by ID
export const fetchPaymentById = async (id, token) => {
  const response = await fetch(`${PAYMENTS_ENDPOINT}/${id}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// PATCH process payment
export const processPayment = async ({
  id,
  returnUrl,
  cancelUrl,
  paymentMethod,
  orderId,
  reservationId,
  token,
}) => {
  const response = await fetch(`${PAYMENTS_ENDPOINT}/${id}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      orderId,
      reservationId,
      returnUrl,
      cancelUrl,
      paymentMethod,
    }),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};
