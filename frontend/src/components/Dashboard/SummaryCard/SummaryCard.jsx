import { Card } from 'react-bootstrap';
import styles from './SummaryCard.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const SummaryCard = ({ title, value, icon, bgColor }) => {
  return (
    <Card className={styles.card} style={{ backgroundColor: bgColor }}>
      <Card.Body className={styles.body}>
        <FontAwesomeIcon className={`${icon} ${styles.icon}`} icon={icon}/>
        <h5 className={styles.value}>{value}</h5>
        <Card.Title className={styles.title}>{title}</Card.Title>
      </Card.Body>
    </Card>
  );
};

export default SummaryCard;