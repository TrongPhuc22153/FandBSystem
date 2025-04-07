import LoginPage from "./Login";
import { Routes, Route } from 'react-router-dom';
import RegisterPage from "./Register";
import ForgetPasswordPage from "./ForgetPassword";
import ResetPasswordPage from "./ResetPassword";

export default function AuthenticationPage() {
    return (
        <div id="auth-body">
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/forget-password" element={<ForgetPasswordPage/>} />
                <Route path="/reset-password" element={<ResetPasswordPage/>} />
            </Routes>
            {/* <div className="screen-center vh-100 d-flex justify-content-center align-items-center">
                <div className="card col-md-4 bg-white shadow-md p-5">
                    <div className="mb-4 text-center">
                        {status === 'true' ?
                            <svg xmlns="http://www.w3.org/2000/svg" className="text-success bi bi-check-circle" width="75" height="75"
                                fill="currentColor" viewBox="0 0 16 16">
                                <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
                                <path
                                    d="M10.97 4.97a.235.235 0 0 0-.02.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-1.071-1.05z" />
                            </svg> :
                            status === 'false' &&
                            <img width={"100px"} src={getError()} alt="Error" />
                        }
                    </div>
                    <div className="text-center">
                        {status === 'true' ?
                            <>
                                <h4>Awesome!</h4>
                                <p>Your email has been verified</p>
                            </> :
                            status === 'false' &&
                            <>
                                <h4>Error!</h4>
                                <p>Your email hasn't been verified</p>
                            </>
                        }

                        <Link to={"/"}>
                            <span className="btn btn-outline-success">Back Home</span>
                        </Link>
                    </div>
                </div>
            </div> */}
        </div>
    )
}