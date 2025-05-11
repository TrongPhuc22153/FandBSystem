import React from "react";

function TableForm({
  table,
  handleInputChange,
  fieldErrors,
  isEditMode = true,
}) {
  return (
    <>
      <div className="row">
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="tableNumber">Table Number</label>
            <input
              type="number"
              id="tableNumber"
              name="tableNumber"
              value={table.tableNumber}
              onChange={handleInputChange}
              className={`form-control ${fieldErrors.tableNumber ? "is-invalid" : ""}`}
              required
              disabled={!isEditMode}
            />
            {fieldErrors?.name &&
              fieldErrors.name.map((error, index) => (
                <div key={index} className="invalid-feedback">
                  {error}
                </div>
              ))}
          </div>
        </div>
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="capacity">Capacity</label>
            <input
              type="number"
              id="capacity"
              name="capacity"
              value={table.capacity}
              onChange={handleInputChange}
              className={`form-control ${
                fieldErrors.capacity ? "is-invalid" : ""
              }`}
              required
              disabled={!isEditMode}
            />
            {fieldErrors?.capacity &&
              fieldErrors.capacity.map((error, index) => (
                <div key={index} className="invalid-feedback">
                  {error}
                </div>
              ))}
          </div>
        </div>
      </div>

      <div className="row">
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="location">Location</label>
            <input
              type="text"
              id="location"
              name="location"
              value={table.location}
              onChange={handleInputChange}
              className={`form-control ${
                fieldErrors.location ? "is-invalid" : ""
              }`}
              required
              disabled={!isEditMode}
            />
            {fieldErrors?.location &&
              fieldErrors.location.map((error, index) => (
                <div key={index} className="invalid-feedback">
                  {error}
                </div>
              ))}
          </div>
        </div>
        <div className="col-md-6">
          <div className="form-group">
            <label htmlFor="status">Status</label>
            <select
              id="status"
              name="status"
              value={table.status}
              onChange={handleInputChange}
              className={`form-control ${
                fieldErrors.status ? "is-invalid" : ""
              }`}
              required
              disabled={!isEditMode}
            >
              <option value="">Select Status</option>
              <option value="OCCUPIED">Occupied</option>
              <option value="UNOCCUPIED">Unoccupied</option>
              {/* Add other status options as needed */}
            </select>
            {fieldErrors?.status &&
              fieldErrors.status.map((error, index) => (
                <div key={index} className="invalid-feedback">
                  {error}
                </div>
              ))}
          </div>
        </div>
      </div>
    </>
  );
}

export default TableForm;
