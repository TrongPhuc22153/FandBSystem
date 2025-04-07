import { Link, useNavigate } from "react-router";
import { LOGIN_URI } from "../../../constants/WebPageURI";
import { useState, useEffect } from "react";

export default function RegisterForm() {
    const [registerInfo, setRegisterInfo] = useState({
        firstname: '',
        lastname: '',
        email: '',
        username: '',
        password: '',
        confirmPassword: ''
    });

    const [error, setError] = useState('');
    const [passwordMatchError, setPasswordMatchError] = useState('');
    const navigate = useNavigate();

    // Handle input changes
    const onChange = (e) => {
        const { name, value } = e.target;
        setRegisterInfo(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    // Real-time password matching validation
    useEffect(() => {
        if (registerInfo.password && registerInfo.confirmPassword) {
            if (registerInfo.password !== registerInfo.confirmPassword) {
                setPasswordMatchError('Passwords do not match');
            } else {
                setPasswordMatchError('');
            }
        } else {
            setPasswordMatchError('');
        }
    }, [registerInfo.password, registerInfo.confirmPassword]);

    const onClickRegister = async (e) => {
        e.preventDefault();

        // Check password match before submission
        if (registerInfo.password !== registerInfo.confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        try {
            const response = await fetch('/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    firstname: registerInfo.firstname,
                    lastname: registerInfo.lastname,
                    email: registerInfo.email,
                    username: registerInfo.username,
                    password: registerInfo.password
                })
            });

            const data = await response.json();

            if (response.ok) {
                setRegisterInfo({
                    firstname: '',
                    lastname: '',
                    email: '',
                    username: '',
                    password: '',
                    confirmPassword: ''
                });
                setError('');
                navigate(LOGIN_URI);
            } else {
                setError(data.message || 'Registration failed. Please try again.');
            }
        } catch (error) {
            console.error('Error during registration:', error);
            setError('An error occurred. Please try again later.');
        }
    };

    return (
        <div className="container">
            <div className="center">
                <h1>Register</h1>
                {error.length > 0 && (
                    <div className="invalid-feedback d-block text-align-center">
                        {error}
                    </div>
                )}
                <form onSubmit={onClickRegister}>
                    <div className="txt_field">
                        <input
                            type="text"
                            name="firstname"
                            required
                            onChange={onChange}
                            value={registerInfo.firstname}
                        />
                        <span></span>
                        <label htmlFor="firstname">First name</label>
                    </div>
                    <div className="txt_field">
                        <input
                            type="text"
                            name="lastname"
                            required
                            onChange={onChange}
                            value={registerInfo.lastname}
                        />
                        <span></span>
                        <label htmlFor="lastname">Last name</label>
                    </div>
                    <div className="txt_field">
                        <input
                            type="email"
                            name="email"
                            required
                            onChange={onChange}
                            value={registerInfo.email}
                        />
                        <span></span>
                        <label htmlFor="email">Email</label>
                    </div>
                    <div className="txt_field">
                        <input
                            type="text"
                            name="username"
                            required
                            onChange={onChange}
                            value={registerInfo.username}
                        />
                        <span></span>
                        <label htmlFor="username">Username</label>
                    </div>
                    <div className="txt_field">
                        <input
                            type="password"
                            name="password"
                            required
                            onChange={onChange}
                            value={registerInfo.password}
                        />
                        <span></span>
                        <label htmlFor="password">Password</label>
                    </div>
                    <div className="txt_field">
                        <input
                            type="password"
                            name="confirmPassword"
                            required
                            onChange={onChange}
                            value={registerInfo.confirmPassword}
                        />
                        <span></span>
                        <label htmlFor="confirmPassword">Confirm Password</label>
                        {passwordMatchError.length > 0 && (
                            <div className="invalid-feedback d-block">
                                {passwordMatchError}
                            </div>
                        )}
                    </div>
                    <input
                        type="submit"
                        value="Sign Up"
                        disabled={passwordMatchError.length > 0}
                    />
                    <div className="signup_link">
                        Have an Account? <Link to={LOGIN_URI}>Login Here</Link>
                    </div>
                </form>
            </div>
        </div>
    );
}