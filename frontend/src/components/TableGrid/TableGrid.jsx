import { useEffect, useState } from "react";
import { Check, ChevronDown, Users } from "lucide-react";
import styles from "./TableGrid.module.css";
import {
  TABLE_STATUSES,
  TABLE_OCCUPANCY_STATUSES,
} from "../../constants/webConstant";
import { formatDistanceToNow } from "date-fns";
import { useTableOccupancyActions } from "../../hooks/tableOccupancyHooks";
import { useAlert } from "../../context/AlertContext";

export function TableGrid({ tables, updateTableStatus, tableOccupancies, mutateTableOccupancies, mutateTables }) {
  const [selectedTable, setSelectedTable] = useState(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState(null);

  const { showNewAlert } = useAlert();

  const {
    handleUpdateTableOccupancyStatus,
    updateStatusError,
    resetUpdateStatus,
  } = useTableOccupancyActions();

  useEffect(() => {
    if (updateStatusError?.message) {
      showNewAlert({
        message: updateStatusError.message,
        variant: "danger",
        action: resetUpdateStatus,
      });
    }
  }, [updateStatusError]);

  const handleTableClick = (table) => {
    setSelectedTable(table);
    setIsDialogOpen(true);
  };

  const handleStatusChange = (status) => {
    if (selectedTable) {
      if(selectedTable?.occupancyId){
        updateTableStatus?.(selectedTable.occupancyId, selectedTable.tableId, status);
      }
      setIsDialogOpen(false);
    }
  };

  const handleSeatCustomer = async (status) => {
    const res = await handleUpdateTableOccupancyStatus(selectedCustomer, {
      status: status,
      tableId: selectedTable.tableId,
    });
    if (res) {
      mutateTableOccupancies?.();
      mutateTables?.();
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
                </div>
              </div>
              {selectedTable?.status !== TABLE_STATUSES.OCCUPIED && (
                <div className={styles.assignSection}>
                  <h4 className={styles.sectionTitle}>
                    Assign Server & Seat Customers
                  </h4>
                  <div className={styles.assignActions}>
                    <div className={styles.dropdownContainer}>
                      <button
                        className={styles.dropdownButton}
                        onClick={() => setDropdownOpen(!dropdownOpen)}
                        aria-expanded={dropdownOpen}
                        aria-label="Select customer to seat"
                      >
                        {selectedCustomer
                          ? tableOccupancies.find((s) => s.id === selectedCustomer)
                              ?.contactName
                          : "Select customer"}
                        <ChevronDown
                          size={16}
                          className={styles.dropdownIcon}
                        />
                      </button>
                      {dropdownOpen && (
                        <div className={styles.dropdownMenu}>
                          {tableOccupancies
                            .filter(
                              (customer) =>
                                customer.partySize <= selectedTable.capacity
                            )
                            .sort(
                              (a, b) =>
                                new Date(a.createdAt) - new Date(b.createdAt)
                            )
                            .map((customer) => (
                              <div
                                key={customer.id}
                                className={styles.dropdownItem}
                                onClick={() => {
                                  setSelectedCustomer(customer.id);
                                  setDropdownOpen(false);
                                }}
                                role="option"
                                aria-selected={selectedCustomer === customer.id}
                              >
                                <div className={styles.customerInfo}>
                                  <div className={styles.customerHeader}>
                                    <span className={styles.customerName}>
                                      {customer.contactName} (ID:{" "}
                                      {customer.id.slice(-4)})
                                    </span>
                                    <br />
                                    <span className={styles.partySize}>
                                      <Users
                                        size={14}
                                        className={styles.usersIcon}
                                      />
                                      Party of {customer.partySize}
                                    </span>
                                  </div>
                                  <div className={styles.customerDetails}>
                                    <span className={styles.phone}>
                                      {customer.phone}
                                    </span>
                                    <br />
                                    <span className={styles.waitTime}>
                                      Waiting:{" "}
                                      {formatDistanceToNow(
                                        new Date(customer.createdAt),
                                        { addSuffix: true }
                                      )}
                                    </span>
                                    {customer.notes && (
                                      <span className={styles.notes}>
                                        Notes:{" "}
                                        {customer.notes.length > 20
                                          ? `${customer.notes.slice(0, 20)}...`
                                          : customer.notes}
                                      </span>
                                    )}
                                  </div>
                                </div>
                                <Check
                                  className={`${styles.checkIcon} ${
                                    selectedCustomer === customer.id
                                      ? styles.visible
                                      : styles.hidden
                                  }`}
                                  size={16}
                                />
                              </div>
                            ))}
                          {tableOccupancies.filter(
                            (customer) =>
                              customer.partySize <= selectedTable.capacity
                          ).length === 0 && (
                            <div className={styles.dropdownEmpty}>
                              No suitable customers available
                            </div>
                          )}
                        </div>
                      )}
                    </div>

                    <button
                      className={`${styles.primaryButton} ${
                        !selectedCustomer ? styles.disabled : ""
                      }`}
                      onClick={() =>
                        handleSeatCustomer(TABLE_OCCUPANCY_STATUSES.SEATED)
                      }
                      disabled={!selectedCustomer}
                    >
                      Seat Customers
                    </button>
                  </div>
                </div>
              )}
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
