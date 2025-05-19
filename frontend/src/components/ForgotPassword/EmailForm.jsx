import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { LOGIN_URI } from "../../constants/routes";
import { useForgotPasswordActions } from "../../hooks/authHooks";
import Loading from "../../components/Loading/Loading";
import styles from "./ForgotPassword.module.css";
import EmailSuccessPopup from "./EmailSuccessPopUp";

export default function EmailForm() {
  const [input, setInput] = useState({
    email: "",
  });
  const [fieldErrors, setFieldErrors] = useState({});
  const [showPopup, setShowPopup] = useState(false);

  const {
    handleForgotPassword,
    loadingForgotPassword,
    forgotPasswordSuccess,
    forgotPasswordError,
  } = useForgotPasswordActions();

  useEffect(() => {
    setFieldErrors(forgotPasswordError?.fields ?? {});
    if (forgotPasswordError?.message) {
      setShowPopup(false);
    }
  }, [forgotPasswordError]);

  useEffect(() => {
    if (forgotPasswordSuccess) {
      setShowPopup(true);
    }
  }, [forgotPasswordSuccess]);

  const onChange = (e) => {
    const { name, value } = e.target;
    setInput((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmitEvent = async (e) => {
    e.preventDefault();
    if (input.email !== "") {
      const data = await handleForgotPassword(input.email);
      if (data) {
        setInput({ email: "" });
      }
    }
  };

  if (loadingForgotPassword) return <Loading />;

  return (
    <>
      <EmailSuccessPopup show={showPopup} onClose={() => setShowPopup(false)} />
      <div className={styles.center} id="forgot-password-page">
        <h1>Forgot Password</h1>
        {forgotPasswordError?.message && (
          <div className="invalid-feedback d-block text-center">
            {forgotPasswordError.message}
          </div>
        )}
        {forgotPasswordSuccess && (
          <div className={`${styles.success} text-center mb-4`}>
            {forgotPasswordSuccess}
          </div>
        )}
        <form onSubmit={handleSubmitEvent} className={styles.form}>
          <div className={styles.txt_field}>
            <input
              type="email"
              name="email"
              required
              value={input.email}
              onChange={onChange}
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
          <input type="submit" value="Send Reset Link" />
          <div className={styles.signup_link}>
            Remembered your password? <Link to={LOGIN_URI}>Login</Link>
          </div>
        </form>
      </div>
    </>
  );
}
