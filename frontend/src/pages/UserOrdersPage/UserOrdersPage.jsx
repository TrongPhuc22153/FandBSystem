import { useState, useCallback, useEffect, useMemo } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useOrders } from "../../hooks/orderHooks";
import DataTable from "../../components/DataTableManagement/DataTable";
import Pagination from "../../components/Pagination/Pagination";
import { debounce } from "lodash";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { formatDate } from "../../utils/datetimeUtils";
import { USER_ORDERS_URI } from "../../constants/routes";
import { Badge } from "react-bootstrap";
import {
  ORDER_STATUS_CLASSES,
  ORDER_TYPE_CLASSES,
  SORTING_DIRECTIONS,
} from "../../constants/webConstant";

const UserOrdersPage = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [selectedItems, setSelectedItems] = useState([]);
  const [searchValue, setSearchValue] = useState(
    searchParams.get("searchValue") || ""
  );
  const [filterType, setFilterType] = useState("ALL");
  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const { data: ordersData, error: ordersError } = useOrders({
    page: currentPage,
    sortField: "orderDate",
    sortDirection: SORTING_DIRECTIONS.DESC,
    type: filterType !== "ALL" ? filterType : undefined,
    search: searchValue,
  });

  const orders = useMemo(() => ordersData?.content || [], [ordersData]);
  const totalPages = ordersData?.totalPages || 0;

  const orderColumns = [
    { key: "orderId", title: "Order ID" },
    {
      key: "orderDate",
      title: "Order Date",
      render: (order) => formatDate(order.orderDate),
    },
    { key: "totalPrice", title: "Total Price" },
    {
      key: "type",
      title: "Type",
      render: (order) => (
        <Badge bg={ORDER_TYPE_CLASSES[order.type]}>{order.type}</Badge>
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
      navigate(`${USER_ORDERS_URI}/${id}`);
    },
    [navigate]
  );

  // select item
  const handleSelectAll = useCallback(
    (event) => {
      const isChecked = event.target.checked;
      if (isChecked) {
        const allOrderIds = orders.map((order) => order.orderId);
        setSelectedItems(allOrderIds);
      } else {
        setSelectedItems([]);
      }
    },
    [orders]
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

  const handleFilterTypeChange = (e) => {
    const newType = e.target.value;
    setFilterType(newType);
    setCurrentPage(0);
    navigate(`?page=1`);
  };

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
                  <div className="row justify-content-end">
                    <div className="col-sm-12 col-lg-3 mb-2">
                      <div className="form-group">
                        <select
                          className="form-select"
                          value={filterType}
                          onChange={handleFilterTypeChange}
                          aria-label="Filter by order type"
                        >
                          <option value="ALL">All Types</option>
                          <option value="TAKE_AWAY">Take Away</option>
                          <option value="DINE_IN">Dine In</option>
                        </select>
                      </div>
                    </div>
                    <div className="col-sm-12 col-lg-6 mb-2">
                      <div className="form-group">
                        <input
                          className="form-control"
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
                data={orders}
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

export default UserOrdersPage;
