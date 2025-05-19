import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { REGISTER_URI, FORGOT_URI } from "../../constants/routes";
import { useAuth } from "../../context/AuthContext";
import Loading from "../../components/Loading/Loading";
import styles from "./Login.module.css";

export default function LoginPage() {
  const [input, setInput] = useState({
    username: "",
    password: "",
  });
  const [fieldErrors, setFieldErrors] = useState({});

  const { loginAction, loginError, loginLoading } = useAuth();

  useEffect(() => {
    setFieldErrors(loginError?.fields ?? {});
  }, [loginError]);

  const onChange = (e) => {
    const { name, value } = e.target;
    setInput((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // Handle form submission
  const handleSubmitEvent = async (e) => {
    e.preventDefault();
    if (input.username !== "" && input.password !== "") {
      await loginAction(input);
    }
  };

  if (loginLoading) return <Loading />;

  return (
    <div className={styles.center} id="login-page">
      <h1>Login</h1>
      {loginError && (
        <div className="invalid-feedback d-block text-center">
          {loginError.message}
        </div>
      )}
      <form onSubmit={handleSubmitEvent} className={styles.form}>
        <div className={styles.txt_field}>
          <input
            type="text"
            name="username"
            required
            value={input.username}
            onChange={onChange}
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
            value={input.password}
            onChange={onChange}
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
        <div className={styles.forgot_password}>
          <Link to={FORGOT_URI}>Forgot Password?</Link>
        </div>
        <input type="submit" value="Login" />
        <div className={styles.signup_link}>
          Not a member? <Link to={REGISTER_URI}>Signup</Link>
        </div>
      </form>
    </div>
  );
}