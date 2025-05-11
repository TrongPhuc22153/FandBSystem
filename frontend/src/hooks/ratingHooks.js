import useSWR from "swr";
import { createRating, deleteRating, fetchRating, updateRating } from "../api/ratingApi";
import { PRODUCT_RATING_ENDPOINT } from "../constants/api";
import { useCallback } from "react";
import { useAuth } from "../context/AuthContext";
import { useState } from "react";

export const useRatings = ({ productId }) => {
  return useSWR(`${PRODUCT_RATING_ENDPOINT}/${productId}`, () =>
    fetchRating({ productId })
  );
};

export const useUserProductRating = ({ productId }) => {
  const { token } = useAuth();
  return {
    data: null
  }
  // return useSWR(`${PRODUCT_RATING_ENDPOINT}/${productId}/me`, () =>
  //   fetchUserProductRating({ productId, token })
  // );
};

export const useRatingActions = () => {
  const { token } = useAuth();
  const [createError, setCreateError] = useState(null);
  const [creating, setCreating] = useState(false);
  const [createSuccess, setCreateSuccess] = useState(null);

  const [updateError, setUpdateError] = useState(null);
  const [updating, setUpdating] = useState(false);
  const [updateSuccess, setUpdateSuccess] = useState(null);

  const [deleteError, setDeleteError] = useState(null);
  const [deleting, setDeleting] = useState(false);
  const [deleteSuccess, setDeleteSuccess] = useState(null);

  const handleCreateRating = useCallback(async (ratingData) => {
    setCreating(true);
    setCreateError(null);
    setCreateSuccess(null);
    try {
      const response = await createRating({ ratingData, token });
      setCreateSuccess(response?.message || 'Rating created successfully');
      return response.data;
    } catch (error) {
      setCreateError(error);
      return null;
    } finally {
      setCreating(false);
    }
  }, [token]);

  const handleUpdateRating = useCallback(async ({ ratingId, ratingData }) => {
    setUpdating(true);
    setUpdateError(null);
    setUpdateSuccess(null);
    try {
      const response = await updateRating({ ratingId, ratingData, token });
      setUpdateSuccess(response?.message || 'Rating updated successfully');
      return response.data;
    } catch (error) {
      setUpdateError(error);
      return null;
    } finally {
      setUpdating(false);
    }
  }, [token]);

  const handleDeleteRating = useCallback(async (ratingId) => {
    setDeleting(true);
    setDeleteError(null);
    setDeleteSuccess(null);
    try {
      const response = await deleteRating({ ratingId, token });
      if (response.ok) {
        setDeleteSuccess('Rating deleted successfully');
        return true;
      } else {
        const error = await response.json();
        setDeleteError(error?.message || 'Failed to delete rating');
        return false;
      }
    } catch (error) {
      setDeleteError(error?.message || 'An error occurred while deleting the rating');
      return false;
    } finally {
      setDeleting(false);
    }
  }, [token]);

  return {
    handleCreateRating,
    creating,
    createError,
    createSuccess,
    resetCreateRating: useCallback(() => {
      setCreateError(null);
      setCreateSuccess(null);
    }, []),

    handleUpdateRating,
    updating,
    updateError,
    updateSuccess,
    resetUpdateRating: useCallback(() => {
      setUpdateError(null);
      setUpdateSuccess(null);
    }, []),

    handleDeleteRating,
    deleting,
    deleteError,
    deleteSuccess,
    resetDeleteRating: useCallback(() => {
      setDeleteError(null);
      setDeleteSuccess(null);
    }, []),
  };
};