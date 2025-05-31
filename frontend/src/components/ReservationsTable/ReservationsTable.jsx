import { useState, useEffect, useCallback, useMemo } from "react";
import styles from "./kitchen-table.module.css";
import ReservationDetailModal from "./ReservationDetailModal";
import {
  RESERVATION_STATUS_CLASSES,
  RESERVATION_STATUSES,
  SORTING_DIRECTIONS,
} from "../../constants/webConstant";
import {
  useReservationActions,
  useReservations,
} from "../../hooks/reservationHooks";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";
import { Badge } from "react-bootstrap";
import { useSearchParams } from "react-router-dom";
import Pagination from "../Pagination/Pagination";
import { formatDate } from "../../utils/datetimeUtils";

export default function ReservationsTable() {
  const [searchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const [filterStatus, setFilterStatus] = useState(null);
  const [selectedReservation, setSelectedReservation] = useState(null);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const { data: reservationsData, mutate } = useReservations({
    status: filterStatus,
    page: currentPage,
    direction: SORTING_DIRECTIONS.ASC,
  });
  const reservations = useMemo(
    () => reservationsData?.content || [],
    [reservationsData]
  );
  const totalPages = reservationsData?.totalPages || 0;

  const {
    handleProcessReservation,
    resetProcess,
    processError,
    processSuccess,
  } = useReservationActions();

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (processError?.message) {
      showNewAlert({
        message: processError.message,
        variant: "danger",
      });
    }
  }, [processError]);

  useEffect(() => {
    if (processSuccess) {
      showNewAlert({
        message: processSuccess,
        action: resetProcess,
      });
    }
  }, [processSuccess]);

  const updateReservationStatus = useCallback(
    async (reservationId, action) => {
      const res = await handleProcessReservation(reservationId, action);
      if (res) {
        mutate();
        closeReservationDetail();
      }
    },
    [mutate, handleProcessReservation]
  );

  const showConfirmModal = useCallback(
    (reservationId, action) => {
      onOpen({
        title: "Process reservation",
        message: "Do you want to continue?",
        onYes: () => updateReservationStatus(reservationId, action),
      });
    },
    [onOpen, updateReservationStatus]
  );

  // Sort reservations by time
  const sortedReservations = [...reservations].sort(
    (a, b) =>
      new Date(a.reservationTime).getTime() -
      new Date(b.reservationTime).getTime()
  );

  // Calculate time until reservation
  const getTimeUntil = (reservation) => {
    const now = new Date();

    const reservationDateTime = new Date(`${reservation.date}T${reservation.startTime}`);
    if (isNaN(reservationDateTime)) return "Invalid time";

    const diffMs = reservationDateTime - now;
    if (diffMs <= 0) return "Overdue";

    const diffMins = Math.floor(diffMs / 60000);
    const hours = Math.floor(diffMins / 60);
    const mins = diffMins % 60;

    return `${hours}h ${mins}m`;
  };

  const handleReservationClick = (reservation) => {
    setSelectedReservation(reservation);
  };

  const closeReservationDetail = useCallback(() => {
    setSelectedReservation(null);
  }, []);

  return (
    <div>
      <div className="d-flex justify-content-between mb-3">
        <h3>Reservations</h3>
        <div className="btn-group">
          <button
            className={`btn ${
              filterStatus === null ? "btn-primary" : "btn-outline-primary"
            }`}
            onClick={() => setFilterStatus(null)}
          >
            All
          </button>
          <button
            className={`btn ${
              filterStatus === RESERVATION_STATUSES.PENDING
                ? "btn-primary"
                : "btn-outline-primary"
            }`}
            onClick={() => setFilterStatus(RESERVATION_STATUSES.PENDING)}
          >
            {RESERVATION_STATUSES.PENDING}
          </button>
          <button
            className={`btn ${
              filterStatus === RESERVATION_STATUSES.PREPARING
                ? "btn-primary"
                : "btn-outline-primary"
            }`}
            onClick={() => setFilterStatus(RESERVATION_STATUSES.PREPARING)}
          >
            {RESERVATION_STATUSES.PREPARING}
          </button>
          <button
            className={`btn ${
              filterStatus === RESERVATION_STATUSES.PREPARED
                ? "btn-primary"
                : "btn-outline-primary"
            }`}
            onClick={() => setFilterStatus(RESERVATION_STATUSES.PREPARED)}
          >
            {RESERVATION_STATUSES.PREPARED}
          </button>
        </div>
      </div>

      <div className="table-responsive">
        <table className="table table-striped table-hover">
          <thead className="table-dark">
            <tr>
              <th>ID</th>
              <th>Customer</th>
              <th>Items</th>
              <th>Party Size</th>
              <th>Time</th>
              <th>Time Until</th>
              <th>Special Requests</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sortedReservations.length === 0 ? (
              <tr>
                <td colSpan={8} className="text-center">
                  No reservations found
                </td>
              </tr>
            ) : (
              sortedReservations.map((reservation) => (
                <tr
                  key={reservation.reservationId}
                  className={`${styles.reservationRow} cursor-pointer`}
                  onClick={() => handleReservationClick(reservation)}
                  style={{ cursor: "pointer" }}
                >
                  <td>{reservation.reservationId}</td>
                  <td>
                    {reservation?.customer?.profile.user.username}
                    <small className="d-block text-muted">
                      {reservation?.customer?.profile.phone}
                    </small>
                  </td>
                  <td>
                    <ul className={styles.itemsList}>
                      {reservation.menuItems.map((item, index) => (
                        <li key={index}>
                          {item.quantity}x {item.product.productName}
                          {item.specialInstructions && (
                            <small className="d-block text-muted">
                              Note: {item?.specialInstructions}
                            </small>
                          )}
                        </li>
                      ))}
                    </ul>
                  </td>
                  <td>{reservation.numberOfGuests}</td>
                  <td>
                    {formatDate(`${reservation.date}T${reservation.startTime}`)}
                  </td>
                  <td>
                    {reservation.status === RESERVATION_STATUSES.PENDING && (
                      <span className={styles.timeUntil}>
                        {getTimeUntil(reservation)}
                      </span>
                    )}
                  </td>
                  <td>{reservation.specialRequests || "-"}</td>
                  <td>
                    <Badge
                      bg={
                        RESERVATION_STATUS_CLASSES[reservation.status] ||
                        RESERVATION_STATUS_CLASSES.DEFAULT
                      }
                    >
                      {reservation.status.charAt(0).toUpperCase() +
                        reservation.status.slice(1)}
                    </Badge>
                  </td>
                  <td onClick={(e) => e.stopPropagation()}>
                    <div className="btn-group">
                      {reservation.status === RESERVATION_STATUSES.PENDING && (
                        <button
                          className="btn btn-sm btn-outline-success"
                          onClick={() =>
                            showConfirmModal(
                              reservation.reservationId,
                              RESERVATION_STATUSES.PREPARING
                            )
                          }
                        >
                          Start Preparing
                        </button>
                      )}
                      {reservation.status ===
                        RESERVATION_STATUSES.PREPARING && (
                        <button
                          className="btn btn-sm btn-outline-secondary"
                          onClick={() =>
                            showConfirmModal(
                              reservation.reservationId,
                              RESERVATION_STATUSES.PREPARED
                            )
                          }
                        >
                          Mark Ready
                        </button>
                      )}
                      {reservation.status === RESERVATION_STATUSES.PREPARED && (
                        <button
                          className="btn btn-sm btn-outline-danger"
                          onClick={() =>
                            showConfirmModal(
                              reservation.reservationId,
                              RESERVATION_STATUSES.COMPLETED
                            )
                          }
                        >
                          Complete
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
        {totalPages > 1 && (
          <Pagination currentPage={currentPage + 1} totalPages={totalPages} />
        )}
      </div>

      {selectedReservation && (
        <ReservationDetailModal
          reservation={selectedReservation}
          onClose={closeReservationDetail}
          onUpdateStatus={showConfirmModal}
        />
      )}
    </div>
  );
}
