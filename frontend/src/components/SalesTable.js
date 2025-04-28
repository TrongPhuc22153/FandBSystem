import config from "../config/WebConfig";

const SalesTable = ({ salesData }) => {
  return (
    <div className="card flex-fill">
      <div className="card-header">
        <h5 className="card-title mb-0">Latest Orders</h5>
      </div>
      <table className="table table-hover my-0">
        <thead>
          <tr>
            <th>Name</th>
            <th className="d-none d-xl-table-cell">Date</th>
            <th>Status</th>
            <th>Total Price</th>
          </tr>
        </thead>
        <tbody>
          {salesData.map((sale, index) => (
            <tr key={index}>
              <td>{sale.name}</td>
              <td className="d-none d-xl-table-cell">{sale.date}</td>
              <td>
                <span
                  className={`badge ${
                    config.order_status.classes[sale.status] ||
                    "badge bg-secondary"
                  }`}
                >
                  {sale.status}
                </span>
              </td>
              <td>{sale.total_price}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default SalesTable;
