import React from 'react';
import { Card, Alert } from 'react-bootstrap';
import styles from './Notifications.module.css';

const notifications = [
  { message: 'Low stock: Tomatoes', variant: 'warning' },
  { message: 'Delayed order #005', variant: 'danger' },
  { message: 'New customer feedback', variant: 'info' },
];

const Notifications = () => {
  return (
    <Card className={styles.card}>
      <Card.Body>
        <Card.Title className={styles.title}>Notifications</Card.Title>
        {notifications.map((notif, index) => (
          <Alert key={index} variant={notif.variant} dismissible className={styles.alert}>
            {notif.message}
          </Alert>
        ))}
      </Card.Body>
    </Card>
  );
};

export default Notifications;