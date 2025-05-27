// src/components/ReceiptRequest/ReceiptRequest.js
import React from 'react';
import styles from './ReceiptRequest.module.css';

const ReceiptRequest = ({ receiptContact, setReceiptContact }) => {
  const handleEmailChange = (e) => {
    setReceiptContact(prev => ({ ...prev, email: e.target.value }));
  };

  const handleSmsChange = (e) => {
    setReceiptContact(prev => ({ ...prev, sms: e.target.value }));
  };

  return (
    <div className={`card mb-4 ${styles.receiptRequestCard}`}>
      <div className="card-header ${styles.cardHeader}">
        <h5 className="mb-0">Digital Receipt</h5>
      </div>
      <div className="card-body">
        <p className="text-muted">Would you like a digital receipt?</p>
        <div className="form-group mb-3">
          <label htmlFor="emailInput" className="form-label">Email Address:</label>
          <input
            type="email"
            className="form-control"
            id="emailInput"
            placeholder="name@example.com"
            value={receiptContact.email}
            onChange={handleEmailChange}
          />
        </div>
        <div className="form-group">
          <label htmlFor="smsInput" className="form-label">SMS Number:</label>
          <input
            type="tel"
            className="form-control"
            id="smsInput"
            placeholder="e.g., +15551234567"
            value={receiptContact.sms}
            onChange={handleSmsChange}
          />
          <small className="form-text text-muted">Include country code for SMS.</small>
        </div>
      </div>
    </div>
  );
};

export default ReceiptRequest;