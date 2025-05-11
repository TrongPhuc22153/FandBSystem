import React from "react";

function CategoryForm({
  category,
  handleInputChange,
  fieldErrors,
  isEditMode = true,
}) {
  return (
    <>
      <div className="row">
        <div className="col-md-12">
          <div className="form-group">
            <label htmlFor="categoryName">Category Name</label>
            <input
              type="text"
              id="categoryName"
              name="categoryName"
              className="form-control"
              value={category?.categoryName || ""}
              onChange={handleInputChange}
              required
              disabled={!isEditMode}
            />
            {fieldErrors?.categoryName &&
              fieldErrors.categoryName.map((error, index) => (
                <div key={index} className="error-message text-danger">
                  {error}
                </div>
              ))}
          </div>
        </div>
      </div>

      <div className="row">
        <div className="col-md-12">
          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              className="form-control"
              value={category?.description || ""}
              onChange={handleInputChange}
              disabled={!isEditMode}
            ></textarea>
            {fieldErrors?.description &&
              fieldErrors.description.map((error, index) => (
                <div key={index} className="error-message text-danger">
                  {error}
                </div>
              ))}
          </div>
        </div>
      </div>
    </>
  );
}

export default CategoryForm;
