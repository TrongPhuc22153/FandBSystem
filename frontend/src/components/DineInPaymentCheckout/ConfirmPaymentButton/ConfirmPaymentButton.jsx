// src/components/ConfirmPaymentButton/ConfirmPaymentButton.js
import React from 'react';
import styles from './ConfirmPaymentButton.module.css';

const ConfirmPaymentButton = ({ onConfirm, isProcessing, total }) => {
  return (
    <div className={`d-grid gap-2 ${styles.confirmButtonContainer}`}>
      <button
        className={`btn btn-lg btn-primary ${styles.confirmButton}`}
        onClick={onConfirm}
        disabled={isProcessing}
      >
        {isProcessing ? (
          <>
            <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
            Processing...
          </>
        ) : (
          <>
            Confirm Payment - ${total.toFixed(2)}
          </>
        )}
      </button>
    </div>
  );
};

export default ConfirmPaymentButton;