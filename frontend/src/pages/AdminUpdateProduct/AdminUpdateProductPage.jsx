import { useState, useRef, useEffect, useCallback } from "react";
import { useCategories } from "../../hooks/categoryHooks";
import { useProduct, useProductActions } from "../../hooks/productHooks";
import Loading from "../../components/Loading/Loading";
import { useParams } from "react-router-dom";
import { formatDate } from "../../utils/datetimeUtils";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useModal } from "../../context/ModalContext";
import SingleImageDisplay from "../../components/SingleImageDisplay/SingleImageDisplay";
import ProductForm from "../../components/ProductForm/ProductForm";
import { useImageUpload } from "../../hooks/imageHooks";
import { useAlert } from "../../context/AlertContext";

function AdminUpdateProductPage() {
  const { id } = useParams();
  const [imagePreview, setImagePreview] = useState(null);
  const [uploadImage, setUploadImage] = useState(null);
  const fileInputRef = useRef(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [productDetails, setProductDetails] = useState({
    productName: "",
    categoryId: 0,
    unitPrice: 0,
    unitsInStock: 0,
    isFeatured: false,
    description: "",
    image: null,
  });

  const { data: categoriesData, isLoading: isCategoriesLoading } =
    useCategories({});
  const categories = categoriesData?.content || [];

  const {
    data: productData,
    isLoading: isProductLoading,
    error,
    mutate: mutateProduct,
  } = useProduct({ productId: id, isDeleted: null });

  const {
    handleUpdateProduct,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate,
  } = useProductActions();

  const { onOpen } = useModal();
  const { handleUploadImage } = useImageUpload();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (productData && !isEditMode) {
      setProductDetails({
        productName: productData.productName || "",
        categoryId: productData.category.categoryId
          ? productData.category.categoryId.toString()
          : 0,
        unitPrice: productData.unitPrice ? productData.unitPrice.toString() : 0,
        unitsInStock: productData.unitsInStock
          ? productData.unitsInStock.toString()
          : 0,
        isFeatured: productData.isFeatured || false,
        description: productData.description || "",
        image: productData.picture,
      });
      if (productData.picture) {
        setImagePreview(productData.picture);
      }
    }
  }, [productData, isEditMode]);

  useEffect(() => {
    setFieldErrors(updateError?.fields ?? {});
  }, [updateError]);

  useEffect(() => {
    if (updateSuccess) {
      mutateProduct();
      showNewAlert({
        message: updateSuccess,
        action: resetUpdate(),
      });
      setIsEditMode(false);
    }
  }, [updateSuccess, mutateProduct, showNewAlert, resetUpdate]);

  const handleInputChange = useCallback((e) => {
    const { name, value, type, checked } = e.target;
    setProductDetails((prevDetails) => ({
      ...prevDetails,
      [name]: type === "checkbox" ? checked : value,
    }));
  }, []);

  const handleFormSubmit = (event) => {
    event.preventDefault();
    if (!isEditMode) return;
    onOpen({
      title: "Update product!",
      message: "Do you want to continue?",
      onYes: handleUpdate,
    });
  };

  const handleUpdate = useCallback(async () => {
    if (!(imagePreview || uploadImage)) {
      showNewAlert({
        message: "Add images to upload!",
        variant: "danger",
      });
      setFieldErrors({ image: ["Image is required"] });
      return;
    }

    if (!(productDetails.image || imagePreview)) {
      setFieldErrors({ image: ["Image is required"] });
      return;
    }

    const uploadedImageData = uploadImage
      ? await handleUploadImage([uploadImage])
      : null;
    if (uploadImage && !uploadedImageData) {
      showNewAlert({
        message: "Failed to upload image!",
        variant: "danger",
      });
      return;
    }

    const productData = {
      productId: +id,
      productName: productDetails.productName,
      categoryId: +productDetails.categoryId,
      unitPrice: productDetails.unitPrice,
      unitsInStock: productDetails.unitsInStock,
      isFeatured: productDetails.isFeatured,
      description: productDetails.description,
      picture: uploadedImageData ? uploadedImageData[0].imageUrl : imagePreview,
    };

    const response = await handleUpdateProduct(id, productData);

    if (response) {
      setImagePreview(response.picture || null);
      setIsEditMode(false);
    }
  }, [
    id,
    productDetails,
    imagePreview,
    uploadImage,
    handleUpdateProduct,
    handleUploadImage,
    showNewAlert,
  ]);

  const handleCancelClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.value = null;
    }
    setImagePreview(productData?.picture || null);
    setUploadImage(null);
    setFieldErrors({});
    resetUpdate();
    setIsEditMode(false);
  };

  const handleImageChange = async (event) => {
    if (!isEditMode) return;
    const file = event.target.files[0];
    if (file) {
      setUploadImage(file);
      setImagePreview(URL.createObjectURL(file));
    } else {
      showNewAlert({
        message: "No image selected!",
        variant: "danger",
      });
    }
  };

  if (isCategoriesLoading || isProductLoading || updateLoading)
    return <Loading />;

  if (error) {
    return <ErrorDisplay message={error.message} />;
  }

  return (
    <div className="container">
      <h1>Product Management</h1>

      {updateError?.message && (
        <div className="alert alert-danger">{updateError.message}</div>
      )}

      <div className="form-section">
        <h2 id="form-title">
          {isEditMode ? "Update Product" : "View Product"}
        </h2>
        <button
          className="btn btn-warning mb-3"
          onClick={() => setIsEditMode(!isEditMode)}
        >
          {isEditMode ? "Disable Edit" : "Enable Edit"}
        </button>
        <form id="product-form" onSubmit={handleFormSubmit}>
          <div className="form-container">
            <div className="form-left">
              <SingleImageDisplay imageUrl={imagePreview} />
              {isEditMode && (
                <div className="form-group">
                  <label htmlFor="image">Product Image</label>
                  <input
                    type="file"
                    id="image"
                    name="image"
                    accept="image/*"
                    onChange={handleImageChange}
                    ref={fileInputRef}
                  />
                  {fieldErrors.image &&
                    fieldErrors.image.map((error, index) => (
                      <div key={index} className="error-message text-danger">
                        {error}
                      </div>
                    ))}
                </div>
              )}
            </div>

            <div className="form-right">
              <ProductForm
                product={productDetails}
                fieldErrors={fieldErrors}
                categories={categories}
                handleInputChange={handleInputChange}
                isEditMode={isEditMode}
              />
              <div className="row">
                <div className="col-lg-6">
                  <div className="form-group">
                    <label htmlFor="createdAt">Created On</label>
                    <input
                      type="text"
                      id="createdAt"
                      name="createdAt"
                      className="form-control"
                      value={formatDate(productData.createdAt)}
                      disabled
                    />
                  </div>
                </div>
                <div className="col-lg-6">
                  <div className="form-group">
                    <label htmlFor="lastModifiedAt">Updated On</label>
                    <input
                      type="text"
                      id="lastModifiedAt"
                      name="lastModifiedAt"
                      className="form-control"
                      value={formatDate(productData.lastModifiedAt)}
                      disabled
                    />
                  </div>
                </div>
              </div>

              <div className="form-buttons">
                {isEditMode ? (
                  <>
                    <button
                      type="submit"
                      className="btn btn-primary"
                      id="submit-btn"
                      disabled={updateLoading}
                    >
                      {updateLoading ? "Updating Product..." : "Update Product"}
                    </button>
                    <button
                      type="button"
                      className="btn btn-secondary"
                      id="cancel-btn"
                      onClick={handleCancelClick}
                    >
                      Cancel
                    </button>
                  </>
                ) : (
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={handleCancelClick}
                  >
                    Back
                  </button>
                )}
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AdminUpdateProductPage;
