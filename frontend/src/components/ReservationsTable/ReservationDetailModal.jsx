import { Badge, Button } from "react-bootstrap";
import {
  RESERVATION_ACTIONS,
  RESERVATION_STATUS_CLASSES,
  RESERVATION_STATUSES,
  RESERVATION_ITEM_STATUS_CLASSES,
  RESERVATION_ITEM_STATUSES,
} from "../../constants/webConstant";
import { formatDate } from "../../utils/datetimeUtils";

export default function ReservationDetailModal({
  reservation,
  onClose,
  onUpdateStatus,
  onCancelReservationItem,
}) {
  if (!reservation) return null;

  const getNextAction = (currentStatus) => {
    switch (currentStatus.toUpperCase()) {
      case RESERVATION_STATUSES.PENDING:
        return RESERVATION_ACTIONS.PREPARING.toLowerCase();
      case RESERVATION_STATUSES.PREPARING:
        return RESERVATION_ACTIONS.PREPARED.toLowerCase();
      case RESERVATION_STATUSES.PREPARED:
        return RESERVATION_ACTIONS.READY.toLowerCase();
      default:
        return null;
    }
  };

  const nextAction = getNextAction(reservation.status);

  const calculateTotal = (items) => {
    return items.reduce(
      (total, item) =>
        total +
        (item.status !== RESERVATION_ITEM_STATUSES.CANCELLED
          ? item.quantity * item.product.unitPrice
          : 0),
      0
    );
  };

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

  return (
    <div
      className="modal show d-block"
      tabIndex="-1"
      role="dialog"
      style={{ backgroundColor: "rgba(0,0,0,0.5)" }}
    >
      <div className="modal-dialog modal-lg" role="document">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              Reservation Details: {reservation.reservationId}
            </h5>
            <button
              type="button"
              className="btn-close"
              onClick={onClose}
              aria-label="Close"
            ></button>
          </div>
          <div className="modal-body">
            <div className="row mb-4">
              <div className="col-md-6">
                <h6>Customer Information</h6>
                <p className="mb-1">
                  <strong>Name:</strong>{" "}
                  {reservation?.customer?.profile.user.username || "UNKNOWN"}
                </p>
                <p className="mb-1">
                  <strong>Phone:</strong>{" "}
                  {reservation?.customer?.profile.phone || "-"}
                </p>
                <p className="mb-1">
                  <strong>Table:</strong>{" "}
                  {reservation?.table?.tableNumber || "-"}
                </p>
                <p className="mb-1">
                  <strong>Party Size:</strong> {reservation.numberOfGuests} people
                </p>
                <p className="mb-1">
                  <strong>Reservation Time:</strong>{" "}
                  {formatDate(`${reservation.date}T${reservation.startTime}`)}
                </p>
                {reservation.status === RESERVATION_STATUSES.PENDING && (
                  <p className="mb-1">
                    <strong>Time Until Arrival:</strong> {getTimeUntil(reservation)}
                  </p>
                )}
                <p className="mb-1">
                  <strong>Special Requests:</strong>{" "}
                  {reservation.specialRequests || "-"}
                </p>
              </div>
              <div className="col-md-6">
                <h6>Reservation Status</h6>
                <div className="d-flex align-items-center mb-3">
                  <Badge
                    className="me-2"
                    bg={
                      RESERVATION_STATUS_CLASSES[reservation.status] ||
                      RESERVATION_STATUS_CLASSES.DEFAULT
                    }
                  >
                    {reservation.status.charAt(0).toUpperCase() +
                      reservation.status.slice(1)}
                  </Badge>
                  {nextAction && (
                    <Button
                      variant="primary"
                      size="sm"
                      onClick={() =>
                        onUpdateStatus(
                          reservation.reservationId,
                          nextAction.toUpperCase()
                        )
                      }
                    >
                      Mark as{" "}
                      {nextAction.charAt(0).toUpperCase() + nextAction.slice(1)}
                    </Button>
                  )}
                </div>
                <div className="progress">
                  <div
                    className={`progress-bar ${
                      reservation.status === RESERVATION_STATUSES.PENDING
                        ? "bg-danger"
                        : reservation.status === RESERVATION_STATUSES.PREPARING
                        ? "bg-warning"
                        : reservation.status === RESERVATION_STATUSES.PREPARED
                        ? "bg-success"
                        : "bg-secondary"
                    }`}
                    role="progressbar"
                    style={{
                      width:
                        reservation.status === RESERVATION_STATUSES.PENDING
                          ? "25%"
                          : reservation.status === RESERVATION_STATUSES.PREPARING
                          ? "50%"
                          : reservation.status === RESERVATION_STATUSES.PREPARED
                          ? "75%"
                          : "100%",
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

            <h6>Reservation Items</h6>
            <div className="table-responsive">
              <table className="table table-striped">
                <thead>
                  <tr>
                    <th>Item</th>
                    <th>Quantity</th>
                    <th>Special Instructions</th>
                    <th className="text-end">Subtotal</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {reservation.menuItems.map((item, index) => (
                    <tr key={index}>
                      <td>
                        <div className="d-flex justify-content-between">
                          <span>{item.product?.productName}</span>
                          <Badge bg={RESERVATION_ITEM_STATUS_CLASSES[item.status]}>
                            {item.status}
                          </Badge>
                        </div>
                      </td>
                      <td>{item.quantity}</td>
                      <td>{item.specialInstructions || "-"}</td>
                      <td className="text-end">
                        $
                        {item.status !== RESERVATION_ITEM_STATUSES.CANCELLED
                          ? (item.quantity * item.product.unitPrice).toFixed(2)
                          : "0.00"}
                      </td>
                      <td>
                        {(item.status === RESERVATION_ITEM_STATUSES.PENDING ||
                          item.status === RESERVATION_ITEM_STATUSES.PREPARING) && (
                          <Button
                            variant="outline-danger"
                            size="sm"
                            onClick={() =>
                              onCancelReservationItem(reservation.reservationId, item.itemId)
                            }
                          >
                            Cancel
                          </Button>
                        )}
                      </td>
                    </tr>
                  ))}
                  <tr className="table-active">
                    <td colSpan={4} className="text-end">
                      <strong>Total:</strong>
                    </td>
                    <td className="text-end">
                      <strong>${calculateTotal(reservation.menuItems).toFixed(2)}</strong>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className="mt-4">
              <h6>Kitchen Notes</h6>
              <textarea
                className="form-control"
                rows={3}
                placeholder="Add notes for kitchen staff..."
              ></textarea>
            </div>
          </div>
          <div className="modal-footer">
            <Button
              variant="secondary"
              onClick={onClose}
            >
              Close
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}