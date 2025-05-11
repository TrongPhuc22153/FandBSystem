import { SHIPPING_ADDRESS_ENDPOINT, USER_SHIPPING_ADDRESS_ENDPOINT } from '../constants/api';

// Get authenticated user's shipping addresses
export const fetchShippingAddresses = async (token) => {
    const response = await fetch(USER_SHIPPING_ADDRESS_ENDPOINT, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

// Update authenticated user's shipping address
export const updateShippingAddress = async ({ addressId, addressData, token }) => {
    const response = await fetch(`${SHIPPING_ADDRESS_ENDPOINT}/${addressId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(addressData),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

// Create authenticated user's shipping address
export const createShippingAddress = async ({ addressData, token }) => {
    const response = await fetch(SHIPPING_ADDRESS_ENDPOINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(addressData),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

// Delete authenticated user's shipping address
export const deleteShippingAddress = async ({ id, token }) => {
    const response = await fetch(`${SHIPPING_ADDRESS_ENDPOINT}/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};