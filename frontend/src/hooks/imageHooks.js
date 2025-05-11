import { useState, useCallback } from "react";
import { useAuth } from "../context/AuthContext";
import { uploadImage } from "../api/imageApi";

export const useImageUpload = () => {
  const { token } = useAuth();
  const [uploadError, setUploadError] = useState(null);
  const [uploadLoading, setUploadLoading] = useState(false);
  const [uploadSuccess, setUploadSuccess] = useState(null);

  const handleUploadImage = useCallback(
    async (files) => {
      setUploadError(null);
      setUploadSuccess(null);
      setUploadLoading(true);
      try {
        const response = await uploadImage({ token, files });
        setUploadSuccess(response?.message || "Image uploaded successfully");
        return response.data;
      } catch (error) {
        setUploadError(error);
        return null;
      } finally {
        setUploadLoading(false);
      }
    },
    [token]
  );

  return {
    handleUploadImage,
    uploadError,
    uploadLoading,
    uploadSuccess,
    resetUpload: useCallback(() => {
      setUploadError(null);
      setUploadSuccess(null);
    }, []),
  };
};
