import React from 'react';
import styles from './PaymentError.module.css'; 

function PaymentError() {
  return (
    <div className={`${styles.card} ${styles['error-card']}`}>
      <div className={`${styles['icon-container']} ${styles['error-icon']}`}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className={`${styles['icon-svg']} ${styles['animate-shake']}`}
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth="2"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
      </div>
      <h2 className={`${styles['heading-primary']} ${styles['error-heading']}`}>Payment Failed</h2>
      <p className={styles['status-paragraph']}>
        There was an issue processing your payment. Please try again or contact support.
      </p>
      <button
        onClick={() => (window.location.hash = '')}
        className={`${styles.btn} ${styles['btn-blue']}`}
      >
        Try Again
      </button>
    </div>
  );
}

export default PaymentError;
