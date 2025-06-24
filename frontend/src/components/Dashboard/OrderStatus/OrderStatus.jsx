import { Badge, Card, Table } from "react-bootstrap";
import styles from "./OrderStatus.module.css";
import { formatDate } from "../../../utils/datetimeUtils";
import {
  ORDER_STATUS_CLASSES,
  ORDER_TYPE_CLASSES,
} from "../../../constants/webConstant";

const OrderStatus = ({ orders }) => {
  return (
    <Card className={styles.card}>
      <Card.Body>
        <Card.Title className={styles.title}>Orders</Card.Title>
        <Table hover className={styles.table}>
          <thead>
            <tr>
              <th>Customer</th>
              <th>Date</th>
              <th>Type</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order, index) => (
              <tr key={index}>
                <td>
                  {order?.customer?.contactName ||
                    order?.tableOccupancy?.contactName ||
                    "UNKNOW"}
                </td>
                <td>{formatDate(order.orderDate)}</td>
                <td>
                  <Badge bg={ORDER_TYPE_CLASSES[order.type]}>
                    {order.type}
                  </Badge>
                </td>
                <td>
                  <Badge bg={ORDER_STATUS_CLASSES[order.status]}>
                    {order.status}
                  </Badge>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Card.Body>
    </Card>
  );
};

export default OrderStatus;
