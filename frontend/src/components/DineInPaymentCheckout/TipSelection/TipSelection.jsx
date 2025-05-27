// src/components/TipSelection/TipSelection.js
import React, { useState } from 'react';
import styles from './TipSelection.module.css';

const TipSelection = ({ selectedTip, setSelectedTip }) => {
  const [customTip, setCustomTip] = useState('');

  const handlePresetTip = (percentage) => {
    setSelectedTip(percentage);
    setCustomTip('');
  };

  const handleCustomTipChange = (e) => {
    const value = e.target.value;
    setCustomTip(value);
    if (value === '' || isNaN(value)) {
      setSelectedTip(0);
    } else {
      setSelectedTip(parseFloat(value));
    }
  };

  return (
    <div className={`card mb-4 ${styles.tipSelectionCard}`}>
      <div className="card-header ${styles.cardHeader}">
        <h5 className="mb-0">Add a Tip</h5>
      </div>
      <div className="card-body">
        <div className="d-flex justify-content-around mb-3">
          {[10, 15, 20].map(percentage => (
            <button
              key={percentage}
              className={`btn ${selectedTip === percentage ? 'btn-success' : 'btn-outline-success'} ${styles.tipButton}`}
              onClick={() => handlePresetTip(percentage)}
            >
              {percentage}%
            </button>
          ))}
        </div>
        <div className="form-group">
          <label htmlFor="customTipInput" className="form-label">Custom Tip (%):</label>
          <input
            type="number"
            id="customTipInput"
            className={`form-control ${styles.customTipInput}`}
            placeholder="Enter custom %"
            value={customTip}
            onChange={handleCustomTipChange}
            min="0"
          />
        </div>
      </div>
    </div>
  );
};

export default TipSelection;