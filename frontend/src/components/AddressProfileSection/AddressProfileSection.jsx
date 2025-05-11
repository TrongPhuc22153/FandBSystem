import React from "react";

const AddressProfileSection = ({
  selectedAddress,
  isEditing,
  fieldErrors,
  handleInputChange,
  handleSubmit,
  isUpdateLoading,
  isCreateLoading,
  handleCancelEdit,
  handleSetDefaultAddress,
  setIsEditing,
  setEditingAddressId 
}) => {
  return (
    <div className="col-xl-8 mb-3">
      <div className="card mb-4">
        <div className="card-header">
          <h5 className="card-title mb-0">
            {selectedAddress?.id === "new" ? "Add New Address" : "Account Details"}
          </h5>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            {selectedAddress && selectedAddress.id !== "new" && (
              <div className="mb-3">
                <label className="small mb-1" htmlFor="inputType">
                  Name
                </label>
                <input
                  className={`form-control ${
                    fieldErrors?.shipName ? "is-invalid" : ""
                  }`}
                  id="inputType"
                  type="text"
                  name="shipName"
                  value={selectedAddress?.shipName || ""}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                />
                {fieldErrors?.shipName &&
                  fieldErrors.shipName.map((error, index) => (
                    <div
                      key={index}
                      className="invalid-feedback d-block"
                    >
                      {error}
                    </div>
                  ))}
              </div>
            )}
            {selectedAddress && selectedAddress.id === "new" && (
              <div className="mb-3">
                <label className="small mb-1" htmlFor="inputType">
                  Name
                </label>
                <input
                  className={`form-control ${
                    fieldErrors?.shipName ? "is-invalid" : ""
                  }`}
                  id="inputType"
                  type="text"
                  name="shipName"
                  value={selectedAddress.shipName || ""}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                />
                {fieldErrors?.shipName &&
                  fieldErrors.shipName.map((error, index) => (
                    <div
                      key={index}
                      className="invalid-feedback d-block"
                    >
                      {error}
                    </div>
                  ))}
              </div>
            )}
            <div className="row gx-3 mb-3">
              <div className="col-md-6">
                <label className="small mb-1" htmlFor="inputPhone">
                  Phone number
                </label>
                <input
                  className={`form-control ${
                    fieldErrors?.phone ? "is-invalid" : ""
                  }`}
                  id="inputPhone"
                  type="tel"
                  placeholder="Enter your phone number"
                  name="phone"
                  value={selectedAddress?.phone || ""}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                />
                {fieldErrors?.phone &&
                  fieldErrors.phone.map((error, index) => (
                    <div
                      key={index}
                      className="invalid-feedback d-block"
                    >
                      {error}
                    </div>
                  ))}
              </div>
              <div className="col-md-6">
                <label className="small mb-1" htmlFor="inputAddress">
                  Address
                </label>
                <input
                  className={`form-control ${
                    fieldErrors?.shipAddress ? "is-invalid" : ""
                  }`}
                  id="inputAddress"
                  type="text"
                  placeholder="Enter your address"
                  name="shipAddress"
                  value={selectedAddress?.shipAddress || ""}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                />
                {fieldErrors?.shipAddress &&
                  fieldErrors.shipAddress.map((error, index) => (
                    <div
                      key={index}
                      className="invalid-feedback d-block"
                    >
                      {error}
                    </div>
                  ))}
              </div>
            </div>

            <div className="row gx-3 mb-3">
              <div className="col-md-6">
                <label className="small mb-1" htmlFor="inputCity">
                  City
                </label>
                <input
                  className={`form-control ${
                    fieldErrors?.shipCity ? "is-invalid" : ""
                  }`}
                  id="inputCity"
                  type="text"
                  placeholder="Enter your city"
                  name="shipCity"
                  value={selectedAddress?.shipCity || ""}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                />
                {fieldErrors?.shipCity &&
                  fieldErrors.shipCity.map((error, index) => (
                    <div
                      key={index}
                      className="invalid-feedback d-block"
                    >
                      {error}
                    </div>
                  ))}
              </div>
              <div className="col-md-6">
                <label className="small mb-1" htmlFor="inputDistrict">
                  District
                </label>
                <input
                  className={`form-control ${
                    fieldErrors?.shipDistrict ? "is-invalid" : ""
                  }`}
                  id="inputDistrict"
                  type="text"
                  placeholder="Enter your district"
                  name="shipDistrict"
                  value={selectedAddress?.shipDistrict || ""}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                />
                {fieldErrors?.shipDistrict &&
                  fieldErrors.shipDistrict.map((error, index) => (
                    <div
                      key={index}
                      className="invalid-feedback d-block"
                    >
                      {error}
                    </div>
                  ))}
              </div>
            </div>

            <div className="row gx-3 mb-3">
              <div className="col-md-6">
                <label className="small mb-1" htmlFor="inputWard">
                  Ward
                </label>
                <input
                  className={`form-control ${
                    fieldErrors?.shipWard ? "is-invalid" : ""
                  }`}
                  id="inputWard"
                  type="text"
                  placeholder="Enter your ward"
                  name="shipWard"
                  value={selectedAddress?.shipWard || ""}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                />
                {fieldErrors?.shipWard &&
                  fieldErrors.shipWard.map((error, index) => (
                    <div
                      key={index}
                      className="invalid-feedback d-block"
                    >
                      {error}
                    </div>
                  ))}
              </div>
            </div>
            <div className="d-flex justify-content-between">
              {isEditing && (
                <>
                  <button
                    className="btn btn-primary"
                    type="submit"
                    disabled={
                      isUpdateLoading ||
                      isCreateLoading ||
                      !isEditing ||
                      !selectedAddress
                    }
                  >
                    {isUpdateLoading || isCreateLoading ? "Saving..." : "Save changes"}
                  </button>
                  <button
                    className="btn btn-secondary"
                    type="button"
                    onClick={handleCancelEdit}
                  >
                    {isEditing ? "Cancel" : "Edit"}
                  </button>
                </>
              )}
              {!isEditing && selectedAddress && (
                <button
                  className="btn btn-outline-primary"
                  type="button"
                  onClick={() => {
                    setIsEditing(true);
                    setEditingAddressId(selectedAddress.id);
                  }}
                >
                  Edit Address
                </button>
              )}
              {!isEditing &&
                selectedAddress &&
                !selectedAddress?.isDefault && (
                  <button
                    className="btn btn-outline-primary"
                    type="button"
                    onClick={() => {
                      handleSetDefaultAddress(selectedAddress.id);
                    }}
                  >
                    Set As Default Address
                  </button>
                )}
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddressProfileSection;