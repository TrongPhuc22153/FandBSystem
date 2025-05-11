import useSWR from 'swr';
import { useCallback, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import {
    fetchUserCart,
    addCartItem,
    updateCartItemQuantity,
    removeItemFromUserCart,
    clearCart,
} from '../api/cartApi';

export const useCart = () => {
    const { token } = useAuth();
    const fetcher = useCallback(() => {
        if (!token) return null;
        return fetchUserCart({ token });
    }, [token]);

    return useSWR(token ? 'cart' : null, fetcher);
};

export const useCartActions = () => {
    const { token, error } = useAuth();
  
    // --- State for handleAddCartItem ---
    const [addLoading, setAddLoading] = useState(false);
    const [addError, setAddError] = useState(null);
    const [addSuccess, setAddSuccess] = useState(null);
    const resetAdd = useCallback(() => {
      setAddError(null);
      setAddSuccess(null);
    }, []);
  
    // --- State for handleUpdateCartItemQuantity ---
    const [updateLoading, setUpdateLoading] = useState(false);
    const [updateError, setUpdateError] = useState(null);
    const [updateSuccess, setUpdateSuccess] = useState(null);
    const resetUpdate = useCallback(() => {
      setUpdateError(null);
      setUpdateSuccess(null);
    }, []);
  
    // --- State for handleRemoveItemFromCart ---
    const [removeLoading, setRemoveLoading] = useState(false);
    const [removeError, setRemoveError] = useState(null);
    const [removeSuccess, setRemoveSuccess] = useState(null);
    const resetRemove = useCallback(() => {
      setRemoveError(null);
      setRemoveSuccess(null);
    }, []);
  
    // --- State for handleClearCart ---
    const [clearLoading, setClearLoading] = useState(false);
    const [clearError, setClearError] = useState(null);
    const [clearSuccess, setClearSuccess] = useState(null);
    const resetClear = useCallback(() => {
      setClearError(null);
      setClearSuccess(null);
    }, []);
  
    // --- Action Handlers ---
    const handleAddCartItem = useCallback(
      async (requestCartItemDTO) => {
        if (!token) {
          setAddError(error.message);
          return null;
        }
        setAddLoading(true);
        setAddError(null);
        setAddSuccess(null);
        try {
          const response = await addCartItem({ token, requestCartItemDTO });
          setAddSuccess(response?.message || "Item added successfully");
          return response;
        } catch (err) {
          setAddError(err);
          return null;
        } finally {
          setAddLoading(false);
        }
      },
      [token]
    );
  
    const handleUpdateCartItemQuantity = useCallback(
      async (requestCartItemDTO) => {
        if (!token) {
          setUpdateError(error);
          return null;
        }
        setUpdateLoading(true);
        setUpdateError(null);
        setUpdateSuccess(null);
        try {
          const { message, data } = await updateCartItemQuantity({ token, requestCartItemDTO });
          setUpdateSuccess(message);
          return data;
        } catch (err) {
          setUpdateError(err);
          return null;
        } finally {
          setUpdateLoading(false);
        }
      },
      [token]
    );
  
    const handleRemoveItemFromCart = useCallback(
      async (productId) => {
        if (!token) {
          setRemoveError(error);
          return null;
        }
        setRemoveLoading(true);
        setRemoveError(null);
        setRemoveSuccess(null);
        try {
          const { message } = await removeItemFromUserCart({ token, productId });
          setRemoveSuccess(message);
          return true;
        } catch (err) {
          setRemoveError(err);
          return null;
        } finally {
          setRemoveLoading(false);
        }
      },
      [token]
    );
  
    const handleClearCart = useCallback(async () => {
      if (!token) {
        setClearError(error);
        return null;
      }
      setClearLoading(true);
      setClearError(null);
      setClearSuccess(null);
      try {
        const { message } = await clearCart({ token });
        setClearSuccess(message);
        return true;
      } catch (err) {
        setClearError(err);
        return null;
      } finally {
        setClearLoading(false);
      }
    }, [token]);
  
    return {
      handleAddCartItem,
      addError,
      addLoading,
      addSuccess,
      resetAdd,

      handleUpdateCartItemQuantity,
      updateError,
      updateLoading,
      updateSuccess,
      resetUpdate,

      handleRemoveItemFromCart,
      removeError,
      removeLoading,
      removeSuccess,
      resetRemove,
      
      handleClearCart,
      clearError,
      clearLoading,
      clearSuccess,
      resetClear,
    };
  };