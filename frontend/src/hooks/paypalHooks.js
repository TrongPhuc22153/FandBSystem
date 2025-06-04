import { useCallback, useState } from "react";
import { completePayPalOrder, refundPayPalOrder } from "../api/paypalApi";
import { useAuth } from "../context/AuthContext";

export const usePayPalActions = () => {
    const { token } = useAuth();

    const [completeError, setCompleteError] = useState(null);
    const [completeLoading, setCompleteLoading] = useState(false);
    const [completeSuccess, setCompleteSuccess] = useState(null);

    const [refundError, setRefundError] = useState(null);
    const [refundLoading, setRefundLoading] = useState(false);
    const [refundSuccess, setRefundSuccess] = useState(null);

    const handleCompleteOrder = useCallback(
        async (paypalOrderId) => {
            setCompleteError(null);
            setCompleteSuccess(null);
            setCompleteLoading(true);
            try {
                const response = await completePayPalOrder({ paypalOrderId, token });
                setCompleteSuccess(response?.message || "Complete payment successfully");
                return response;
            } catch (error) {
                setCompleteError(error);
                return null;
            } finally {
                setCompleteLoading(false);
            }
        }, [token]
    );

    const resetComplete = useCallback(() => {
        setCompleteError(null);
        setCompleteSuccess(null);
    }, []);

    const handleRefundOrder = useCallback(
        async (paymentId) => {
            setRefundError(null);
            setRefundSuccess(null);
            setRefundLoading(true);
            try {
                const response = await refundPayPalOrder({ paymentId, token });
                setRefundSuccess(response?.message || "Refund payment successfully");
                return response;
            } catch (error) {
                setRefundError(error);
                return null;
            } finally {
                setRefundLoading(false);
            }
        }, [token]
    );

    const resetRefund = useCallback(() => {
        setRefundError(null);
        setRefundSuccess(null);
    }, []);

    return {
        handleCompleteOrder,
        completeError,
        completeLoading,
        completeSuccess,
        resetComplete,
        handleRefundOrder,
        refundError,
        refundLoading,
        refundSuccess,
        resetRefund,
    };
};