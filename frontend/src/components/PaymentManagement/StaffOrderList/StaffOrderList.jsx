import { Badge } from 'react-bootstrap';
import styles from './StaffOrderList.module.css';
import { ORDER_TYPE_CLASSES } from '../../../constants/webConstant';

const StaffOrderList = ({ payments = [], onProceedToCheckout }) => {
  if (!payments || payments.length === 0) {
    return (
      <div className={`alert alert-info mt-4 text-center ${styles.noOrdersMessage}`} role="alert">
        No unpaid payments found.
      </div>
    );
  }

  return (
    <div className={`card mt-4 ${styles.orderListCard}`}>
      <div className={`card-header ${styles.cardHeader}`}>
        <h5 className="mb-0">Unpaid Payment Overview</h5>
      </div>
      <div className="card-body p-0">
        <div className="table-responsive">
          <table className={`table table-hover mb-0 ${styles.orderTable}`}>
            <thead className={styles.tableHeader}>
              <tr>
                <th scope="col">Payment ID</th>
                <th scope="col">Table</th>
                <th scope="col">Time Placed</th>
                <th scope="col">Total Due</th>
                <th scope="col">Type</th>
                <th scope="col">Status</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
              {payments.map((payment, index) => (
                <tr key={index} className={styles.tableRow}>
                  <td>{payment.paymentId}</td>
                  <td>{payment?.order?.waitList?.table.tableNumber || 'N/A'}</td>
                  <td>{new Date(payment.paymentDate).toLocaleString()}</td>
                  <td className={styles.totalAmount}>${payment.amount.toFixed(2)}</td>
                  <td><Badge bg={ORDER_TYPE_CLASSES[payment.order.type]}>{payment.order.type}</Badge></td>
                  <td>
                    <span className={`badge bg-warning text-dark ${styles.statusBadge}`}>
                      {payment.status}
                    </span>
                  </td>
                  <td>
                    <button
                      className={`btn btn-sm btn-success ${styles.checkoutButton}`}
                      onClick={() => onProceedToCheckout(payment.paymentId)}
                    >
                      Proceed to Checkout
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default StaffOrderList;