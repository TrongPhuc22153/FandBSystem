import { Link, Navigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { REGISTER_URI, FORGOT_URI, HOME_URI } from "../../../../shared/constants/routes";
import Loading from "../../../../shared/components/Loading/Loading";
import RequiredResetPasswordForm from "../../components/RequiredResetPasswordForm/RequiredResetPasswordForm";
import styles from "./Login.module.css";
import {useDispatch, useSelector} from "react-redux";
import {loginUser} from "../../thunks/authThunk";
import {resetLoginState} from "../../slices/authSlice";

export default function LoginPage() {
  const [input, setInput] = useState({
    username: "",
    password: "",
  });
  const [fieldErrors, setFieldErrors] = useState({});
  const {
    token,
    isResetPassword,
    login: {
      error: loginError,
      isLoading: loginLoading,
      success: loginSuccess,
    }
  } = useSelector(state => state.auth);
  const dispatch = useDispatch();

  useEffect(() => {
    if(loginSuccess){
      setFieldErrors(loginError?.fields ?? {});
    }
  }, [loginError]);

  useEffect(() => {
    if(loginSuccess){
      dispatch(resetLoginState())
    }
  }, [loginSuccess])

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
      await dispatch(loginUser(input))
    }
  };

  if(token){
    return <Navigate to={HOME_URI}/>
  }

  if (loginLoading) return <Loading />;

  if(isResetPassword){
    return <RequiredResetPasswordForm />
  }

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