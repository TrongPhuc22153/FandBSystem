import React, { useCallback, useState, useEffect, useRef } from "react";
import { getImageSrc } from "../../utils/imageUtils";
import { useAuthProfileActions } from "../../hooks/profileHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useModal } from "../../context/ModalContext";
import { useCustomerProfile } from "../../hooks/customerHooks";

const CustomerProfilePage = () => {
  const imageInputRef = useRef();
  const [isEditing, setIsEditing] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [selectedImage, setSelectedImage] = useState(null);

  const {
    data: customerProfile,
    isLoading: loadingCustomer,
    error: errorCustomer,
  } = useCustomerProfile();

  const {
    handleUpdateUserAuthenticatedProfile,
    loadingProfile: loadingUpdateProfile,
    updateError,
    updateSuccess,
    resetUpdateUserAuthProfile,
  } = useAuthProfileActions();
  const { onOpen } = useModal();

  const [userProfile, setUserProfile] = useState({
    picture: null,
    user: {
      username: "",
      firstName: "",
      lastName: "",
      email: "",
    },
    address: "",
    city: "",
    district: "",
    ward: "",
    phone: "",
  });

  useEffect(() => {
    if (customerProfile?.profile) {
      setUserProfile({
        picture: customerProfile.profile.picture || null,
        user: {
          username: customerProfile.profile.user?.username || "",
          firstName: customerProfile.profile.user?.firstName || "",
          lastName: customerProfile.profile.user?.lastName || "",
          email: customerProfile.profile.user?.email || "",
        },
        address: customerProfile.profile.address || "",
        city: customerProfile.profile.city || "",
        district: customerProfile.profile.district || "",
        ward: customerProfile.profile.ward || "",
        phone: customerProfile.profile.phone || "",
      });
    }
  }, [customerProfile]);

  useEffect(() => {
    setFieldErrors(updateError?.fields ?? {});
  }, [updateError]);

  useEffect(() => {
    if (updateSuccess) {
      setIsEditing(false);
      const timer = setTimeout(() => {
        resetUpdateUserAuthProfile();
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [updateSuccess, resetUpdateUserAuthProfile]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setUserProfile((prevProfile) => ({
      ...prevProfile,
      user: {
        ...prevProfile.user,
        [name]: value,
      },
    }));
    setFieldErrors((prevErrors) => ({
      ...prevErrors,
      user: { ...prevErrors.user, [name]: null },
    }));
  };

  const handleAddressChange = (event) => {
    const { name, value } = event.target;
    setUserProfile((prevProfile) => ({
      ...prevProfile,
      [name]: value,
    }));
    setFieldErrors((prevErrors) => ({ ...prevErrors, [name]: null }));
  };

  const handleImageUpload = (event) => {
    const file = event.target.files?.[0];
    if (file) {
      // Basic client-side validation (file type and size)
      if (file.type !== "image/jpeg" && file.type !== "image/png") {
        alert("Invalid file type. Please upload a JPG or PNG image.");
        return;
      }
      if (file.size > 1024 * 1024) {
        // 1MB limit
        alert("File size exceeds 1MB limit.");
        return;
      }

      setSelectedImage(file); // Store the selected file // Read the file and set it to state, so we can display it.
      const reader = new FileReader();
      reader.onload = (e) => {
        if (e.target?.result) {
          setUserProfile((prevProfile) => ({
            ...prevProfile,
            picture: e.target.result, // Store as data URL
          }));
        }
      };
      reader.readAsDataURL(file);
    }
  };

  const submitForm = async (event) => {
    event.preventDefault();
    onOpen({
      title: "Update Profile",
      message: "Do you want to save these changes?",
      onYes: handleUpdate,
    });
  };

  const handleUpdate = useCallback(async () => {
    const data = {
      id: customerProfile?.customerId, // Use customerId
      address: userProfile.address,
      city: userProfile.city,
      district: userProfile.district,
      ward: userProfile.ward,
      phone: userProfile.phone,
      picture: selectedImage
        ? userProfile.picture
        : customerProfile?.profile?.picture, // Use nested picture
      user: {
        firstName: userProfile.user.firstName,
        lastName: userProfile.user.lastName,
      },
    };
    const response = await handleUpdateUserAuthenticatedProfile(data);
    if (response) {
      setFieldErrors({});
    }
  }, [
    userProfile,
    handleUpdateUserAuthenticatedProfile,
    selectedImage,
    customerProfile,
  ]);

  if (loadingCustomer || loadingUpdateProfile) return <Loading />;

  if (errorCustomer?.message)
    return <ErrorDisplay message={errorCustomer.message} />;

  return (
    <main className="content px-5 py-3">
      <div className="container-fluid">
        <div className="container-xl px-4 mt-4">
          {updateError?.message && (
            <div className="alert alert-danger">{updateError.message}</div>
          )}
          <div className="row">
            <div className="col-xl-4">
              <div className="card mb-4 mb-xl-0">
                <div className="card-header">
                  <h5 className="card-title mb-0">Profile Picture</h5>
                </div>
                <div className="card-body text-center d-flex flex-column align-items-center justify-content-center">
                  <img
                    className="img-account-profile rounded-circle mb-2"
                    src={userProfile?.picture || getImageSrc()}
                    alt={`${userProfile?.user?.firstName} ${userProfile?.user?.lastName}`}
                  />
                  <div className="small font-italic text-muted mb-4">
                    JPG or PNG no larger than 1 MB
                  </div>
                  <button
                    className="btn btn-primary"
                    type="button"
                    disabled={!isEditing}
                    onClick={() => {
                      imageInputRef?.current.click();
                    }}
                  >
                    Upload new image
                  </button>
                  <input
                    ref={imageInputRef}
                    id="profileImageUpload"
                    type="file"
                    accept="image/png, image/jpeg"
                    className="d-none"
                    onChange={handleImageUpload}
                  />
                </div>
              </div>
            </div>
            <div className="col-xl-8">
              <div className="card mb-4">
                <div className="card-header">
                  <h5 className="card-title mb-0">Account Details</h5>
                </div>
                <div className="card-body">
                  {updateSuccess && (
                    <div className="alert alert-success">
                      Profile updated successfully!
                    </div>
                  )}
                  <form onSubmit={submitForm}>
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
                    <div className="d-flex justify-content-between">
                      <button
                        className="btn btn-primary"
                        type="submit"
                        disabled={loadingUpdateProfile || !isEditing}
                      >
                        {loadingUpdateProfile ? "Saving..." : "Save changes"}
                      </button>
                      <button
                        className="btn btn-secondary"
                        type="button"
                        onClick={() => setIsEditing(!isEditing)}
                      >
                        {isEditing ? "Cancel" : "Edit"}
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default CustomerProfilePage;
