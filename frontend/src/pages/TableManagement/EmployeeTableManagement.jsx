import { useCallback, useEffect, useMemo, useState } from "react";
import { Clock, Plus, Search } from "lucide-react";
import { TableGrid } from "../../components/TableGrid/TableGrid";
import styles from "./EmployeeTableManagement.module.css";
import { useAvailableTables, useTableStatusSummary } from "../../hooks/tableHooks";
import {
  TABLE_OCCUPANCY_STATUSES,
  TABLE_OCCUPANCY_TYPES,
  ORDER_ACTIONS,
  RESERVATION_ACTIONS,
} from "../../constants/webConstant";
import { useAlert } from "../../context/AlertContext";
import Pagination from "../../components/Pagination/Pagination";
import { useSearchParams } from "react-router-dom";
import {
  useTableOccupancies,
  useTableOccupancyActions,
} from "../../hooks/tableOccupancyHooks";
import { WaitingList } from "../../components/WaitingList/WaitingList";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import moment from "moment";
import {
  useReservationActions,
  useReservations,
} from "../../hooks/reservationHooks";
import { UpcommingReservations } from "../../components/WaitingList/UpcommingReservations";
import { useOrderActions } from "../../hooks/orderHooks";

export default function EmployeeTableManagement() {
  const [searchParams, setSearchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [searchValue, setSearchValue] = useState(
    searchParams.get("searchValue") || ""
  );

  const [currentPage, setCurrentPage] = useState(currentPageFromURL);
  const [view, setView] = useState("waiting");

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const now = moment();
  const currentDate = now.format("YYYY-MM-DD");
  const currentTime = now.format("HH:mm");

  const {
    data: reservationsData,
    error: reservationError,
    mutate: mutateReservations,
  } = useReservations({
    page: 0,
    size: 20,
    sortBy: "startTime",
    startDate: currentDate,
    endDate: currentDate,
  });
  const reservations = useMemo(
    () => reservationsData?.content || [],
    [reservationsData]
  );

  const {
    data: tableStatusSummaryData,
    error: tableStatusSummaryError,
    mutate: mutateTableStatusSummary,
  } = useTableStatusSummary({
    date: currentDate,
    time: currentTime,
  })
  const { unoccupied, occupied, reserved, cleaning, total } =
    tableStatusSummaryData || {};

  const { data: tablesData, mutate: mutateTables } = useAvailableTables({
    page: currentPage,
    size: 20,
    date: currentDate,
    time: currentTime,
    search: searchValue,
  });
  const totalPages = tablesData?.totalPages || 0;
  const tables = useMemo(() => tablesData?.content || [], [tablesData]);

  const {
    data: tableOccupanciesData,
    error: tableOccupanciesError,
    mutate: mutateTableOccupancies,
  } = useTableOccupancies({
    page: 0,
    size: 20,
    status: TABLE_OCCUPANCY_STATUSES.WAITING,
  });
  const tableOccupancies = useMemo(
    () => tableOccupanciesData?.content || [],
    [tableOccupanciesData]
  );

  const {
    handleCreateTableOccupancy,
    createError,
    createSuccess,
    resetCreate,

    handleUpdateTableOccupancyStatus,
    updateStatusError,
    updateStatusSuccess,
    resetUpdateStatus,
  } = useTableOccupancyActions();

  const {
    handleProcessOrder,
    processError: processOrderError,
    processSuccess: processOrderSuccess,
    resetProcess: resetOrderProcess,
  } = useOrderActions();

  const {
    handleProcessReservation,
    processError: processReservationError,
    processSuccess: processReservationSuccess,
    resetProcess: resetReservationProcess,
  } = useReservationActions();

  const { showNewAlert } = useAlert();

  // Create Table Occupancy Effects
  useEffect(() => {
    if (createError?.message) {
      showNewAlert({
        message: createError.message,
        variant: "danger",
      });
    }
  }, [createError, showNewAlert]);

  useEffect(() => {
    if (createSuccess) {
      showNewAlert({
        message: createSuccess,
        action: resetCreate,
      });
    }
  }, [createSuccess, resetCreate, showNewAlert]);

  // Process Order Effects
  useEffect(() => {
    if (processOrderError?.message) {
      showNewAlert({
        message: processOrderError.message,
        variant: "danger",
      });
    }
  }, [processOrderError, showNewAlert]);

  useEffect(() => {
    if (processOrderSuccess) {
      showNewAlert({
        message: processOrderSuccess,
        action: resetOrderProcess,
      });
    }
  }, [processOrderSuccess, resetOrderProcess, showNewAlert]);

  // Process Reservation Effects
  useEffect(() => {
    if (processReservationError?.message) {
      showNewAlert({
        message: processReservationError.message,
        variant: "danger",
      });
    }
  }, [processReservationError, showNewAlert]);

  useEffect(() => {
    if (processReservationSuccess) {
      showNewAlert({
        message: processReservationSuccess,
        action: resetReservationProcess,
      });
    }
  }, [processReservationSuccess, resetReservationProcess, showNewAlert]);

  useEffect(() => {
    if (updateStatusError?.message) {
      showNewAlert({
        message: updateStatusError.message,
        variant: "danger",
      });
    }
  }, [updateStatusError, showNewAlert]);

  useEffect(() => {
    if (updateStatusSuccess) {
      showNewAlert({
        message: updateStatusSuccess,
        action: resetUpdateStatus,
      });
    }
  }, [updateStatusSuccess, resetUpdateStatus, showNewAlert]);

  const onServedReservation = useCallback(
    async (reservationId) => {
      await handleProcessReservation(reservationId, RESERVATION_ACTIONS.SERVED);
    },
    [handleProcessReservation]
  );

  const onCompleteReservation = useCallback(
    async (reservationId) => {
      const res = await handleProcessReservation(
        reservationId,
        RESERVATION_ACTIONS.COMPLETE
      );
      if (res) {
        mutateTables();
        mutateReservations();
        mutateTableStatusSummary();
      }
    },
    [handleProcessReservation, mutateTableStatusSummary, mutateReservations, mutateTables]
  );

  const onServedOrder = useCallback(
    async (orderId, type) => {
      await handleProcessOrder(orderId, ORDER_ACTIONS.SERVED, type);
    },
    [handleProcessOrder]
  );

  const updateTableStatus = useCallback(
    async (id, tableId, newStatus) => {
      const res = await handleUpdateTableOccupancyStatus(id, {
        tableId,
        status: newStatus,
      });
      if (res) {
        mutateTables();
      }
    },
    [handleUpdateTableOccupancyStatus, mutateTables]
  );

  const handleSeatCustomerReservation = useCallback(
    async (table) => {
      const requestSeatReservation = {
        contactName: table.contactName,
        partySize: table.partySize,
        phone: table.phone,
        notes: table.notes,
        reservationId: table.reservationId,
        type: TABLE_OCCUPANCY_TYPES.RESERVATION,
      };

      try {
        const res = await handleCreateTableOccupancy(requestSeatReservation);
        if (res) {
          mutateReservations(
            (prevData) => ({
              ...prevData,
              content: [...(prevData?.content || []), res],
            }),
            false
          );
          mutateReservations();
          mutateTables();
          mutateTableStatusSummary();
        }
      } catch (error) {
        showNewAlert({
          message: "Failed to seat customer. Please try again.",
          variant: "danger",
        });
      }
    },
    [handleCreateTableOccupancy, mutateTableStatusSummary, showNewAlert, mutateReservations, mutateTables]
  );

  const debouncedSearch = useCallback(
    (newSearchValue) => {
      searchParams.set("searchValue", newSearchValue);
      setSearchParams(searchParams);
    },
    [setSearchParams, searchParams]
  );

  const handleSearchInputChange = (e) => {
    const newSearchValue = e.target.value;
    setSearchValue(newSearchValue);
    debouncedSearch(newSearchValue);
  };

  if( tableStatusSummaryError?.message ||
      reservationError?.message ||
      tableOccupanciesError?.message) {
    return (
      <ErrorDisplay
        message={
          tableStatusSummaryError?.message ||
          reservationError?.message ||
          tableOccupanciesError?.message
        }
      />
    );
  }

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
                {unoccupied}
              </span>
            </div>
            <div className={styles.statValue}>
              {Math.round((unoccupied / total) * 100)}%
            </div>
            <p className={styles.statDescription}>of total capacity</p>
          </div>
          <div className={styles.statCard}>
            <div className={styles.statHeader}>
              <h3 className={styles.statTitle}>Occupied Tables</h3>
              <span className={`${styles.badge} ${styles.badgeDanger}`}>
                {occupied}
              </span>
            </div>
            <div className={styles.statValue}>
              {Math.round((occupied / total) * 100)}%
            </div>
            <p className={styles.statDescription}>of total capacity</p>
          </div>
          <div className={styles.statCard}>
            <div className={styles.statHeader}>
              <h3 className={styles.statTitle}>Cleaing Tables</h3>
              <span className={`${styles.badge} ${styles.badgePrimary}`}>
                {cleaning}
              </span>
            </div>
            <div className={styles.statValue}>
              {Math.round((cleaning / total) * 100)}%
            </div>
            <p className={styles.statDescription}>of total capacity</p>
          </div>
          <div className={styles.statCard}>
            <div className={styles.statHeader}>
              <h3 className={styles.statTitle}>Reserved Tables</h3>
              <span className={`${styles.badge} ${styles.badgeWarning}`}>
                {reserved}
              </span>
            </div>
            <div className={styles.statValue}>{reserved}</div>
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
                    value={searchValue}
                    onChange={handleSearchInputChange}
                  />
                </div>
              </div>
              <div className={styles.tabContent}>
                <TableGrid
                  tables={tables}
                  onSeatCustomerReservation={handleSeatCustomerReservation}
                  onUpdateTableStatus={updateTableStatus}
                  onServedOrder={onServedOrder}
                  onServedReservation={onServedReservation}
                  onCompleteReservation={onCompleteReservation}
                  tableOccupancies={tableOccupancies}
                  mutateTableOccupancies={mutateTableOccupancies}
                  mutateTableStatusSummary={mutateTableStatusSummary}
                  mutateTables={mutateTables}
                />
                <Pagination
                  currentPage={currentPage + 1}
                  totalPages={totalPages}
                />
              </div>
            </div>
          </div>

          <div className={styles.tableOccupanciesSection}>
            <div className={styles.card}>
              <div className={styles.cardHeader}>
                <select
                  className={styles.viewSelect}
                  value={view}
                  onChange={(e) => setView(e.target.value)}
                >
                  <option value="waiting">Waiting List</option>
                  <option value="reservations">Upcoming Reservations</option>
                </select>
                <div className={styles.headerControls}>
                  <button className={styles.addButton}>
                    <Plus size={16} />
                    <span>Add</span>
                  </button>
                </div>
              </div>
              <div className={styles.cardContent}>
                {view === "waiting" ? (
                  <>
                    <p className={styles.waitingCount}>
                      {tableOccupancies.length} parties waiting
                    </p>
                    {tableOccupanciesError?.message ? (
                      <ErrorDisplay message={tableOccupanciesError.message} />
                    ) : (
                      <WaitingList
                        waitingList={tableOccupancies}
                        mutate={mutateTableOccupancies}
                      />
                    )}
                  </>
                ) : (
                  <>
                    {reservationError?.message ? (
                      <ErrorDisplay message={reservationError.message} />
                    ) : (
                      <UpcommingReservations
                        reservations={reservations}
                        mutate={mutateReservations}
                      />
                    )}
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
