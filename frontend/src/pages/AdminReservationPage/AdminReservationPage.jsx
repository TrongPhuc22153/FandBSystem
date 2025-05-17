import { useCallback, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  useReservation,
  useReservationActions,
} from "../../hooks/reservationHooks";
import { formatDate } from "../../utils/datetimeUtils";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useModal } from "../../context/ModalContext";
import {
  RESERVATION_ACTIONS,
  RESERVATION_STATUSES,
} from "../../constants/webConstant";
import { useAlert } from "../../context/AlertContext";
import ReservationDetails from "../../components/ReservationDetails/ReservationDetails";

function AdminReservationDetailsPage() {
  const { id } = useParams();
  const {
    data: reservationData,
    isLoading: loadingReservation,
    error: reservationError,
    mutate,
  } = useReservation({ reservationId: id });
  const {
    handleProcessReservation,
    processError,
    processLoading,
    processSuccess,
    resetProcess,
  } = useReservationActions();
  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (processSuccess) {
      showNewAlert({
        message: processSuccess,
        action: resetProcess,
      });
    }
  }, [processSuccess, resetProcess, showNewAlert]);

  const handleConfirm = useCallback(async () => {
    const data = await handleProcessReservation(
      id,
      RESERVATION_ACTIONS.CONFIRM
    );
    if (data) {
      mutate();
    }
  }, [id, handleProcessReservation, mutate]);

  const handleCancel = useCallback(async () => {
    const data = await handleProcessReservation(id, RESERVATION_ACTIONS.CANCEL);
    if (data) {
      mutate();
    }
  }, [id, handleProcessReservation, mutate]);

  const showConfirmModal = useCallback(
    (action) => {
      switch (action) {
        case RESERVATION_ACTIONS.CONFIRM:
          onOpen({
            title: "Confirm reservation",
            message: "Do you want to confirm this reservation?",
            onYes: handleConfirm,
          });
          break;
        case RESERVATION_ACTIONS.CANCEL:
          onOpen({
            title: "Cancel reservation",
            message: "Do you want to cancel this reservation?",
            onYes: handleCancel,
          });
          break;
        default:
          showNewAlert({
            message: `There is no action ${action}`,
            variant: "danger",
          });
          break;
      }
    },
    [handleConfirm, handleCancel, onOpen, showNewAlert]
  );

  if (loadingReservation || processLoading) {
    return <Loading />;
  }

  if (reservationError?.message || processError?.message) {
    return (
      <ErrorDisplay
        message={reservationError?.message || processError?.message}
      />
    );
  }

  const startTime = formatDate(reservationData?.startTime);
  const reservationNumber = reservationData?.reservationId;
  const reservationStatus = reservationData?.status?.toLowerCase();
  const table = reservationData?.table; //Get Table
  const customer = reservationData?.customer; //Get Customer
  const menuItems = reservationData?.menuItems || []; //Get Menu Items
  const totalPrice = menuItems.reduce(
    (total, item) => total + item.price * item.quantity,
    0
  );

  return (
    <div>
      <ReservationDetails 
        startTime={startTime}
        reservationNumber={reservationNumber}
        reservationStatus={reservationStatus}
        reservedItems={menuItems}
        table={table}
        total={totalPrice}
        customer={customer}
      />
      {reservationData?.status === RESERVATION_STATUSES.PENDING && (
        <div
          className="d-flex justify-content-end mt-3 container"
          style={{ maxWidth: "800px" }}
        >
          {reservationStatus !== "confirmed" && (
            <button
              className="btn btn-success me-2"
              onClick={() => showConfirmModal(RESERVATION_ACTIONS.CONFIRM)}
              disabled={processLoading}
            >
              Confirm Reservation
            </button>
          )}
          {reservationStatus !== "cancelled" && (
            <button
              className="btn btn-danger"
              onClick={() => showConfirmModal(RESERVATION_ACTIONS.CANCEL)}
              disabled={processLoading}
            >
              Cancel Reservation
            </button>
          )}
        </div>
      )}
    </div>
  );
}

export default AdminReservationDetailsPage;
