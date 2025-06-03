import React from 'react';
import { Card, Badge } from 'react-bootstrap';
import styles from './StaffOverview.module.css';

const staff = [
  { name: 'Alice Brown', role: 'Chef', shift: '9 AM - 5 PM' },
  { name: 'Bob White', role: 'Server', shift: '10 AM - 6 PM', late: true },
];

const StaffOverview = () => {
  return (
    <Card className={styles.card}>
      <Card.Body>
        <Card.Title className={styles.title}>Staff Overview</Card.Title>
        {staff.map((member, index) => (
          <div key={index} className={styles.staffRow}>
            <div>
              <span className={styles.name}>{member.name}</span>
              <Badge bg={member.role === 'Chef' ? 'primary' : 'secondary'} className={styles.badge}>
                {member.role}
              </Badge>
              {member.late && <Badge bg="danger" className={styles.badge}>Late</Badge>}
            </div>
            <span className={styles.shift}>{member.shift}</span>
          </div>
        ))}
      </Card.Body>
    </Card>
  );
};

export default StaffOverview;