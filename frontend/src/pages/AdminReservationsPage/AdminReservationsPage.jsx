import React, { useState, useCallback, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useReservations } from "../../hooks/reservationHooks";
import DataTable from "../../components/DataTableManagement/DataTable";
import Pagination from "../../components/Pagination/Pagination";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { formatDate } from "../../utils/datetimeUtils";
import { Badge } from "react-bootstrap";
import { RESERVATION_STATUS_CLASSES } from "../../constants/webConstant";
import { ADMIN_RESERVATIONS_URI } from "../../constants/routes";

const AdminReservationsPage = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const currentPage = parseInt(searchParams.get("page") || "0");
  const [selectedItems, setSelectedItems] = useState([]);
  const [searchValue, setSearchValue] = useState(
    searchParams.get("searchValue") || ""
  );
  const [searchBy, setSearchBy] = useState(() => {
    const param = searchParams.get("searchBy");
    return param ? [param] : ["id"];
  });

  const {
    data: reservationsData,
    isLoading: loadingReservations,
    error: reservationsError,
  } = useReservations();

  const reservationData = reservationsData?.content || [];
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
      key: "startTime",
      title: "Start Time",
      render: (reservation) => formatDate(reservation.startTime),
    },
    {
        key: "endTime",
        title: "End Time",
        render: (reservation) => formatDate(reservation.endTime),
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

  useEffect(() => {
    const searchByParam = searchParams.get("searchBy");
    if (searchByParam) {
      setSearchBy(searchByParam.split(","));
    }
  }, [searchParams]);

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
        const allReservationIds = reservationData.map(
          (reservation) => reservation.reservationId
        );
        setSelectedItems(allReservationIds);
      } else {
        setSelectedItems([]);
      }
    },
    [reservationData]
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

  const debouncedSearch = useCallback(
    (newSearchValue) => {
      searchParams.set("searchValue", newSearchValue);
      setSearchParams(searchParams);
    },
    [setSearchParams]
  );

  const handleSearchInputChange = (e) => {
    const newSearchValue = e.target.value;
    setSearchValue(newSearchValue);
    debouncedSearch(newSearchValue);
  };

  if (loadingReservations) return <Loading />;

  if (reservationsError?.message)
    return <ErrorDisplay message={reservationsError.message} />;

  return (
    <main className="content px-5 py-3">
      <h3 className="my-3">
        <strong>Reservations</strong> {/* Adjusted heading */}
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
                          placeholder="Search Reservations" // Adjusted placeholder
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
                data={reservationData}
                columns={reservationColumns}
                selectedItems={selectedItems}
                handleSelectItem={handleSelectItem}
                handleSelectAll={handleSelectAll}
                hadnleViewItem={handleViewReservation} // Adjusted
                uniqueIdKey="reservationId" // Adjusted key
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
