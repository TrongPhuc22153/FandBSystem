import { Card, Button, OverlayTrigger, Tooltip } from 'react-bootstrap';
import styles from './TableMap.module.css';
import { TABLE_STATUSES } from '../../../constants/webConstant';

const TableMap = ({ tables }) => {
  const getStatusClass = (status) => {
    switch (status) {
      case TABLE_STATUSES.UNOCCUPIED:
        return styles.free;
      case TABLE_STATUSES.OCCUPIED:
        return styles.occupied;
      case TABLE_STATUSES.RESERVED:
        return styles.reserved;
      case TABLE_STATUSES.CLEANING:
        return styles.cleaning;
      default:
        return '';
    }
  };

  return (
    <Card className={styles.card}>
      <Card.Body>
        <Card.Title className={styles.title}>Table Map</Card.Title>
        <div className={styles.tableGrid}>
          {tables.map((table) => (
            <OverlayTrigger
              key={table.tableId}
              placement="top"
              overlay={
                <Tooltip>
                  {table.customer ? `Seated: ${table.occupiedAt}` : `Table ${table.tableNumber}: ${table.status}`}
                </Tooltip>
              }
            >
              <Button className={`${styles.tableButton} ${getStatusClass(table.status)}`}>
                Table {table.tableNumber}
              </Button>
            </OverlayTrigger>
          ))}
        </div>
      </Card.Body>
    </Card>
  );
};

export default TableMap;