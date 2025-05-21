import React from 'react';
import styles from './PaymentSuccess.module.css';
import { Link } from 'react-router-dom';
import { HOME_URI } from '../../../constants/routes';

function PaymentSuccess() {
  return (
    <div className={`${styles.card} ${styles['success-card']}`}>
      <div className={`${styles['icon-container']} ${styles['success-icon']}`}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className={`${styles['icon-svg']} ${styles['animate-bounce-in']}`}
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth="2"
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      </div>
      <h2 className={`${styles['heading-primary']} ${styles['success-heading']}`}>Payment Successful!</h2>
      <p className={styles['status-paragraph']}>
        Your transaction has been completed successfully. Thank you for your purchase!
      </p>
      <Link
        to={HOME_URI}
        className={`${styles.btn} ${styles['btn-blue']}`}
      >
        Go Back
      </Link>
    </div>
  );
}

export default PaymentSuccess;
