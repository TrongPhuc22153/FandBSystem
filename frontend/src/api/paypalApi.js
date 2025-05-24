import { CAPTURE_PAYPAL_ORDER } from "../constants/api";

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