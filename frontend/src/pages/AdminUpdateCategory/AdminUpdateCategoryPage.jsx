import React, { useState, useRef, useEffect, useCallback } from "react";
import { useCategory, useCategoryActions } from "../../hooks/categoryHooks";
import Loading from "../../components/Loading/Loading";
import { useParams } from "react-router-dom";
import { formatDate } from "../../utils/datetimeUtils";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useModal } from "../../context/ModalContext";
import SingleImageDisplay from "../../components/SingleImageDisplay/SingleImageDisplay";
import CategoryForm from "../../components/CategoryForm/CategoryForm";
import { useImageUpload } from "../../hooks/imageHooks";
import { useAlert } from "../../context/AlertContext";

function AdminUpdateCategoryPage() {
  const { id } = useParams();
  const [imagePreview, setImagePreview] = useState(null);
  const [uploadImage, setUploadImage] = useState(null);
  const fileInputRef = useRef(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [categoryDetails, setCategoryDetails] = useState({
    categoryName: "",
    description: "",
    picture: null,
  });

  const {
    data: categoryData,
    isLoading: isCategoryLoading,
    error,
    mutate: mutateCategory,
  } = useCategory({ id: id, isDeleted: null });

  const {
    handleUpdateCategory,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate,
  } = useCategoryActions();

  const { onOpen } = useModal();
  const { handleUploadImage } = useImageUpload();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (categoryData && !isEditMode) {
      setCategoryDetails({
        categoryName: categoryData.categoryName || "",
        description: categoryData.description || "",
        picture: categoryData.picture,
      });
      if (categoryData.picture) {
        setImagePreview(categoryData.picture);
      }
    }
  }, [categoryData, isEditMode]);

  useEffect(() => {
    setFieldErrors(updateError?.fields ?? {});
  }, [updateError]);

  useEffect(() => {
    if (updateSuccess) {
      mutateCategory();
      showNewAlert({
        message: updateSuccess,
        action: resetUpdate(),
      });
    }
  }, [updateSuccess, mutateCategory, resetUpdate, showNewAlert]);

  const handleInputChange = useCallback((e) => {
    const { name, value } = e.target;
    setCategoryDetails((prevDetails) => ({
      ...prevDetails,
      [name]: value,
    }));
  }, []);

  const handleFormSubmit = (event) => {
    event.preventDefault();
    if (!isEditMode) return;
    onOpen({
      title: "Update category!",
      message: "Do you want to continue?",
      onYes: handleUpdate,
    });
  };

  const handleUpdate = useCallback(async () => {
    if(!(imagePreview || uploadImage)){
      showNewAlert({
        message: "Add images to upload!",
        variant: "danger",
      });
      return;
    }

    if(!(categoryDetails.picture || imagePreview)){
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
    // update category
    const data = {
      categoryId: +id,
      categoryName: categoryDetails.categoryName,
      description: categoryDetails.description,
      picture: uploadedImageData[0].imageUrl || imagePreview,
    };

    const response = await handleUpdateCategory(id, data);

    if (response) {
      setImagePreview(response.picture || null);
      setIsEditMode(false);
    }
  }, [id, categoryDetails, uploadImage, handleUpdateCategory, imagePreview, showNewAlert, handleUploadImage]);

  const handleCancelClick = () => {
    setImagePreview(categoryData?.picture || null);
    setFieldErrors({});
    resetUpdate();
    setIsEditMode(false);
  };

  const handleImageChange = async (event) => {
    if(!isEditMode) return
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

  if (isCategoryLoading) return <Loading />;

  if (error) {
    return <ErrorDisplay message={error.message} />;
  }

  return (
    <div className="container">
      <h1>Category Management</h1>

      {updateError?.message && (
        <div className="alert alert-danger">{updateError.message}</div>
      )}

      <div className="form-section">
        <h2 id="form-title">
          {isEditMode ? "Update Category" : "View Category"}
        </h2>
        <button
          className="btn btn-warning mb-3"
          onClick={() => setIsEditMode(!isEditMode)}
        >
          {isEditMode ? "Disable Edit" : "Enable Edit"}
        </button>

        <form id="category-form" onSubmit={handleFormSubmit}>
          <div className="form-container">
            <div className="form-left">
              <SingleImageDisplay imageUrl={imagePreview} />
              {isEditMode && (
                <div className="form-group">
                  <label htmlFor="image">Category Image</label>
                  <input
                    type="file"
                    id="image"
                    name="image"
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
              )}
            </div>

            <div className="form-right">
              <CategoryForm
                category={categoryDetails}
                handleInputChange={handleInputChange}
                fieldErrors={fieldErrors}
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
                      value={formatDate(categoryData.createdAt)}
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
                      value={formatDate(categoryData.lastModifiedAt)}
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
                      {updateLoading
                        ? "Updating Category..."
                        : "Update Category"}
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

export default AdminUpdateCategoryPage;