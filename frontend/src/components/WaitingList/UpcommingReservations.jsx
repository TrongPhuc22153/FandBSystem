import { useCallback, useEffect, useState } from "react";
import { Calendar, Plus, Trash, User, Users } from "lucide-react";
import { useModal } from "../../context/ModalContext";
import styles from "./WaitingList.module.css";
import { formatTime } from "../../utils/datetimeUtils";
import { useTableOccupancyActions } from "../../hooks/tableOccupancyHooks";
import { useAlert } from "../../context/AlertContext";
import {
  TABLE_OCCUPANCY_STATUSES,
  TABLE_OCCUPANCY_TYPES,
} from "../../constants/webConstant";

export function UpcommingReservations({ reservations, mutate }) {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [newReservation, setNewReservation] = useState({
    name: "",
    size: 2,
    phone: "",
    notes: "",
    reservationTime: "",
  });
  const [viewingReservation, setViewingReservation] = useState(null);
  const [fieldErrors, setFieldErrors] = useState({});

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const {
    handleCreateTableOccupancy,
    createError,
    createLoading,
    createSuccess,
    resetCreate,
    handleUpdateTableOccupancyStatus,
    updateStatusError,
    updateStatusSuccess,
    resetUpdateStatus,
  } = useTableOccupancyActions();

  useEffect(() => {
    setFieldErrors(createError?.fields ?? {});
    if (createError?.message || updateStatusError?.message) {
      showNewAlert({
        message: createError.message || updateStatusError.message,
        variant: "danger",
      });
    }
  }, [createError, updateStatusError, showNewAlert]);

  useEffect(() => {
    if (updateStatusSuccess) {
      showNewAlert({
        message: updateStatusSuccess,
        action: resetUpdateStatus,
      });
    }
    if (createSuccess) {
      showNewAlert({
        message: createSuccess,
        action: resetCreate,
      });
    }
  }, [
    createSuccess,
    updateStatusSuccess,
    showNewAlert,
    resetCreate,
    resetUpdateStatus,
  ]);

  const handleAddReservation = useCallback(async () => {
    const errors = {};
    if (!newReservation.name) {
      errors.contactName = ["Customer name is required"];
    }
    if (newReservation.size <= 0) {
      errors.partySize = ["Party size must be greater than 0"];
    }
    if (!newReservation.phone) {
      errors.phone = ["Phone number is required"];
    } else if (!/^[0-9\-+]{9,15}$/.test(newReservation.phone)) {
      errors.phone = [
        "Phone number must be 9-15 characters and contain only digits, hyphens, or plus signs",
      ];
    }
    if (!newReservation.reservationTime) {
      errors.reservationTime = ["Reservation time is required"];
    }

    if (Object.keys(errors).length > 0) {
      setFieldErrors(errors);
      showNewAlert({
        message: "Please fix the errors in the form",
        variant: "danger",
      });
      return;
    }

    const requestAddReservation = {
      contactName: newReservation.name,
      partySize: newReservation.size,
      phone: newReservation.phone,
      notes: newReservation.notes,
      type: TABLE_OCCUPANCY_TYPES.RESERVATION,
    };

    try {
      const res = await handleCreateTableOccupancy(requestAddReservation);
      if (res) {
        mutate(
          (prevData) => ({
            ...prevData,
            content: [...(prevData?.content || []), res],
          }),
          false
        );
        mutate();
        setNewReservation({
          name: "",
          size: 2,
          phone: "",
          notes: "",
          reservationTime: "",
        });
        setFieldErrors({});
        setIsDialogOpen(false);
      }
    } catch (error) {
      showNewAlert({
        message: "Failed to add reservation. Please try again.",
        variant: "danger",
      });
    }
  }, [handleCreateTableOccupancy, newReservation, showNewAlert, mutate]);

  const showAddReservationConfirmModal = () => {
    onOpen({
      title: "Add Reservation",
      message: "Do you want to add this reservation?",
      onYes: handleAddReservation,
    });
  };

  const handleCancelReservation = useCallback(
    async (idToCancel) => {
      const res = await handleUpdateTableOccupancyStatus(idToCancel, {
        status: TABLE_OCCUPANCY_STATUSES.CANCELLED,
      });
      if (res) {
        mutate((prevData) => {
          if (!prevData?.content) return prevData;
          return {
            ...prevData,
            content: prevData.content.filter(
              (reservation) => reservation.id !== idToCancel
            ),
          };
        }, false);
        mutate();
      }
    },
    [handleUpdateTableOccupancyStatus, mutate]
  );

  const showCancelReservationConfirmModal = (id) => {
    onOpen({
      title: "Cancel Reservation",
      message: "Do you want to cancel this reservation?",
      onYes: () => handleCancelReservation(id),
    });
  };

  const handleViewReservation = useCallback((reservation) => {
    setViewingReservation(reservation);
    setIsDialogOpen(true);
  }, []);

  return (
    <div className={styles.container}>
      <button
        className={styles.addButton}
        onClick={() => {
          setNewReservation({
            name: "",
            size: 2,
            phone: "",
            notes: "",
            reservationTime: "",
          });
          setViewingReservation(null);
          setIsDialogOpen(true);
        }}
      >
        <Plus size={16} className={styles.plusIcon} />
        Add Reservation
      </button>

      <div className={styles.reservationsContainer}>
        {reservations.length === 0 ? (
          <div className={styles.emptyState}>No upcoming reservations</div>
        ) : (
          reservations.map((reservation) => (
            <div
              key={reservation.reservationId}
              className={styles.customerCard}
              onClick={() => handleViewReservation(reservation)}
              style={{ cursor: "pointer" }}
            >
              <div className={styles.cardHeader}>
                <div>
                  <h4 className={styles.customerName}>
                    {reservation?.customer?.contactName}
                  </h4>
                  <div className={styles.partySize}>
                    <Users size={14} className={styles.icon} />
                    <span>Party of {reservation.numberOfGuests}</span>
                  </div>
                </div>
                <button
                  className={styles.removeButton}
                  onClick={(e) => {
                    e.stopPropagation();
                    showCancelReservationConfirmModal(reservation.reservationId);
                  }}
                >
                  <Trash size={16} />
                  <span className={styles.srOnly}>Cancel</span>
                </button>
              </div>
              <div className={styles.waitingTime}>
                <Calendar size={14} className={styles.icon} />
                <span>
                  Reserved for {formatTime(reservation.startTime)}
                </span>
              </div>
              <div className={styles.phoneNumber}>
                <User size={14} className={styles.icon} />
                <span>{reservation?.customer?.profile.phone}</span>
              </div>
              {reservation.notes && (
                <div className={styles.notes}>{reservation.notes}</div>
              )}
            </div>
          ))
        )}
      </div>

      {isDialogOpen && (
        <div
          className={styles.modalOverlay}
          onClick={() => {
            setIsDialogOpen(false);
            setViewingReservation(null);
          }}
        >
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>
                {viewingReservation ? "View Reservation" : "Add New Reservation"}
              </h2>
              <button
                className={styles.closeButton}
                onClick={() => {
                  setIsDialogOpen(false);
                  setViewingReservation(null);
                }}
              >
                ×
              </button>
            </div>
            <div className={styles.modalBody}>
              {viewingReservation ? (
                <>
                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Customer Name</label>
                    <p className={styles.formInput}>
                      {viewingReservation.customer?.contactName}
                    </p>
                  </div>
                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Party Size</label>
                    <p className={styles.formInput}>
                      {viewingReservation.numberOfGuests}
                    </p>
                  </div>
                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Phone Number</label>
                    <p className={styles.formInput}>
                      {viewingReservation.customer?.profile.phone}
                    </p>
                  </div>
                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Reservation Time</label>
                    <p className={styles.formInput}>
                      {formatTime(viewingReservation.startTime)}
                    </p>
                  </div>
                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Notes</label>
                    <p className={styles.formInput}>
                      {viewingReservation.notes || "No notes"}
                    </p>
                  </div>
                </>
              ) : (
                <>
                  <div className={styles.formGroup}>
                    <label htmlFor="contactName" className={styles.formLabel}>
                      Customer Name
                    </label>
                    <input
                      type="text"
                      id="contactName"
                      className={styles.formInput}
                      placeholder="Customer name..."
                      value={newReservation.name}
                      onChange={(e) =>
                        setNewReservation({
                          ...newReservation,
                          name: e.target.value,
                        })
                      }
                    />
                    {fieldErrors?.contactName &&
                      fieldErrors.contactName.map((error, index) => (
                        <div key={index} className="error-message text-danger">
                          {error}
                        </div>
                      ))}
                  </div>
                  <div className={styles.formGroup}>
                    <label htmlFor="partySize" className={styles.formLabel}>
                      Party Size
                    </label>
                    <input
                      type="number"
                      id="partySize"
                      className={styles.formInput}
                      min="1"
                      value={newReservation.size}
                      onChange={(e) =>
                        setNewReservation({
                          ...newReservation,
                          size: parseInt(e.target.value),
                        })
                      }
                    />
                    {fieldErrors?.partySize &&
                      fieldErrors.partySize.map((error, index) => (
                        <div key={index} className="error-message text-danger">
                          {error}
                        </div>
                      ))}
                  </div>
                  <div className={styles.formGroup}>
                    <label htmlFor="phone" className={styles.formLabel}>
                      Phone Number
                    </label>
                    <input
                      type="text"
                      id="phone"
                      className={styles.formInput}
                      placeholder="e.g., +123456789 or 123-456-7890"
                      value={newReservation.phone}
                      onChange={(e) =>
                        setNewReservation({
                          ...newReservation,
                          phone: e.target.value,
                        })
                      }
                    />
                    {fieldErrors?.phone &&
                      fieldErrors.phone.map((error, index) => (
                        <div key={index} className="error-message text-danger">
                          {error}
                        </div>
                      ))}
                  </div>
                  <div className={styles.formGroup}>
                    <label htmlFor="reservationTime" className={styles.formLabel}>
                      Reservation Time
                    </label>
                    <input
                      type="datetime-local"
                      id="reservationTime"
                      className={styles.formInput}
                      value={newReservation.reservationTime}
                      onChange={(e) =>
                        setNewReservation({
                          ...newReservation,
                          reservationTime: e.target.value,
                        })
                      }
                    />
                    {fieldErrors?.reservationTime &&
                      fieldErrors.reservationTime.map((error, index) => (
                        <div key={index} className="error-message text-danger">
                          {error}
                        </div>
                      ))}
                  </div>
                  <div className={styles.formGroup}>
                    <label htmlFor="notes" className={styles.formLabel}>
                      Notes (Optional)
                    </label>
                    <textarea
                      id="notes"
                      className={styles.formTextarea}
                      rows={3}
                      placeholder="Special requests or notes"
                      value={newReservation.notes}
                      onChange={(e) =>
                        setNewReservation({
                          ...newReservation,
                          notes: e.target.value,
                        })
                      }
                    ></textarea>
                    {fieldErrors?.notes &&
                      fieldErrors.notes.map((error, index) => (
                        <div key={index} className="error-message text-danger">
                          {error}
                        </div>
                      ))}
                  </div>
                </>
              )}
            </div>
            <div className={styles.modalFooter}>
              <button
                className={styles.cancelButton}
                onClick={() => {
                  setIsDialogOpen(false);
                  setViewingReservation(null);
                }}
              >
                Close
              </button>
              {!viewingReservation && (
                <button
                  className={styles.submitButton}
                  onClick={showAddReservationConfirmModal}
                  disabled={createLoading}
                >
                  Add Reservation
                </button>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}