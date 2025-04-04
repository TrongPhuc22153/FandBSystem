import config from "../../../config/WebConfig";

const OrderNotificationsTable = ({ orderNotifications }) => {
  return (
    <div className="card flex-fill">
      <div className="card-header">
        <h5 className="card-title mb-0">Order Notifications</h5>
      </div>
      <table className="table table-hover my-0">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Customer</th>
            <th>Status</th>
            <th>Time</th>
          </tr>
        </thead>
        <tbody>
          {orderNotifications.map((order, index) => (
            <tr key={index}>
              <td>{order.id}</td>
              <td>{order.customer}</td>
              <td>
                <span
                  className={
                    config.order_status.classes[order.status] ||
                    "badge bg-secondary"
                  }
                >
                  {order.status || "Unknown"}
                </span>
              </td>
              <td>{order.time}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default OrderNotificationsTable;
