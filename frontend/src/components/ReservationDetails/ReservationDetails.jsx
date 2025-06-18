import { Button, Badge, Card } from "react-bootstrap";
import {
  PAYMENT_STATUS_CLASSES,
} from "../../constants/webConstant";
import { CreditCard, FileText, MapPin } from "lucide-react";
import ReservationProcessingBar from "../ProcessingBar/ReservationProcessingBar/ReservationProcessingBar";

export default function ReservationDetail({
  reservation,
  processLoading,
  onHandleCancel,
  renderActions = false,
}) {
  return (
    <div
      className="bg-white py-3 px-4 rounded-3 flex-grow-1 flex-shrink-1 shadow rounded"
      style={{ maxWidth: "800px" }}
    >
      <div className="text-sm-center">
        <h3 className="text-2xl font-bold text-gray-900">Reservation Details</h3>
        <p className="text-gray-600">Reservation ID: {reservation.reservationId}</p>
      </div>

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
            <strong>Reservation Date:</strong>{" "}
            {new Date(reservation.date).toLocaleDateString()}
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
          <ReservationProcessingBar status={reservation.status}/>
        </div>
      </div>

      <div className="row mb-4">
        <div className="col-md-6">
          <div className="d-flex mb-1">
            <CreditCard className="w-5 h-5" />
            <h6 className="ms-1 mb-0">Payment Details</h6>
          </div>
          <Card>
            <Card.Body>
              <p className="mb-1">
                <strong>Method:</strong>{" "}
                {reservation.payment.method.toUpperCase()}
              </p>
              <p className="mb-1">
                <strong>Status:</strong>{" "}
                <Badge
                  bg={PAYMENT_STATUS_CLASSES[reservation.payment.status]}
                  aria-label={`Payment status: ${reservation.payment.status}`}
                >
                  {reservation.payment.status.charAt(0).toUpperCase() +
                    reservation.payment.status.slice(1)}
                </Badge>
              </p>
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-6">
          <div className="d-flex mb-1">
            <MapPin className="w-5 h-5" />
            <h6 className="ms-1 mb-0">Table Assignment</h6>
          </div>
          <Card>
            <Card.Body>
              <p className="mb-1">
                <strong>Table:</strong> {reservation.table.tableNumber}
              </p>
              <p className="mb-1">
                <strong>Location:</strong> {reservation.table.location}
              </p>
            </Card.Body>
          </Card>
        </div>
      </div>

      <div className="row mb-4">
        <div className="col-md-12">
          <div className="d-flex mb-1">
            <FileText className="w-5 h-5" />
            <h6 className="ms-1 mb-0">Special Requests</h6>
          </div>
          <Card>
            <Card.Body>
              {reservation.notes || "No special requests provided."}
            </Card.Body>
          </Card>
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
                <td>{item.specialInstructions || "-"}</td>
                <td className="text-end">
                  ${(item.quantity * item.product.unitPrice).toFixed(2)}
                </td>
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
              
      {(renderActions) && (
        <div className="d-flex justify-content-end mt-3">
          <Button
            variant="danger"
            onClick={onHandleCancel}
            disabled={processLoading}
          >
            Cancel Reservation
          </Button>
        </div>
      )}
    </div>
  );
}
