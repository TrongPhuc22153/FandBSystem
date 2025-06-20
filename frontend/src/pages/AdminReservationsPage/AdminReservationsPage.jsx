import { useState, useCallback, useEffect, useMemo } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useReservations } from "../../hooks/reservationHooks";
import DataTable from "../../components/DataTableManagement/DataTable";
import Pagination from "../../components/Pagination/Pagination";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { Badge } from "react-bootstrap";
import { RESERVATION_STATUS_CLASSES, SORTING_DIRECTIONS } from "../../constants/webConstant";
import { ADMIN_RESERVATIONS_URI } from "../../constants/routes";
import { debounce } from "lodash";

const AdminReservationsPage = () => {
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

  const {
    data: reservationsData,
    error: reservationsError,
  } = useReservations({
    currentPage: currentPage,
    direction: SORTING_DIRECTIONS.DESC,
    sortBy: "createdAt",
    search: searchValue
  });

  const reservations = useMemo(
    () => reservationsData?.content || [],
    [reservationsData]
  );
  const totalPages = reservationsData?.totalPages || 0;

  const reservationColumns = [
    { key: "reservationId", title: "Reservation ID" },
    {
      key: "customerName",
      title: "Customer Name",
      render: (reservation) => reservation.customer.contactName || "Anonymous",
    },
    {
      key: "tableNumber",
      title: "Table Number",
      render: (reservation) => reservation.table?.tableNumber || "N/A",
    },
    {
      key:"date",
      title: "Reservation date",
      render: (reservation) => {
        const date = new Date(reservation.date);
        return date.toLocaleDateString()
      }
    },
    {
      key: "startTime",
      title: "Start Time",
      render: (reservation) => reservation.startTime
    },
    {
      key: "endTime",
      title: "End Time",
      render: (reservation) => reservation.endTime
    },
    {
      key: "status",
      title: "Status",
      render: (reservation) => (
        <Badge bg={RESERVATION_STATUS_CLASSES[reservation.status]}>
          {reservation.status}
        </Badge>
      ),
    },
  ];

  const handleViewReservation = useCallback(
    (id) => {
      navigate(`${ADMIN_RESERVATIONS_URI}/${id}`);
    },
    [navigate]
  );

  // select item
  const handleSelectAll = useCallback(
    (event) => {
      const isChecked = event.target.checked;
      if (isChecked) {
        const allReservationIds = reservations.map(
          (reservation) => reservation.reservationId
        );
        setSelectedItems(allReservationIds);
      } else {
        setSelectedItems([]);
      }
    },
    [reservations]
  );

  const handleSelectItem = useCallback((event, reservationId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, reservationId]),
      ]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== reservationId)
      );
    }
  }, []);

  const debouncedSearch = useCallback(() =>
    debounce((newSearchValue) => {
      searchParams.set("searchValue", newSearchValue);
      searchParams.set("page", "1");
      setSearchParams(searchParams);
      setCurrentPage(0);
    }, 300),
    [setSearchParams, searchParams]
  );

  const handleSearchInputChange = (e) => {
    const newSearchValue = e.target.value;
    setSearchValue(newSearchValue);
    debouncedSearch(newSearchValue);
  };

  if (reservationsError?.message)
    return <ErrorDisplay message={reservationsError.message} />;

  return (
    <main className="content px-5 py-3">
      <h3 className="my-3">
        <strong>Reservations</strong>
      </h3>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill p-3">
              <div className="row mb-3">
                <div className="col-sm-5"></div>
                <div className="col-sm-7">
                  <div className="row">
                    <div className="col-sm-12 col-md-6 mb-2"></div>
                    <div className="col-sm-12 col-md-6 mb-2">
                      <div className="form-group">
                        <input
                          className="form-control me-2"
                          type="search"
                          placeholder="Search Reservations"
                          aria-label="Search"
                          value={searchValue}
                          onChange={handleSearchInputChange}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <DataTable
                data={reservations}
                columns={reservationColumns}
                selectedItems={selectedItems}
                handleSelectItem={handleSelectItem}
                handleSelectAll={handleSelectAll}
                hadnleViewItem={handleViewReservation}
                uniqueIdKey="reservationId"
              />

              {totalPages > 1 && (
                <Pagination
                  totalPages={totalPages}
                  currentPage={currentPage + 1}
                />
              )}
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default AdminReservationsPage;
