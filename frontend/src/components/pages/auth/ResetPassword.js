import { useState } from 'react';
import { Link, useNavigate } from "react-router-dom"
import { HOME_URI, REGISTER_URI } from "../../../constants/WebPageURI";

export default function ResetPasswordPage() {
    const [passwordChange, setPasswordChange] = useState({
        newPassword: '',
        confirmNewPassword: ''
    });
    const [resetPasswordError, setResetPasswordError] = useState('');
    const [isShowResultModal, setIsShowResultModal] = useState(false);
    const navigate = useNavigate();

    // Handle password input changes
    const onChangePassword = (e) => {
        const { name, value } = e.target;
        setPasswordChange(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    // Handle form submission
    const onClickUpdatePassword = async (e) => {
        e.preventDefault();

        // Basic validation
        if (passwordChange.newPassword !== passwordChange.confirmNewPassword) {
            setResetPasswordError('Passwords do not match');
            return;
        }

        if (passwordChange.newPassword.length < 8) {
            setResetPasswordError('Password must be at least 8 characters long');
            return;
        }

        try {
            // Assuming you get the reset token from URL params or another source
            const urlParams = new URLSearchParams(window.location.search);
            const token = urlParams.get('token'); // Adjust based on your URL structure

            if (!token) {
                setResetPasswordError('Invalid or missing reset token');
                return;
            }

            const response = await fetch('/api/reset-password', { // Adjust endpoint as needed
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    token,
                    newPassword: passwordChange.newPassword
                })
            });

            const data = await response.json();

            if (response.ok) {
                setIsShowResultModal(true);
                setResetPasswordError('');
                setPasswordChange({
                    newPassword: '',
                    confirmNewPassword: ''
                });

                // Optional: Auto-redirect after a delay
                setTimeout(() => {
                    setIsShowResultModal(false);
                    navigate('/');
                }, 3000);
            } else {
                setResetPasswordError(data.message || 'Failed to reset password. Please try again.');
            }
        } catch (error) {
            console.error('Error during password reset:', error);
            setResetPasswordError('An error occurred. Please try again later.');
        }
    };

    return (
        <>
            <div className="center" id="reset-password-page">
                <h2>Reset password</h2>
                {resetPasswordError && resetPasswordError.length > 0 && (
                    <div className="invalid-feedback d-block text-center">
                        {resetPasswordError}
                    </div>
                )}
                <form method="post" onSubmit={onClickUpdatePassword}>
                    <div className="txt_field">
                        <input
                            type="password"
                            name="newPassword"
                            required
                            value={passwordChange.newPassword}
                            onChange={onChangePassword}
                        />
                        <span></span>
                        <label htmlFor="newPassword">New password</label>
                    </div>
                    <div className="txt_field">
                        <input
                            type="password"
                            name="confirmNewPassword"
                            required
                            value={passwordChange.confirmNewPassword}
                            onChange={onChangePassword}
                        />
                        <span></span>
                        <label htmlFor="confirmNewPassword">Confirm new password</label>
                    </div>
                    <input type="submit" value="Submit" />
                    <div className="signup_link">
                        Not a member? <Link to={REGISTER_URI}>Signup</Link>
                    </div>
                </form>
            </div>
            {isShowResultModal && (
                <div className="modal overlay d-block box-shadow-default" tabIndex={-1} id="verification-email-modal" role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-body">
                                <div className="w-100 d-flex justify-content-center">
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        className="text-success bi bi-check-circle"
                                        width="75"
                                        height="75"
                                        fill="currentColor"
                                        viewBox="0 0 16 16"
                                    >
                                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
                                        <path d="M10.97 4.97a.235.235 0 0 0-.02.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-1.071-1.05z" />
                                    </svg>
                                </div>
                                <h4 className="text-center mt-4"><b>Successfully</b></h4>
                                <p className="text-center">Your password has been reset!</p>
                                <p className="text-center">
                                    <Link to={HOME_URI} className="btn btn-primary px-4">Home</Link>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}