import React, { useEffect, useState } from 'react';
import { Button, Badge, Card, Modal } from 'react-bootstrap';
import { useRefundActions } from '../../hooks/refundHooks';
import { useReservationActions } from '../../hooks/reservationHooks';
import { useAlert } from '../../context/AlertContext';
import { RESERVATION_STATUS_CLASSES, RESERVATION_STATUSES, PAYMENT_STATUS_CLASSES } from '../../constants/webConstant';
import RefundPreviewModal from '../RefundPreviewModal/RefundPreviewModal';
import Loading from '../Loading/Loading';

export default function ReservationDetail({ reservation }) {
  const { fetchReservationPreview } = useRefundActions();
  const [refundPreview, setRefundPreview] = useState(null);
  const [showRefundModal, setShowRefundModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const {
    handleProcessReservation,
    processError,
    processLoading,
    resetProcess,
  } = useReservationActions();

  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (processError?.message) {
      showNewAlert({
        message: processError.message,
        variant: 'danger',
        action: resetProcess,
      });
    }
  }, [processError, showNewAlert, resetProcess]);

  const handleCancelClick = async () => {
    try {
      if (reservation.payment.method.toUpperCase() === 'PAYPAL') {
        const preview = await fetchReservationPreview(reservation.reservationId);
        if (preview) {
          setRefundPreview(preview);
          setShowRefundModal(true);
        }
      } else {
        // For non-PayPal methods, directly process cancellation
        const result = await handleProcessReservation(reservation.reservationId, 'CANCEL');
        if (result) {
          setShowSuccessModal(true);
        } else {
          showNewAlert({
            message: 'Failed to cancel reservation. Please try again.',
            variant: 'danger',
          });
        }
      }
    } catch (err) {
      console.error('Error handling cancellation:', err);
      showNewAlert({
        message: 'Failed to process cancellation. Please try again.',
        variant: 'danger',
      });
    }
  };

  const handleConfirmRefund = async () => {
    setShowRefundModal(false);
    try {
      const result = await handleProcessReservation(reservation.reservationId, 'CANCEL');
      if (result) {
        setShowSuccessModal(true);
      } else {
        showNewAlert({
          message: 'Failed to initiate refund. Please try again.',
          variant: 'danger',
        });
      }
    } catch (err) {
      console.error('Error processing refund:', err);
      showNewAlert({
        message: 'Failed to initiate refund. Please try again.',
        variant: 'danger',
      });
    }
  };

  if (processLoading) return <Loading />;

  return (
    <div className="container bg-white py-3 px-3 rounded-3" style={{ maxWidth: '800px' }}>
      <h5>Reservation Details: {reservation.reservationId}</h5>
      <div className="row mb-4">
        <div className="col-md-6">
          <h6>Customer Information</h6>
          <p className="mb-1">
            <strong>Name:</strong> {reservation.customer.profile.user.username}
          </p>
          <p className="mb-1">
            <strong>Phone:</strong> {reservation.customer.profile.phone}
          </p>
          <p className="mb-1">
            <strong>Party Size:</strong> {reservation.numberOfGuests} people
          </p>
          <p className="mb-1">
            <strong>Reservation Date:</strong> {new Date(reservation.date).toLocaleDateString()}
          </p>
          <p className="mb-1">
            <strong>Start time:</strong> {reservation.startTime}
          </p>
          <p className="mb-1">
            <strong>End time:</strong> {reservation.endTime}
          </p>
        </div>
        <div className="col-md-6">
          <h6>Reservation Status</h6>
          <div className="d-flex align-items-center mb-3">
            <Badge
              className="me-2"
              bg={RESERVATION_STATUS_CLASSES[reservation.status] || RESERVATION_STATUS_CLASSES.DEFAULT}
            >
              {reservation.status.charAt(0).toUpperCase() + reservation.status.slice(1)}
            </Badge>
          </div>
          <div className="progress">
            <div
              className={`progress-bar ${
                reservation.status === RESERVATION_STATUSES.PENDING
                  ? 'bg-danger'
                  : reservation.status === RESERVATION_STATUSES.PREPARING
                  ? 'bg-warning'
                  : reservation.status === RESERVATION_STATUSES.PREPARED
                  ? 'bg-success'
                  : 'bg-secondary'
              }`}
              role="progressbar"
              style={{
                width:
                  reservation.status === RESERVATION_STATUSES.PENDING
                    ? '25%'
                    : reservation.status === RESERVATION_STATUSES.PREPARING
                    ? '50%'
                    : reservation.status === RESERVATION_STATUSES.PREPARED
                    ? '75%'
                    : '100%',
              }}
              aria-valuenow={
                reservation.status === RESERVATION_STATUSES.PENDING
                  ? 25
                  : reservation.status === RESERVATION_STATUSES.PREPARING
                  ? 50
                  : reservation.status === RESERVATION_STATUSES.PREPARED
                  ? 75
                  : 100
              }
              aria-valuemin={0}
              aria-valuemax={100}
            ></div>
          </div>
        </div>
      </div>

      <div className="row mb-4">
        <div className="col-md-6">
          <h6>Payment Details</h6>
          <Card>
            <Card.Body>
              <p className="mb-1">
                <strong>Method:</strong> {reservation.payment.method.toUpperCase()}
              </p>
              <p className="mb-1">
                <strong>Status:</strong>{' '}
                <Badge bg={PAYMENT_STATUS_CLASSES[reservation.payment.status]} aria-label={`Payment status: ${reservation.payment.status}`}>
                  {reservation.payment.status.charAt(0).toUpperCase() + reservation.payment.status.slice(1)}
                </Badge>
              </p>
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-6">
          <h6>Table Assignment</h6>
          <div className="card">
            <div className="card-body">
              Table {reservation.table.tableNumber} - Capacity ({reservation.table.capacity} people) - Location {reservation.table.location}
            </div>
          </div>
        </div>
      </div>

      <div className="row mb-4">
        <div className="col-md-12">
          <h6>Special Requests</h6>
          <div className="card">
            <div className="card-body">{reservation.notes || 'No special requests provided.'}</div>
          </div>
        </div>
      </div>

      <h6>Reservation Items</h6>
      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              <th>Item</th>
              <th>Quantity</th>
              <th>Special Instructions</th>
              <th className="text-end">Subtotal</th>
            </tr>
          </thead>
          <tbody>
            {reservation.menuItems.map((item, index) => (
              <tr key={index}>
                <td>{item.product.productName}</td>
                <td>{item.quantity}</td>
                <td>{item.specialInstructions || '-'}</td>
                <td className="text-end">${(item.quantity * item.product.unitPrice).toFixed(2)}</td>
              </tr>
            ))}
            <tr className="table-active">
              <td colSpan={3} className="text-end">
                <strong>Total:</strong>
              </td>
              <td className="text-end">
                <strong>${reservation.totalPrice.toFixed(2)}</strong>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div className="d-flex justify-content-end mt-3">
        <Button variant="danger" onClick={handleCancelClick} disabled={processLoading}>
          Cancel Reservation
        </Button>
      </div>

      <RefundPreviewModal
        show={showRefundModal}
        onHide={() => setShowRefundModal(false)}
        preview={refundPreview}
        onConfirm={handleConfirmRefund}
      />

      <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Reservation Cancelled</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Your reservation #{reservation.reservationId} has been successfully cancelled.</p>
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