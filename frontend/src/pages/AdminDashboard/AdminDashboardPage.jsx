import { Container } from "react-bootstrap";
import SummaryCard from "../../components/Dashboard/SummaryCard/SummaryCard";
import OrderStatus from "../../components/Dashboard/OrderStatus/OrderStatus";
import TableMap from "../../components/Dashboard/TableMap/TableMap";
import Analytics from "../../components/Dashboard/Analytics/Analytics";
import Reservations from "../../components/Dashboard/Reservations/Reservations";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useMetrics, useReport } from "../../hooks/reportHooks";
import { format } from "date-fns";
import {
  faCalendarCheck,
  faDollarSign,
  faMoneyBillWave,
  faUtensils,
} from "@fortawesome/free-solid-svg-icons";
import { useReservations } from "../../hooks/reservationHooks";
import { useAvailableTables } from "../../hooks/tableHooks";
import { useOrders } from "../../hooks/orderHooks";
import { SORTING_DIRECTIONS } from "../../constants/webConstant";
import { DATE_FILTER } from "../../constants/filter";
import { useCallback, useEffect, useMemo, useState } from "react";

function AdminDashboardPage() {
  // Date and time formatting
  const now = useMemo(() => new Date(), []); // Memoize to prevent re-creation
  const currentDate = format(now, "yyyy-MM-dd");
  const currentTime = format(now, "HH:mm");

  // State for filters and pagination
  const [filter, setFilter] = useState(DATE_FILTER[0].value);
  const [reservationPage, setReservationPage] = useState(0);
  const [tablePage, setTablePage] = useState(0);
  const [orderPage, setOrderPage] = useState(0);

  // Memoized date range
  const { start: startDate, end: endDate } = useMemo(() => {
    const selectedFilter =
      DATE_FILTER.find((f) => f.value === filter) || DATE_FILTER[0];
    return selectedFilter.getRange();
  }, [filter]);

  // Data fetching hooks
  const {
    data: reportData = {
      totalOrders: 0,
      totalRevenue: 0,
      totalReservations: 0,
      totalOccupiedTables: 0,
    },
    isLoading: loadingReport,
    error: reportError,
  } = useReport({ startDate, endDate });

  const {
    data: metricsData = {},
    isLoading: loadingMetrics,
    error: metricsError,
    mutate: refetchMetrics,
  } = useMetrics({ startDate, endDate });

  const {
    data: reservationsData = { content: [], totalPages: 0 },
    isLoading: loadingReservations,
    error: reservationError,
    mutate: refetchReservations,
  } = useReservations({
    page: reservationPage,
    size: 5,
    sortBy: "date",
    startDate,
    endDate,
  });

  const {
    data: tablesData = { content: [], totalPages: 0 },
    isLoading: loadingTables,
    error: tablesError,
  } = useAvailableTables({
    page: tablePage,
    size: 20,
    date: currentDate,
    time: currentTime,
  });

  const {
    data: ordersData = { content: [], totalPages: 0 },
    isLoading: loadingOrders,
    error: ordersError,
    mutate: refetchOrders,
  } = useOrders({
    sortDirection: SORTING_DIRECTIONS.DESC,
    sortField: "orderDate",
    size: 5,
    page: orderPage,
    startDate,
    endDate,
  });

  // Memoized data
  const metrics = useMemo(() => metricsData, [metricsData]);
  const reservations = useMemo(
    () => reservationsData.content,
    [reservationsData]
  );
  const tables = useMemo(() => tablesData.content, [tablesData]);
  const orders = useMemo(() => ordersData.content, [ordersData]);

  const summaryCards = useMemo(
    () => [
      {
        title: `Total Orders ${
          DATE_FILTER.find((f) => f.value === filter)?.label ||
          DATE_FILTER[0].label
        }`,
        value: reportData.totalOrders,
        icon: faUtensils,
        bgColor: "#e3f2fd",
      },
      {
        title: `Total Revenue ${
          DATE_FILTER.find((f) => f.value === filter)?.label ||
          DATE_FILTER[0].label
        }`,
        value: reportData.totalRevenue,
        icon: faDollarSign,
        bgColor: "#e8f5e9",
      },
      {
        title: `Reservations ${
          DATE_FILTER.find((f) => f.value === filter)?.label ||
          DATE_FILTER[0].label
        }`,
        value: reportData.totalReservations,
        icon: faCalendarCheck,
        bgColor: "#f3e5f5",
      },
      {
        title: `Average Order Value ${
          DATE_FILTER.find((f) => f.value === filter)?.label ||
          DATE_FILTER[0].label
        }`,
        value: (reportData.averageOrderValue || 0).toFixed(2),
        icon: faMoneyBillWave,
        bgColor: "#e0f7fa",
      },
    ],
    [filter, reportData]
  );

  // Handle filter and pagination changes
  const handleFilterChange = useCallback((value) => {
    setFilter(value);
    setReservationPage(0); // Reset pagination on filter change
    setTablePage(0);
    setOrderPage(0);
  }, []);

  const handlePageChange = useCallback((type, page) => {
    switch (type) {
      case "reservations":
        setReservationPage(page);
        break;
      case "tables":
        setTablePage(page);
        break;
      case "orders":
        setOrderPage(page);
        break;
      default:
        break;
    }
  }, []);

  // Refetch data on filter or page change
  useEffect(() => {
    refetchMetrics();
    refetchReservations();
    refetchOrders();
  }, [
    startDate,
    endDate,
    reservationPage,
    orderPage,
    refetchMetrics,
    refetchReservations,
    refetchOrders,
  ]);

  // Loading and error states
  const isLoading =
    loadingReport ||
    loadingReservations ||
    loadingMetrics ||
    loadingTables ||
    loadingOrders;
  const errors = useMemo(
    () =>
      [reportError, reservationError, metricsError, tablesError, ordersError]
        .filter((value) => value)
        .map((err) => err.message),
    [reportError, reservationError, metricsError, tablesError, ordersError]
  );

  // Early returns for loading and error states
  if (isLoading) return <Loading />;
  if (errors.length > 0) return <ErrorDisplay messages={errors} />;

  return (
    <div className="AdminDashboardPage">
      <Container fluid class P="py-4">
        <div className="row g-4 mt-3">
          {summaryCards.map((card, index) => (
            <div key={index} className="col-lg-3 col-md-6 col-sm-6">
              <SummaryCard {...card} />
            </div>
          ))}
        </div>
        <div className="row g-4 mt-4">
          <div className="col-lg-7 col-md-12">
            <Analytics
              metrics={metrics}
              filter={filter}
              onChangeFilter={handleFilterChange}
            />
          </div>
          <div className="col-lg-5 col-md-12">
            <div className="mb-4">
              <Reservations
                reservations={reservations}
                currentPage={reservationPage}
                totalPages={reservationsData.totalPages}
                onPageChange={(page) => handlePageChange("reservations", page)}
              />
            </div>
            <div className="mb-4">
              <OrderStatus
                orders={orders}
                currentPage={orderPage}
                totalPages={ordersData.totalPages}
                onPageChange={(page) => handlePageChange("orders", page)}
              />
            </div>
            <div className="mb-4">
              <TableMap
                tables={tables}
                currentPage={tablePage}
                totalPages={tablesData.totalPages}
                onPageChange={(page) => handlePageChange("tables", page)}
              />
            </div>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default AdminDashboardPage;
