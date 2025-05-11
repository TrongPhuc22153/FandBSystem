import { useState, useEffect } from "react";
import styles from "./ReservationForm.module.css";
import { useReservationTables } from "../../hooks/tableHooks";
import { useAlert } from "../../context/AlertContext";

export default function ReservationDetails({
  reservationData,
  updateReservationData,
  onNext,
}) {
  const [startDate, setStartDate] = useState(
    reservationData.startDateTime.toISOString().slice(0, 16)
  );
  const [endDate, setEndDate] = useState(
    reservationData.endDateTime.toISOString().slice(0, 16)
  );
  const [formValid, setFormValid] = useState(false);

  const { data: tablesData, isLoading: tablesLoading, error: tablesError } =
    useReservationTables();
  const tables = tablesData?.content || [];

  const { showNewAlert } = useAlert();
  
  // Update end time to default (3 hours) only when startDate changes
  useEffect(() => {
    const startTime = new Date(startDate).getTime();
    const currentStartTime = reservationData.startDateTime.getTime();

    // Only proceed if startDate has changed
    if (startTime !== currentStartTime) {
      const defaultEndTime = startTime + 3 * 60 * 60 * 1000; // 3 hours
      const newEndDate = new Date(defaultEndTime).toISOString().slice(0, 16);

      // Only update endDate if endDateTime is the default (3 hours after startDateTime)
      const currentEndTime = reservationData.endDateTime.getTime();
      const defaultEndTimeForStart = currentStartTime + 3 * 60 * 60 * 1000;

      if (currentEndTime === defaultEndTimeForStart) {
        setEndDate(newEndDate);
        updateReservationData({
          startDateTime: new Date(startDate),
          endDateTime: new Date(defaultEndTime),
        });
      } else {
        updateReservationData({
          startDateTime: new Date(startDate),
        });
      }
    }
  }, [startDate, updateReservationData]);

  // Validate form
  useEffect(() => {
    const isValid =
      reservationData.numberOfGuests > 0 &&
      (reservationData.autoSelectTable || reservationData.tableSelection !== "") &&
      new Date(endDate) > new Date(startDate) &&
      (!reservationData.autoSelectTable ? tables.length > 0 : true);
    setFormValid(isValid);
  }, [
    reservationData.numberOfGuests,
    reservationData.autoSelectTable,
    reservationData.tableSelection,
    startDate,
    endDate,
    tables,
  ]);

  // Handle start date change
  const handleStartDateChange = (e) => {
    setStartDate(e.target.value);
  };

  // Handle end date change
  const handleEndDateChange = (e) => {
    const newEndDate = e.target.value;
    if (new Date(newEndDate) > new Date(startDate)) {
      setEndDate(newEndDate);
      updateReservationData({ endDateTime: new Date(newEndDate) });
    } else {
      showNewAlert({
        message: "End time must be after start time.",
        variant: "danger"
      })
    }
  };

  // Handle number of guests change
  const handleGuestsChange = (e) => {
    const value = Number.parseInt(e.target.value);
    if (value >= 1 && value <= 20) {
      updateReservationData({ numberOfGuests: value });
    }
  };

  // Handle table selection change
  const handleTableChange = (e) => {
    updateReservationData({ tableSelection: e.target.value });
  };

  // Handle auto-select table toggle
  const handleAutoSelectChange = (e) => {
    updateReservationData({
      autoSelectTable: e.target.checked,
      tableSelection: e.target.checked ? "" : reservationData.tableSelection,
    });
  };

  // Handle notes change
  const handleNotesChange = (e) => {
    updateReservationData({ notes: e.target.value });
  };

  // Handle clear notes
  const handleClearNotes = () => {
    updateReservationData({ notes: "" });
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    if (formValid) {
      onNext();
    }
  };

  // Handle loading and error states for tables
  if (tablesLoading) {
    return <div>Loading available tables...</div>;
  }
  if (tablesError) {
    return (
      <div>
        Error loading tables: {tablesError.message}
        <button
          type="button"
          className="btn btn-outline-primary mt-2"
          onClick={() => window.location.reload()}
        >
          Retry
        </button>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className={styles.detailsForm}>
      <h2 className="mb-4">Reservation Details</h2>

      <div className="mb-3">
        <label htmlFor="startDateTime" className="form-label">
          Start Date & Time
        </label>
        <input
          type="datetime-local"
          className="form-control"
          id="startDateTime"
          value={startDate}
          onChange={handleStartDateChange}
          min={new Date().toISOString().slice(0, 16)}
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="endDateTime" className="form-label">
          End Date & Time
        </label>
        <input
          type="datetime-local"
          className="form-control"
          id="endDateTime"
          value={endDate}
          onChange={handleEndDateChange}
          min={startDate}
          required
        />
        <div className="form-text">Default duration is 3 hours</div>
      </div>

      <div className="mb-3">
        <label htmlFor="numberOfGuests" className="form-label">
          Number of Guests
        </label>
        <input
          type="number"
          className="form-control"
          id="numberOfGuests"
          min="1"
          max="20"
          value={reservationData.numberOfGuests}
          onChange={handleGuestsChange}
          required
        />
      </div>

      <div className="mb-3 form-check">
        <input
          type="checkbox"
          className="form-check-input"
          id="autoSelectTable"
          checked={reservationData.autoSelectTable}
          onChange={handleAutoSelectChange}
        />
        <label className="form-check-label" htmlFor="autoSelectTable">
          Auto-select best table
        </label>
      </div>

      {!reservationData.autoSelectTable && (
        <div className="mb-3">
          <label htmlFor="tableSelection" className="form-label">
            Select Table
          </label>
          <select
            className="form-select"
            id="tableSelection"
            value={reservationData.tableSelection}
            onChange={handleTableChange}
            required={!reservationData.autoSelectTable}
          >
            <option value="">Choose a table...</option>
            {tables
              .filter((table) => table.capacity >= reservationData.numberOfGuests)
              .map((table) => (
                <option key={table.tableId} value={table.tableId}>
                  {table.tableNumber} (Seats {table.capacity})
                </option>
              ))}
          </select>
          {tables.length === 0 && (
            <div className="text-danger">No tables available.</div>
          )}
        </div>
      )}

      <div className="mb-3">
        <label htmlFor="notes" className="form-label">
          Special Requests / Notes
        </label>
        <div className="d-flex">
          <textarea
            className="form-control"
            id="notes"
            rows={3}
            value={reservationData.notes || ""}
            onChange={handleNotesChange}
          />
          {reservationData.notes && (
            <button
              type="button"
              className="btn btn-outline-secondary ms-2"
              onClick={handleClearNotes}
            >
              Clear
            </button>
          )}
        </div>
      </div>

      <div className="d-flex justify-content-end">
        <button type="submit" className="btn btn-primary" disabled={!formValid}>
          Next: Select Menu Items
        </button>
      </div>
    </form>
  );
}