import { Badge } from "react-bootstrap";
import { ORDER_ACTIONS, ORDER_ITEM_STATUS_CLASSES, ORDER_STATUS_CLASSES, ORDER_STATUSES } from "../../constants/webConstant";

export default function OrderDetailModal({ order, onClose, onUpdateStatus }) {
  if (!order) return null;

  const getNextAction = (currentStatus) => {
    switch (currentStatus.toUpperCase()) {
      case ORDER_STATUSES.PENDING:
        return ORDER_ACTIONS.PREPARING.toLowerCase();
      case ORDER_STATUSES.PREPARING:
        return ORDER_ACTIONS.READY.toLowerCase();
      case ORDER_STATUSES.PREPARED:
        return ORDER_ACTIONS.COMPLETE.toLowerCase();
      default:
        return null;
    }
  };

  const nextAction = getNextAction(order.status);

  const calculateTotal = (items) => {
    return items.reduce((total, item) => total + (item.quantity * item.product.unitPrice), 0);
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
              Order Details: {order.orderId} - Table {order?.table?.tableNumber}
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
                  <strong>Name:</strong> {order?.customer?.profile.user.username || order?.tableOccupancy.contactName || "UNKNOW"}
                </p>
                <p className="mb-1">
                  <strong>Table:</strong> {order?.tableOccupancy.table.tableNumber}
                </p>
                <p className="mb-1">
                  <strong>Order Time:</strong>{" "}
                  {new Date(order.orderDate).toLocaleString()}
                </p>
                <p className="mb-1">
                  <strong>Est. Completion:</strong>{" "}
                  {new Date(order.estimatedCompletionTime).toLocaleString()}
                </p>
              </div>
              <div className="col-md-6">
                <h6>Order Status</h6>
                <div className="d-flex align-items-center mb-3">
                  <Badge className={`me-2`} bg={ORDER_STATUS_CLASSES[order.status] || ORDER_STATUS_CLASSES.DEFAULT}>
                    {order.status.charAt(0).toUpperCase() +
                      order.status.slice(1)}
                  </Badge>
                  {nextAction && (
                    <button
                      className="btn btn-sm btn-primary"
                      onClick={() => onUpdateStatus(order.orderId, nextAction.toUpperCase(), order.type)}
                    >
                      Mark as{" "}
                      {nextAction.charAt(0).toUpperCase() + nextAction.slice(1)}
                    </button>
                  )}
                </div>
                <div className="progress">
                  <div
                    className={`progress-bar ${
                      order.status === ORDER_STATUSES.PENDING
                        ? "bg-danger"
                        : order.status === ORDER_STATUSES.PREPARING
                        ? "bg-warning"
                        : order.status === ORDER_STATUSES.PREPARED
                        ? "bg-success"
                        : "bg-secondary"
                    }`}
                    role="progressbar"
                    style={{
                      width:
                        order.status === ORDER_STATUSES.PENDING
                          ? "25%"
                          : order.status === ORDER_STATUSES.PREPARING
                          ? "50%"
                          : order.status === ORDER_STATUSES.PREPARED
                          ? "75%"
                          : "100%",
                    }}
                    aria-valuenow={
                      order.status === ORDER_STATUSES.PENDING
                        ? 25
                        : order.status === ORDER_STATUSES.PREPARING
                        ? 50
                        : order.status === ORDER_STATUSES.PREPARED
                        ? 75
                        : 100
                    }
                    aria-valuemin={0}
                    aria-valuemax={100}
                  ></div>
                </div>
              </div>
            </div>

            <h6>Order Items</h6>
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
                  {order.orderDetails.map((item, index) => (
                    <tr key={index}>
                      <td>
                        <div className="d-flex justify-content-between">
                          <span>{item.product?.productName} </span>
                          <Badge bg={ORDER_ITEM_STATUS_CLASSES[item.status]}>{item.status}</Badge>
                        </div>
                      </td>
                      <td>{item.quantity}</td>
                      <td>{item.specialInstructions || "-"}</td>
                      <td className="text-end">${item.quantity * item.product.unitPrice}</td>
                    </tr>
                  ))}
                  <tr className="table-active">
                    <td colSpan={3} className="text-end">
                      <strong>Total:</strong>
                    </td>
                    <td className="text-end">
                      <strong>${calculateTotal(order.orderDetails).toFixed(2)}</strong>
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
