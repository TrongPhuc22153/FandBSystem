import { ORDERS_ENDPOINT } from '../constants/api';

// Get all orders
export const fetchOrders = async ({ token, page = 0, size = 10, field = "orderData", direction = "DESC", type, status }) => {
  const params = new URLSearchParams();
  params.append('page', page.toString());
  params.append('size', size.toString());
  params.append('direction', direction.toString());
  params.append('field', field.toString());
  if (type) {
    params.append('type', type);
  }
  if (status) {
    params.append('status', status);
  }

  const response = await fetch(`${ORDERS_ENDPOINT}?${params.toString()}`, {
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

// Get order by ID
export const fetchOrderById = async ({ token, orderId }) => {
  const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}`, {
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


// Get user order by ID
export const fetchUserOrder = async ({ token, orderId, isRated }) => {
  const params = new URLSearchParams();
  if (isRated) {
    params.append('isRated', isRated.toString());
  }
  const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}?${params.toString()}`, {
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

// Get user orders with pagination and optional status filter
export const fetchUserOrders = async ({ token, page = 0, size = 10, status, isRated }) => {
  const params = new URLSearchParams();
  params.append('page', page.toString());
  params.append('size', size.toString());
  if (status) {
    params.append('status', status);
  }
  if (isRated !== undefined) {
    params.append('isRated', isRated.toString());
  }

  const response = await fetch(`${ORDERS_ENDPOINT}?${params.toString()}`, {
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

// Place a new order
export const placeOrder = async ({ token, requestOrderDTO }) => {
  const response = await fetch(ORDERS_ENDPOINT, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(requestOrderDTO),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Process/update an existing order
export const processOrder = async ({ token, orderId, action, type }) => {
  const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      action: action,
      type: type
    })
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};