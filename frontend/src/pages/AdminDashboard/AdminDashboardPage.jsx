import { Container } from "react-bootstrap";
import SummaryCard from "../../components/Dashboard/SummaryCard/SummaryCard";
import OrderStatus from "../../components/Dashboard/OrderStatus/OrderStatus";
import TableMap from "../../components/Dashboard/TableMap/TableMap";
import Analytics from "../../components/Dashboard/Analytics/Analytics";
import Reservations from "../../components/Dashboard/Reservations/Reservations";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useMetrics, useReport } from "../../hooks/reportHooks";
import moment from "moment";
import {
  faCalendarCheck,
  faChair,
  faDollarSign,
  faUtensils,
} from "@fortawesome/free-solid-svg-icons";
import { useReservations } from "../../hooks/reservationHooks";
import { useMemo, useState } from "react";
import { useAvailableTables } from "../../hooks/tableHooks";
import { useOrders } from "../../hooks/orderHooks";
import { SORTING_DIRECTIONS } from "../../constants/webConstant";
import { getDateRange } from "../../utils/metricUtils";

function AdminDashboardPage() {
  const now = moment();
  const currentDate = now.format("YYYY-MM-DD");
  const currentTime = now.format("HH:mm");

  // State for filters and pagination
  const [filter, setFilter] = useState("today");
  const [reservationPage] = useState(0);
  const [tablePage] = useState(0);
  const [orderPage] = useState(0);

  // Data fetching hooks
  const {
    data: reportData,
    isLoading: loadingReport,
    error: reportError,
  } = useReport(currentDate, currentDate);

  const {
    data: metricsData,
    isLoading: loadingMetrics,
    error: metricsError,
  } = useMetrics(getDateRange(filter));
  const metrics = useMemo(() => metricsData || {}, [metricsData]);

  const {
    data: reservationsData,
    error: reservationError,
    isLoading: loadingReservations,
  } = useReservations({
    page: reservationPage,
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
    data: tablesData,
    isLoading: loadingTables,
    error: tablesError,
  } = useAvailableTables({
    page: tablePage,
    size: 20,
    date: currentDate,
    time: currentTime,
  });
  const tables = useMemo(() => tablesData?.content || [], [tablesData]);

  const {
    data: ordersData,
    isLoading: loadingOrders,
    error: ordersError,
  } = useOrders({
    sortDirection: SORTING_DIRECTIONS.ASC,
    sortField: "orderDate",
    page: orderPage,
  });
  const orders = useMemo(() => ordersData?.content || [], [ordersData]);

  // Loading state check
  if (
    loadingReport ||
    loadingReservations ||
    loadingMetrics ||
    loadingTables ||
    loadingOrders
  ) {
    return <Loading />;
  }

  // Error state check
  if (
    reportError?.message ||
    reservationError?.message ||
    metricsError?.message ||
    tablesError?.message ||
    ordersError?.message
  ) {
    return (
      <ErrorDisplay
        message={
          reportError?.message ||
          reservationError?.message ||
          metricsError?.message ||
          tablesError?.message ||
          ordersError?.message
        }
      />
    );
  }

  return (
    <div className="AdminDashboardPage">
      <Container fluid className="py-4">
        <div className="row g-4">
          <div className="col-lg-3 col-md-6 col-sm-6">
            <SummaryCard
              title={"Total Orders Today"}
              value={reportData.totalOrders}
              icon={faUtensils}
              bgColor={"#e3f2fd"}
            />
          </div>
          <div className="col-lg-3 col-md-6 col-sm-6">
            <SummaryCard
              title={"Total Revenue Today"}
              value={reportData.totalRevenue}
              icon={faDollarSign}
              bgColor={"#e8f5e9"}
            />
          </div>
          <div className="col-lg-3 col-md-6 col-sm-6">
            <SummaryCard
              title={"Reservations Today"}
              value={reportData.totalReservations}
              icon={faCalendarCheck}
              bgColor={"#f3e5f5"}
            />
          </div>
          <div className="col-lg-3 col-md-6 col-sm-6">
            <SummaryCard
              title={"Table Occupancy"}
              value={reportData.totalOccupiedTables}
              icon={faChair}
              bgColor={"#e0f7fa"}
            />
          </div>
        </div>
        <div className="row g-4 mt-4">
          <div className="col-lg-7 col-md-12">
            <Analytics metrics={metrics} filter={filter} onChangeFilter={setFilter} />
          </div>
          <div className="col-lg-5 col-md-12">
            <div className="mb-4">
              <Reservations reservations={reservations} />
            </div>
            <div className="mb-4">
              <OrderStatus orders={orders} />
            </div>
            <div className="mb-4">
              <TableMap tables={tables}/>
            </div>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default AdminDashboardPage;
