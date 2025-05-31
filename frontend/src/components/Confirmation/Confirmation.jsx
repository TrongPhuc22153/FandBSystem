import { useCallback, useEffect, useMemo, useState } from "react";
import styles from "./confirmation.module.css";
import { useReservationActions } from "../../hooks/reservationHooks";
import { useModal } from "../../context/ModalContext";
import { usePaymentMethods } from "../../hooks/paymentMethodHooks";
import PaymentMethodOptions from "../PaymentMethodOptions/PaymentMethodOptions";
import {
  CANCEL_PAYMENT_URL,
  PAYMENT_TYPES,
  SUCCESS_PAYMENT_URL,
} from "../../constants/paymentConstants";
import { Link } from "react-router-dom";
import { HOME_URI } from "../../constants/routes";
import { usePaymentActions } from "../../hooks/paymentHooks";
import ErrorDisplay from "../ErrorDisplay/ErrorDisplay";

export default function Confirmation({ reservationData, onPrevious, onReset }) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isConfirmed, setIsConfirmed] = useState(false);
  const [confirmationNumber, setConfirmationNumber] = useState("");
  const [fieldErrors, setFieldErrors] = useState({});
  const [errorMessage, setErrorMessage] = useState("");

  const [selectedPayment, setSelectedPayment] = useState(null);

  const {
    data: paymentMethodsData,
    isLoading: loadingMethods,
    error: methodsError,
  } = usePaymentMethods(PAYMENT_TYPES.RESERVATION);

  const { handleProcessPayment, paymentError } = usePaymentActions();
  const paymentMethods = useMemo(
    () => paymentMethodsData || [],
    [paymentMethodsData]
  );

  const { handleCreateReservation, createError } = useReservationActions();
  const { onOpen } = useModal();

  const handlePaymentMethodChange = useCallback((id) => {
    setSelectedPayment(id);
    setErrorMessage("");
  }, []);

  // Format date and time for display
  const formatDateTime = useCallback((date, time) => {
    if (!date || !time) return "a specified time";
    const dateTime = new Date(`${date.toISOString().slice(0, 10)}T${time}`);
    return dateTime.toLocaleString("en-US", {
      day: "2-digit",
      month: "short",
      year: "numeric",
      hour: "numeric",
      minute: "2-digit",
      hour12: true,
    });
  }, []);

  // Calculate total price
  const totalPrice = useMemo(
    () =>
      reservationData.selectedItems.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
      ),
    [reservationData.selectedItems]
  );

  useEffect(() => {
    setFieldErrors(createError?.fields ?? {});
    if (createError?.message || paymentError?.message) {
      setErrorMessage(createError?.message || paymentError?.message);
    }
  }, [createError, paymentError]);

  const handlePlaceReservation = useCallback(async () => {
    if (!selectedPayment) {
      setErrorMessage("Please select a payment method");
      setIsSubmitting(false);
      return;
    }

    const data = {
      numberOfGuests: reservationData.numberOfGuests,
      date: reservationData.date,
      startTime: reservationData.startTime,
      endTime: reservationData.endTime,
      notes: reservationData.notes,
      menuItems: reservationData.selectedItems.map((item) => ({
        productId: item.id,
        quantity: item.quantity,
      })),
    };
    if (reservationData.tableSelection) {
      data.tableId = reservationData.tableSelection;
    }

    try {
      const res = await handleCreateReservation(data);
      if (res) {
        const reservationId = res.data.reservationId;
        const paymentId = res.data.payment.paymentId;
        const paymentRes = await handleProcessPayment({
          id: paymentId,
          returnUrl: SUCCESS_PAYMENT_URL,
          cancelUrl: CANCEL_PAYMENT_URL,
          paymentMethod: selectedPayment,
          reservationId: reservationId,
        });
        if (paymentRes) {
          if (paymentRes.data.link) {
            window.location.href = paymentRes.data.link;
          }
          setConfirmationNumber(reservationId);
          setIsConfirmed(true);
        }
      } else {
        setErrorMessage("Failed to create reservation. Please try again.");
      }
    } catch (error) {
      setErrorMessage(
        error.message || "An error occurred while processing your reservation."
      );
    } finally {
      setIsSubmitting(false);
    }
  }, [
    handleCreateReservation,
    handleProcessPayment,
    reservationData,
    selectedPayment,
  ]);

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedPayment) {
      setErrorMessage("Please select a payment method");
      return;
    }
    setIsSubmitting(true);
    onOpen({
      title: "Submit Reservation",
      message: "Do you want to confirm your reservation?",
      onYes: handlePlaceReservation,
      onNo: () => setIsSubmitting(false),
    });
  };

  if (methodsError?.message) {
    return <ErrorDisplay message={methodsError.message} />;
  }

  if (isConfirmed) {
    return (
      <div className={styles.confirmation}>
        <div className={styles.successIcon} aria-label="Reservation confirmed">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="64"
            height="64"
            fill="currentColor"
            className="bi bi-check-circle-fill"
            viewBox="0 0 16 16"
          >
            <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z" />
          </svg>
        </div>
        <h2 className="text-center mb-4">Reservation Confirmed!</h2>
        <p className="text-center mb-3">
          Your confirmation number is: <strong>{confirmationNumber}</strong>
        </p>
        <p className="text-center">
          A confirmation has been sent to your account.
        </p>
        <div className="text-center mt-4">
          <Link
            to={HOME_URI}
            type="button"
            className="btn btn-primary"
            onClick={onReset}
            aria-label="Back to home"
          >
            Back to home
          </Link>
        </div>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className={styles.confirmationForm}>
      <h2 className="mb-4">Confirm Your Reservation</h2>

      {errorMessage && (
        <div className="alert alert-danger" role="alert">
          {errorMessage}
        </div>
      )}

      {fieldErrors?.startTime &&
        fieldErrors.startTime.map((error, index) => (
          <div
            key={`startTime-${index}`}
            className="alert alert-danger"
            role="alert"
          >
            {error}
          </div>
        ))}

      {fieldErrors?.endTime &&
        fieldErrors.endTime.map((error, index) => (
          <div
            key={`endTime-${index}`}
            className="alert alert-danger"
            role="alert"
          >
            {error}
          </div>
        ))}

      {fieldErrors?.numberOfGuests &&
        fieldErrors.numberOfGuests.map((error, index) => (
          <div
            key={`numberOfGuests-${index}`}
            className="alert alert-danger"
            role="alert"
          >
            {error}
          </div>
        ))}

      <div className="card mb-4">
        <div className="card-header">
          <h5 className="mb-0">Reservation Details</h5>
        </div>
        <div className="card-body">
          <div className="row mb-2">
            <div className="col-md-4 fw-bold">Date & Time:</div>
            <div className="col-md-8">
              {formatDateTime(reservationData.date, reservationData.startTime)}{" "}
              to {formatDateTime(reservationData.date, reservationData.endTime)}
            </div>
          </div>
          <div className="row mb-2">
            <div className="col-md-4 fw-bold">Number of Guests:</div>
            <div className="col-md-8">{reservationData.numberOfGuests}</div>
          </div>
          <div className="row mb-2">
            <div className="col-md-4 fw-bold">Table:</div>
            <div className="col-md-8">
              {reservationData.autoSelectTable
                ? "Best available table (auto-selected)"
                : `Table ${reservationData.tableSelection}`}
            </div>
          </div>
          {reservationData.notes && (
            <div className="row mb-2">
              <div className="col-md-4 fw-bold">Special Requests:</div>
              <div className="col-md-8">{reservationData.notes}</div>
            </div>
          )}
        </div>
      </div>

      <div className="card mb-4">
        <div className="card-header">
          <h5 className="mb-0">Selected Menu Items</h5>
        </div>
        <div className="card-body p-0">
          {reservationData.selectedItems.length === 0 ? (
            <p className="p-3 mb-0">No menu items selected</p>
          ) : (
            <div className="table-responsive">
              <table className="table table-striped mb-0">
                <thead>
                  <tr>
                    <th scope="col">Item</th>
                    <th scope="col">Quantity</th>
                    <th scope="col" className="text-end">
                      Price
                    </th>
                    <th scope="col" className="text-end">
                      Subtotal
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {reservationData.selectedItems.map((item) => (
                    <tr key={item.id}>
                      <td>{item.name}</td>
                      <td>{item.quantity}</td>
                      <td className="text-end">${item.price.toFixed(2)}</td>
                      <td className="text-end">
                        ${(item.price * item.quantity).toFixed(2)}
                      </td>
                    </tr>
                  ))}
                  <tr className="table-active">
                    <td colSpan={3} className="text-end fw-bold">
                      Total:
                    </td>
                    <td className="text-end fw-bold">
                      ${totalPrice.toFixed(2)}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      <div className="card mb-4">
        <div className="card-header">
          <h5 className="mb-0">Payment Method</h5>
        </div>
        <div className="card-body">
          {!loadingMethods && (
            <PaymentMethodOptions
              paymentMethods={paymentMethods}
              selectedPaymentMethod={selectedPayment}
              onPaymentMethodChange={handlePaymentMethodChange}
            />
          )}
        </div>
      </div>

      <div className="d-flex justify-content-between">
        <button
          type="button"
          className="btn btn-outline-secondary"
          onClick={onPrevious}
          disabled={isSubmitting}
          aria-label="Go back to menu selection"
        >
          Back to Menu Selection
        </button>
        <button
          type="submit"
          className="btn btn-success"
          disabled={isSubmitting}
          aria-label="Confirm reservation"
        >
          {isSubmitting ? (
            <>
              <span
                className="spinner-border spinner-border-sm me-2"
                role="status"
                aria-label="Processing"
              ></span>
              Processing...
            </>
          ) : (
            "Confirm Reservation"
          )}
        </button>
      </div>
    </form>
  );
}
