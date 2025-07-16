import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { LOGIN_URI } from "../../../../shared/constants/routes";
import Loading from "../../../../shared/components/Loading/Loading";
import styles from "./ResetPasswordForm.module.css";
import {checkPasswordStrength} from "../../utils/passwordUtils";
import {useDispatch, useSelector} from "react-redux";
import {resetPassword} from "../../thunks/authThunk";

export default function ResetPasswordForm({ token }) {
  const [input, setInput] = useState({
    newPassword: "",
    confirmPassword: "",
  });
  const [fieldErrors, setFieldErrors] = useState({});
  const [passwordStrength, setPasswordStrength] = useState("");

  const {
    success: resetPasswordSuccess,
    error: resetPasswordError,
    isLoading: loadingResetPassword
  } = useSelector((state) => state.auth.resetPassword);
  const dispatch = useDispatch();

  useEffect(() => {
    setFieldErrors(resetPasswordError?.fields ?? {});
  }, [resetPasswordError]);

  useEffect((() => {
    if(resetPasswordSuccess){
      setInput({ newPassword: "", confirmPassword: "" });
    }
  }), [resetPasswordSuccess])

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

      dispatch(resetPassword({
        token: token,
        newPassword: input.newPassword,
      }));
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
