import { useState } from "react";
import { Link, useNavigate } from "react-router";
import { LOGIN_URI } from "../../../constants/WebPageURI";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelopeCircleCheck, faLock } from "@fortawesome/free-solid-svg-icons";

export default function ForgetPasswordPage() {
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [emailIsShowed, setEmailIsShowed] = useState(false);
    const navigate = useNavigate();

    // Handle email input change
    const onChange = (e) => {
        setEmail(e.target.value);
    };

    // Handle form submission
    const onClickForgetPassword = async (e) => {
        e.preventDefault();

        // Basic email validation
        if (!email.includes('@') || !email.includes('.')) {
            setError('Please enter a valid email address');
            return;
        }

        try {
            const response = await fetch('/api/forgot-password', { // Adjust endpoint as needed
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email })
            });

            const data = await response.json();

            if (response.ok) {
                // Show the success modal
                setEmailIsShowed(true);
                setError('');

                // Optional: Auto-hide modal and redirect after a delay
                setTimeout(() => {
                    setEmailIsShowed(false);
                    setEmail('');
                    navigate(LOGIN_URI);
                }, 5000); // Redirects after 5 seconds
            } else {
                setError(data.message || 'Failed to send reset email. Please try again.');
            }
        } catch (error) {
            console.error('Error during password reset request:', error);
            setError('An error occurred. Please try again later.');
        }
    };

    // Optional: Function to close modal manually
    const closeModal = () => {
        setEmailIsShowed(false);
        setEmail('');
        navigate(LOGIN_URI);
    };

    return (
        <>
            <div className="center" id="forgotpassword-page">
                <div className="row mt-4">
                    <div className="col-md-12 d-flex justify-content-center mb-2" style={{ height: "40px" }}>
                        <FontAwesomeIcon icon={faLock} className="h-100" />
                    </div>
                    <h3 className="text-center">Forgot Password?</h3>
                    <div className="col-md-12">
                        <p className="text-center">You can reset your password here</p>
                    </div>
                    {error && error.length > 0 && (
                        <div className="invalid-feedback d-block text-center">
                            {error}
                        </div>
                    )}
                </div>
                <form method="post" action="/check" onSubmit={onClickForgetPassword}>
                    <div className="txt_field">
                        <input
                            type="email"
                            name="email"
                            required
                            value={email}
                            onChange={onChange}
                        />
                        <span></span>
                        <label htmlFor="email">Email</label>
                    </div>
                    <input type="submit" value="Send email" />
                    <div className="signup_link">
                        Login here? <Link to={LOGIN_URI}>Signin</Link>
                    </div>
                </form>
            </div>
            {emailIsShowed && (
                <div className="modal overlay d-block" tabIndex={-1} id="verification-email-modal" role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-body">
                                <div className="w-100 d-flex justify-content-center">
                                    <header>
                                        {/* <img className="w-100" src={getEmailSentIcon()} alt="Email sent icon" /> */}
                                        <FontAwesomeIcon icon={faEnvelopeCircleCheck} />
                                    </header>
                                </div>
                                <h4 className="text-center mt-4"><b>Reset your password</b></h4>
                                <p className="text-center mt-4">
                                    The reset password link has been sent to <b>{email}</b>
                                </p>
                                <p className="text-center">Please access this link</p>

                                <div className="text-center mt-4">
                                    <button
                                        className="btn btn-primary"
                                        onClick={closeModal}
                                    >
                                        Close
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}