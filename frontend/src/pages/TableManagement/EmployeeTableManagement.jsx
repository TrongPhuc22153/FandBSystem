import { useCallback, useEffect, useState } from "react";
import { Clock, Search } from "lucide-react";
import { TableGrid } from "../../components/TableGrid/TableGrid";
import styles from "./EmployeeTableManagement.module.css";
import {
  useReservationTableActions,
  useReservationTables,
} from "../../hooks/tableHooks";
import { TABLE_STATUSES } from "../../constants/webConstant";
import { useAlert } from "../../context/AlertContext";
import Pagination from "../../components/Pagination/Pagination";
import { useNavigate, useSearchParams } from "react-router-dom";

export default function EmployeeTableManagement() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [selectedItems, setSelectedItems] = useState([]);
  const [searchValue, setSearchValue] = useState(
    searchParams.get("searchValue") || ""
  );

  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const { data: tablesData, mutate } = useReservationTables({
    page: currentPage,
  });
  const totalPages = tablesData?.totalPages || 0;
  const tables = tablesData?.content || [];

  const {
    handleUpdateReservationTableStatus,
    updateStatusError,
    updateStatusSuccess,
    resetUpdateStatus,
  } = useReservationTableActions();

  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (updateStatusError?.message) {
      showNewAlert({
        message: updateStatusError.message,
        variant: "danger",
      });
    }
  }, [updateStatusError]);

  useEffect(() => {
    if (updateStatusSuccess) {
      showNewAlert({
        message: updateStatusSuccess,
        action: resetUpdateStatus,
      });
    }
  }, [updateStatusSuccess]);

  const updateTableStatus = useCallback(
    async (tableId, newStatus) => {
      const res = await handleUpdateReservationTableStatus({
        id: tableId,
        status: newStatus,
      });
      if (res) {
        mutate();
      }
    },
    [handleUpdateReservationTableStatus, mutate]
  );

  // Calculate statistics
  const availableTables = tables.filter(
    (t) => t.status === TABLE_STATUSES.UNOCCUPIED
  ).length;
  const occupiedTables = tables.filter(
    (t) => t.status === TABLE_STATUSES.OCCUPIED
  ).length;
  const reservedTables = tables.filter(
    (t) => t.status === TABLE_STATUSES.RESERVED
  ).length;
  const cleaningTables = tables.filter(
    (t) => t.status === TABLE_STATUSES.CLEANING
  ).length;

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerContent}>
          <h1 className={styles.title}>Table Management System</h1>
          <div className={styles.headerRight}>
            <div className={styles.timeDisplay}>
              <Clock className={styles.clockIcon} />
              <span className={styles.time}>
                {new Date().toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div className={styles.content}>
        <div className={styles.statsGrid}>
          <div className={styles.statCard}>
            <div className={styles.statHeader}>
              <h3 className={styles.statTitle}>Available Tables</h3>
              <span className={`${styles.badge} ${styles.badgeSuccess}`}>
                {availableTables}
              </span>
            </div>
            <div className={styles.statValue}>
              {Math.round((availableTables / tables.length) * 100)}%
            </div>
            <p className={styles.statDescription}>of total capacity</p>
          </div>
          <div className={styles.statCard}>
            <div className={styles.statHeader}>
              <h3 className={styles.statTitle}>Occupied Tables</h3>
              <span className={`${styles.badge} ${styles.badgeDanger}`}>
                {occupiedTables}
              </span>
            </div>
            <div className={styles.statValue}>
              {Math.round((occupiedTables / tables.length) * 100)}%
            </div>
            <p className={styles.statDescription}>of total capacity</p>
          </div>
          <div className={styles.statCard}>
            <div className={styles.statHeader}>
              <h3 className={styles.statTitle}>Reserved Tables</h3>
              <span className={`${styles.badge} ${styles.badgeWarning}`}>
                {reservedTables}
              </span>
            </div>
            <div className={styles.statValue}>{reservedTables}</div>
            <p className={styles.statDescription}>upcoming reservations</p>
          </div>
        </div>

        <div className={styles.mainGrid}>
          <div className={styles.tablesSection}>
            <div className={styles.card}>
              <div className={styles.tabsContainer}>
                <div className={styles.searchContainer}>
                  <Search className={styles.searchIcon} />
                  <input
                    type="text"
                    placeholder="Search tables..."
                    className={styles.searchInput}
                  />
                </div>
              </div>
              <div className={styles.tabContent}>
                <TableGrid
                  tables={tables}
                  updateTableStatus={updateTableStatus}
                />
                <Pagination
                  currentPage={currentPage + 1}
                  totalPages={totalPages}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
