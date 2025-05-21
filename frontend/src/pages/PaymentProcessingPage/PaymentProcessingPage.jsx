import { useState, useEffect } from "react";
import styles from "./PaymentProcessingPage.module.css";
import PaymentSuccess from "../../components/PaymentProcessing/PaymentSuccess/PaymentSuccess";
import PaymentError from "../../components/PaymentProcessing/PaymentErrror/PaymentError";
import PaymentCancelled from "../../components/PaymentProcessing/PaymentCancelled/PaymentCancelled";
import ProcessingPage from "../../components/PaymentProcessing/ProcessingPage/ProcessingPage";
import { usePayPalActions } from "../../hooks/paypalHooks";

export default function PaymentProcessingPage() {
  const [currentPage, setCurrentPage] = useState("processing");

  const {
    handleCaptureOrder,
    captureError,
    captureSuccess,
    resetCapture,
  } = usePayPalActions();

  useEffect(() => {
    if(captureSuccess){
        setCurrentPage("success");
    }
  }, [captureSuccess])

  useEffect(() => {
    if(captureError){
        setCurrentPage("error");
    }
  }, [captureError])

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get("token"); // PayPal success redirect
    const cancel = urlParams.get("cancel"); // PayPal cancel redirect

    const captureOrder = async ()=>{
        await handleCaptureOrder(token);
    }

    if (token) {
      // Handle PayPal success redirect
      setCurrentPage("processing");
      captureOrder();
    } else if (cancel === "true") {
      // Handle PayPal cancel redirect
      setCurrentPage("cancelled");
    } else {
      // Default or invalid state
      setCurrentPage("processing");
    }
  }, []);

  const renderPage = () => {
    switch (currentPage) {
      case "success":
        return <PaymentSuccess />;
      case "error":
        return <PaymentError />;
      case "cancelled":
        return <PaymentCancelled />;
      case "processing":
        return <ProcessingPage />;
      default:
        return <ProcessingPage />;
    }
  };

  return <div className={styles["app-container"]}>{renderPage()}</div>;
}
