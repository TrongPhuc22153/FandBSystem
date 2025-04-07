import { Link } from "react-router";
import { FORGET_PASSWORD_URI, REGISTER_URI } from "../../../constants/WebPageURI";
import { useState } from "react";

export default function LoginPage() {
    // State to manage form data
    const [user, setUser] = useState({
        username: '',
        password: ''
    });

    // Handle input changes
    const onChange = (e) => {
        const { name, value } = e.target;
        setUser(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    // Handle form submission
    const onClickLogin = async (e) => {
        e.preventDefault(); // Prevent default form submission

        try {
            // Example API call to your backend
            const response = await fetch('/check', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: user.username,
                    password: user.password
                })
            });

            const data = await response.json();

            if (response.ok) {
                // Handle successful login
                console.log('Login successful:', data);
                // You might want to:
                // - Store a token in localStorage
                // - Redirect to a dashboard
                // - Update app state
            } else {
                // Handle login failure
                console.error('Login failed:', data.message);
                alert('Invalid username or password');
            }
        } catch (error) {
            console.error('Error during login:', error);
            alert('An error occurred. Please try again later.');
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
            <form onSubmit={onClickLogin}>
                <div className="txt_field">
                    <input type="text" name="username" required value={user.username} onChange={onChange} />
                    <span></span>
                    <label htmlFor="username">Username</label>
                </div>
                <div className="txt_field">
                    <input type="password" name="password" required value={user.password} onChange={onChange} />
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