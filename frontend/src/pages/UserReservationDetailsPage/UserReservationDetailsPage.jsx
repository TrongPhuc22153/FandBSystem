import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useReservation, useReservationActions } from "../../hooks/reservationHooks";
import { useRefundActions } from "../../hooks/refundHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import ReservationDetails from "../../components/ReservationDetails/ReservationDetails";
import { useAlert } from "../../context/AlertContext";
import { useModal } from "../../context/ModalContext";
import { RESERVATION_ACTIONS, RESERVATION_STATUSES } from "../../constants/webConstant";
import { Button, Modal } from "react-bootstrap";
import RefundPreviewModal from "../../components/RefundPreviewModal/RefundPreviewModal";
import { PAYMENT_METHODS } from "../../constants/paymentConstants";

export default function UserReservationDetailsPage() {
  const { id } = useParams();

  const [refundPreview, setRefundPreview] = useState(null);
  const [showRefundModal, setShowRefundModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const {
    data: reservationData,
    isLoading: loadingReservation,
    error: reservationError,
    mutate: refetchReservation,
  } = useReservation({ reservationId: id });

  const {
    handleProcessReservation,
    processError,
    processLoading,
    processSuccess,
    resetProcess,
  } = useReservationActions();

  const { fetchReservationPreview } = useRefundActions();
  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (processError?.message) {
      showNewAlert({
        message: processError.message,
        variant: "danger",
        action: resetProcess,
      });
    }
  }, [processError, showNewAlert, resetProcess]);

  useEffect(() => {
    if (processSuccess) {
      showNewAlert({
        message: processSuccess || "Reservation processed successfully.",
        variant: "success",
        action: () => {
          resetProcess();
        },
      });
    }
  }, [processSuccess, showNewAlert, resetProcess]);

  const handleCancelReservation = useCallback(async () => {
    const res = await handleProcessReservation(
      reservationData.reservationId,
      RESERVATION_ACTIONS.CANCEL
    );
    if( res ) {
      refetchReservation();
      setShowSuccessModal(true);
    }
  }, [reservationData, handleProcessReservation, refetchReservation]);

  const handleCancelClick = useCallback(async () => {
    if (reservationData.payment.method.toLowerCase() === PAYMENT_METHODS.PAYPAL) {
      const preview = await fetchReservationPreview(
        reservationData.reservationId
      );
      if (preview) {
        setRefundPreview(preview);
        setShowRefundModal(true);
      }
    } else {
      onOpen({
        title: "Cancel Reservation",
        message: `Are you sure you want to cancel reservation #${reservationData.reservationId}?`,
        onYes: handleCancelReservation,
      });
    }
  }, [
    reservationData,
    fetchReservationPreview,
    onOpen,
    handleCancelReservation,
  ]);

  const handleConfirmRefund = useCallback(async () => {
    setShowRefundModal(false);
    const res =await handleProcessReservation(
      reservationData.reservationId,
      RESERVATION_ACTIONS.CANCEL
    );
    if(res){
      refetchReservation();
    }
  }, [reservationData, handleProcessReservation, refetchReservation]);

  if (loadingReservation || processLoading) {
    return <Loading />;
  }

  if (reservationError?.message) {
    return <ErrorDisplay message={reservationError.message} />;
  }

  return (
    <div className="d-flex justify-content-center p-4">
      <ReservationDetails
        reservation={reservationData}
        processLoading={processLoading}
        onHandleCancel={handleCancelClick}
        renderActions={
          reservationData.status === RESERVATION_STATUSES.PENDING ||
          reservationData.status === RESERVATION_STATUSES.PREPARING ||
          reservationData.status === RESERVATION_STATUSES.PREPARED ||
          reservationData.status === RESERVATION_STATUSES.CONFIRMED
        }
      />

      <RefundPreviewModal
        show={showRefundModal}
        onHide={() => setShowRefundModal(false)}
        preview={refundPreview}
        onConfirm={handleConfirmRefund}
      />

      <Modal
        show={showSuccessModal}
        onHide={() => setShowSuccessModal(false)}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Reservation Cancelled</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>
            Your reservation #{reservationData.reservationId} has been successfully
            cancelled.
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => setShowSuccessModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
