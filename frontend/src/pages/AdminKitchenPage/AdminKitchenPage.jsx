import React, { useState, useEffect } from "react";
import OrderCard from "../../components/OrderCard/OrderCard";
import styles from "./AdminKitchenPage.module.css";
import {
  useOrdersForPreparation,
  useReservationsForPreparation,
  usePreparationActions,
} from "../../hooks/kitchenHooks";

const AdminKitchenPage = () => {
  const [activeTab, setActiveTab] = useState("orders");
  const [selectedOrderId, setSelectedOrderId] = useState(null);
  const [selectedReservationId, setSelectedReservationId] = useState(null);
  const {
    data: ordersData,
    error: ordersError,
    isLoading: isOrdersLoading,
    mutate: mutateOrders,
  } = useOrdersForPreparation();
  const {
    data: reservationsData,
    error: reservationsError,
    isLoading: isReservationsLoading,
    mutate: mutateReservations,
  } = useReservationsForPreparation();
  const {
    handleMarkOrderAsPrepared,
    markOrderError,
    markOrderLoading,
    markOrderSuccess,
    handleMarkReservationAsPrepared,
    markReservationError,
    markReservationLoading,
    markReservationSuccess,
  } = usePreparationActions();
  const [orders, setOrders] = useState([]);
  const [reservations, setReservations] = useState([]);
  const selectedOrder = orders.find((order) => order.id === selectedOrderId);
  const selectedReservation = reservations.find(
    (reservation) => reservation.id === selectedReservationId
  );

  useEffect(() => {
    if (ordersData && ordersData.content) {
      setOrders(ordersData.content);
    }
  }, [ordersData]);

  useEffect(() => {
    if (reservationsData && reservationsData.content) {
      setReservations(reservationsData.content);
    }
  }, [reservationsData]);

  const getPrimaryProductImage = (images) => {
    return images && images.length > 0 ? images[0] : "/images/default.png";
  };

  const handlePrepareOrder = async (orderId) => {
    const result = await handleMarkOrderAsPrepared(orderId);
    if (result) {
      // Optimistically update the UI or trigger a refetch
      setOrders((prevOrders) =>
        prevOrders.map((order) =>
          order.id === orderId ? { ...order, status: "Prepared" } : order
        )
      );
      setSelectedOrderId(null);
      mutateOrders(); // Re-fetch orders to get the latest state from the server
    }
  };

  const handlePrepareReservation = async (reservationId) => {
    const result = await handleMarkReservationAsPrepared(reservationId);
    if (result) {
      // Optimistically update the UI or trigger a refetch
      setReservations((prevReservations) =>
        prevReservations.map((reservation) =>
          reservation.id === reservationId
            ? { ...reservation, preparationStatus: "Prepared" }
            : reservation
        )
      );
      setSelectedReservationId(null);
      mutateReservations(); // Re-fetch reservations
    }
  };

  if (isOrdersLoading || isReservationsLoading) {
    return <div className="container-fluid">Loading...</div>;
  }

  if (ordersError || reservationsError) {
    return <div className="container-fluid">Error loading data.</div>;
  }

  return (
    <div className="container-fluid">
      <div className="row">
        <div className={`col-md-4 ${styles.sidebar}`}>
          <div className={styles.tabButtons}>
            <button
              className={`${styles.tabButton} ${
                activeTab === "orders" ? styles.active : ""
              }`}
              onClick={() => setActiveTab("orders")}
            >
              Orders
            </button>
            <button
              className={`${styles.tabButton} ${
                activeTab === "reservations" ? styles.active : ""
              }`}
              onClick={() => setActiveTab("reservations")}
            >
              Reservations
            </button>
          </div>

          {activeTab === "orders" && (
            <>
              <h2 className="h5 mb-3 text-dark">Incoming Orders</h2>
              {orders.map((order) => (
                <OrderCard
                  key={order.id}
                  order={order}
                  isSelected={selectedOrderId === order.id}
                  onSelect={setSelectedOrderId}
                />
              ))}
            </>
          )}

          {activeTab === "reservations" && (
            <>
              <h2 className="h5 mb-3 text-dark">Upcoming Reservations</h2>
              <ul className={styles.reservationList}>
                {reservations.map((reservation) => (
                  <li
                    key={reservation.id}
                    className={`${styles.reservationItem} ${
                      selectedReservationId === reservation.id
                        ? styles.selected
                        : ""
                    }`}
                    onClick={() => setSelectedReservationId(reservation.id)}
                  >
                    <h3>Reservation #{reservation.id}</h3>
                    <p>Customer: {reservation.customer}</p>
                    <p>Time: {reservation.time}</p>
                    <p>Guests: {reservation.guests}</p>
                    <p
                      className={`${styles.status} ${
                        styles[
                          `status--${reservation.preparationStatus
                            ?.toLowerCase()
                            ?.replace(" ", "")}`
                        ]
                      }`}
                    >
                      Preparation: {reservation.preparationStatus}
                    </p>
                  </li>
                ))}
              </ul>
            </>
          )}
        </div>
        <div className={`col-md-8 ${styles.mainContent}`}>
          {activeTab === "orders" && selectedOrder ? (
            <>
              <h2 className={styles.title}>
                Order #{selectedOrder.id} Products
              </h2>
              <ul className={styles.productList}>
                {selectedOrder.items.map((item) => (
                  <div
                    key={item.product.productId}
                    className="row border-top border-bottom"
                  >
                    <div className="row w-100 m-0 px-3 py-3 align-items-center">
                      <div className="col-2">
                        <img
                          className="img-fluid"
                          src={getPrimaryProductImage(item.product.images)}
                          alt={item.product.productName}
                        />
                      </div>
                      <div className="col-lg-8 col-sm-7">
                        <div className="row text-muted">
                          {item.product.productName}
                        </div>
                        <div className="row">{item.description}</div>
                        <div className="row text-muted">
                          Quantity: {item.quantity}
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </ul>
              <div className="mt-3 d-flex justify-content-end">
                <button
                  className="btn btn-primary"
                  onClick={() => handlePrepareOrder(selectedOrder.id)}
                  disabled={markOrderLoading}
                >
                  {markOrderLoading ? "Marking..." : "Mark as Prepared"}
                </button>
                {markOrderError && (
                  <p className="text-danger ml-2">
                    {JSON.stringify(markOrderError)}
                  </p>
                )}
                {markOrderSuccess && (
                  <p className="text-success ml-2">{markOrderSuccess}</p>
                )}
              </div>
            </>
          ) : activeTab === "reservations" && selectedReservation ? (
            <>
              <h2 className={styles.title}>
                Reservation #{selectedReservation.id} Details
              </h2>
              <p>Customer: {selectedReservation.customer}</p>
              <p>Time: {selectedReservation.time}</p>
              <p>Number of Guests: {selectedReservation.guests}</p>
              <h3 className="mt-3">Products for Reservation</h3>
              {selectedReservation.products &&
              selectedReservation.products.length > 0 ? (
                <ul className={styles.productList}>
                  {selectedReservation.products.map((item) => (
                    <div
                      key={item.product.productId}
                      className="row border-top border-bottom"
                    >
                      <div className="row w-100 m-0 px-3 py-3 align-items-center">
                        <div className="col-2">
                          <img
                            className="img-fluid"
                            src={getPrimaryProductImage(item.product.images)}
                            alt={item.product.productName}
                          />
                        </div>
                        <div className="col-lg-8 col-sm-7">
                          <div className="row text-muted">
                            {item.product.productName}
                          </div>
                          <div className="row">{item.description}</div>
                          <div className="row text-muted">
                            Quantity: {item.quantity}
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </ul>
              ) : (
                <p>No products associated with this reservation.</p>
              )}
              <div className="mt-3 d-flex justify-content-end">
                <button
                  className="btn btn-primary mr-2"
                  onClick={() =>
                    handlePrepareReservation(selectedReservation.id)
                  }
                  disabled={markReservationLoading}
                >
                  {markReservationLoading
                    ? "Marking..."
                    : "Mark Reservation Prepared"}
                </button>
                {markReservationError && (
                  <p className="text-danger ml-2">
                    {JSON.stringify(markReservationError)}
                  </p>
                )}
                {markReservationSuccess && (
                  <p className="text-success ml-2">{markReservationSuccess}</p>
                )}
                <button
                  className="ms-2 btn btn-outline-secondary"
                  onClick={() => setSelectedReservationId(null)}
                >
                  Close Reservation
                </button>
              </div>
            </>
          ) : (
            <p className={styles.noSelection}>
              {activeTab === "orders"
                ? "Select an order to view products"
                : "Select a reservation to view details and products"}
            </p>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminKitchenPage;
