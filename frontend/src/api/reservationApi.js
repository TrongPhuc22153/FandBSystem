import { RESERVATION_ITEM_ENDPOINT, RESERVATION_ITEMS_ENDPOINT, RESERVATIONS_ENDPOINT } from '../constants/api'; // Make sure this path is correct
import { SORTING_DIRECTIONS } from '../constants/webConstant';

// Get reservation by ID
export const fetchReservationById = async ({ token, reservationId }) => {
  const response = await fetch(`${RESERVATIONS_ENDPOINT}/${reservationId}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Get reservations with pagination and optional status filter
export const fetchReservations = async ({ token, page = 0, size = 10, field = "startTime", direction = SORTING_DIRECTIONS.ASC, status, startDate, endDate, search }) => {
  const params = new URLSearchParams();
  params.append('page', page.toString());
  params.append('size', size.toString());
  params.append('field', field.toString());
  params.append('direction', direction.toString());
  if (status !== null && status!==undefined) {
    params.append('status', status.toString());
  }
  if (startDate !== null && startDate!== undefined) {
    params.append('startDate', startDate.toString());
  }
  if (endDate !== null && endDate !== undefined) {
    params.append('endDate', endDate.toString());
  }
  if (search !== null && search !== undefined) {
    params.append('search', search.toString());
  }

  const response = await fetch(`${RESERVATIONS_ENDPOINT}?${params.toString()}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Update an existing reservation's status
export const processReservation = async ({ token, reservationId, action }) => {
  const response = await fetch(`${RESERVATIONS_ENDPOINT}/${reservationId}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ action }),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Create a new reservation
export const createReservation = async ({ token, requestReservationDTO }) => {
  const response = await fetch(RESERVATIONS_ENDPOINT, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(requestReservationDTO),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const updateReservation = async ({
  token,
  id,
  requestReservationDTO,
}) => {
  const response = await fetch(`${RESERVATIONS_ENDPOINT}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(requestReservationDTO),
  });

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const addReservationItem = async ({
  token,
  reservationId,
  productId, 
  quantity, 
  specialInstruction
}) => {
  const response = await fetch(RESERVATION_ITEMS_ENDPOINT(reservationId), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      productId,
      quantity,
      specialInstruction
    }),
  });

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const updateReservationItemQuantity = async ({
  token,
  reservationId,
  itemId,
  productId,
  quantity,
  specialInstruction
}) => {
  const response = await fetch(
    RESERVATION_ITEM_ENDPOINT(reservationId, itemId),
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        productId,
        quantity,
        specialInstruction
      }),
    }
  );

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const updateReservationItemStatus = async ({
  token,
  reservationId,
  itemId,
  status,
}) => {
  const response = await fetch(
    RESERVATION_ITEM_ENDPOINT(reservationId, itemId),
    {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ status }),
    }
  );

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const cancelReservationItem = async ({ token, reservationId, itemId }) => {
  const response = await fetch(
    RESERVATION_ITEM_ENDPOINT(reservationId, itemId),
    {
      method: "DELETE",
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