// src/components/BillSplitting/BillSplitting.js
import styles from "./BillingSplitting.module.css";

const BillSplitting = ({
  billSplitOption,
  setBillSplitOption,
  numPeopleSplit,
  setNumPeopleSplit,
  totalAmount,
}) => {
  const handleNumPeopleChange = (e) => {
    const value = parseInt(e.target.value, 10);
    if (!isNaN(value) && value >= 1) {
      setNumPeopleSplit(value);
    }
  };

  const amountPerPerson = totalAmount / numPeopleSplit;

  return (
    <div className={`card mb-4 ${styles.billSplittingCard}`}>
      <div className="card-header ${styles.cardHeader}">
        <h5 className="mb-0">Bill Splitting</h5>
      </div>
      <div className="card-body">
        <div className="form-check mb-2">
          <input
            className={`form-check-input ${styles.radioInput}`}
            type="radio"
            name="billSplit"
            id="splitNone"
            value="none"
            checked={billSplitOption === "none"}
            onChange={() => setBillSplitOption("none")}
          />
          <label
            className={`form-check-label ${styles.radioLabel}`}
            htmlFor="splitNone"
          >
            No Split (Pay Full Amount)
          </label>
        </div>
        <div className="form-check mb-3">
          <input
            className={`form-check-input ${styles.radioInput}`}
            type="radio"
            name="billSplit"
            id="splitEven"
            value="even"
            checked={billSplitOption === "even"}
            onChange={() => setBillSplitOption("even")}
          />
          <label
            className={`form-check-label ${styles.radioLabel}`}
            htmlFor="splitEven"
          >
            Even Split by Number of People
          </label>
        </div>

        {billSplitOption === "even" && (
          <div className="input-group mb-3">
            <span className="input-group-text">Number of People:</span>
            <input
              type="number"
              className="form-control"
              value={numPeopleSplit}
              onChange={handleNumPeopleChange}
              min="1"
            />
          </div>
        )}

        {billSplitOption === "even" && (
          <div className="alert alert-info mt-3" role="alert">
            Each person pays: **${amountPerPerson.toFixed(2)}**
          </div>
        )}

        {/* Manual Item-Based Split - Placeholder for future implementation */}
        <div className="form-check mb-3">
          <input
            className={`form-check-input ${styles.radioInput}`}
            type="radio"
            name="billSplit"
            id="splitManual"
            value="manual"
            checked={billSplitOption === "manual"}
            onChange={() => setBillSplitOption("manual")}
            disabled // Disable for now as it's optional
          />
          <label
            className={`form-check-label ${styles.radioLabel}`}
            htmlFor="splitManual"
          >
            Manual Item-Based Split (Coming Soon)
          </label>
        </div>
      </div>
    </div>
  );
};

export default BillSplitting;
