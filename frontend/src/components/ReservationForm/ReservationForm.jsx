import { useState, useEffect, useMemo } from "react";
import styles from "./ReservationForm.module.css";
import { useReservationTables } from "../../hooks/tableHooks";
import { useAlert } from "../../context/AlertContext";
import { RESERVATION_TIME } from "../../constants/webConstant";

export default function ReservationDetails({
  reservationData,
  updateReservationData,
  onNext,
}) {

  // Initialize date, default to today if not set
  const [date, setDate] = useState(
    reservationData.date
      ? reservationData.date.toISOString().slice(0, 10)
      : new Date().toISOString().slice(0, 10)
  );

  // Initialize startTime, default to 10:00 if not set or outside valid range
  const [startTime, setStartTime] = useState(() => {
    const storedTime = reservationData.startTime || RESERVATION_TIME.MIN_TIME;
    return storedTime >= RESERVATION_TIME.MIN_TIME && storedTime <= RESERVATION_TIME.MAX_TIME
      ? storedTime
      : RESERVATION_TIME.MIN_TIME;
  });

  // Initialize endTime, default to startTime + 2 hours or 10:00 if not set
  const [endTime, setEndTime] = useState(() => {
    const storedEndTime = reservationData.endTime;
    if (
      storedEndTime &&
      storedEndTime >= RESERVATION_TIME.MIN_TIME &&
      storedEndTime <= RESERVATION_TIME.MAX_TIME
    ) {
      return storedEndTime;
    }
    const defaultEndDateTime = new Date(`${date}T${startTime}`);
    defaultEndDateTime.setHours(
      defaultEndDateTime.getHours() + RESERVATION_TIME.DEFAULT_DURATION_HOURS
    );
    const calculatedEndTime = defaultEndDateTime.toTimeString().slice(0, 5);
    return calculatedEndTime <= RESERVATION_TIME.MAX_TIME ? calculatedEndTime : RESERVATION_TIME.MIN_TIME;
  });

  const [formValid, setFormValid] = useState(false);

  const {
    data: tablesData,
    isLoading: tablesLoading,
    error: tablesError,
  } = useReservationTables();
  const tables = useMemo(() => tablesData?.content || [], [tablesData]);

  const { showNewAlert } = useAlert();

  // Update end time to default duration when date or start time changes
  useEffect(() => {
    const startDateTime = new Date(`${date}T${startTime}`);
    const currentStartDateTime =
      reservationData.date && reservationData.startTime
        ? new Date(
            `${reservationData.date.toISOString().slice(0, 10)}T${
              reservationData.startTime
            }`
          ).getTime()
        : startDateTime.getTime();

    // Only proceed if date or start time has changed
    if (startDateTime.getTime() !== currentStartDateTime) {
      const defaultEndDateTime = new Date(
        startDateTime.getTime() + RESERVATION_TIME.DEFAULT_DURATION_HOURS * 60 * 60 * 1000
      );
      const newEndTime = defaultEndDateTime.toTimeString().slice(0, 5);

      // Ensure new end time is within allowed range
      const finalEndTime = newEndTime <= RESERVATION_TIME.MAX_TIME ? newEndTime : RESERVATION_TIME.MIN_TIME;

      // Only update endTime if current endTime is the default duration
      const currentEndDateTime =
        reservationData.date && reservationData.endTime
          ? new Date(
              `${reservationData.date.toISOString().slice(0, 10)}T${
                reservationData.endTime
              }`
            ).getTime()
          : startDateTime.getTime();
      const defaultEndTimeForStart =
        currentStartDateTime + RESERVATION_TIME.DEFAULT_DURATION_HOURS * 60 * 60 * 1000;

      if (
        currentEndDateTime === defaultEndTimeForStart ||
        !reservationData.endTime
      ) {
        setEndTime(finalEndTime);
        updateReservationData({
          date: new Date(date),
          startTime,
          endTime: finalEndTime,
        });
      } else {
        updateReservationData({
          date: new Date(date),
          startTime,
        });
      }
    }
  }, [
    date,
    startTime,
    updateReservationData,
    reservationData.date,
    reservationData.startTime,
    reservationData.endTime,
  ]);

  // Validate form
  useEffect(() => {
    const startDateTime = new Date(`${date}T${startTime}`);
    const endDateTime = new Date(`${date}T${endTime}`);
    const isValid =
      reservationData.numberOfGuests > 0 &&
      (reservationData.autoSelectTable ||
        reservationData.tableSelection !== "") &&
      endDateTime > startDateTime &&
      startTime >= RESERVATION_TIME.MIN_TIME &&
      startTime <= RESERVATION_TIME.MAX_TIME &&
      endTime >= RESERVATION_TIME.MIN_TIME &&
      endTime <= RESERVATION_TIME.MAX_TIME &&
      (!reservationData.autoSelectTable ? tables.length > 0 : true);
    setFormValid(isValid);
  }, [
    reservationData.numberOfGuests,
    reservationData.autoSelectTable,
    reservationData.tableSelection,
    date,
    startTime,
    endTime,
    tables,
  ]);

  // Handle date change
  const handleDateChange = (e) => {
    setDate(e.target.value);
  };

  // Handle start time change
  const handleStartTimeChange = (e) => {
    const newStartTime = e.target.value;
    if (newStartTime >= RESERVATION_TIME.MIN_TIME && newStartTime <= RESERVATION_TIME.MAX_TIME) {
      setStartTime(newStartTime);
    } else {
      showNewAlert({
        message: "Start time must be between 10:00 AM and 9:00 PM.",
        variant: "danger",
      });
    }
  };

  // Handle end time change
  const handleEndTimeChange = (e) => {
    const newEndTime = e.target.value;
    const startDateTime = new Date(`${date}T${startTime}`);
    const endDateTime = new Date(`${date}T${newEndTime}`);

    if (
      newEndTime >= RESERVATION_TIME.MIN_TIME &&
      newEndTime <= RESERVATION_TIME.MAX_TIME &&
      endDateTime > startDateTime
    ) {
      setEndTime(newEndTime);
      updateReservationData({ endTime: newEndTime });
    } else {
      showNewAlert({
        message:
          newEndTime < RESERVATION_TIME.MIN_TIME || newEndTime > RESERVATION_TIME.MAX_TIME
            ? "End time must be between 10:00 AM and 9:00 PM."
            : "End time must be after start time.",
        variant: "danger",
      });
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
        <label htmlFor="reservationDate" className="form-label">
          Reservation Date
        </label>
        <input
          type="date"
          className="form-control"
          id="reservationDate"
          value={date}
          onChange={handleDateChange}
          min={new Date().toISOString().slice(0, 10)}
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="startTime" className="form-label">
          Start Time
        </label>
        <input
          type="time"
          className="form-control"
          id="startTime"
          value={startTime}
          onChange={handleStartTimeChange}
          min={RESERVATION_TIME.MIN_TIME}
          max={RESERVATION_TIME.MAX_TIME}
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="endTime" className="form-label">
          End Time
        </label>
        <input
          type="time"
          className="form-control"
          id="endTime"
          value={endTime}
          onChange={handleEndTimeChange}
          min={RESERVATION_TIME.MIN_TIME}
          max={RESERVATION_TIME.MAX_TIME}
          required
        />
        <div className="form-text">
          Default duration is {RESERVATION_TIME.DEFAULT_DURATION_HOURS} hours
        </div>
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
              .filter(
                (table) => table.capacity >= reservationData.numberOfGuests
              )
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
