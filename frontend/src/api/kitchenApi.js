import {
  KITCHEN_ORDERS_ENPOINT,
  KITCHEN_RESERVATIONS_ENDPOINT,
} from "../constants/api";

// Get orders for preparation
export const fetchOrdersForPreparation = async ({
  token,
  page = 0,
  size = 10,
}) => {
  const params = new URLSearchParams();
  params.append("page", page.toString());
  params.append("size", size.toString());

  const response = await fetch(
    `${KITCHEN_ORDERS_ENPOINT}?${params.toString()}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Get reservations for preparation
export const fetchReservationsForPreparation = async ({
  token,
  page = 0,
  size = 10,
}) => {
  const params = new URLSearchParams();
  params.append("page", page.toString());
  params.append("size", size.toString());

  const response = await fetch(
    `${KITCHEN_RESERVATIONS_ENDPOINT}?${params.toString()}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Mark an order as prepared
export const markOrderAsPrepared = async ({ token, orderId }) => {
  const response = await fetch(`${KITCHEN_ORDERS_ENPOINT}/${orderId}`, {
    method: "PATCH",
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

// Mark a reservation as prepared
export const markReservationAsPrepared = async ({ token, reservationId }) => {
  const response = await fetch(
    `${KITCHEN_RESERVATIONS_ENDPOINT}/${reservationId}`,
    {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};
