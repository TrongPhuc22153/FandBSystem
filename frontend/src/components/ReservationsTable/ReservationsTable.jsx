import { useState, useEffect, useCallback, useMemo } from "react";
import styles from "./kitchen-table.module.css";
import ReservationDetailModal from "./ReservationDetailModal";
import {
  RESERVATION_ACTIONS,
  RESERVATION_STATUS_CLASSES,
  RESERVATION_STATUSES,
  SORTING_DIRECTIONS,
} from "../../constants/webConstant";
import {
  useReservationActions,
  useReservationItemActions,
  useReservations,
} from "../../hooks/reservationHooks";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";
import { useAuth } from "../../context/AuthContext";
import { useStompSubscription } from "../../hooks/websocketHooks";
import { Badge } from "react-bootstrap";
import { useSearchParams } from "react-router-dom";
import Pagination from "../Pagination/Pagination";
import { TOPIC_KITCHEN } from "../../constants/webSocketEnpoint";
import { formatDate } from "../../utils/datetimeUtils";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import { RESERVATION_FILTER_MAPPING } from "../../constants/filter";

export default function ReservationsTable() {
  const [searchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const [filterStatus, setFilterStatus] = useState(RESERVATION_FILTER_MAPPING[0].statuses);
  const [selectedReservation, setSelectedReservation] = useState(null);

  const { data: reservationsData, mutate } = useReservations({
    status: filterStatus,
    page: currentPage,
    sortDirection: SORTING_DIRECTIONS.ASC,
    sortField: "date",
  });
  const reservations = useMemo(
    () => reservationsData?.content || [],
    [reservationsData]
  );
  const totalPages = reservationsData?.totalPages || 0;

  const {
    handleProcessReservation,
    processError,
    processSuccess,
    resetProcess,
  } = useReservationActions();

  const {
    handleCancelReservationItem,
    cancelError,
    resetCancel,
  } = useReservationItemActions();

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();
  const { user } = useAuth();

  useEffect(() => {
    if (processError?.message) {
      showNewAlert({
        message: processError.message,
        variant: "danger",
      });
    }
  }, [processError, showNewAlert]);

  useEffect(() => {
    if (processSuccess) {
      showNewAlert({
        message: processSuccess,
        action: resetProcess,
      });
    }
  }, [processSuccess, showNewAlert, resetProcess]);

  useEffect(() => {
    if (cancelError?.message) {
      showNewAlert({
        message: cancelError.message,
        variant: "danger",
        action: resetCancel,
      });
    }
  }, [cancelError, showNewAlert, resetCancel]);

  const closeReservationDetail = useCallback(() => {
    setSelectedReservation(null);
  }, []);

  const updateReservationStatus = useCallback(
    async (reservationId, action) => {
      const res = await handleProcessReservation(reservationId, action);
      if (res) {
        mutate();
        closeReservationDetail();
      }
    },
    [mutate, handleProcessReservation, closeReservationDetail]
  );

  const cancelReservationItem = useCallback(
    async (reservationId, itemId) => {
      const res = await handleCancelReservationItem({
        reservationId,
        itemId,
      });
      if (res) {
        mutate();
      }
    },
    [mutate, handleCancelReservationItem]
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

  const showConfirmCancelReservationItem = useCallback(
    (reservationId, itemId) => {
      onOpen({
        title: "Cancel item",
        message: "Do you want to cancel this item?",
        onYes: () => cancelReservationItem(reservationId, itemId),
      });
    },
    [cancelReservationItem, onOpen]
  );

  const handleMessage = useCallback((newNotification) => {
    try {
      if (!newNotification?.id) {
        return;
      }
      mutate(); // Refresh data on new notification
    } catch (error) {
      console.error("Error processing notification:", error);
    }
  }, [mutate]);

  useStompSubscription({
    topic: TOPIC_KITCHEN,
    onMessage: handleMessage,
    shouldSubscribe: hasRole(user, ROLES.EMPLOYEE),
  });

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

  return (
    <>
      <div className="d-flex justify-content-between mb-3">
        <h3>Reservations</h3>
        <div className="btn-group">
          {RESERVATION_FILTER_MAPPING.map((filter) => (
            <button
              key={filter.label}
              className={`btn ${
                filterStatus === filter.statuses
                  ? filter.activeClass
                  : filter.inactiveClass
              }`}
              onClick={() => setFilterStatus(filter.statuses)}
            >
              {filter.label}
            </button>
          ))}
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
                <td colSpan={9} className="text-center">
                  No reservations found
                </td>
              </tr>
            ) : (
              sortedReservations.map((reservation) => (
                <tr
                  key={reservation.reservationId}
                  className={`${styles.orderRow} cursor-pointer`}
                  onClick={() => handleReservationClick(reservation)}
                  style={{ cursor: "pointer" }}
                >
                  <td>{reservation.reservationId}</td>
                  <td>
                    {reservation?.customer?.profile.user.username || "UNKNOWN"}
                    <small className="d-block text-muted">
                      {reservation?.customer?.profile.phone || "-"}
                    </small>
                  </td>
                  <td>
                    <ul className={styles.itemsList}>
                      {reservation.menuItems.map((item, index) => (
                        <li key={index}>
                          {item.quantity}x {item.product.productName}
                          {item.specialInstructions && (
                            <small className="d-block text-muted">
                              Note: {item.specialInstructions}
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
                          className="btn btn-sm btn-outline-warning"
                          onClick={() =>
                            showConfirmModal(
                              reservation.reservationId,
                              RESERVATION_ACTIONS.PREPARING
                            )
                          }
                        >
                          Start Preparing
                        </button>
                      )}
                      {reservation.status === RESERVATION_STATUSES.PREPARING && (
                        <button
                          className="btn btn-sm btn-outline-success"
                          onClick={() =>
                            showConfirmModal(
                              reservation.reservationId,
                              RESERVATION_ACTIONS.PREPARED
                            )
                          }
                        >
                          Mark as Prepared
                        </button>
                      )}
                      {reservation.status === RESERVATION_STATUSES.PREPARED && (
                        <button
                          className="btn btn-sm btn-outline-secondary"
                          onClick={() =>
                            showConfirmModal(
                              reservation.reservationId,
                              RESERVATION_ACTIONS.READY
                            )
                          }
                        >
                          Mark as Ready
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
          onCancelReservationItem={showConfirmCancelReservationItem}
        />
      )}
    </>
  );
}