import useSWR from "swr";
import {
  fetchCategories,
  createCategory,
  updateCategory,
  deleteCategory,
  fetchCategoryById,
} from "../api/categoryApi";
import { CATEGORIES_ENDPOINT } from "../constants/api";
import { useAuth } from "../context/AuthContext";
import { useCallback, useState } from "react";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

export const useCategories = ({
  page = 0,
  size = 10,
  direction = SORTING_DIRECTIONS.ASC,
  field = "categoryName",
  search,
  isDeleted = false,
} = {}) => {
  return useSWR(
    [CATEGORIES_ENDPOINT, page, size, direction, field, search, isDeleted],
    () =>
      fetchCategories({
        page: page,
        size: size,
        direction: direction,
        field: field,
        search: search,
        isDeleted: isDeleted,
      }),
    { keepPreviousData: true }
  );
};

export const useCategory = ({ id, isDeleted = false }) => {
  return useSWR([CATEGORIES_ENDPOINT, id, isDeleted], () =>
    fetchCategoryById(id, isDeleted)
  );
};

export const useCategoryActions = () => {
  const { token } = useAuth();
  const [updateError, setUpdateError] = useState(null);
  const [createError, setCreateError] = useState(null);
  const [deleteError, setDeleteError] = useState(null);

  const [updateLoading, setUpdateLoading] = useState(false);
  const [createLoading, setCreateLoading] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);

  const [updateSuccess, setUpdateSuccess] = useState(null);
  const [createSuccess, setCreateSuccess] = useState(null);
  const [deleteSuccess, setDeleteSuccess] = useState(null);

  const handleCreateCategory = useCallback(
    async (requestData) => {
      setCreateError(null);
      setCreateSuccess(null);
      setCreateLoading(true);
      try {
        const response = await createCategory(requestData, token);
        setCreateSuccess(response?.message || "Category created successfully");
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

  const handleUpdateCategory = useCallback(
    async (id, requestData) => {
      setUpdateError(null);
      setUpdateSuccess(null);
      setUpdateLoading(true);
      try {
        const response = await updateCategory(id, requestData, token);
        setUpdateSuccess(response?.message || "Category updated successfully");
        return response;
      } catch (error) {
        setUpdateError(error);
        return null;
      } finally {
        setUpdateLoading(false);
      }
    },
    [token]
  );

  const handleDeleteCategory = useCallback(
    async (categoryId, isDeleted) => {
      setDeleteError(null);
      setDeleteSuccess(null);
      setDeleteLoading(true);
      try {
        const response = await deleteCategory(categoryId, isDeleted, token);
        setDeleteSuccess(response?.message || "Category updated successfully");
        return response;
      } catch (error) {
        setDeleteError(error);
        return null;
      } finally {
        setDeleteLoading(false);
      }
    },
    [token]
  );

  return {
    handleCreateCategory,
    createError,
    createLoading,
    createSuccess,
    resetCreate: useCallback(() => {
      setCreateError(null);
      setCreateSuccess(null);
    }, []),

    handleUpdateCategory,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate: useCallback(() => {
      setUpdateError(null);
      setUpdateSuccess(null);
    }, []),

    handleDeleteCategory,
    deleteError,
    deleteLoading,
    deleteSuccess,
    resetDelete: useCallback(() => {
      setDeleteError(null);
      setDeleteSuccess(null);
    }, []),
  };
};
