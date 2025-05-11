export default function ProfileForm({
    fieldErrors,
    userProfile,
    handleChange,
    isEditing = true,
    handleAddressChange,
  }) {
    return (
      <>
        <div className="mb-3">
          <label className="small mb-1" htmlFor="inputUsername">
            Username
          </label>
          <input
            required
            className={`form-control ${
              fieldErrors?.user?.username ? "is-invalid" : ""
            }`}
            id="inputUsername"
            type="text"
            placeholder="Enter your username"
            name="username"
            value={userProfile?.user?.username || ""}
            onChange={handleChange}
            disabled={!isEditing || true}
          />
          {fieldErrors?.user?.username &&
            fieldErrors.user.username.map((error, index) => (
              <div key={index} className="invalid-feedback d-block">
                {error}
              </div>
            ))}
        </div>
        <div className="row gx-3 mb-3">
          <div className="col-md-6">
            <label className="small mb-1" htmlFor="inputFirstName">
              First name
            </label>
            <input
              required
              className={`form-control ${
                fieldErrors?.user?.firstName ? "is-invalid" : ""
              }`}
              id="inputFirstName"
              type="text"
              placeholder="Enter your first name"
              name="firstName"
              value={userProfile?.user?.firstName || ""}
              onChange={handleChange}
              disabled={!isEditing || true}
            />
            {fieldErrors?.user?.firstName &&
              fieldErrors.user.firstName.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <div className="col-md-6">
            <label className="small mb-1" htmlFor="inputLastName">
              Last name
            </label>
            <input
              required
              className={`form-control ${
                fieldErrors?.user?.lastName ? "is-invalid" : ""
              }`}
              id="inputLastName"
              type="text"
              placeholder="Enter your last name"
              name="lastName"
              value={userProfile?.user?.lastName || ""}
              onChange={handleChange}
              disabled={!isEditing || true}
            />
            {fieldErrors?.user?.lastName &&
              fieldErrors.user.lastName.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
        </div>
        <div className="mb-3">
          <label className="small mb-1" htmlFor="inputEmailAddress">
            Email address
          </label>
          <input
            required
            className={`form-control ${
              fieldErrors?.user?.email ? "is-invalid" : ""
            }`}
            id="inputEmailAddress"
            type="email"
            placeholder="Enter your email address"
            name="email"
            value={userProfile?.user?.email || ""}
            onChange={handleChange}
            disabled={!isEditing || true}
          />
          {fieldErrors?.user?.email &&
            fieldErrors.user.email.map((error, index) => (
              <div key={index} className="invalid-feedback d-block">
                {error}
              </div>
            ))}
        </div>
        <div className="row gx-3 mb-3">
          <div className="col-md-6">
            <label className="small mb-1" htmlFor="inputPhone">
              Phone number
            </label>
            <input
              className={`form-control ${fieldErrors?.phone ? "is-invalid" : ""}`}
              id="inputPhone"
              type="tel"
              placeholder="Enter your phone number"
              name="phone"
              value={userProfile?.phone || ""}
              onChange={handleAddressChange}
              disabled={!isEditing}
            />
            {fieldErrors?.phone &&
              fieldErrors.phone.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
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
                fieldErrors?.address ? "is-invalid" : ""
              }`}
              id="inputAddress"
              type="text"
              placeholder="Enter your address"
              name="address"
              value={userProfile?.address || ""}
              onChange={handleAddressChange}
              disabled={!isEditing}
            />
            {fieldErrors?.address &&
              fieldErrors.address.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
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
              className={`form-control ${fieldErrors?.city ? "is-invalid" : ""}`}
              id="inputCity"
              type="text"
              placeholder="Enter your city"
              name="city"
              value={userProfile?.city || ""}
              onChange={handleAddressChange}
              disabled={!isEditing}
            />
            {fieldErrors?.city &&
              fieldErrors.city.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <div className="col-md-6">
            <label className="small mb-1" htmlFor="inputState">
              District
            </label>
            <input
              className={`form-control ${
                fieldErrors?.district ? "is-invalid" : ""
              }`}
              id="inputState"
              type="text"
              placeholder="Enter your district"
              name="district"
              value={userProfile?.district || ""}
              onChange={handleAddressChange}
              disabled={!isEditing}
            />
            {fieldErrors?.district &&
              fieldErrors.district.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
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
              className={`form-control ${fieldErrors?.ward ? "is-invalid" : ""}`}
              id="inputWard"
              type="text"
              placeholder="Enter your ward"
              name="ward"
              value={userProfile?.ward || ""}
              onChange={handleAddressChange}
              disabled={!isEditing}
            />
            {fieldErrors?.ward &&
              fieldErrors.ward.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
        </div>
      </>
    );
  }
  