import React, { useState, useRef, useEffect, useCallback } from "react";
import { useCategories } from "../../hooks/categoryHooks";
import { useProductActions } from "../../hooks/productHooks";
import Loading from "../../components/Loading/Loading";
import { useModal } from "../../context/ModalContext";
import SingleImageDisplay from "../../components/SingleImageDisplay/SingleImageDisplay";
import ProductForm from "../../components/ProductForm/ProductForm";
import { useImageUpload } from "../../hooks/imageHooks";
import { useAlert } from "../../context/AlertContext";
import { useNavigate } from "react-router-dom";
import { ADMIN_PRODUCTS_URI } from "../../constants/routes";

function AdminCreateProductPage() {
  const navigate = useNavigate();
  const [imagePreview, setImagePreview] = useState(null);
  const [uploadImage, setUploadImage] = useState(null);
  const fileInputRef = useRef(null);
  const [fieldErrors, setFieldErrors] = useState({});
  const [newProduct, setNewProduct] = useState({
    productName: "",
    categoryId: 0,
    unitPrice: 0,
    unitsInStock: 0,
    minimumStock: 0,
    isFeatured: false,
    description: "",
    picture: null,
  });

  const { data: categoriesData, isLoading: isCategoriesLoading } = useCategories({});
  const categories = categoriesData?.content || [];

  const {
    handleCreateProduct,
    createError,
    createLoading,
    createSuccess,
    resetCreate,
  } = useProductActions();

  const { handleUploadImage } = useImageUpload();
  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    setFieldErrors(createError?.fields ?? {});
  }, [createError]);

  useEffect(() => {
    if (createSuccess) {
      showNewAlert({
        message: createSuccess,
        action: resetCreate,
      });
      navigate(ADMIN_PRODUCTS_URI);
    }
  }, [createSuccess, showNewAlert, resetCreate, navigate]);

  const handleFormSubmit = (event) => {
    event.preventDefault();
    onOpen({
      title: "Create product!",
      message: "Do you want to continue?",
      onYes: handleCreate,
    });
  };

  const handleCreate = useCallback(async () => {
    if (!(imagePreview || uploadImage)) {
      showNewAlert({
        message: "Add image to upload!",
        variant: "danger",
      });
      return;
    }

    if (!(newProduct.picture || imagePreview)) {
      setFieldErrors({ picture: ["Image is required"] });
      return;
    }

    // upload image
    const uploadedImageData = await handleUploadImage([uploadImage]);
    if (!uploadedImageData) {
      showNewAlert({
        message: "Failed to upload image!",
        variant: "danger",
      });
      return;
    }

    // create product
    const productData = {
      productName: newProduct.productName,
      categoryId: +newProduct.categoryId,
      unitPrice: newProduct.unitPrice,
      unitsInStock: newProduct.unitsInStock,
      minimumStock: newProduct.minimumStock,
      isFeatured: newProduct.isFeatured,
      description: newProduct.description,
      picture: uploadedImageData[0].imageUrl || imagePreview,
    };

    const response = await handleCreateProduct(productData);

    if (response) {
      setNewProduct({
        productName: "",
        categoryId: 0,
        unitPrice: 0,
        unitsInStock: 0,
        isFeatured: false,
        description: "",
        picture: null,
      });
      setImagePreview(null);
      setUploadImage(null);
      setFieldErrors({});
    }
  }, [newProduct, uploadImage, imagePreview, handleCreateProduct, handleUploadImage, showNewAlert]);

  const handleCancelClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.value = null;
    }
    setNewProduct({
      productName: "",
      categoryId: 0,
      unitPrice: 0,
      unitsInStock: 0,
      minimumStock: 0,
      isFeatured: false,
      description: "",
      picture: null,
    });
    setImagePreview(null);
    setUploadImage(null);
    setFieldErrors({});
    resetCreate();
  };

  const handleImageChange = async (event) => {
    const file = event.target.files[0];
    if (file) {
      setUploadImage(file);
      setImagePreview(URL.createObjectURL(file));
      setNewProduct((prevProduct) => ({
        ...prevProduct,
        picture: file,
      }));
    } else {
      showNewAlert({
        message: "Image not found!",
        variant: "danger",
      });
      setImagePreview(null);
      setUploadImage(null);
      setNewProduct((prevProduct) => ({
        ...prevProduct,
        picture: null,
      }));
    }
  };

  const handleInputChange = useCallback((e) => {
    const { name, value, type, checked } = e.target;
    setNewProduct((prevProduct) => ({
      ...prevProduct,
      [name]: type === "checkbox" ? checked : value,
    }));
  }, []);

  if (isCategoriesLoading || createLoading) return <Loading />;

  return (
    <div className="container">
      <h1 className="mb-4">Product Management</h1>

      {createError?.message && (
        <div className="alert alert-danger">{createError.message}</div>
      )}

      <div className="form-section">
        <h2 id="form-title">Add New Product</h2>
        <form id="product-form" onSubmit={handleFormSubmit}>
          <div className="form-container">
            <div className="form-left">
              <SingleImageDisplay imageUrl={imagePreview} />
              <div className="form-group">
                <label htmlFor="picture">Product Image</label>
                <input
                  type="file"
                  id="picture"
                  name="picture"
                  accept="image/*"
                  onChange={handleImageChange}
                  ref={fileInputRef}
                />
                {fieldErrors.picture &&
                  fieldErrors.picture.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
              </div>
            </div>

            <div className="form-right">
              <ProductForm
                product={newProduct}
                fieldErrors={fieldErrors}
                handleInputChange={handleInputChange}
                categories={categories}
              />
              <div className="form-buttons">
                <button
                  type="submit"
                  className="btn btn-primary"
                  id="submit-btn"
                  disabled={createLoading}
                >
                  {createLoading ? "Adding Product..." : "Add Product"}
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  id="cancel-btn"
                  onClick={handleCancelClick}
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AdminCreateProductPage;