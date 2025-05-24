import React from 'react';
import styles from './PaymentCancelled.module.css';
import { Link } from 'react-router-dom';
import { HOME_URI } from '../../../constants/routes';

function PaymentCancelled() {
  return (
    <div className={`${styles.card} ${styles['cancelled-card']}`}>
      <div className={`${styles['icon-container']} ${styles['cancelled-icon']}`}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className={`${styles['icon-svg']} ${styles['animate-pulse-slow']}`}
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth="2"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
      </div>
      <h2 className={`${styles['heading-primary']} ${styles['cancelled-heading']}`}>Payment Cancelled</h2>
      <p className={styles['status-paragraph']}>
        You have cancelled the payment process. You can try again if you wish.
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

export default PaymentCancelled;
