import { useState } from "react";
import { Users } from "lucide-react";
import styles from "./TableGrid.module.css";
import { TABLE_STATUSES } from "../../constants/webConstant";

export function TableGrid({ tables, updateTableStatus }) {
  const [selectedTable, setSelectedTable] = useState(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const handleTableClick = (table) => {
    setSelectedTable(table);
    setIsDialogOpen(true);
  };

  const handleStatusChange = (status) => {
    if (selectedTable) {
      updateTableStatus(selectedTable.tableId, status);
      setIsDialogOpen(false);
    }
  };

  const getTableStatusClass = (status) => {
    switch (status) {
      case TABLE_STATUSES.UNOCCUPIED:
        return styles.tableAvailable;
      case TABLE_STATUSES.OCCUPIED:
        return styles.tableOccupied;
      case TABLE_STATUSES.RESERVED:
        return styles.tableReserved;
      case TABLE_STATUSES.CLEANING:
        return styles.tableCleaning;
      default:
        return "";
    }
  };

  return (
    <>
      <div className={styles.tableGrid}>
        {tables.map((table) => (
          <div
            key={table.tableId}
            className={`${styles.tableCard} ${getTableStatusClass(
              table.status
            )}`}
            onClick={() => handleTableClick(table)}
          >
            <div className={styles.tableHeader}>
              <div>
                <h3 className={styles.tableNumber}>
                  Table {table.tableNumber}
                </h3>
                <div className={styles.seatsInfo}>
                  <Users size={14} className={styles.usersIcon} />
                  <span>{table.capacity} seats</span>
                </div>
              </div>
              <span className={styles.statusBadge}>{table.status}</span>
            </div>

            {table.location && (
              <div className={styles.tableInfo}>
                <span className={styles.infoLabel}>Location:</span>{" "}
                {table.location}
              </div>
            )}

            {table.lastModifiedAt &&
              table.status === TABLE_STATUSES.OCCUPIED && (
                <div className={styles.tableInfo}>
                  <span className={styles.infoLabel}>Since:</span>{" "}
                  {table.lastModifiedAt}
                </div>
              )}

            {table.reservedFor && table.status === TABLE_STATUSES.RESERVED && (
              <div className={styles.tableInfo}>
                <span className={styles.infoLabel}>Reserved:</span>{" "}
                {table.reservedFor}
              </div>
            )}
          </div>
        ))}
      </div>
      
      {isDialogOpen && (
        <div
          className={styles.modalOverlay}
          onClick={() => setIsDialogOpen(false)}
        >
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>
                Table {selectedTable?.number}
              </h2>
              <button
                className={styles.closeButton}
                onClick={() => setIsDialogOpen(false)}
              >
                ×
              </button>
            </div>
            <div className={styles.modalBody}>
              <div className={styles.statusSection}>
                <h4 className={styles.sectionTitle}>
                  Current Status:{" "}
                  <span className={styles.statusText}>
                    {selectedTable?.status}
                  </span>
                </h4>

                {selectedTable?.status === TABLE_STATUSES.OCCUPIED && (
                  <div className={styles.statusDetails}>
                    <p>
                      <span className={styles.detailLabel}>
                        Occupied since:
                      </span>{" "}
                      {selectedTable.lastModifiedAt}
                    </p>
                  </div>
                )}

                {selectedTable?.status === TABLE_STATUSES.RESERVED && (
                  <div className={styles.statusDetails}>
                    <p>
                      <span className={styles.detailLabel}>Reserved for:</span>{" "}
                      {selectedTable.reservedFor}
                    </p>
                  </div>
                )}
              </div>

              <div className={styles.statusActions}>
                <h4 className={styles.sectionTitle}>Change Status</h4>
                <div className={styles.actionButtons}>
                  <button
                    className={`${styles.actionButton} ${
                      selectedTable?.status === TABLE_STATUSES.UNOCCUPIED
                        ? styles.activeButton
                        : ""
                    } ${styles.availableButton}`}
                    onClick={() =>
                      handleStatusChange(TABLE_STATUSES.UNOCCUPIED)
                    }
                  >
                    Available
                  </button>
                  <button
                    className={`${styles.actionButton} ${
                      selectedTable?.status === TABLE_STATUSES.RESERVED
                        ? styles.activeButton
                        : ""
                    } ${styles.reservedButton}`}
                    onClick={() => handleStatusChange(TABLE_STATUSES.RESERVED)}
                  >
                    Reserved
                  </button>
                  <button
                    className={`${styles.actionButton} ${
                      selectedTable?.status === TABLE_STATUSES.CLEANING
                        ? styles.activeButton
                        : ""
                    } ${styles.cleaningButton}`}
                    onClick={() => handleStatusChange(TABLE_STATUSES.CLEANING)}
                  >
                    Cleaning
                  </button>
                  <button
                    className={`${styles.actionButton} ${
                      selectedTable?.status === TABLE_STATUSES.OCCUPIED
                        ? styles.activeButton
                        : ""
                    } ${styles.occupiedButton}`}
                    onClick={() => handleStatusChange(TABLE_STATUSES.OCCUPIED)}
                  >
                    Occupied
                  </button>
                </div>
              </div>
            </div>
            <div className={styles.modalFooter}>
              <button
                className={styles.cancelButton}
                onClick={() => setIsDialogOpen(false)}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
