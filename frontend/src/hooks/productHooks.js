import useSWR from "swr";
import {
  createProduct,
  deleteProduct,
  fetchProduct,
  fetchProducts,
  updateProduct,
  updateProductQuantity,
} from "../api/productApi";
import { PRODUCTS_ENDPOINT } from "../constants/api";
import { useCallback, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

export const useProducts = ({
  page = 0,
  size = 10,
  categoryId,
  search,
  isFeatured,
  isDeleted = false,
  direction = SORTING_DIRECTIONS.ASC,
  discontinued,
  sortBy = "productName",
} = {}) => {
  return useSWR(
    [
      PRODUCTS_ENDPOINT,
      page,
      size,
      direction,
      sortBy,
      isFeatured,
      isDeleted,
      categoryId,
      search,
      discontinued
    ],
    () =>
      fetchProducts({
        page: page,
        size: size,
        categoryId: categoryId,
        search: search,
        isFeatured: isFeatured,
        isDeleted: isDeleted,
        direction: direction,
        sortBy: sortBy,
        discontinued: discontinued,
      })
  );
};

export const useProduct = ({ productId, isDeleted = false }) => {
  return useSWR([PRODUCTS_ENDPOINT, productId, isDeleted], () =>
    fetchProduct({ productId, isDeleted })
  );
};

export const useProductActions = () => {
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

  const [updateQuantityError, setUpdateQuantityError] = useState(null);
  const [updateQuantityLoading, setUpdateQuantityLoading] = useState(false);
  const [updateQuantitySuccess, setUpdateQuantitySuccess] = useState(null);

  const handleUpdateProduct = useCallback(
    async (id, requestData) => {
      setUpdateError(null);
      setUpdateSuccess(null);
      setUpdateLoading(true);
      try {
        const response = await updateProduct({
          productId: id,
          productData: requestData,
          token,
        });
        setUpdateSuccess(response?.message || "Product updated successfully");
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

  const handleCreateProduct = useCallback(
    async (requestData) => {
      setCreateError(null);
      setCreateSuccess(null);
      setCreateLoading(true);
      try {
        const response = await createProduct({
          productData: requestData,
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

  const handleDeleteProduct = useCallback(
    async (productId, isDeleted) => {
      setDeleteError(null);
      setDeleteSuccess(null);
      setDeleteLoading(true);
      try {
        const response = await deleteProduct({ productId, isDeleted, token });
        setDeleteSuccess(response?.message || "Product updated successfully");
        return response.message;
      } catch (error) {
        setDeleteError(error);
        return null;
      } finally {
        setDeleteLoading(false);
      }
    },
    [token]
  );

  const handleUpdateProductQuantity = useCallback(
    async (productId, quantity) => {
      setUpdateQuantityError(null);
      setUpdateQuantitySuccess(null);
      setUpdateQuantityLoading(true);
      try {
        const response = await updateProductQuantity({
          productId,
          quantity,
          token,
        });
        setUpdateQuantitySuccess(response?.message || 'Product quantity updated successfully');
        return response.data;
      } catch (error) {
        setUpdateQuantityError(error);
        return null;
      } finally {
        setUpdateQuantityLoading(false);
      }
    },
    [token]
  );

  return {
    handleUpdateProduct,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate: useCallback(() => {
      setUpdateError(null);
      setUpdateSuccess(null);
    }, []),

    handleCreateProduct,
    createError,
    createLoading,
    createSuccess,
    resetCreate: useCallback(() => {
      setCreateError(null);
      setCreateSuccess(null);
    }, []),

    handleDeleteProduct,
    deleteError,
    deleteLoading,
    deleteSuccess,
    resetDelete: useCallback(() => {
      setDeleteError(null);
      setDeleteSuccess(null);
    }, []),

    handleUpdateProductQuantity,
    updateQuantityError,
    updateQuantityLoading,
    updateQuantitySuccess,
    resetUpdateQuantity: useCallback(() => {
      setUpdateQuantityError(null);
      setUpdateQuantitySuccess(null);
    }, [])
  };
};
