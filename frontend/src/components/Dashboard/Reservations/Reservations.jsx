import { Badge, Card, Table } from "react-bootstrap";
import styles from "./Reservations.module.css";
import { formatTime } from "../../../utils/datetimeUtils";
import { RESERVATION_STATUS_CLASSES } from "../../../constants/webConstant";

const Reservations = ({ reservations }) => {
  return (
    <Card className={styles.card}>
      <Card.Body>
        <Card.Title className={styles.title}>Reservations</Card.Title>
        <Table hover className={styles.table}>
          <thead>
            <tr>
              <th>Customer</th>
              <th>Date</th>
              <th>Start</th>
              <th>End</th>
              <th>Party</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {reservations.map((res, index) => (
              <tr key={index}>
                <td>{res?.customer?.contactName || "UNKNOW"}</td>
                <td>{new Date(res.date).toLocaleDateString()}</td>
                <td>{formatTime(res.startTime)}</td>
                <td>{formatTime(res.endTime)}</td>
                <td>{res.numberOfGuests}</td>
                <td>
                  <Badge
                    bg={
                      RESERVATION_STATUS_CLASSES[res.status] ||
                      RESERVATION_STATUS_CLASSES.DEFAULT
                    }
                  >
                    {res.status}
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

export default Reservations;
