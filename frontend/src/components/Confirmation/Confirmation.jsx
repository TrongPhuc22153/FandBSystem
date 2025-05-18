import { useCallback, useEffect, useState } from "react";
import styles from "./confirmation.module.css";
import { useReservationActions } from "../../hooks/reservationHooks";
import { useModal } from "../../context/ModalContext";

export default function Confirmation({ reservationData, onPrevious }) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isConfirmed, setIsConfirmed] = useState(false);
  const [confirmationNumber, setConfirmationNumber] = useState("");
  const [fieldErrors, setFieldErrors] = useState({})

  // Format date for display
  const formatDate = (date) => {
    return new Date(date).toLocaleString("en-US", {
      weekday: "short",
      month: "short",
      day: "numeric",
      year: "numeric",
      hour: "numeric",
      minute: "numeric",
      hour12: true,
    });
  };

  // Calculate total price
  const totalPrice = reservationData.selectedItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  const {
    handleCreateReservation,
    createError,
  } = useReservationActions();

  const { onOpen } = useModal();

  useEffect(() =>{
    setFieldErrors(createError?.fields ?? {})
  }, [createError])

  const handlePlaceReservation = useCallback(async () => {
    const data = {
      numberOfGuests: reservationData.numberOfGuests,
      startTime: reservationData.startDateTime,
      endTime: reservationData.endDateTime,
      notes: reservationData.notes,
      menuItems: reservationData.selectedItems.map((item) => ({
        productId: item.id,
        quantity: item.quantity
      }))
    }
    if(reservationData.tableSelection){
      data.tableId = reservationData.tableSelection
    }
    
    const res = await handleCreateReservation(data)
    if(res){
      setConfirmationNumber(res.data.reservationId);
      setIsConfirmed(true);
    }
    setIsSubmitting(false);
  }, [handleCreateReservation, reservationData])

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    onOpen({
      title: "Submit reservation",
      message: "Do you want to continue?",
      onYes: handlePlaceReservation
    })
  };

  if (isConfirmed) {
    return (
      <div className={styles.confirmation}>
        <div className={styles.successIcon}>
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
          We've sent a confirmation email with all the details.
        </p>
        <div className="text-center mt-4">
          <button
            type="button"
            className="btn btn-primary"
            onClick={() => window.location.reload()}
          >
            Make Another Reservation
          </button>
        </div>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className={styles.confirmationForm}>
      <h2 className="mb-4">Confirm Your Reservation</h2>

      {createError?.message && 
        <div className="alert alert-danger" role="alert">
          {createError.message}
        </div>
      }

      {fieldErrors?.startTime && (
        fieldErrors.startTime.map((error, index) => (
          <div key={`startTime-${index}`} className="alert alert-danger" role="alert">
            {error}
          </div>
        ))
      )}

      {fieldErrors?.endTime && (
        fieldErrors.endTime.map((error, index) => (
          <div key={`endTime-${index}`} className="alert alert-danger" role="alert">
            {error}
          </div>
        ))
      )}

      {fieldErrors?.numberOfGuests && (
        fieldErrors.numberOfGuests.map((error, index) => (
          <div key={`numberOfGuests-${index}`} className="alert alert-danger" role="alert">
            {error}
          </div>
        ))
      )}

      <div className="card mb-4">
        <div className="card-header">
          <h5 className="mb-0">Reservation Details</h5>
        </div>
        <div className="card-body">
          <div className="row mb-2">
            <div className="col-md-4 fw-bold">Date & Time:</div>
            <div className="col-md-8">
              {formatDate(reservationData.startDateTime)} to{" "}
              {formatDate(reservationData.endDateTime)}
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
                    <th>Item</th>
                    <th>Quantity</th>
                    <th className="text-end">Price</th>
                    <th className="text-end">Subtotal</th>
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

      <div className="d-flex justify-content-between">
        <button
          type="button"
          className="btn btn-outline-secondary"
          onClick={onPrevious}
          disabled={isSubmitting}
        >
          Back to Menu Selection
        </button>
        <button
          type="submit"
          className="btn btn-success"
          disabled={isSubmitting}
        >
          {isSubmitting ? (
            <>
              <span
                className="spinner-border spinner-border-sm me-2"
                role="status"
                aria-hidden="true"
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
