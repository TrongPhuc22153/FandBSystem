import { Card, Table } from 'react-bootstrap';
import styles from './Reservations.module.css';
import { formatTime } from '../../../utils/datetimeUtils';

const Reservations = ({ reservations }) => {
  return (
    <Card className={styles.card}>
      <Card.Body>
        <Card.Title className={styles.title}>Upcoming Reservations</Card.Title>
        <Table hover className={styles.table}>
          <thead>
            <tr>
              <th>Customer</th>
              <th>Date</th>
              <th>Start time</th>
              <th>End time</th>
              <th>Party Size</th>
            </tr>
          </thead>
          <tbody>
            {reservations.map((res, index) => (
              <tr key={index}>
                <td>{res?.customer?.contactName || "UNKNOW"}</td>
                <td>{res.date}</td>
                <td>{formatTime(res.startTime)}</td>
                <td>{formatTime(res.endTime)}</td>
                <td>{res.numberOfGuests}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Card.Body>
    </Card>
  );
};

export default Reservations;