import { USER_CART_ENDPOINT, USER_CART_ITEMS_ENDPOINT } from "../constants/api";

// Helper function to handle non-ok responses
const handleResponse = async (response) => {
    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

// Get user cart
export const fetchUserCart = async ({ token }) => {
    const response = await fetch(USER_CART_ENDPOINT, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
    return handleResponse(response);
};

// Add cart item quantity
export const addCartItem = async ({ token, requestCartItemDTO }) => {
    const response = await fetch(USER_CART_ITEMS_ENDPOINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(requestCartItemDTO),
    });
    return handleResponse(response);
};

// Update cart item quantity
export const updateCartItemQuantity = async ({ token, requestCartItemDTO }) => {
    const response = await fetch(USER_CART_ITEMS_ENDPOINT, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(requestCartItemDTO),
    });
    return handleResponse(response);
};

// Remove item from user cart
export const removeItemFromUserCart = async ({ token, productId }) => {
    const response = await fetch(`${USER_CART_ITEMS_ENDPOINT}/${productId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
    return handleResponse(response);
};

// Clear user cart
export const clearCart = async ({ token }) => {
    const response = await fetch(USER_CART_ENDPOINT, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
    return handleResponse(response);
};