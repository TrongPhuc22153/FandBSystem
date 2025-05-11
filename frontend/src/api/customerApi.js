import { CUSTOMER_PROFILE_ENDPOINT } from "../constants/api";

export const fetchCustomerProfile = async (token) => {
    const response = await fetch(CUSTOMER_PROFILE_ENDPOINT, {
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

// --- Updating Customer Data ---
export const updateCustomerProfile = async (token, requestCustomerDTO) => {
    const response = await fetch(CUSTOMER_PROFILE_ENDPOINT, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(requestCustomerDTO),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};