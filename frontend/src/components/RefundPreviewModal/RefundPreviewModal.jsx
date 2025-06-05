import { Modal, Button, Badge } from "react-bootstrap";

export default function RefundPreviewModal({ show, onHide, preview, onConfirm }) {
  if (!preview) return null;

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Refund Preview</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>
          <strong>Refund Amount:</strong>{" "}
          <span className="text-success">
            ${preview.refundAmount} ({(preview.refundPercentage * 100).toFixed(0)}%)
          </span>
        </p>
        <p>
          <strong>Status:</strong>{" "}
          <Badge bg="info">{preview.status}</Badge>
        </p>
        <p>
          <strong>Payment Method:</strong> {preview.paymentMethod.toUpperCase()}
        </p>
        <p>
          <strong>Payment Status:</strong>{" "}
          <Badge bg={"secondary"}>{preview.paymentStatus}</Badge>
        </p>
        <p>
          <strong>Reason:</strong> {preview.reason}
        </p>
        <p className="text-muted">{preview.refundPolicyNote}</p>
        {!preview.eligible && (
          <div className="alert alert-warning">
            Refund is not eligible for this reservation.
          </div>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Close
        </Button>
        {preview.eligible && (
          <Button variant="danger" onClick={onConfirm}>
            Confirm Cancellation & Refund
          </Button>
        )}
      </Modal.Footer>
    </Modal>
  );
}
