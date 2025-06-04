import { COMPLETE_PAYPAL_ORDER, REFUND_PAYPAL_ORDER } from "../constants/api";

export const completePayPalOrder = async ({ paypalOrderId, token }) => {
    const response = await fetch(
        `${COMPLETE_PAYPAL_ORDER}?paypalOrderId=${encodeURIComponent(paypalOrderId)}`,
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

export const refundPayPalOrder = async ({ paymentId, token }) => {
    const response = await fetch(
        `${REFUND_PAYPAL_ORDER}?paymentId=${encodeURIComponent(paymentId)}`,
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
}