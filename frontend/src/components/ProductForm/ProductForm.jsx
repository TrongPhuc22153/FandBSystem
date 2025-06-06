export default function ProductForm({
  product,
  fieldErrors,
  handleInputChange,
  categories,
  isEditMode = true,
}) {
  return (
    <>
      <div className="row">
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="productName">Product Name</label>
            <input
              type="text"
              id="productName"
              name="productName"
              className="form-control"
              value={product.productName}
              onChange={handleInputChange}
              required
              disabled={!isEditMode}
            />
            {fieldErrors?.productName &&
              fieldErrors.productName.map((error, index) => (
                <div key={index} className="error-message text-danger">
                  {error}
                </div>
              ))}
          </div>
        </div>
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="categoryId">Category</label>
            <select
              id="categoryId"
              name="categoryId"
              className="form-control"
              value={product.categoryId}
              onChange={handleInputChange}
              required
              disabled={!isEditMode}
            >
              <option value="">Select Category</option>
              {categories.map((category, index) => (
                <option value={category.categoryId} key={index}>
                  {category.categoryName}
                </option>
              ))}
            </select>
            {fieldErrors?.categoryId &&
              fieldErrors.categoryId.map((error, index) => (
                <div key={index} className="error-message text-danger">
                  {error}
                </div>
              ))}
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="isFeatured">Is Featured</label>
            <select
              id="isFeatured"
              name="isFeatured"
              className="form-control"
              value={product.isFeatured}
              onChange={handleInputChange}
              disabled={!isEditMode}
            >
              <option value={false}>No</option>
              <option value={true}>Yes</option>
            </select>
            {fieldErrors?.isFeatured &&
              fieldErrors.isFeatured.map((error, index) => (
                <div key={index} className="error-message text-danger">
                  {error}
                </div>
              ))}
          </div>
        </div>
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="unitPrice">Unit Price ($)</label>
            <input
              type="number"
              id="unitPrice"
              name="unitPrice"
              className="form-control"
              step="0.01"
              min="0"
              value={product.unitPrice}
              onChange={handleInputChange}
              required
              disabled={!isEditMode}
            />
            {fieldErrors?.unitPrice &&
              fieldErrors.unitPrice.map((error, index) => (
                <div key={index} className="error-message text-danger">
                  {error}
                </div>
              ))}
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="unitsInStock">Units in Stock</label>
            <input
              type="number"
              id="unitsInStock"
              name="unitsInStock"
              className="form-control"
              min="0"
              value={product.unitsInStock}
              onChange={handleInputChange}
              required
              disabled={!isEditMode}
            />
            {fieldErrors?.unitsInStock &&
              fieldErrors.unitsInStock.map((error, index) => (
                <div key={index} className="error-message text-danger">
                  {error}
                </div>
              ))}
          </div>
        </div>
        <div className="col-md-6">
          <div className="form-group">
            <div className="form-group">
              <label htmlFor="minimumStock">Minimum Stock</label>
              <input
                type="number"
                id="minimumStock"
                name="minimumStock"
                className="form-control"
                min="0"
                value={product.minimumStock}
                onChange={handleInputChange}
                required
                disabled={!isEditMode}
              />
              {fieldErrors?.minimumStock &&
                fieldErrors.minimumStock.map((error, index) => (
                  <div key={index} className="error-message text-danger">
                    {error}
                  </div>
                ))}
            </div>
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
              value={product.description}
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
