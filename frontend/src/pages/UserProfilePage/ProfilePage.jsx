import { useCallback, useState, useEffect, useRef } from "react";
import {
  useAuthenticatedProfile,
  useAuthProfileActions,
} from "../../hooks/profileHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";
import { useImageUpload } from "../../hooks/imageHooks";
import SingleImageDisplay from "../../components/SingleImageDisplay/SingleImageDisplay";
import { mutate } from "swr";
import { USER_ENDPOINT } from "../../constants/api";

const ProfilePage = () => {
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
  const imageInputRef = useRef();
  const [uploadImage, setUploadImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});

  const {
    data: profileData,
    isLoading: loadingProfile,
    error,
  } = useAuthenticatedProfile();

  const {
    handleUpdateUserAuthenticatedProfile,
    loadingProfile: loadingUpdateProfile,
    updateError,
    updateSuccess,
    resetUpdateUserAuthProfile,
  } = useAuthProfileActions();

  const { handleUploadImage } = useImageUpload();

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (profileData) {
      setUserProfile({
        picture: profileData.picture,
        user: {
          username: profileData.user?.username || "",
          firstName: profileData.user?.firstName || "",
          lastName: profileData.user?.lastName || "",
          email: profileData.user?.email || "",
        },
        address: profileData.address || "",
        city: profileData.city || "",
        district: profileData.district || "",
        ward: profileData.ward || "",
        phone: profileData.phone || "",
      });

      if (profileData.picture) {
        setImagePreview(profileData.picture);
      }
    }
  }, [profileData]);

  useEffect(() => {
    setFieldErrors(updateError?.fields ?? {});
  }, [updateError]);

  useEffect(() => {
    if (updateSuccess) {
      showNewAlert({
        message: updateSuccess,
        action: resetUpdateUserAuthProfile,
      });
    }
  }, [updateSuccess, resetUpdateUserAuthProfile, showNewAlert]);

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

  const handleImageUpload = async (event) => {
    const file = event.target.files?.[0];
    if (file) {
      if (file.type !== "image/jpeg" && file.type !== "image/png") {
        showNewAlert({
          message: "Invalid file type. Please upload a JPG or PNG image.",
          variant: "danger",
        });
        return;
      }
      if (file.size > 1024 * 1024) {
        showNewAlert({
          message: "File size exceeds 1MB limit.",
          variant: "danger",
        });
        return;
      }

      setUploadImage(file);
      setImagePreview(URL.createObjectURL(file));
    } else {
      showNewAlert({
        message: "Image not found!",
        variant: "danger",
      });
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
    //upload image
    if (!isEditing) return;

    let uploadedImageData = null;
    // upload image
    if (uploadImage) {
      uploadedImageData = await handleUploadImage([uploadImage]);
      if (!uploadedImageData) {
        showNewAlert({
          message: "Fail to upload images!",
          variant: "danger",
        });
        return;
      }
    }

    const data = {
      id: profileData.id,
      address: userProfile.address,
      city: userProfile.city,
      district: userProfile.district,
      ward: userProfile.ward,
      phone: userProfile.phone,
      picture: uploadedImageData
        ? uploadedImageData[0].imageUrl
        : profileData.picture,
    };
    const response = await handleUpdateUserAuthenticatedProfile(data);
    if (response) {
      setFieldErrors({});
      setIsEditing(false);
      mutate(USER_ENDPOINT);
    }
  }, [
    userProfile,
    handleUpdateUserAuthenticatedProfile,
    uploadImage,
    handleUploadImage,
    isEditing,
    profileData,
    showNewAlert,
  ]);

  if ((!profileData && loadingProfile) || loadingUpdateProfile)
    return <Loading />;

  if (error?.message) return <ErrorDisplay message={error.message} />;

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
                  <SingleImageDisplay imageUrl={imagePreview} />
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
                    id=" farceUpload"
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
                            <div
                              key={index}
                              className="invalid-feedback d-block"
                            >
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
                            <div
                              key={index}
                              className="invalid-feedback d-block"
                            >
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
                          className={`form-control ${
                            fieldErrors?.phone ? "is-invalid" : ""
                          }`}
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
                            fieldErrors?.city ? "is-invalid" : ""
                          }`}
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
                            <div
                              key={index}
                              className="invalid-feedback d-block"
                            >
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
                            fieldErrors?.ward ? "is-invalid" : ""
                          }`}
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

export default ProfilePage;
