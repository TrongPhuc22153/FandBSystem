import { useState, useCallback } from 'react';
import { fetchShippingAddresses, updateShippingAddress, createShippingAddress } from '../api/shippingAddressApi';
import { useAuth } from '../context/AuthContext';
import useSWR from 'swr';
import { USER_SHIPPING_ADDRESS_ENDPOINT } from '../constants/api';


export const useShippingAddresses = () => {
    const { token } = useAuth();
    return useSWR(USER_SHIPPING_ADDRESS_ENDPOINT, () => fetchShippingAddresses(token))
};

export const useShippingAddressActions = () => {
    const { token } = useAuth();
    const [updateError, setUpdateError] = useState(null);
    const [createError, setCreateError] = useState(null);
    const [updateLoading, setUpdateLoading] = useState(false);
    const [createLoading, setCreateLoading] = useState(false);
    const [updateSuccess, setUpdateSuccess] = useState(null);
    const [createSuccess, setCreateSuccess] = useState(null);

    const handleUpdateShippingAddress = useCallback(
        async (addressId, addressData) => {
            setUpdateError(null);
            setUpdateSuccess(null);
            setUpdateLoading(true);
            try {
                const response = await updateShippingAddress({
                    addressId,
                    addressData,
                    token,
                });
                setUpdateSuccess(response?.message || "Shipping address updated successfully");
                return response.data;
            } catch (error) {
                setUpdateError(error);
                return null;
            } finally {
                setUpdateLoading(false);
            }
        },
        [token]
    );

    const handleCreateShippingAddress = useCallback(
        async (addressData) => {
            setCreateError(null);
            setCreateSuccess(null);
            setCreateLoading(true);
            try {
                const response = await createShippingAddress({
                    addressData,
                    token,
                });
                setCreateSuccess(response.message);
                return response.data;
            } catch (error) {
                setCreateError(error);
                return null;
            } finally {
                setCreateLoading(false);
            }
        },
        [token]
    );

    return {
        handleUpdateShippingAddress,
        updateError,
        updateLoading,
        updateSuccess,
        resetUpdate: () => {
            setUpdateError(null);
            setUpdateSuccess(null);
        },
        handleCreateShippingAddress,
        createError,
        createLoading,
        createSuccess,
        resetCreate: () => {
            setCreateError(null);
            setCreateSuccess(null);
        },
    };
};
