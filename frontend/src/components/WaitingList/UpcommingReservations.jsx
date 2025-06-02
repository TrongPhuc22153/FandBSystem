import { useCallback, useEffect, useState } from "react";
import { Calendar, Trash, User, Users } from "lucide-react";
import { useModal } from "../../context/ModalContext";
import styles from "./WaitingList.module.css";
import { formatTime } from "../../utils/datetimeUtils";
import { useTableOccupancyActions } from "../../hooks/tableOccupancyHooks";
import { useAlert } from "../../context/AlertContext";
import {
  TABLE_OCCUPANCY_STATUSES,
} from "../../constants/webConstant";

export function UpcommingReservations({ reservations, mutate }) {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [viewingReservation, setViewingReservation] = useState(null);

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const {
    handleUpdateTableOccupancyStatus,
    updateStatusError,
    updateStatusSuccess,
    resetUpdateStatus,
  } = useTableOccupancyActions();

  useEffect(() => {
    if (updateStatusError?.message) {
      showNewAlert({
        message: updateStatusError.message,
        variant: "danger",
      });
    }
  }, [updateStatusError, showNewAlert]);

  useEffect(() => {
    if (updateStatusSuccess) {
      showNewAlert({
        message: updateStatusSuccess,
        action: resetUpdateStatus,
      });
    }
  }, [
    updateStatusSuccess,
    showNewAlert,
    resetUpdateStatus,
  ]);

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
                    showCancelReservationConfirmModal(
                      reservation.reservationId
                    );
                  }}
                >
                  <Trash size={16} />
                  <span className={styles.srOnly}>Cancel</span>
                </button>
              </div>
              <div className={styles.waitingTime}>
                <Calendar size={14} className={styles.icon} />
                <span>Reserved for {formatTime(reservation.startTime)}</span>
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
                View Reservation
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
              {viewingReservation && (
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
            </div>
          </div>
        </div>
      )}
    </div>
  );
}