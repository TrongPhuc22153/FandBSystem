
import { useMemo, useState } from "react"
import ErrorDisplay from "../../ErrorDisplay/ErrorDisplay"
import styles from "./Step1ReservationDetails.module.css"
import { useReservationTables } from "../../../hooks/tableHooks";

export default function Step1ReservationDetails({ data, updateData, onNext }) {
    const {
      data: tablesData,
      error: tablesError,
    } = useReservationTables();
    const tables = useMemo(() => tablesData?.content || [], [tablesData]);

  const [errors, setErrors] = useState({})

  const validateForm = () => {
    const newErrors = {}

    if (!data.date) newErrors.date = "Reservation date is required"
    if (!data.startTime) newErrors.startTime = "Start time is required"
    if (!data.endTime) newErrors.endTime = "End time is required"
    if (data.numberOfGuests < 1) newErrors.numberOfGuests = "At least 1 guest is required"
    if (!data.autoAssignTable && !data.tableId) {
      newErrors.table = "Please select a table or enable auto-assign"
    }

    if (data.startTime && data.endTime && data.startTime >= data.endTime) {
      newErrors.endTime = "End time must be after start time"
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    if (validateForm()) {
      onNext()
    }
  }

  const handleInputChange = (field, value) => {
    updateData({ [field]: value })
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: "" }))
    }
  }

  if( tablesError?.message) {
    return <ErrorDisplay message={tablesError.message} />
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Reservation Details</h2>
        <p>Let's start with the basics of your reservation</p>
      </div>

      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.row}>
          <div className={styles.field}>
            <label htmlFor="date">Reservation Date *</label>
            <input
              type="date"
              id="date"
              value={data.date}
              onChange={(e) => handleInputChange("date", e.target.value)}
              className={errors.date ? styles.error : ""}
              min={new Date().toISOString().split("T")[0]}
            />
            {errors.date && <span className={styles.errorText}>{errors.date}</span>}
          </div>

          <div className={styles.field}>
            <label htmlFor="guests">Number of Guests *</label>
            <input
              type="number"
              id="numberOfGuests"
              value={data.numberOfGuests}
              onChange={(e) => handleInputChange("numberOfGuests", Number.parseInt(e.target.value) || 0)}
              className={errors.numberOfGuests ? styles.error : ""}
              min="1"
              max="20"
            />
            {errors.guests && <span className={styles.errorText}>{errors.guests}</span>}
          </div>
        </div>

        <div className={styles.row}>
          <div className={styles.field}>
            <label htmlFor="startTime">Start Time *</label>
            <input
              type="time"
              id="startTime"
              value={data.startTime}
              onChange={(e) => handleInputChange("startTime", e.target.value)}
              className={errors.startTime ? styles.error : ""}
            />
            {errors.startTime && <span className={styles.errorText}>{errors.startTime}</span>}
          </div>

          <div className={styles.field}>
            <label htmlFor="endTime">End Time *</label>
            <input
              type="time"
              id="endTime"
              value={data.endTime}
              onChange={(e) => handleInputChange("endTime", e.target.value)}
              className={errors.endTime ? styles.error : ""}
            />
            {errors.endTime && <span className={styles.errorText}>{errors.endTime}</span>}
          </div>
        </div>

        <div className={styles.field}>
          <label htmlFor="notes">Special Notes (Optional)</label>
          <textarea
            id="notes"
            value={data.notes}
            onChange={(e) => handleInputChange("notes", e.target.value)}
            placeholder="Any special requests or dietary restrictions..."
            rows={3}
          />
        </div>

        <div className={styles.tableSection}>
          <div className={styles.autoAssignWrapper}>
            <label className={styles.checkboxLabel}>
              <input
                type="checkbox"
                checked={data.autoAssignTable}
                onChange={(e) => handleInputChange("autoAssignTable", e.target.checked)}
              />
              <span className={styles.checkmark}></span>
              Auto-assign table
            </label>
          </div>

          {!data.autoAssignTable && (
            <div className={styles.tableGrid}>
              <h4>Select Your Table</h4>
              <div className={styles.tables}>
                {tables.map((table) => (
                  <div
                    key={table.tableId}
                    className={`${styles.tableCard} ${data.tableId === table.tableId ? styles.selected : ""}`}
                    onClick={() => handleInputChange("tableId", table.tableId)}
                  >
                    <div className={styles.tableName}>{`Table ${table.tableNumber}`}</div>
                    <div className={styles.tableInfo}>
                      <span>{table.capacity} seats</span>
                      <span>{table.location}</span>
                    </div>
                  </div>
                ))}
              </div>
              {errors.table && <span className={styles.errorText}>{errors.table}</span>}
            </div>
          )}
        </div>

        <div className={styles.actions}>
          <button type="submit" className={styles.nextButton}>
            Next Step
          </button>
        </div>
      </form>
    </div>
  )
}
