import React, { useState, useCallback, useEffect, useMemo } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useOrders } from "../../hooks/orderHooks";
import DataTable from "../../components/DataTableManagement/DataTable";
import Pagination from "../../components/Pagination/Pagination";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { formatDate } from "../../utils/datetimeUtils";
import { Badge } from "react-bootstrap";
import {
  ORDER_STATUS_CLASSES,
  SORTING_DIRECTIONS,
} from "../../constants/webConstant";
import { ADMIN_ORDERS_URI } from "../../constants/routes";

const AdminOrdersPage = () => {
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
    data: ordersData,
    isLoading: loadingOrders,
    error: ordersError,
  } = useOrders({
    page: currentPage,
    sortDirection: SORTING_DIRECTIONS.DESC,
    sortField: "orderDate",
  });

  const orderData = useMemo(() => 
    ordersData?.content || [], 
  [ordersData]);
  
  const totalPages = ordersData?.totalPages || 0;

  const orderColumns = [
    { key: "orderId", title: "Order ID" },
    {
      key: "customerName",
      title: "Customer Name",
      render: (order) => order.customer?.contactName || "Anonymous",
    },
    {
      key: "orderDate",
      title: "Order Date",
      render: (order) => formatDate(order.orderDate),
    },
    {
      key: "type",
      title: "type",
      render: (order) => (
        <Badge bg={ORDER_STATUS_CLASSES[order.type]}>{order.type}</Badge>
      ),
    },
    {
      key: "status",
      title: "Status",
      render: (order) => (
        <Badge bg={ORDER_STATUS_CLASSES[order.status]}>{order.status}</Badge>
      ),
    },
  ];

  const handleViewOrder = useCallback(
    (id) => {
      navigate(`${ADMIN_ORDERS_URI}/${id}`);
    },
    [navigate]
  );

  // select item
  const handleSelectAll = useCallback(
    (event) => {
      const isChecked = event.target.checked;
      if (isChecked) {
        const allOrderIds = orderData.map((order) => order.orderId);
        setSelectedItems(allOrderIds);
      } else {
        setSelectedItems([]);
      }
    },
    [orderData]
  );

  const handleSelectItem = useCallback((event, orderId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, orderId]),
      ]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== orderId)
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

  if (loadingOrders) return <Loading />;

  if (ordersError?.message)
    return <ErrorDisplay message={ordersError.message} />;

  return (
    <main className="content px-5 py-3">
      <h3 className="my-3">
        <strong>Orders</strong>
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
                          placeholder="Search Orders"
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
                data={orderData}
                columns={orderColumns}
                selectedItems={selectedItems}
                handleSelectItem={handleSelectItem}
                handleSelectAll={handleSelectAll}
                hadnleViewItem={handleViewOrder}
                uniqueIdKey="orderId"
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

export default AdminOrdersPage;
