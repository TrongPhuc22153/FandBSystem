import { ORDER_ITEM_ENDPOINT, ORDER_ITEMS_ENDPOINT, ORDERS_ENDPOINT } from "../constants/api";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

// Get all orders
export const fetchOrders = async ({
  token,
  page = 0,
  size = 10,
  field = "orderData",
  direction = SORTING_DIRECTIONS.DESC,
  type,
  status,
  search,
  startDate,
  endDate
}) => {
  const params = new URLSearchParams();
  params.append("page", page.toString());
  params.append("size", size.toString());
  params.append("direction", direction.toString());
  params.append("field", field.toString());
  if (type !== null && type !== undefined) {
    params.append("type", type);
  }
  if (status !== null && status !== undefined) {
    params.append("status", status);
  }
  if (startDate !== null && startDate !== undefined) {
    params.append("startDate", startDate);
  }
  if (endDate !== null && endDate !== undefined) {
    params.append("endDate", endDate);
  }
  if (search !== null && search !== undefined) {
    params.append("search", search);
  }

  const response = await fetch(`${ORDERS_ENDPOINT}?${params.toString()}`, {
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

// Get order by ID
export const fetchOrderById = async ({ token, orderId }) => {
  const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}`, {
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

// Get user order by ID
export const fetchUserOrder = async ({ token, orderId, isRated }) => {
  const params = new URLSearchParams();
  if (isRated) {
    params.append("isRated", isRated.toString());
  }
  const response = await fetch(
    `${ORDERS_ENDPOINT}/${orderId}?${params.toString()}`,
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

// Get user orders with pagination and optional status filter
export const fetchUserOrders = async ({
  token,
  page = 0,
  size = 10,
  status,
  isRated,
}) => {
  const params = new URLSearchParams();
  params.append("page", page.toString());
  params.append("size", size.toString());
  if (status) {
    params.append("status", status);
  }
  if (isRated !== undefined) {
    params.append("isRated", isRated.toString());
  }

  const response = await fetch(`${ORDERS_ENDPOINT}?${params.toString()}`, {
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

// Cancel order item
export const cancelOrderItem = async ({ token, orderId, orderItemId }) => {
  const response = await fetch(ORDER_ITEM_ENDPOINT(orderId, orderItemId), {
    method: "DELETE",
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

// Update order item status
export const updateOrderItemStatus = async ({ token, orderId, orderItemId, status }) => {
  const response = await fetch(ORDER_ITEM_ENDPOINT(orderId, orderItemId), {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ status }),
  });

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Place a new order
export const placeOrder = async ({ token, requestOrderDTO }) => {
  const response = await fetch(ORDERS_ENDPOINT, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(requestOrderDTO),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const updateOrder = async ({
  orderId,
  type,
  tableOccupancyId,
  orderDetails,
  token
}) => {
  const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      tableOccupancyId,
      orderDetails,
      type
    }),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
}

export const addOrderItem = async ({ token, orderId, productId, quantity, specialInstruction }) => {
  const response = await fetch(ORDER_ITEMS_ENDPOINT(orderId), {
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

export const updateOrderItemQuantity = async ({ token, orderId, orderItemId, productId, quantity, specialInstruction }) => {
  const response = await fetch(ORDER_ITEM_ENDPOINT(orderId, orderItemId), {
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
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

// Process/update an existing order
export const processOrder = async ({ token, orderId, action, type }) => {
  const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      action: action,
      type: type,
    }),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};
