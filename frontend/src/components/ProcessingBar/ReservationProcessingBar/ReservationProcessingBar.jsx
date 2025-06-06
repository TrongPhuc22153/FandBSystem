import { Badge } from "react-bootstrap";
import { RESERVATION_STATUS_CLASSES, RESERVATION_STATUSES } from "../../../constants/webConstant";

export default function ReservationProcessingBar({ status }) {
  return (
    <>
      <div className="d-flex align-items-center mb-3">
        <Badge
          className="me-2"
          bg={
            RESERVATION_STATUS_CLASSES[status] ||
            RESERVATION_STATUS_CLASSES.DEFAULT
          }
        >
          {status.charAt(0).toUpperCase() +
            status.slice(1)}
        </Badge>
      </div>

      <div className="progress">
        <div
          className={`progress-bar ${
            status === RESERVATION_STATUSES.PENDING
              ? "bg-danger"
              : status === RESERVATION_STATUSES.PREPARING
              ? "bg-warning"
              : status === RESERVATION_STATUSES.PREPARED
              ? "bg-success"
              : "bg-secondary"
          }`}
          role="progressbar"
          style={{
            width:
              status === RESERVATION_STATUSES.PENDING
                ? "25%"
                : status === RESERVATION_STATUSES.PREPARING
                ? "50%"
                : status === RESERVATION_STATUSES.PREPARED
                ? "75%"
                : "100%",
          }}
          aria-valuenow={
            status === RESERVATION_STATUSES.PENDING
              ? 25
              : status === RESERVATION_STATUSES.PREPARING
              ? 50
              : status === RESERVATION_STATUSES.PREPARED
              ? 75
              : 100
          }
          aria-valuemin={0}
          aria-valuemax={100}
        ></div>
      </div>
    </>
  );
}
