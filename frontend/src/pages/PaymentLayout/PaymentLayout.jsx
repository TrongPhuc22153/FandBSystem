import { useEffect } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { HOME_URI } from "../../constants/routes";
import { usePayPalActions } from "../../hooks/paypalHooks";
import ProcessingPage from "../../components/PaymentProcessing/ProcessingPage/ProcessingPage";
import PaymentError from "../../components/PaymentProcessing/PaymentErrror/PaymentError";

export default function PaymentLayout() {
  const location = useLocation();
  const navigate = useNavigate();
  const { handleCaptureOrder, captureError, captureLoading, resetCapture } =
    usePayPalActions();

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const token = queryParams.get("token");

    if (!token) {
      navigate(HOME_URI, { replace: true });
      return;
    }

    let isMounted = true;

    const finalizePayment = async () => {
      const res = await handleCaptureOrder(token);
      if (res && isMounted) {
        resetCapture();
      }
    };

    finalizePayment();

    return () => {
      isMounted = false;
    };
  }, [location, navigate, handleCaptureOrder, resetCapture]);

  if (captureError?.message)
    return (
      <div className="center">
        <PaymentError />
      </div>
    );

  return (
    <div className="center">
      {captureLoading ? <ProcessingPage /> : <Outlet />}
    </div>
  );
}
