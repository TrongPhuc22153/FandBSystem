// src/components/StaffSearchForm/StaffSearchForm.js
import React, { useState } from 'react';
import styles from './StaffSearchForm.module.css';

const StaffSearchForm = ({ onSearch, onClearSearch, isSearching }) => {
  const [searchCriteria, setSearchCriteria] = useState({
    tableNumber: '',
    phoneNumber: '',
    orderCode: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSearchCriteria(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSearch(searchCriteria);
  };

  const handleClear = () => {
    setSearchCriteria({ tableNumber: '', phoneNumber: '', orderCode: '' });
    onClearSearch();
  };

  // Disable search button if all fields are empty AND not currently searching
  const isSearchButtonDisabled = isSearching || (
    !searchCriteria.tableNumber &&
    !searchCriteria.phoneNumber &&
    !searchCriteria.orderCode
  );

  return (
    <div className={`card mb-4 ${styles.searchFormCard}`}>
      <div className={`card-header ${styles.cardHeader}`}>
        <h5 className="mb-0">Search Orders</h5>
      </div>
      <div className="card-body">
        <form onSubmit={handleSubmit}>
          <div className="row g-3 mb-3">
            <div className="col-md-4">
              <label htmlFor="tableNumber" className="form-label">Table Number:</label>
              <input
                type="text"
                className={`form-control ${styles.inputField}`}
                id="tableNumber"
                name="tableNumber"
                value={searchCriteria.tableNumber}
                onChange={handleChange}
                placeholder="e.g., 5"
                disabled={isSearching}
              />
            </div>
            <div className="col-md-4">
              <label htmlFor="phoneNumber" className="form-label">Phone Number:</label>
              <input
                type="tel"
                className={`form-control ${styles.inputField}`}
                id="phoneNumber"
                name="phoneNumber"
                value={searchCriteria.phoneNumber}
                onChange={handleChange}
                placeholder="e.g., 1234567890"
                disabled={isSearching}
              />
            </div>
            <div className="col-md-4">
              <label htmlFor="orderCode" className="form-label">Order Code:</label>
              <input
                type="text"
                className={`form-control ${styles.inputField}`}
                id="orderCode"
                name="orderCode"
                value={searchCriteria.orderCode}
                onChange={handleChange}
                placeholder="e.g., ORD1001"
                disabled={isSearching}
              />
            </div>
          </div>
          <div className="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
            <button
              type="button"
              className={`btn btn-outline-secondary ${styles.clearButton}`}
              onClick={handleClear}
              disabled={isSearching}
            >
              Clear Search
            </button>
            <button
              type="submit"
              className={`btn btn-primary ${styles.searchButton}`}
              disabled={isSearchButtonDisabled}
            >
              {isSearching ? (
                <>
                  <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                  Searching...
                </>
              ) : (
                'Search Orders'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default StaffSearchForm;