import React from 'react';
import { Card, ButtonGroup, Button } from 'react-bootstrap';
import styles from './QuickActions.module.css';

const actions = [
  { label: 'Add Menu Item', icon: 'fas fa-plus' },
  { label: 'Update Inventory', icon: 'fas fa-box' },
  { label: 'Assign Table', icon: 'fas fa-chair' },
  { label: 'Manage Staff', icon: 'fas fa-users' },
  { label: 'Generate Report', icon: 'fas fa-chart-line' },
];

const QuickActions = () => {
  return (
    <Card className={styles.card}>
      <Card.Body>
        <Card.Title className={styles.title}>Quick Actions</Card.Title>
        <ButtonGroup className={styles.buttonGroup}>
          {actions.map((action, index) => (
            <Button key={index} variant="outline-primary" className={styles.button}>
              <i className={`${action.icon} me-2`}></i>
              {action.label}
            </Button>
          ))}
        </ButtonGroup>
      </Card.Body>
    </Card>
  );
};

export default QuickActions;