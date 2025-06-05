import { useCallback, useEffect, useState } from "react";
import StepIndicator from "../StepIndicator/StepIndicator";
import Step1ReservationDetails from "../Step1ReservationDetails/Step1ReservationDetails";
import Step2ProductSelection from "../Step2ProductSelection/Step2ProductSelection";
import Step3Overview from "../Step3Overview/Step3Overview";
import styles from "./ReservationForm.module.css";
import { useReservationActions } from "../../../hooks/reservationHooks";
import { usePaymentActions } from "../../../hooks/paymentHooks";
import { useAlert } from "../../../context/AlertContext";
import {
  CANCEL_PAYMENT_URL,
  SUCCESS_PAYMENT_URL,
} from "../../../constants/paymentConstants";

export default function ReservationForm() {
  const [currentStep, setCurrentStep] = useState(1);
  const [reservationData, setReservationData] = useState({
    date: "",
    startTime: "",
    endTime: "",
    numberOfGuests: 2,
    notes: "",
    autoAssignTable: true,
    tableId: null,
    menuItems: [],
    paymentMethod: null,
  });

  const { showNewAlert } = useAlert();
  const { handleCreateReservation, createError, createSuccess } =
    useReservationActions();
  const { handleProcessPayment, paymentError } = usePaymentActions();

  useEffect(() => {
    if (createError) {
      showNewAlert({
        message: createError.message || "Failed to create reservation",
        variant: "danger",
      });
    }
    if (paymentError) {
      showNewAlert({
        message: paymentError.message || "Failed to process payment",
        variant: "danger",
      });
    }
  }, [createError, showNewAlert, paymentError]);

  useEffect(() => {
    if (createSuccess) {
      showNewAlert({
        message: createSuccess,
        variant: "success",
      });
      setCurrentStep(1);
      setReservationData({
        date: "",
        startTime: "",
        endTime: "",
        numberOfGuests: 2,
        notes: "",
        autoAssignTable: true,
        tableId: null,
        menuItems: [],
        paymentMethod: null,
      });
    }
  }, [createSuccess, showNewAlert]);

  const updateReservationData = (updates) => {
    setReservationData((prev) => ({ ...prev, ...updates }));
  };

  const nextStep = () => {
    if (currentStep < 3) {
      setCurrentStep((prev) => prev + 1);
    }
  };

  const prevStep = () => {
    if (currentStep > 1) {
      setCurrentStep((prev) => prev - 1);
    }
  };

  const goToStep = (step) => {
    setCurrentStep(step);
  };

  const handlePlaceReservation = useCallback(async () => {
    if (!reservationData.paymentMethod) {
      showNewAlert({
        message: "Please select a payment method",
        variant: "danger",
      });
      return;
    }

    const data = {
      numberOfGuests: reservationData.numberOfGuests,
      date: reservationData.date,
      startTime: reservationData.startTime,
      endTime: reservationData.endTime,
      notes: reservationData.notes,
      menuItems: reservationData.menuItems.map((item) => ({
        productId: item.productId,
        quantity: item.quantity,
      })),
    };
    if (!reservationData.autoAssignTable) {
      data.tableId = reservationData.tableId;
    }

    const res = await handleCreateReservation(data);
    if (res) {
      const reservationId = res.data.reservationId;
      const paymentId = res.data.payment.paymentId;
      const paymentRes = await handleProcessPayment({
        id: paymentId,
        returnUrl: SUCCESS_PAYMENT_URL,
        cancelUrl: CANCEL_PAYMENT_URL,
        paymentMethod: reservationData.paymentMethod,
        reservationId: reservationId,
      });
      if (paymentRes) {
        if (paymentRes.data.link) {
          window.location.href = paymentRes.data.link;
        }
      }
    }
  }, [
    handleCreateReservation,
    handleProcessPayment,
    reservationData,
    showNewAlert,
  ]);

  return (
    <div className={styles.container}>
      <div className={styles.formWrapper}>
        <StepIndicator currentStep={currentStep} />

        <div className={styles.stepContent}>
          {currentStep === 1 && (
            <Step1ReservationDetails
              data={reservationData}
              updateData={updateReservationData}
              onNext={nextStep}
            />
          )}

          {currentStep === 2 && (
            <Step2ProductSelection
              data={reservationData}
              updateData={updateReservationData}
              onNext={nextStep}
              onBack={prevStep}
            />
          )}

          {currentStep === 3 && (
            <Step3Overview
              data={reservationData}
              updateData={updateReservationData}
              onBack={prevStep}
              onSubmit={handlePlaceReservation}
              onEdit={goToStep}
            />
          )}
        </div>
      </div>
    </div>
  );
}
