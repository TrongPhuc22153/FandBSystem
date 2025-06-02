import { Badge } from "react-bootstrap";
import {
  RESERVATION_ACTIONS,
  RESERVATION_STATUS_CLASSES,
  RESERVATION_STATUSES,
} from "../../constants/webConstant";

export default function ReservationDetailModal({
  reservation,
  onClose,
  onUpdateStatus,
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

  // Calculate time until reservation
  const getTimeUntil = (reservation) => {
    const now = new Date();

    const reservationDateTime = new Date(
      `${reservation.date}T${reservation.startTime}`
    );
    if (isNaN(reservationDateTime)) return "Invalid time";

    const diffMs = reservationDateTime - now;
    if (diffMs <= 0) return "Overdue";

    const diffMins = Math.floor(diffMs / 60000);
    const hours = Math.floor(diffMins / 60);
    const mins = diffMins % 60;

    return `${hours}h ${mins}m`;
  };

  const calculateTotal = (items) => {
    return items.reduce(
      (total, item) => total + item.quantity * item.product.unitPrice,
      0
    );
  };

  return (
    <div
      className="modal show d-block"
      tabIndex={-1}
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
                  {reservation.customer.profile.user.username}
                </p>
                <p className="mb-1">
                  <strong>Phone:</strong> {reservation.customer.profile.phone}
                </p>
                <p className="mb-1">
                  <strong>Party Size:</strong> {reservation.numberOfGuests}{" "}
                  people
                </p>
                <p className="mb-1">
                  <strong>Reservation Time:</strong>{" "}
                  {new Date(
                    `${reservation.date}T${reservation.startTime}`
                  ).toLocaleString()}
                </p>
                {reservation.status === RESERVATION_STATUSES.PENDING && (
                  <p className="mb-1">
                    <strong>Time Until Arrival:</strong>{" "}
                    {getTimeUntil(reservation)}
                  </p>
                )}
              </div>
              <div className="col-md-6">
                <h6>Reservation Status</h6>
                <div className="d-flex align-items-center mb-3">
                  <Badge
                    className={`me-2`}
                    bg={
                      RESERVATION_STATUS_CLASSES[reservation.status] ||
                      RESERVATION_STATUS_CLASSES.DEFAULT
                    }
                  >
                    {reservation.status.charAt(0).toUpperCase() +
                      reservation.status.slice(1)}
                  </Badge>
                  {nextAction && (
                    <button
                      className="btn btn-sm btn-primary"
                      onClick={() =>
                        onUpdateStatus(
                          reservation.reservationId,
                          nextAction.toUpperCase()
                        )
                      }
                    >
                      Mark as{" "}
                      {nextAction.charAt(0).toUpperCase() + nextAction.slice(1)}
                    </button>
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
                          : reservation.status ===
                            RESERVATION_STATUSES.PREPARING
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

            <div className="row">
              <div className="col-md-6">
                <h6>Special Requests</h6>
                <div className="card">
                  <div className="card-body">
                    {reservation.specialRequests ||
                      "No special requests provided."}
                  </div>
                </div>
              </div>
              <div className="col-md-6">
                <h6>Table Assignment</h6>
                <div className="card">
                  <div className="card-body">
                    Table {reservation.table.tableNumber} - Capacity (
                    {reservation.table.capacity} people) - Location{" "}
                    {reservation.table.location}
                  </div>
                </div>
              </div>
            </div>

            <h6>reservation Items</h6>
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
                      <td>{item.specialInstructions || "-"}</td>
                      <td className="text-end">
                        ${item.quantity * item.product.unitPrice}
                      </td>
                    </tr>
                  ))}
                  <tr className="table-active">
                    <td colSpan={3} className="text-end">
                      <strong>Total:</strong>
                    </td>
                    <td className="text-end">
                      <strong>
                        ${calculateTotal(reservation.menuItems).toFixed(2)}
                      </strong>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className="mt-4">
              <h6>Staff Notes</h6>
              <textarea
                className="form-control"
                rows={3}
                placeholder="Add notes about this reservation..."
              ></textarea>
            </div>
          </div>
          <div className="modal-footer">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={onClose}
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
