import { ORDER_STATUS_CLASSES, ORDER_STATUSES } from "../../../constants/webConstant";

export default function OrderProcessingBar({ status, orderId, type, nextAction, onUpdateStatus }) {
  return (
    <>
      <div className="d-flex align-items-center mb-3">
        <Badge
          className={`me-2`}
          bg={
            ORDER_STATUS_CLASSES[status] || ORDER_STATUS_CLASSES.DEFAULT
          }
        >
          {status.charAt(0).toUpperCase() + status.slice(1)}
        </Badge>
        {nextAction && (
          <button
            className="btn btn-sm btn-primary"
            onClick={() =>
              onUpdateStatus(
                orderId,
                nextAction.toUpperCase(),
                type
              )
            }
          >
            Mark as {nextAction.charAt(0).toUpperCase() + nextAction.slice(1)}
          </button>
        )}
      </div>
      <div className="progress">
        <div
          className={`progress-bar ${
            status === ORDER_STATUSES.PENDING
              ? "bg-danger"
              : status === ORDER_STATUSES.PREPARING
              ? "bg-warning"
              : status === ORDER_STATUSES.PREPARED
              ? "bg-success"
              : "bg-secondary"
          }`}
          role="progressbar"
          style={{
            width:
              status === ORDER_STATUSES.PENDING
                ? "25%"
                : status === ORDER_STATUSES.PREPARING
                ? "50%"
                : status === ORDER_STATUSES.PREPARED
                ? "75%"
                : "100%",
          }}
          aria-valuenow={
            status === ORDER_STATUSES.PENDING
              ? 25
              : status === ORDER_STATUSES.PREPARING
              ? 50
              : status === ORDER_STATUSES.PREPARED
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
