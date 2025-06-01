import { useState, useEffect, useCallback, useMemo } from "react";
import styles from "./kitchen-table.module.css";
import OrderDetailModal from "./OrderDetailModal";
import { Badge } from "react-bootstrap";
import {
  ORDER_ACTIONS,
  ORDER_STATUS_CLASSES,
  ORDER_STATUSES,
  ORDER_TYPE_CLASSES,
  SORTING_DIRECTIONS,
} from "../../constants/webConstant";
import { useOrderActions, useOrders } from "../../hooks/orderHooks";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";
import { useSearchParams } from "react-router-dom";
import Pagination from "../Pagination/Pagination";
import { TOPIC_KITCHEN } from "../../constants/webSocketEnpoint";
import { useAuth } from "../../context/AuthContext";
import { useStompSubscription } from "../../hooks/websocketHooks";
import { hasRole } from "../../utils/authUtils";
import { ROLES } from "../../constants/roles";

export default function OrdersTable() {
  const [searchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const [filterStatus, setFilterStatus] = useState(null);
  const [selectedOrder, setSelectedOrder] = useState(null);

  const { data: ordersData, mutate } = useOrders({
    status: filterStatus,
    sortDirection: SORTING_DIRECTIONS.ASC,
    sortField: "orderDate",
    page: currentPage,
  });
  const orders = useMemo(() => ordersData?.content || [], [ordersData]);
  const totalPages = ordersData?.totalPages || 0;

  const { handleProcessOrder, processError, processSuccess, resetProcess } =
    useOrderActions();

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (processError?.message) {
      showNewAlert({
        message: processError.message,
        variant: "danger",
      });
    }
  }, [processError]);

  useEffect(() => {
    if (processSuccess) {
      showNewAlert({
        message: processSuccess,
        action: resetProcess,
      });
    }
  }, [processSuccess]);

  const updateOrderStatus = useCallback(
    async (orderId, action, type) => {
      const res = await handleProcessOrder(orderId, action, type);
      if (res) {
        mutate();
        closeOrderDetail();
      }
    },
    [mutate, handleProcessOrder]
  );

  const showConfirmModal = useCallback(
    (orderId, action, type) => {
      onOpen({
        title: "Process order",
        message: "Do you want to continue?",
        onYes: () => updateOrderStatus(orderId, action, type),
      });
    },
    [onOpen, updateOrderStatus]
  );

  const handleOrderClick = (order) => {
    setSelectedOrder(order);
  };

  const closeOrderDetail = useCallback(() => {
    setSelectedOrder(null);
  }, []);

  const { user } = useAuth();

  const handleMessage = useCallback((newNotification) => {
    try {
      if (!newNotification?.id) {
        return;
      }

    } catch (error) {
      console.error("Error processing notification:", error);
    }
  }, []);

  useStompSubscription({
    topic: TOPIC_KITCHEN,
    onMessage: handleMessage,
    shouldSubscribe: hasRole(user, ROLES.EMPLOYEE),
  });

  return (
    <>
      <div className="d-flex justify-content-between mb-3">
        <h3>Incoming Orders</h3>
        <div className="btn-group">
          <button
            className={`btn ${
              filterStatus === null ? "btn-primary" : "btn-outline-primary"
            }`}
            onClick={() => setFilterStatus(null)}
          >
            All
          </button>
          <button
            className={`btn ${
              filterStatus === ORDER_STATUSES.PENDING
                ? "btn-primary"
                : "btn-outline-primary"
            }`}
            onClick={() => setFilterStatus(ORDER_STATUSES.PENDING)}
          >
            {ORDER_STATUSES.PENDING}
          </button>
          <button
            className={`btn ${
              filterStatus === ORDER_STATUSES.PREPARING
                ? "btn-primary"
                : "btn-outline-primary"
            }`}
            onClick={() => setFilterStatus(ORDER_STATUSES.PREPARING)}
          >
            {ORDER_STATUSES.PREPARING}
          </button>
          <button
            className={`btn ${
              filterStatus === ORDER_STATUSES.PREPARED
                ? "btn-primary"
                : "btn-outline-primary"
            }`}
            onClick={() => setFilterStatus(ORDER_STATUSES.PREPARED)}
          >
            {ORDER_STATUSES.PREPARED}
          </button>
        </div>
      </div>

      <div className="table-responsive">
        <table className="table table-striped table-hover">
          <thead className="table-dark">
            <tr>
              <th>Order ID</th>
              <th>Table</th>
              <th>Customer</th>
              <th>Items</th>
              <th>Order Time</th>
              <th>Type</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {orders.length === 0 ? (
              <tr>
                <td colSpan={7} className="text-center">
                  No orders found
                </td>
              </tr>
            ) : (
              orders.map((order) => (
                <tr
                  key={order.orderId}
                  className={`${styles.orderRow} cursor-pointer`}
                  onClick={() => handleOrderClick(order)}
                  style={{ cursor: "pointer" }}
                >
                  <td>{order.orderId}</td>
                  <td>{order?.tableOccupancy?.table.tableNumber}</td>
                  <td>{order?.customer?.profile.user.username || order?.tableOccupancy.contactName || "UNKNOW"}</td>
                  <td>
                    <ul className={styles.itemsList}>
                      {order.orderDetails.map((item, index) => (
                        <li key={index}>
                          {item.quantity}x {item.product?.productName}
                          {item.specialInstructions && (
                            <small className="d-block text-muted">
                              Note: {item?.specialInstructions}
                            </small>
                          )}
                        </li>
                      ))}
                    </ul>
                  </td>
                  <td>
                    {new Date(order.orderDate).toLocaleTimeString()}
                    <small className="d-block text-muted">
                      Est. completion:{" "}
                      {new Date(
                        order.estimatedCompletionTime
                      ).toLocaleTimeString()}
                    </small>
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
                      {order.status.charAt(0).toUpperCase() +
                        order.status.slice(1)}
                    </Badge>
                  </td>
                  <td onClick={(e) => e.stopPropagation()}>
                    <div className="btn-group">
                      {order.status === ORDER_STATUSES.PENDING && (
                        <button
                          className="btn btn-sm btn-outline-warning"
                          onClick={() =>
                            showConfirmModal(
                              order.orderId,
                              ORDER_ACTIONS.PREPARING,
                              order.type
                            )
                          }
                        >
                          Start Preparing
                        </button>
                      )}
                      {order.status === ORDER_STATUSES.PREPARING && (
                        <button
                          className="btn btn-sm btn-outline-success"
                          onClick={() =>
                            showConfirmModal(
                              order.orderId,
                              ORDER_ACTIONS.READY,
                              order.type
                            )
                          }
                        >
                          Mark Ready
                        </button>
                      )}
                      {order.status === ORDER_STATUSES.PREPARED && (
                        <button
                          className="btn btn-sm btn-outline-secondary"
                          onClick={() =>
                            showConfirmModal(
                              order.orderId,
                              ORDER_ACTIONS.COMPLETE,
                              order.type
                            )
                          }
                        >
                          Complete
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
        {totalPages > 1 && (
          <Pagination totalPages={totalPages} currentPage={currentPage + 1} />
        )}
      </div>

      {selectedOrder && (
        <OrderDetailModal
          order={selectedOrder}
          onClose={closeOrderDetail}
          onUpdateStatus={showConfirmModal}
        />
      )}
    </>
  );
}
