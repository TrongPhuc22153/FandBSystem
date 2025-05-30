import styles from './ConfirmPaymentButton.module.css';

const ConfirmPaymentButton = ({ onConfirm, isProcessing }) => {
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
            Confirm Payment
          </>
        )}
      </button>
    </div>
  );
};

export default ConfirmPaymentButton;