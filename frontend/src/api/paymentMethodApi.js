import { PAYMENT_METHODS_ENDPOINT } from "../constants/api";

export const fetchPaymentMethods = async () => {
    const response = await fetch(PAYMENT_METHODS_ENDPOINT);

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

export const fetchPaymentMethodById = async ({ id }) => {
    const response = await fetch(`${PAYMENT_METHODS_ENDPOINT}/${id}`);

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

export const updatePaymentMethod = async ({ id, requestPaymentMethodDTO, token }) => {
    const response = await fetch(`${PAYMENT_METHODS_ENDPOINT}/${id}`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(requestPaymentMethodDTO),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

export const createPaymentMethod = async ({ requestPaymentMethodDTO, token }) => {
    const response = await fetch(PAYMENT_METHODS_ENDPOINT, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(requestPaymentMethodDTO),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};
