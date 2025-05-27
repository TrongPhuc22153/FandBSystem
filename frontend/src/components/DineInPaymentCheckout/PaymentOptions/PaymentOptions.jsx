// src/components/PaymentOptions/PaymentOptions.js
import React from 'react';
import styles from './PaymentOptions.module.css';

const PaymentOptions = ({ selectedPaymentMethod, setSelectedPaymentMethod }) => {
  const paymentMethods = [
    { id: 'card', name: 'Credit/Debit Card', icon: '💳' },
    { id: 'mobile', name: 'Mobile Wallet (Apple Pay/Google Pay)', icon: '📱' },
    { id: 'cash', name: 'Cash', icon: '💵' },
  ];

  return (
    <div className={`card mb-4 ${styles.paymentOptionsCard}`}>
      <div className="card-header ${styles.cardHeader}">
        <h5 className="mb-0">Payment Options</h5>
      </div>
      <div className="card-body">
        {paymentMethods.map(method => (
          <div key={method.id} className="form-check mb-2">
            <input
              className={`form-check-input ${styles.radioInput}`}
              type="radio"
              name="paymentMethod"
              id={`payment-${method.id}`}
              value={method.id}
              checked={selectedPaymentMethod === method.id}
              onChange={() => setSelectedPaymentMethod(method.id)}
            />
            <label className={`form-check-label ${styles.radioLabel}`} htmlFor={`payment-${method.id}`}>
              <span className="me-2">{method.icon}</span> {method.name}
            </label>
          </div>
        ))}
        {selectedPaymentMethod === 'card' && (
          <div className="alert alert-info mt-3" role="alert">
            Please present your card to the terminal.
          </div>
        )}
        {selectedPaymentMethod === 'mobile' && (
          <div className="alert alert-info mt-3" role="alert">
            Please use your mobile device to tap and pay.
          </div>
        )}
        {selectedPaymentMethod === 'cash' && (
          <div className="alert alert-info mt-3" role="alert">
            Please hand the cash to your server.
          </div>
        )}
      </div>
    </div>
  );
};

export default PaymentOptions;