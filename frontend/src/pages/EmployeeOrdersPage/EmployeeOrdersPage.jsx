import { useCallback, useEffect, useMemo, useState } from "react";
import OrderModal from "../../components/OrderModal/OrderModal";
import styles from "./EmployeeOrdersPage.module.css";
import { useOrder, useOrderActions, useOrders } from "../../hooks/orderHooks";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { Badge } from "react-bootstrap";
import {
  ORDER_ACTIONS,
  ORDER_STATUS_CLASSES,
  ORDER_STATUSES,
  ORDER_TYPE_CLASSES,
  ORDER_TYPES,
} from "../../constants/webConstant";
import Loading from "../../components/Loading/Loading";
import { useAlert } from "../../context/AlertContext";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye } from "@fortawesome/free-solid-svg-icons";
import Pagination from "../../components/Pagination/Pagination";
import { useModal } from "../../context/ModalContext";
import { useAuth } from "../../context/AuthContext";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";
import { useNavigate, useSearchParams } from "react-router-dom";
import { Search } from "lucide-react";
import { debounce } from "lodash";

export default function EmployeeOrdersPage() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [searchValue, setSearchValue] = useState(
    searchParams.get("searchValue") || ""
  );
  const [filterType, setFilterType] = useState("ALL");
  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  const debouncedSearch = useCallback(
    debounce((newSearchValue) => {
      searchParams.set("searchValue", newSearchValue);
      searchParams.set("page", "1");
      setSearchParams(searchParams);
      setCurrentPage(1);
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

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const [selectedOrderId, setSelectedOrderId] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const { user } = useAuth();

  const {
    data: ordersData,
    error: ordersError,
    mutate: refetchOrders,
  } = useOrders({
    page: currentPage,
    sortField: "orderDate",
    search: searchValue,
    type: filterType !== "ALL" ? filterType : undefined,
  });

  const {
    data: orderDetails,
    error: orderError,
    isLoading: orderLoading,
    mutate: refetchOrder,
  } = useOrder({
    orderId: selectedOrderId,
  });

  const {
    handleProcessOrder,
    processError,
    processSuccess,
    processLoading,
    resetProcess,
  } = useOrderActions();

  const { showNewAlert } = useAlert();
  const { onOpen } = useModal();

  useEffect(() => {
    if (orderError?.message) {
      showNewAlert({
        message: orderError.message,
        variant: "danger",
      });
    }
  }, [orderError]);

  useEffect(() => {
    if (processError?.message) {
      showNewAlert({
        message: processError.message,
        variant: "danger",
        action: resetProcess,
      });
    }
  }, [processError, showNewAlert, resetProcess]);

  useEffect(() => {
    if (processSuccess) {
      showNewAlert({
        message: processSuccess || "Reservation processed successfully.",
        variant: "success",
        action: () => {
          resetProcess();
        },
      });
    }
  }, [processSuccess, showNewAlert, resetProcess]);

  const totalPages = useMemo(() => ordersData?.totalPages || 0, [ordersData]);
  const orders = useMemo(() => ordersData?.content || [], [ordersData]);
  
  const handleOrderClick = (orderId) => {
    setSelectedOrderId(orderId);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedOrderId(null);
  };

  const onCompleteOrder = useCallback(
    async (order) => {
      const result = await handleProcessOrder(
        order.orderId,
        ORDER_ACTIONS.COMPLETE,
        order.type
      );
      if (result) {
        refetchOrder();
        refetchOrders();
      }
    },
    [refetchOrder, refetchOrders, handleProcessOrder]
  );

  const showConfirmOrder = useCallback(
    (order) => {
      onOpen({
        title: "Confirm Order",
        message: "Do you want to confirm this order?",
        onYes: () => onCompleteOrder(order),
      });
    },
    [onCompleteOrder, onOpen]
  );

  if (ordersError?.message) {
    return <ErrorDisplay message={ordersError.message} />;
  }

  if (processLoading) {
    return <Loading />;
  }

  return (
    <div className={`container-fluid ${styles.ordersContainer}`}>
      <div className="row">
        <div className="col-12">
          <div className={styles.header}>
            <h1 className="h2 mb-4">Orders Management</h1>
          </div>

          <div className={`card ${styles.ordersCard}`}>
            <div className="card-header d-flex justify-content-between align-items-center">
              <h5 className="mb-0">Orders List</h5>
              <div className="row">
                <div className="col-md-5">
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
                <div className="col-md-7">
                  <div className={styles.searchContainer}>
                    <Search className={styles.searchIcon} />
                    <input
                      type="text"
                      placeholder="Search orders..."
                      className={styles.searchInput}
                      value={searchValue}
                      onChange={handleSearchInputChange}
                    />
                  </div>
                </div>
              </div>
            </div>

            <div className="card-body p-0">
              <div className="table-responsive">
                <table className="table table-hover mb-0">
                  <thead className="table-light">
                    <tr>
                      <th>Order ID</th>
                      <th>Customer</th>
                      <th>Date</th>
                      <th>Type</th>
                      <th>Status</th>
                      <th>Total</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders.map((order) => (
                      <tr key={order.orderId} className={styles.orderRow}>
                        <td>
                          {order.orderId}
                        </td>
                        <td>
                          <div>
                            <div className="fw-medium">
                              {order?.customer?.contactName ||
                                order?.tableOccupancy?.contactName ||
                                "UNKNOWN"}
                            </div>
                            <small className="text-muted">
                              {order.type === ORDER_TYPES.TAKE_AWAY &&
                                order?.customer?.profile?.user?.email}
                              {order.type === ORDER_TYPES.DINE_IN &&
                                order?.tableOccupancy?.phone}
                            </small>
                          </div>
                        </td>
                        <td>
                          {new Date(order.orderDate).toLocaleDateString(
                            "en-US",
                            {
                              year: "numeric",
                              month: "short",
                              day: "numeric",
                            }
                          )}
                        </td>
                        <td>
                          <Badge
                            bg={
                              ORDER_TYPE_CLASSES[order.type] ||
                              ORDER_TYPE_CLASSES.DEFAULT
                            }
                          >
                            {order.type}
                          </Badge>
                        </td>
                        <td>
                          <Badge
                            bg={
                              ORDER_STATUS_CLASSES[order.status] ||
                              ORDER_STATUS_CLASSES.DEFAULT
                            }
                          >
                            {order.status}
                          </Badge>
                        </td>
                        <td>
                          ${order.totalPrice.toFixed(2)}
                        </td>
                        <td>
                          <button
                            className="btn btn-outline-primary btn-sm"
                            onClick={() => handleOrderClick(order.orderId)}
                          >
                            <FontAwesomeIcon icon={faEye} />
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            <div className="card-footer">
              <Pagination
                totalPages={totalPages}
                currentPage={currentPage + 1}
              />
            </div>
          </div>
        </div>
      </div>

      {!orderDetails && orderLoading ? (
        <Loading />
      ) : (
        showModal &&
        selectedOrderId && (
          <OrderModal
            order={orderDetails}
            show={showModal}
            renderOrderActions={
              hasRole(user, ROLES.EMPLOYEE) &&
              orderDetails.type === ORDER_TYPES.TAKE_AWAY &&
              orderDetails.status === ORDER_STATUSES.READY_TO_PICKUP
            }
            onClose={handleCloseModal}
            onCompleteOrder={showConfirmOrder}
          />
        )
      )}
    </div>
  );
}
