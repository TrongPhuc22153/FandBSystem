import { ORDER_ITEM_STATUSES, ORDER_ITEM_STATUS_CLASSES } from "../../constants/webConstant";
import { Badge, Button } from "react-bootstrap";
import styles from "./OrderItemDataTable.module.css";
import { CheckCircle } from "lucide-react";

const calculateTotal = (orderDetails) =>
  orderDetails.reduce(
    (sum, item) =>
      item.status !== ORDER_ITEM_STATUSES.CANCELED
        ? sum + item.quantity * item.product.unitPrice
        : sum,
    0
  );

export function OrderItemsDataTable({ order, isLoading, onCancelOrderItem, onServeOrderItem }) {
  return (
    <div>
      <h4 className={styles.sectionTitle}>Order Items</h4>
      {isLoading ? (
        <div className={styles.loading}>Loading order...</div>
      ) : order && order.orderDetails?.length > 0 ? (
        <div className="table-responsive">
          <table className="table table-striped" aria-label="Order items">
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
              {order.orderDetails.map((item, index) => (
                <tr key={item.id || index}>
                  <td>
                    <div className={styles.itemContainer}>
                      <span>{item.product?.productName}</span>
                      <div className={styles.statusContainer}>
                        {item.status === ORDER_ITEM_STATUSES.SERVED && (
                          <CheckCircle size={16} className={styles.servedIcon} aria-label="Served" />
                        )}
                        <Badge bg={ORDER_ITEM_STATUS_CLASSES[item.status]}>
                          {item.status}
                        </Badge>
                      </div>
                    </div>
                  </td>
                  <td>{item.quantity}</td>
                  <td>{item.specialInstructions || '-'}</td>
                  <td className="text-end">
                    ${item.status !== ORDER_ITEM_STATUSES.CANCELED
                      ? (item.quantity * item.product.unitPrice).toFixed(2)
                      : '0.00'}
                  </td>
                  <td>
                    <div className={styles.actionButtons}>
                      {item.status === ORDER_ITEM_STATUSES.PREPARED && (
                        <Button
                          variant="outline-success"
                          size="sm"
                          onClick={() => onServeOrderItem?.(order.orderId, item.id)}
                          className={styles.actionButton}
                          aria-label={`Mark ${item.product?.productName} as ready to serve`}
                        >
                          Serve
                        </Button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
              <tr className="table-active">
                <td colSpan={4} className="text-end">
                  <strong>Total:</strong>
                </td>
                <td className="text-end">
                  <strong>${calculateTotal(order.orderDetails).toFixed(2)}</strong>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      ) : (
        <div className={styles.emptyOrder}>No active order items</div>
      )}
    </div>
  );
}