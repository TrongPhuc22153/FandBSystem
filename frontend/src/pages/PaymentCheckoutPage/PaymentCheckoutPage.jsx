import { useCallback, useEffect, useMemo, useState } from "react";
import ConfirmPaymentButton from "../../components/DineInPaymentCheckout/ConfirmPaymentButton/ConfirmPaymentButton";
import OrderSummary from "../../components/DineInPaymentCheckout/OrderSummary/OrderSummary";
import PaymentOptions from "../../components/DineInPaymentCheckout/PaymentOptions/PaymentOptions";
import styles from "./PaymentCheckoutPage.module.css";
import { usePayment, usePaymentActions } from "../../hooks/paymentHooks";
import { useNavigate, useParams } from "react-router-dom";
import { usePaymentMethods } from "../../hooks/paymentMethodHooks";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";
import {
  CANCEL_PAYMENT_URL,
  PAYMENT_TYPES,
  SUCCESS_PAYMENT_URL,
} from "../../constants/paymentConstants";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { PAYMENT_CHECKOUT_URI } from "../../constants/routes";

export default function PaymentCheckoutPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [selectedPaymentMethod, setSelectedPaymentMethod] = useState("");

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const {
    data: paymentData,
    isLoading: loadingPayment,
    error: paymentError,
  } = usePayment(id);

  const order = useMemo(() => paymentData?.order || {}, [paymentData]);

  const {
    data: paymentMethods,
    error: paymentMethodsError,
    isLoading: loadingPaymentMethods,
  } = usePaymentMethods(PAYMENT_TYPES.DINE_IN);

  const {
    handleProcessPayment,
    paymentError: processPaymentError,
    paymentLoading: loadingPaymentProcess,
    paymentSuccess: processPaymentSuccess,
    resetPayment,
  } = usePaymentActions();

  // Handle payment errors
  useEffect(() => {
    if (processPaymentError?.message) {
      showNewAlert({
        message: processPaymentError.message,
        action: () => {
          resetPayment();
          setSelectedPaymentMethod("");
        },
      });
    }
  }, [processPaymentError, resetPayment, showNewAlert]);

  // Handle payment success
  useEffect(() => {
    if (processPaymentSuccess && !processPaymentError) {
      showNewAlert({
        message: "Payment processed successfully!",
        type: "success",
      });
    }
  }, [processPaymentSuccess, processPaymentError, showNewAlert]);

  const handleConfirmPayment = useCallback(async () => {
    const orderId = order.orderId;
    const paymentId = paymentData.paymentId;

    const paymentRes = await handleProcessPayment({
      id: paymentId,
      returnUrl: SUCCESS_PAYMENT_URL,
      cancelUrl: CANCEL_PAYMENT_URL,
      paymentMethod: selectedPaymentMethod,
      orderId: orderId,
    });
    if (paymentRes) {
      const link = paymentRes.data.link;
      if (link) {
        window.location.href = link;
      }else{
        navigate(PAYMENT_CHECKOUT_URI)
      }
    }
  }, [selectedPaymentMethod, order, handleProcessPayment, navigate, paymentData]);

  const showConfirmPayment = useCallback(() => {
    if(selectedPaymentMethod){
      onOpen({
        title: "Process payment",
        message: "Do you want to process this payment?",
        onYes: handleConfirmPayment,
      });
    }else{
      showNewAlert({
        message: "Please choose a payment method",
        variant: "danger"
      })
    }
  }, [onOpen, handleConfirmPayment, showNewAlert, selectedPaymentMethod]);

  if (paymentError?.message || paymentMethodsError?.message) {
    return (
      <ErrorDisplay
        message={paymentError?.message || paymentMethodsError?.message}
      />
    );
  }

  return (
    <div className={`container mt-5 mb-5 ${styles.checkoutPage}`}>
      <h1 className={`text-center mb-4 ${styles.pageTitle}`}>
        Checkout
      </h1>

      <div className="row g-4">
        <div className="col-lg-12">
          {!loadingPayment && (
            <OrderSummary order={order} totalPrice={order.totalPrice} />
          )}

          {!loadingPaymentMethods && (
            <PaymentOptions
              paymentMethods={paymentMethods}
              selectedPaymentMethod={selectedPaymentMethod}
              setSelectedPaymentMethod={setSelectedPaymentMethod}
            />
          )}
        </div>
      </div>

      <div className="row mt-4">
        <div className="col-12">
          <ConfirmPaymentButton
            onConfirm={showConfirmPayment}
            isProcessing={loadingPaymentProcess}
          />
        </div>
      </div>
    </div>
  );
}
