import { useEffect } from "react";
import styles from "./OrderModal.module.css";
import { Badge } from "react-bootstrap";
import {
  ORDER_STATUS_CLASSES,
  ORDER_TYPE_CLASSES,
  ORDER_TYPES,
  TABLE_STATUS_CLASSES,
} from "../../constants/webConstant";

export default function OrderModal({ order, renderOrderActions = false, show, onClose, onCompleteOrder }) {
  useEffect(() => {
    if (show) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "unset";
    }

    return () => {
      document.body.style.overflow = "unset";
    };
  }, [show]);

  if (!show) return null;

  return (
    <div
      className={`modal fade show ${styles.modalOverlay}`}
      style={{ display: "block" }}
    >
      <div className="modal-dialog modal-lg modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">Order Details - {order.orderId}</h5>
            <button
              type="button"
              className="btn-close"
              onClick={onClose}
              aria-label="Close"
            ></button>
          </div>

          <div className="modal-body">
            <div className={`row ${styles.equalHeightRow}`}>
              {/* Order Information */}
              <div className="col-md-6">
                <div className={styles.infoSection}>
                  <h6 className="fw-bold mb-3">Order Information</h6>
                  <div className="mb-2">
                    <strong>Order ID:</strong> {order.orderId}
                  </div>
                  <div className="mb-2">
                    <strong>Date:</strong>{" "}
                    {new Date(order.orderDate).toLocaleDateString()}
                  </div>
                  <div className="mb-2">
                    <strong>Status:</strong>{" "}
                    <Badge
                      bg={
                        ORDER_STATUS_CLASSES[order.status] ||
                        ORDER_STATUS_CLASSES.DEFAULT
                      }
                    >
                      {order.status}
                    </Badge>
                  </div>
                  <div className="mb-2">
                    <strong>Type:</strong>{" "}
                    <Badge
                      bg={
                        ORDER_TYPE_CLASSES[order.type] ||
                        ORDER_TYPE_CLASSES.DEFAULT
                      }
                    >
                      {order.type}
                    </Badge>
                  </div>
                </div>
              </div>

              <div className="col-md-6">
                <div className={styles.infoSection}>
                  <h6 className="fw-bold mb-3">Customer Information</h6>
                  <div className="mb-2">
                    <strong>Name:</strong>{" "}
                    {order?.customer?.contactName ||
                      order?.tableOccupancy?.contactName ||
                      "UNKNOWN"}
                  </div>
                  <div className="mb-2">
                    <strong>Email:</strong>{" "}
                    {order?.customer?.profile?.user?.email || "N/A"}
                  </div>
                  <div className="mb-2">
                    {order.type === ORDER_TYPES.TAKE_AWAY && (
                      <div className={styles.compactInfo}>
                        <strong>Shipping Address:</strong>
                        <div className="text-muted">
                          {order?.shippingAddress?.shipName}
                          <br />
                          {order?.shippingAddress?.shipAddress},{" "}
                          {order?.shippingAddress?.shipWard},{" "}
                          {order?.shippingAddress?.shipDistrict},{" "}
                          {order?.shippingAddress?.shipCity}
                          <br />
                          Phone: {order?.shippingAddress?.phone}
                        </div>
                      </div>
                    )}
                    {order.type === ORDER_TYPES.DINE_IN && (
                      <div className={styles.compactInfo}>
                        <strong>Table Information:</strong>
                        <div className="text-muted">
                          Name: {order?.tableOccupancy?.contactName}
                          <br />
                          Phone: {order?.tableOccupancy?.phone}
                          <br />
                          Table: {
                            order?.tableOccupancy?.table?.tableNumber
                          } - {order?.tableOccupancy?.table?.location}
                          <br />
                          Party Size: {order?.tableOccupancy?.partySize}
                          <br />
                          Status:{" "}
                          <Badge
                            bg={
                              TABLE_STATUS_CLASSES[
                                order?.tableOccupancy?.status
                              ] || TABLE_STATUS_CLASSES.DEFAULT
                            }
                          >
                            {order?.tableOccupancy?.status}
                          </Badge>
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </div>

            {/* Order Items */}
            <div className="mt-4">
              <h6 className="fw-bold mb-3">Order Items</h6>
              <div className="table-responsive">
                <table className="table table-sm">
                  <thead className="table-light">
                    <tr>
                      <th>Item</th>
                      <th>Quantity</th>
                      <th>Price</th>
                      <th>Subtotal</th>
                    </tr>
                  </thead>
                  <tbody>
                    {order.orderDetails.map((item, index) => (
                      <tr key={index}>
                        <td>{item.product.productName}</td>
                        <td>{item.quantity}</td>
                        <td>${item.unitPrice.toFixed(2)}</td>
                        <td>${(item.quantity * item.unitPrice).toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                  <tfoot>
                    <tr className="table-light">
                      <th colSpan={3}>Total</th>
                      <th>${order.totalPrice.toFixed(2)}</th>
                    </tr>
                  </tfoot>
                </table>
              </div>
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
            {renderOrderActions && (
              <button type="button" className="btn btn-primary" onClick={() => onCompleteOrder?.(order)}>
                Complete Order
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
