import { useCallback, useState, useEffect, useRef } from "react";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";
import { useImageUpload } from "../../hooks/imageHooks";
import SingleImageDisplay from "../../components/SingleImageDisplay/SingleImageDisplay";
import { mutate } from "swr";
import { USER_ENDPOINT } from "../../constants/api";
import { useEmployeeProfile, useEmployeeProfileActions } from "../../hooks/employeeHooks";

const EmployeeProfilePage = () => {
  const [employeeProfile, setEmployeeProfile] = useState({
    employeeId: "",
    title: "",
    birthDate: "",
    hireDate: "",
    notes: "",
    profile: {
      address: "",
      ward: "",
      district: "",
      city: "",
      phone: "",
      picture: "",
      user: {
        userId: "",
        username: "",
        email: "",
        firstName: "",
        lastName: "",
        enabled: true,
        emailVerified: true,
        image: "",
        roles: [],
        createdAt: "",
        lastModifiedAt: ""
      },
      createdAt: "",
      lastModifiedAt: ""
    },
    createdAt: "",
    lastModifiedAt: ""
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
  } = useEmployeeProfile();

  const {
    handleUpdateProfile,
    updateLoading,
    updateError,
    updateSuccess,
    resetUpdate,
  } = useEmployeeProfileActions();

  const { handleUploadImage } = useImageUpload();

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (profileData) {
      setEmployeeProfile({
        employeeId: profileData.employeeId || "",
        title: profileData.title || "",
        birthDate: profileData.birthDate || "",
        hireDate: profileData.hireDate || "",
        notes: profileData.notes || "",
        profile: {
          address: profileData.profile?.address || "",
          ward: profileData.profile?.ward || "",
          district: profileData.profile?.district || "",
          city: profileData.profile?.city || "",
          phone: profileData.profile?.phone || "",
          picture: profileData.profile?.picture || "",
          user: {
            userId: profileData.profile?.user?.userId || "",
            username: profileData.profile?.user?.username || "",
            email: profileData.profile?.user?.email || "",
            firstName: profileData.profile?.user?.firstName || "",
            lastName: profileData.profile?.user?.lastName || "",
            enabled: profileData.profile?.user?.enabled ?? true,
            emailVerified: profileData.profile?.user?.emailVerified ?? true,
            image: profileData.profile?.user?.image || "",
            roles: profileData.profile?.user?.roles || [],
            createdAt: profileData.profile?.user?.createdAt || "",
            lastModifiedAt: profileData.profile?.user?.lastModifiedAt || ""
          },
          createdAt: profileData.profile?.createdAt || "",
          lastModifiedAt: profileData.profile?.lastModifiedAt || ""
        },
        createdAt: profileData.createdAt || "",
        lastModifiedAt: profileData.lastModifiedAt || ""
      });

      if (profileData.profile?.picture) {
        setImagePreview(profileData.profile.picture);
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
        action: resetUpdate,
      });
    }
  }, [updateSuccess, resetUpdate, showNewAlert]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setEmployeeProfile((prevProfile) => ({
      ...prevProfile,
      profile: {
        ...prevProfile.profile,
        user: {
          ...prevProfile.profile.user,
          [name]: value,
        },
      },
    }));
    setFieldErrors((prevErrors) => ({
      ...prevErrors,
      user: { ...prevErrors.user, [name]: null },
    }));
  };

  const handleProfileChange = (event) => {
    const { name, value } = event.target;
    setEmployeeProfile((prevProfile) => ({
      ...prevProfile,
      profile: {
        ...prevProfile.profile,
        [name]: value,
      },
    }));
    setFieldErrors((prevErrors) => ({ ...prevErrors, [name]: null }));
  };

  const handleEmployeeChange = (event) => {
    const { name, value } = event.target;
    setEmployeeProfile((prevProfile) => ({
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
      title: "Update Employee Profile",
      message: "Do you want to save these changes?",
      onYes: handleUpdate,
    });
  };

  const handleUpdate = useCallback(async () => {
    if (!isEditing) return;

    let uploadedImageData = null;
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
      employeeId: employeeProfile.employeeId,
      birthDate: employeeProfile.birthDate,
      notes: employeeProfile.notes,
      profile: {
        address: employeeProfile.profile.address,
        city: employeeProfile.profile.city,
        district: employeeProfile.profile.district,
        ward: employeeProfile.profile.ward,
        phone: employeeProfile.profile.phone,
        picture: uploadedImageData
          ? uploadedImageData[0].imageUrl
          : employeeProfile.profile.picture,
      },
    };
    const response = await handleUpdateProfile(data);
    if (response) {
      setFieldErrors({});
      setIsEditing(false);
      mutate(USER_ENDPOINT);
    }
  }, [
    employeeProfile,
    handleUpdateProfile,
    uploadImage,
    handleUploadImage,
    isEditing,
    showNewAlert,
  ]);

  if ((!profileData && loadingProfile) || updateLoading)
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
                    id="forceUpload"
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
                  <h5 className="card-title mb-0">Employee Details</h5>
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
                        value={employeeProfile?.profile?.user?.username || ""}
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
                          value={employeeProfile?.profile?.user?.firstName || ""}
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
                          value={employeeProfile?.profile?.user?.lastName || ""}
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
                        value={employeeProfile?.profile?.user?.email || ""}
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
                        <label className="small mb-1" htmlFor="inputBirthDate">
                          Birth Date
                        </label>
                        <input
                          required
                          className={`form-control ${
                            fieldErrors?.birthDate ? "is-invalid" : ""
                          }`}
                          id="inputBirthDate"
                          type="date"
                          name="birthDate"
                          value={employeeProfile?.birthDate || ""}
                          onChange={handleEmployeeChange}
                          disabled={!isEditing}
                        />
                        {fieldErrors?.birthDate &&
                          fieldErrors.birthDate.map((error, index) => (
                            <div
                              key={index}
                              className="invalid-feedback d-block"
                            >
                              {error}
                            </div>
                          ))}
                      </div>
                      <div className="col-md-6">
                        <label className="small mb-1" htmlFor="inputHireDate">
                          Hire Date
                        </label>
                        <input
                          className={`form-control ${
                            fieldErrors?.hireDate ? "is-invalid" : ""
                          }`}
                          id="inputHireDate"
                          type="date"
                          name="hireDate"
                          value={employeeProfile?.hireDate || ""}
                          onChange={handleEmployeeChange}
                          disabled={!isEditing}
                        />
                        {fieldErrors?.hireDate &&
                          fieldErrors.hireDate.map((error, index) => (
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
                          value={employeeProfile?.profile?.phone || ""}
                          onChange={handleProfileChange}
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
                          value={employeeProfile?.profile?.address || ""}
                          onChange={handleProfileChange}
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
                          value={employeeProfile?.profile?.city || ""}
                          onChange={handleProfileChange}
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
                          value={employeeProfile?.profile?.district || ""}
                          onChange={handleProfileChange}
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
                          value={employeeProfile?.profile?.ward || ""}
                          onChange={handleProfileChange}
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
                    <div className="mb-3">
                      <label className="small mb-1" htmlFor="inputNotes">
                        Notes
                      </label>
                      <textarea
                        className={`form-control ${
                          fieldErrors?.notes ? "is-invalid" : ""
                        }`}
                        id="inputNotes"
                        placeholder="Enter any notes"
                        name="notes"
                        value={employeeProfile?.notes || ""}
                        onChange={handleEmployeeChange}
                        disabled={!isEditing}
                        rows="4"
                      />
                      {fieldErrors?.notes &&
                        fieldErrors.notes.map((error, index) => (
                          <div key={index} className="invalid-feedback d-block">
                            {error}
                          </div>
                        ))}
                    </div>
                    <div className="d-flex justify-content-between">
                      <button
                        className="btn btn-primary"
                        type="submit"
                        disabled={updateLoading || !isEditing}
                      >
                        {updateLoading ? "Saving..." : "Save changes"}
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

export default EmployeeProfilePage;