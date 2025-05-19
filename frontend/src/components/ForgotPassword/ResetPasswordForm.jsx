import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { LOGIN_URI } from "../../constants/routes";
import { useForgotPasswordActions } from "../../hooks/authHooks";
import Loading from "../../components/Loading/Loading";
import styles from "./ForgotPassword.module.css";

const checkPasswordStrength = (password) => {
  if (!password) return "";
  if (password.length < 6) return "Weak";
  if (password.length < 10) return "Medium";
  return "Strong";
};

export default function ResetPasswordForm({ token }) {
  const [input, setInput] = useState({
    newPassword: "",
    confirmPassword: "",
  });
  const [fieldErrors, setFieldErrors] = useState({});
  const [passwordStrength, setPasswordStrength] = useState("");

  const {
    handleResetPassword,
    resetPasswordError,
    resetPasswordSuccess,
    loadingResetPassword,
  } = useForgotPasswordActions();

  useEffect(() => {
    setFieldErrors(resetPasswordError?.fields ?? {});
  }, [resetPasswordError]);

  useEffect(() => {
    setPasswordStrength(checkPasswordStrength(input.newPassword));

    if (input.confirmPassword && input.newPassword !== input.confirmPassword) {
      setFieldErrors((prevErrors) => ({
        ...prevErrors,
        confirmPassword: ["Passwords do not match"],
      }));
    } else if (input.confirmPassword) {
      setFieldErrors((prevErrors) => {
        const { confirmPassword, ...rest } = prevErrors;
        return rest;
      });
    }
  }, [input.newPassword, input.confirmPassword]);

  const onChange = (e) => {
    const { name, value } = e.target;
    setInput((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmitEvent = async (e) => {
    e.preventDefault();
    if (input.newPassword !== "" && input.confirmPassword !== "") {
      if (input.newPassword !== input.confirmPassword) {
        setFieldErrors({
          confirmPassword: ["Passwords do not match"],
        });
        return;
      }
      const resetData = {
        token: token,
        newPassword: input.newPassword,
      };
      const data = await handleResetPassword(resetData);
      if (data) {
        setInput({ newPassword: "", confirmPassword: "" });
      }
    }
  };

  if (loadingResetPassword) return <Loading />;

  return (
    <div className={styles.center} id="reset-password-page">
      <h1>Reset Password</h1>
      {resetPasswordError && (
        <div className="invalid-feedback d-block text-center">
          {resetPasswordError.message}
        </div>
      )}
      {resetPasswordSuccess && (
        <div className={`${styles.success} text-center mb-4`}>
          {resetPasswordSuccess}
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
          {passwordStrength && (
            <div className={styles.passwordStrength}>
              Strength:{" "}
              <span className={styles[passwordStrength.toLowerCase()]}>
                {passwordStrength}
              </span>
            </div>
          )}
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
          {fieldErrors.confirmPassword &&
            fieldErrors.confirmPassword.map((error, index) => (
              <div key={index} className="invalid-feedback d-block">
                {error}
              </div>
            ))}
        </div>
        <input
          type="submit"
          value="Reset Password"
          disabled={
            passwordStrength !== "Strong" ||
            fieldErrors.confirmPassword?.length > 0
          }
        />
        <div className={styles.signup_link}>
          Back to <Link to={LOGIN_URI}>Login</Link>
        </div>
      </form>
    </div>
  );
}
