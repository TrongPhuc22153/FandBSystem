import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { HOME_URI } from "../../constants/routes";
import { useAuth } from "../../context/AuthContext";
import styles from "./PasswordForm.module.css";
import Loading from "../Loading/Loading";

export default function RequiredResetPasswordForm() {
  const [input, setInput] = useState({
    newPassword: "",
    confirmPassword: "",
  });
  const { 
    handleResetPassword, resetPasswordError, resetPasswordLoading, resetResetPassword
   } = useAuth();
  const [fieldErrors, setFieldErrors] = useState({});
  const navigate = useNavigate();
  const [passwordMatchError, setPasswordMatchError] = useState("");

  useEffect(() => {
    setFieldErrors(resetPasswordError?.fields ?? {});
  }, [resetPasswordError]);

  const onChange = (e) => {
    const { name, value } = e.target;
    setInput((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  useEffect(() => {
    const { newPassword, confirmPassword } = input;

    if (!newPassword || !confirmPassword) {
      setPasswordMatchError("");
      return;
    }

    setPasswordMatchError(
      newPassword !== confirmPassword ? "New passwords do not match" : ""
    );
  }, [input.newPassword, input.confirmPassword]);

  const validateForm = () => {
    const errors = {};
    if (input.newPassword.length < 8) {
      errors.newPassword = ["Password must be at least 8 characters long"];
    }
    if (input.newPassword !== input.confirmPassword) {
      errors.confirmPassword = ["Passwords do not match"];
    }
    return errors;
  };

  const handleSubmitEvent = async (e) => {
    e.preventDefault();
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setFieldErrors(validationErrors);
      return;
    }
    if (input.newPassword && input.confirmPassword) {
      const response = await handleResetPassword(input.newPassword);
      if (response) {
        resetResetPassword();
      }
    }
  };

  if(resetPasswordLoading) return <Loading/>

  return (
    <div className={styles.center} id="change-password-page">
      <h1>Change Password</h1>
      {resetPasswordError && (
        <div className="invalid-feedback d-block text-center">
          {resetPasswordError.message}
        </div>
      )}
      <form onSubmit={handleSubmitEvent} className={styles.form}>
        <div className={styles.txt_field}>
          <input
            type="password"
            name="newPassword"
            required
            value={input.newPassword}
            onChange={onChange}
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
        <div className={styles.txt_field}>
          <input
            type="password"
            name="confirmPassword"
            required
            value={input.confirmPassword}
            onChange={onChange}
          />
          <span></span>
          <label htmlFor="confirmPassword">Confirm Password</label>
          {passwordMatchError && (
            <div className="invalid-feedback d-block">{passwordMatchError}</div>
          )}
          {fieldErrors.confirmPassword &&
            fieldErrors.confirmPassword.map((error, index) => (
              <div key={index} className="invalid-feedback d-block">
                {error}
              </div>
            ))}
        </div>
        <input
          type="submit"
          value="Change Password"
          disabled={resetPasswordLoading || passwordMatchError.length > 0}
        />
        <div className={styles.signup_link}></div>
      </form>
    </div>
  );
}
