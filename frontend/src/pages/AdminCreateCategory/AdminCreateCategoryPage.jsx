import React, { useState, useRef, useEffect, useCallback } from "react";
import { useCategoryActions } from "../../hooks/categoryHooks";
import Loading from "../../components/Loading/Loading";
import { useModal } from "../../context/ModalContext";
import SingleImageDisplay from "../../components/SingleImageDisplay/SingleImageDisplay";
import CategoryForm from "../../components/CategoryForm/CategoryForm";
import { useImageUpload } from "../../hooks/imageHooks";
import { useAlert } from "../../context/AlertContext";
import { useNavigate } from "react-router-dom";
import { ADMIN_CATEGORIES_URI } from "../../constants/routes";

function AdminCategoryPage() {
  const navigate = useNavigate();
  const [imagePreview, setImagePreview] = useState(null);
  const [uploadImage, setUploadImage] = useState(null);
  const fileInputRef = useRef(null);
  const [fieldErrors, setFieldErrors] = useState({});
  const [newCategory, setNewCategory] = useState({
    categoryName: "",
    description: "",
    picture: null,
  });

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const {
    handleCreateCategory,
    createError,
    createLoading,
    createSuccess,
    resetCreate,
  } = useCategoryActions();

  const { handleUploadImage } = useImageUpload();

  useEffect(() => {
    setFieldErrors(createError?.fields ?? {});
  }, [createError]);

  useEffect(() => {
    if (createSuccess) {
      showNewAlert({
        message: createSuccess,
        action: resetCreate,
      });
      navigate(ADMIN_CATEGORIES_URI)
    }
  }, [createSuccess, showNewAlert, resetCreate, navigate]);

  const handleFormSubmit = (event) => {
    event.preventDefault();
    onOpen({
      title: "Create category!",
      message: "Do you want to continue?",
      onYes: handleCreate,
    });
  };

  const handleCreate = useCallback(async () => {
    if(!(imagePreview || uploadImage)){
      showNewAlert({
        message: "Add images to upload!",
        variant: "danger",
      });
      return;
    }

    if (!(newCategory.picture || imagePreview)) {
      setFieldErrors({picture: ["Image is required"]})
      return;
    }
    // upload image
    const uploadedImageData = await handleUploadImage([uploadImage]);
    if (!uploadedImageData) {
      showNewAlert({
        message: "Fail to upload images!",
        variant: "danger",
      });
      return;
    }
    // create category
    const data = {
      categoryName: newCategory.categoryName,
      description: newCategory.description,
      picture: uploadedImageData[0].imageUrl || imagePreview,
    };
    const response = await handleCreateCategory(data);
    if (response) {
      setUploadImage(null);
      setNewCategory({ categoryName: "", description: "", picture: null });
      setImagePreview(null);
      setFieldErrors({});
    }
  }, [newCategory, handleCreateCategory, handleUploadImage, imagePreview, uploadImage, showNewAlert]);

  const handleCancelClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.value = null;
    }
    setNewCategory({ categoryName: "", description: "", picture: null });
    setImagePreview(null);
    setFieldErrors({});
    resetCreate();
  };

  const handleImageChange = async (event) => {
    const file = event.target.files[0];
    if(file){
      setUploadImage(file)
      setImagePreview(URL.createObjectURL(file))
    }else{
      showNewAlert({
        message: "Image not found!",
        variant: "danger",
      });
    }
  };

  const handleInputChange = useCallback((e) => {
    const { name, value } = e.target;
    setNewCategory((prevCategory) => ({
      ...prevCategory,
      [name]: value,
    }));
  }, []);

  if (createLoading) return <Loading />;

  return (
    <div className="container">
      <h1 className="mb-4">Category Management</h1>

      {createError?.message && (
        <div className="alert alert-danger">{createError.message}</div>
      )}

      <div className="form-section">
        <h2 id="form-title">Add New Category</h2>
        <form id="category-form" onSubmit={handleFormSubmit}>
          <div className="form-container">
            <div className="form-left">
              <SingleImageDisplay imageUrl={imagePreview} />
              <div className="form-group">
                <label htmlFor="picture">Category Image</label>
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
              <CategoryForm
                category={newCategory}
                fieldErrors={fieldErrors}
                handleInputChange={handleInputChange}
              />

              <div className="form-buttons">
                <button
                  type="submit"
                  className="btn btn-primary"
                  id="submit-btn"
                  disabled={createLoading}
                >
                  {createLoading ? "Adding Category..." : "Add Category"}
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

export default AdminCategoryPage;
