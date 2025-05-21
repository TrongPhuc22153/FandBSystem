import { useCallback, useState } from "react";
import { capturePayPalOrder, createPayPalOrder } from "../api/paypalApi";
import { useAuth } from "../context/AuthContext";

export const usePayPalActions = () => {
    const { token } = useAuth();

    const [createError, setCreateError] = useState(null);
    const [createLoading, setCreateLoading] = useState(false);
    const [createSuccess, setCreateSuccess] = useState(null);

    const [captureError, setCaptureError] = useState(null);
    const [captureLoading, setCaptureLoading] = useState(false);
    const [captureSuccess, setCaptureSuccess] = useState(null);

    const handleCreateOrder = useCallback(
        async (amount, currency, returnUrl, cancelUrl) => {
            setCreateError(null);
            setCreateSuccess(null);
            setCreateLoading(true);
            try {
                const response = await createPayPalOrder({
                    amount,
                    currency,
                    returnUrl,
                    cancelUrl,
                    token
                });
                setCreateSuccess(response?.message || "Payment created successfully");
                return response;
            } catch (error) {
                setCreateError(error);
                return null;
            } finally {
                setCreateLoading(false);
            }
        },
        [token]
    );

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

    const resetCreate = useCallback(() => {
        setCreateError(null);
        setCreateSuccess(null);
    }, []);

    const resetCapture = useCallback(() => {
        setCaptureError(null);
        setCaptureSuccess(null);
    }, []);

    return {
        handleCreateOrder,
        createError,
        createLoading,
        createSuccess,
        resetCreate,

        handleCaptureOrder,
        captureError,
        captureLoading,
        captureSuccess,
        resetCapture,
    };
};