import styles from './PaymentOptions.module.css';

const PaymentOptions = ({ paymentMethods, selectedPaymentMethod, setSelectedPaymentMethod }) => {
  return (
    <div className={`card mb-4 ${styles.paymentOptionsCard}`}>
      <div className="card-header ${styles.cardHeader}">
        <h5 className="mb-0">Payment Options</h5>
      </div>
      <div className="card-body">
        {paymentMethods.map(method => (
          <div key={method.methodId} className="form-check mb-2">
            <input
              className={`form-check-input ${styles.radioInput}`}
              type="radio"
              name="paymentMethod"
              id={`payment-${method.methodId}`}
              value={method.methodName}
              checked={selectedPaymentMethod === method.methodName}
              onChange={() => setSelectedPaymentMethod(method.methodName)}
            />
            <label className={`form-check-label ${styles.radioLabel}`} htmlFor={`payment-${method.methodId}`}>
              <span className="me-2">{method.icon}</span> {method.methodName}
            </label>
          </div>
        ))}
      </div>
    </div>
  );
};

export default PaymentOptions;