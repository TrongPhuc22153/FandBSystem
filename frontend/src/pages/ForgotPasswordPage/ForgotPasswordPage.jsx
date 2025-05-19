import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import EmailForm from "../../components/ForgotPassword/EmailForm";
import ResetPasswordForm from "../../components/ForgotPassword/ResetPasswordForm";
import { useForgotPasswordActions } from "../../hooks/authHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";

function ForgotPasswordPage() {
  const location = useLocation();
  const [token, setToken] = useState(null);
  const [showResetForm, setShowResetForm] = useState(false);

  const {
    handleValidateToken,
    loadingValidateToken,
    validateTokenError,
    validateTokenSuccess,

  } = useForgotPasswordActions();

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
      const validateToken = async () => {
        await handleValidateToken(urlToken);
      };

      validateToken();
    }
  }, [location.search]);

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
