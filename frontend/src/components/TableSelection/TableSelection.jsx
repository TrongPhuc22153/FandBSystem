import styles from "./TableSelection.module.css"

export default function TableSelection({ tables, selectedTable, onSelectTable }) {
  return (
    <div className={styles.tableGrid}>
      {tables.map((table) => (
        <button
          key={table.tableId}
          className={`${styles.tableButton} ${selectedTable === table.tableId ? styles.selected : ""}`}
          onClick={() => onSelectTable(table.tableId)}
        >
          Table {table.tableNumber}
          {table.capacity && (
            <div className={styles.seatInfo}>
              <small>({table.capacity} seats)</small><br />
              <small>{table.location}</small>
            </div>
          )}
        </button>
      ))}
    </div>
  )
}
