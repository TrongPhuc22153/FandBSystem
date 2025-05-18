import { useCallback, useEffect, useState } from "react";
import { useModal } from "../../context/ModalContext";
import styles from "./PasswordForm.module.css";
import { useAuthActions } from "../../hooks/authHooks";

function ChangePasswordForm() {
  const [fieldErrors, setFieldErrors] = useState({});
  const {
    handleChangePassword,
    loadingChangePassword,
    changePasswordError,
    changePasswordSuccess,
    resetChangePassword,
  } = useAuthActions();

  const [passwordInfo, setPasswordInfo] = useState({
    oldPassword: "",
    newPassword: "",
    confirmNewPassword: "",
  });
  const [passwordMatchError, setPasswordMatchError] = useState("");

  const { onOpen } = useModal();

  useEffect(() => {
    setFieldErrors(changePasswordError?.fields ?? {});
  }, [changePasswordError]);

  useEffect(() => {
    if (changePasswordSuccess) {
      const timer = setTimeout(() => {
        resetChangePassword();
      }, 5000);

      return () => {
        clearTimeout(timer);
      };
    }
  }, [changePasswordSuccess, resetChangePassword]);

  useEffect(() => {
    const { newPassword, confirmNewPassword } = passwordInfo;

    if (!newPassword || !confirmNewPassword) {
      setPasswordMatchError("");
      return;
    }

    setPasswordMatchError(
      newPassword !== confirmNewPassword ? "New passwords do not match" : ""
    );
  }, [passwordInfo.newPassword, passwordInfo.confirmNewPassword]);

  const handleFormSubmit = async (event) => {
    event.preventDefault();
    onOpen({
      title: "Change Password",
      message: "Do you want to proceed with changing your password?",
      onYes: handleChange,
    });
  };

  const handleChange = useCallback(async () => {
    const passwordData = {
      oldPassword: passwordInfo.oldPassword,
      newPassword: passwordInfo.newPassword,
    };

    const response = await handleChangePassword(passwordData);

    if (response) {
      setFieldErrors({});
      setPasswordInfo({
        oldPassword: "",
        newPassword: "",
        confirmNewPassword: "",
      });
    }
  }, [passwordInfo, handleChangePassword]);

  const onChange = (event) => {
    setPasswordInfo((prev) => ({
      ...prev,
      [event.target.name]: event.target.value,
    }));
  };

  return (
    <div className="container">
      <div className={styles["center"]}>
        <h1>Change Password</h1>
        {changePasswordSuccess && (
          <div className="alert alert-success text-center">
            Password changed successfully!
          </div>
        )}
        {changePasswordError?.message && (
          <div className="invalid-feedback d-block text-center">
            {changePasswordError.message}
          </div>
        )}
        <form onSubmit={handleFormSubmit}>
          <div className={styles["txt_field"]}>
            <input
              type="password"
              name="oldPassword"
              required
              onChange={onChange}
              value={passwordInfo.oldPassword}
            />
            <span></span>
            <label htmlFor="oldPassword">Current Password</label>
            {fieldErrors.oldPassword &&
              fieldErrors.oldPassword.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <div className={styles["txt_field"]}>
            <input
              type="password"
              name="newPassword"
              required
              onChange={onChange}
              value={passwordInfo.newPassword}
            />
            <span></span>
            <label htmlFor="newPassword">New Password</label>
            {fieldErrors.newPassword &&
              fieldErrors.newPassword.map((error, index) => (
                <div key={index} className="invalid-feedback d-block">
                  {error}
                </div>
              ))}
          </div>
          <div className={styles["txt_field"]}>
            <input
              type="password"
              name="confirmNewPassword"
              required
              onChange={onChange}
              value={passwordInfo.confirmNewPassword}
            />
            <span></span>
            <label htmlFor="confirmNewPassword">Confirm New Password</label>
            {passwordMatchError && (
              <div className="invalid-feedback d-block">
                {passwordMatchError}
              </div>
            )}
          </div>
          <input
            type="submit"
            className="my-3"
            value={loadingChangePassword ? "Saving..." : "Save"}
            disabled={loadingChangePassword || passwordMatchError.length > 0}
          />
        </form>
      </div>
    </div>
  );
}

export default ChangePasswordForm;