import { Link } from "react-router";
import { FORGET_PASSWORD_URI, REGISTER_URI } from "../constants/WebPageURI";
import { useState } from "react";
import { useAuth } from "../hooks/AuthContext";

export default function LoginPage() {
    // State to manage form data
    const [input, setInput] = useState({
        username: '',
        password: ''
    });
    const auth = useAuth();

    // Handle input changes
    const onChange = (e) => {
        const { name, value } = e.target;
        setInput(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    // Handle form submission
    const handleSubmitEvent = (e) => {
        e.preventDefault();
        if(input.username !== '' && input.password !== ""){
            auth.loginAction(input)
            return;
        }
    };

    return (
        <div className="center" id="login-page">
            <h1>Login</h1>
            {/* {error && error.length > 0 &&
                <div className="invalid-feedback d-block text-align-center">
                    {error}
                </div>
            } */}
            <form onSubmit={handleSubmitEvent}>
                <div className="txt_field">
                    <input type="text" name="username" required value={input.username} onChange={onChange} />
                    <span></span>
                    <label htmlFor="username">Username</label>
                </div>
                <div className="txt_field">
                    <input type="password" name="password" required value={input.password} onChange={onChange} />
                    <span></span>
                    <label htmlFor="password">Password</label>
                </div>
                <div className="pass"><Link to={FORGET_PASSWORD_URI}>Forgot Password?</Link></div>
                <input type="submit" value="Login" />
                <div className="signup_link">Not a member? <Link to={REGISTER_URI}>Signup</Link></div>
            </form>
        </div>
    )
}