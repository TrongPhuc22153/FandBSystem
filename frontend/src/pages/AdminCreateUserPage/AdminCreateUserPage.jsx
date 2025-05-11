import React, { useCallback, useEffect, useState } from "react";
import { useUserActions } from "../../hooks/userHook";
import Loading from "../../components/Loading/Loading";
import Select from "react-select";
import { useRoles } from "../../hooks/roleHook";
import { useModal } from "../../context/ModalContext";
import styles from "./AdminCreateUserPage.module.css";

function AdminCreateUserPage() {
  const [fieldErrors, setFieldErrors] = useState({});
  const {
    handleCreateUser,
    createError,
    createLoading,
    createSuccess,
    resetCreate,
  } = useUserActions();

  const [registerInfo, setRegisterInfo] = useState({
    username: "",
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
    roles: [],
  });
  const [passwordMatchError, setPasswordMatchError] = useState("");

  const { data: rolesData, isLoading: loadingRole } = useRoles();

  const { onOpen } = useModal();

  const availableRoles =
    rolesData?.map((role) => ({
      value: role.roleName,
      label: role.roleName,
    })) || [];

  useEffect(() => {
    setFieldErrors(createError?.fields ?? {});
  }, [createError]);

  useEffect(() => {
    if (createSuccess) {
      const timer = setTimeout(() => {
        resetCreate();
      }, 5000);

      return () => {
        clearTimeout(timer);
      };
    }
  }, [createSuccess]);

  useEffect(() => {
    const { password, confirmPassword } = registerInfo;

    if (!password || !confirmPassword) {
      setPasswordMatchError("");
      return;
    }

    setPasswordMatchError(
      password !== confirmPassword ? "Passwords do not match" : ""
    );
  }, [registerInfo.password, registerInfo.confirmPassword]);

  const handleFormSubmit = async (event) => {
    event.preventDefault();
    onOpen({
      title: "Create user!",
      message: "Do you want to continue?",
      onYes: handleCreate,
    });
  };

  const handleCreate = useCallback(async () => {
    const userData = {
      username: registerInfo.username,
      firstName: registerInfo.firstName,
      lastName: registerInfo.lastName,
      email: registerInfo.email,
      password: registerInfo.password,
      roles: registerInfo.roles.map((role) => role.value),
    };

    const response = await handleCreateUser(userData);

    if (response) {
      setFieldErrors({});
      setRegisterInfo({
        username: "",
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: "",
        roles: [],
      });
    }
  }, [registerInfo, handleCreateUser]);

  const onChange = (event) => {
    setRegisterInfo((prev) => ({
      ...prev,
      [event.target.name]: event.target.value,
    }));
  };

  const onRoleChange = (selectedOptions) => {
    setRegisterInfo((prev) => ({
      ...prev,
      roles: selectedOptions || [],
    }));
  };

  if (createLoading || loadingRole) return <Loading />;

  return (
    <div className="container">
      <div className={styles["center"]}>
        <h1>Create User</h1>
        {createSuccess && (
          <div className="alert alert-success text-center">{createSuccess}</div>
        )}
        {createError?.message && (
          <div className="invalid-feedback d-block text-center">
            {createError.message}
          </div>
        )}
        <form onSubmit={handleFormSubmit}>
          <div className={styles["txt_field"]}>
            <input
              type="text"
              name="username"
              required
              onChange={onChange}
              value={registerInfo.username}
            />
            <span></span>
            <label htmlFor="username">Username</label>
            {fieldErrors.username &&
              fieldErrors.username.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <div className={styles["txt_field"]}>
            <input
              type="email"
              name="email"
              required
              onChange={onChange}
              value={registerInfo.email}
            />
            <span></span>
            <label htmlFor="email">Email</label>
            {fieldErrors.email &&
              fieldErrors.email.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <div className={styles["txt_field"]}>
            <input
              type="text"
              name="firstName"
              required
              onChange={onChange}
              value={registerInfo.firstName}
            />
            <span></span>
            <label htmlFor="firstName">First name</label>
            {fieldErrors.firstName &&
              fieldErrors.firstName.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <div className={styles["txt_field"]}>
            <input
              type="text"
              name="lastName"
              required
              onChange={onChange}
              value={registerInfo.lastName}
            />
            <span></span>
            <label htmlFor="lastName">Last name</label>
            {fieldErrors.lastName &&
              fieldErrors.lastName.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>

          <div className={styles["txt_field"]}>
            <input
              type="password"
              name="password"
              required
              onChange={onChange}
              value={registerInfo.password}
            />
            <span></span>
            <label htmlFor="password">Password</label>
            {fieldErrors.password &&
              fieldErrors.password.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <div className={styles["txt_field"]}>
            <input
              type="password"
              name="confirmPassword"
              required
              onChange={onChange}
              value={registerInfo.confirmPassword}
            />
            <span></span>
            <label htmlFor="confirmPassword">Confirm Password</label>
            {passwordMatchError && (
              <div className="invalid-feedback d-block">
                {passwordMatchError}
              </div>
            )}
          </div>
          <div className="txt_field border-0">
            <Select
              isMulti
              name="roles"
              options={availableRoles}
              className="basic-multi-select"
              classNamePrefix="select"
              placeholder="Select Roles"
              onChange={onRoleChange}
              value={registerInfo.roles}
            />
            {fieldErrors.roles &&
              fieldErrors.roles.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <input
            type="submit"
            className="my-3"
            value={createLoading ? "Registering..." : "Add user"}
            disabled={createLoading || passwordMatchError.length > 0}
          />
        </form>
      </div>
    </div>
  );
}

export default AdminCreateUserPage;
