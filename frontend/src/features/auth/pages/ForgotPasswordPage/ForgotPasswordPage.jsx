import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import EmailForm from "../../components/EmailForm/EmailForm";
import ResetPasswordForm from "../../components/ResetPasswordForm/ResetPasswordForm";
import Loading from "../../../../shared/components/Loading/Loading";
import ErrorDisplay from "../../../../shared/components/ErrorDisplay/ErrorDisplay";
import {useDispatch, useSelector} from "react-redux";
import {validateToken} from "../../thunks/authThunk";

function ForgotPasswordPage() {
  const location = useLocation();
  const [token, setToken] = useState(null);
  const [showResetForm, setShowResetForm] = useState(false);

  const {
    success: validateTokenSuccess,
    error: validateTokenError,
    isLoading: loadingValidateToken
  } = useSelector((state) => state.auth.validateToken);
  const dispatch = useDispatch();

  useEffect(() => {
    if(validateTokenSuccess){
      setShowResetForm(true)
    }
  }, [validateTokenSuccess])

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const urlToken = searchParams.get("token");
    setToken(urlToken);

    if (urlToken) {
      dispatch(validateToken(urlToken));
    }
  }, [location.search, dispatch]);

  if (loadingValidateToken) return <Loading />;

  if (validateTokenError?.message)
    return <ErrorDisplay message={validateTokenError.message} />;

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-md mx-auto">
        <div className="relative mb-6">
          <div className="w-full bg-gray-200 rounded-full h-2.5">
            <div
              className={`bg-blue-600 h-2.5 rounded-full transition-all duration-300 w-${
                showResetForm ? 100 : 50
              }`}
            ></div>
          </div>
        </div>

        {showResetForm ? (
          <ResetPasswordForm token={token} />
        ) : (
          <EmailForm />
        )}
      </div>
    </div>
  );
}

export default ForgotPasswordPage;
