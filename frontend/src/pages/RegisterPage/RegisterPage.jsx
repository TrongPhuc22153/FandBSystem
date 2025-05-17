import { Link, useNavigate } from "react-router";
import { useState, useEffect } from "react";
import { LOGIN_URI } from "../../constants/routes";
import { useAuthActions } from "../../hooks/authHooks";
import Loading from "../../components/Loading/Loading";
import styles from "./Register.module.css";

export default function RegisterPage() {
  const [registerInfo, setRegisterInfo] = useState({
    firstName: "",
    lastName: "",
    email: "",
    username: "",
    password: "",
    confirmPassword: "",
  });
  const [fieldErrors, setFieldErrors] = useState({});
  const [passwordMatchError, setPasswordMatchError] = useState("");
  const navigate = useNavigate();

  const {
    handleRegisterUser,
    loadingRegister,
    registerError,
    registerSuccess,
    resetRegister,
  } = useAuthActions();

  // Handle input changes
  const onChange = (e) => {
    const { name, value } = e.target;
    setRegisterInfo((prevState) => ({
      ...prevState,
      [name]: value,
    }));
    if (fieldErrors[name]) {
      setFieldErrors((prev) => ({ ...prev, [name]: null }));
    }
  };

  // Real-time password matching validation
  useEffect(() => {
    const { password, confirmPassword } = registerInfo;

    if (!password || !confirmPassword) {
      setPasswordMatchError("");
      return;
    }

    setPasswordMatchError(
      password !== confirmPassword ? "Passwords do not match" : ""
    );
  }, [registerInfo]);

  useEffect(() => {
    setFieldErrors(registerError?.fields ?? {});
  }, [registerError]);

  const onSubmitForm = async (e) => {
    e.preventDefault();
    const data = await handleRegisterUser(registerInfo);
    if (data) {
      resetRegister();
      navigate(LOGIN_URI);
    }
  };

  if (loadingRegister) return <Loading />;

  return (
    <div className={styles.container}>
      <div className={styles.center}>
        <h1>Register</h1>
        {registerError && (
          <div className="invalid-feedback d-block text-center">
            {registerError.message}
          </div>
        )}

        {registerSuccess && (
          <div
            className={styles["alert-success"] + " " + styles["text-center"]}
          >
            {registerSuccess}
          </div>
        )}

        <form onSubmit={onSubmitForm} className={styles.form}>
          <div className={styles.txt_field}>
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
          <div className={styles.txt_field}>
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
          <div className={styles.txt_field}>
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
          <div className={styles.txt_field}>
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
          <div className={styles.txt_field}>
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
          <div className={styles.txt_field}>
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
          <input
            type="submit"
            value={loadingRegister ? "Registering..." : "Sign Up"}
            disabled={loadingRegister || passwordMatchError.length > 0}
          />
          <div className={styles.signup_link}>
            Have an Account? <Link to={LOGIN_URI}>Login Here</Link>
          </div>
        </form>
      </div>
    </div>
  );
}
