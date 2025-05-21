import { CAPTURE_PAYPAL_ORDER, CREATE_PAYPAL_ORDER } from "../constants/api";

/**
 * Creates a new PayPal order.
 * @param {object} params - The parameters for creating the order.
 * @param {number} params.amount - The amount of the payment.
 * @param {string} params.currency - The currency of the payment (e.g., "USD").
 * @param {string} params.returnUrl - The URL to redirect to after successful payment.
 * @param {string} params.cancelUrl - The URL to redirect to if the payment is cancelled.
 * @returns {Promise<object>} A promise that resolves with the PayPal response DTO.
 * @throws {object} An error object if the API call fails.
 */
export const createPayPalOrder = async ({
    amount,
    currency,
    returnUrl,
    cancelUrl,
    token
}) => {
    const response = await fetch(CREATE_PAYPAL_ORDER, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            amount,
            currency,
            returnUrl,
            cancelUrl,
        }),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

/**
 * Captures an existing PayPal order.
 * @param {object} params - The parameters for capturing the order.
 * @param {string} params.orderId - The ID of the order to capture.
 * @returns {Promise<object>} A promise that resolves with the response DTO (likely void or success message).
 * @throws {object} An error object if the API call fails.
 */
export const capturePayPalOrder = async ({ orderId, token }) => {
    const response = await fetch(
        `${CAPTURE_PAYPAL_ORDER}?orderId=${encodeURIComponent(orderId)}`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
        }
    );

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};