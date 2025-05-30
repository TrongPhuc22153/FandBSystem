import { useState, useCallback } from 'react';
import useSWR from 'swr';
import { createPaymentMethod, fetchPaymentMethods, updatePaymentMethod } from '../api/paymentMethodApi';
import { PAYMENT_METHODS_ENDPOINT } from '../constants/api';
import { useAuth } from '../context/AuthContext';

export const usePaymentMethods = (type) => {
    return useSWR(
        [PAYMENT_METHODS_ENDPOINT, type],
        () => fetchPaymentMethods(type)
    );
};

export const usePaymentMethodActions = () => {
    const { token } = useAuth();

    const [updateMethodError, setUpdateMethodError] = useState(null);
    const [updateMethodLoading, setUpdateMethodLoading] = useState(false);
    const [updateMethodSuccess, setUpdateMethodSuccess] = useState(null);

    const [createMethodError, setCreateMethodError] = useState(null);
    const [createMethodLoading, setCreateMethodLoading] = useState(false);
    const [createMethodSuccess, setCreateMethodSuccess] = useState(null);

    const handleUpdatePaymentMethod = useCallback(
        async ({ id, requestPaymentMethodDTO }) => {
            setUpdateMethodError(null);
            setUpdateMethodSuccess(null);
            setUpdateMethodLoading(true);
            try {
                const response = await updatePaymentMethod({ id, requestPaymentMethodDTO, token });
                setUpdateMethodSuccess(response?.message || "Payment method updated successfully");
                return response.data;
            } catch (error) {
                setUpdateMethodError(error);
                return null;
            } finally {
                setUpdateMethodLoading(false);
            }
        },
        [token]
    );

    const handleCreatePaymentMethod = useCallback(
        async (requestPaymentMethodDTO) => {
            setCreateMethodError(null);
            setCreateMethodSuccess(null);
            setCreateMethodLoading(true);
            try {
                const response = await createPaymentMethod({ requestPaymentMethodDTO, token });
                setCreateMethodSuccess(response?.message || "Payment method created successfully");
                return response.data;
            } catch (error) {
                setCreateMethodError(error);
                return null;
            } finally {
                setCreateMethodLoading(false);
            }
        },
        [token]
    );

    const resetUpdateMethod = useCallback(() => {
        setUpdateMethodError(null);
        setUpdateMethodSuccess(null);
    }, []);

    const resetCreateMethod = useCallback(() => {
        setCreateMethodError(null);
        setCreateMethodSuccess(null);
    }, []);

    return {
        handleUpdatePaymentMethod,
        updateMethodError,
        updateMethodLoading,
        updateMethodSuccess,
        resetUpdateMethod,

        handleCreatePaymentMethod,
        createMethodError,
        createMethodLoading,
        createMethodSuccess,
        resetCreateMethod,
    };
};
