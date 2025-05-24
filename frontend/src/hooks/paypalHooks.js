import { useCallback, useState } from "react";
import { capturePayPalOrder } from "../api/paypalApi";
import { useAuth } from "../context/AuthContext";

export const usePayPalActions = () => {
    const { token } = useAuth();

    const [captureError, setCaptureError] = useState(null);
    const [captureLoading, setCaptureLoading] = useState(false);
    const [captureSuccess, setCaptureSuccess] = useState(null);

    const handleCaptureOrder = useCallback(
        async (orderId) => {
            setCaptureError(null);
            setCaptureSuccess(null);
            setCaptureLoading(true);
            try {
                const response = await capturePayPalOrder({ orderId, token });
                setCaptureSuccess(response?.message || "Payment captured successfully");
                return response;
            } catch (error) {
                setCaptureError(error);
                return null;
            } finally {
                setCaptureLoading(false);
            }
        }, [token]
    );

    const resetCapture = useCallback(() => {
        setCaptureError(null);
        setCaptureSuccess(null);
    }, []);

    return {
        handleCaptureOrder,
        captureError,
        captureLoading,
        captureSuccess,
        resetCapture,
    };
};