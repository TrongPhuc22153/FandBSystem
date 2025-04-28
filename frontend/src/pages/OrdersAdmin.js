import {
  faEye,
  faPenToSquare,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { Link } from "react-router";
import config from "../config/WebConfig";
import { Pagination } from "../components/Pagination";

const dummyOrders = [
  {
    id: "ORD001",
    date: "2025-03-30",
    payment_status: "Paid",
    total: 45.99,
    payment_method: "Credit Card",
    status: "Delivered",
  },
  {
    id: "ORD002",
    date: "2025-03-29",
    payment_status: "Pending",
    total: 89.5,
    payment_method: "PayPal",
    status: "Processing",
  },
  {
    id: "ORD003",
    date: "2025-03-28",
    payment_status: "Failed",
    total: 32.75,
    payment_method: "Bank Transfer",
    status: "Cancelled",
  },
  {
    id: "ORD004",
    date: "2025-03-27",
    payment_status: "Paid",
    total: 120.0,
    payment_method: "Debit Card",
    status: "Shipped",
  },
  {
    id: "ORD005",
    date: "2025-03-26",
    payment_status: "Refunded",
    total: 60.25,
    payment_method: "Credit Card",
    status: "Refunded",
  },
];

export default function OrdersAdmin() {
  const [orders, setOrders] = useState([...dummyOrders]);
  const [selectedItems, setSelectedItems] = useState([]);
  const [selectedOrderStatus, setSelectedOrderStatus] = useState("Total");
  const [searchValue, setSearchValue] = useState("");
  // Select Items
  const handleSelectAll = (event) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      // Select all: Get all order IDs and update state
      const allorderIds = orders.map((order) => order.id);
      setSelectedItems(allorderIds);
    } else {
      // Deselect all: Clear the selected items state
      setSelectedItems([]);
    }
  };

  // Select single item

  const handleSelectItem = (event, orderId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      // Add the orderId to the selected list (prevent duplicates just in case)
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, orderId]),
      ]);
    } else {
      // Remove the orderId from the selected list
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== orderId)
      );
    }
  };
  //  Table action
  const handleDeleteItem = (id) => { };
  const handleUpdateItem = (id) => { };
  const handleViewItem = (id) => { };

  // Search orders
  const searchOrder = (e) => {
    e.preventDefault();
    const searchTerm = searchValue.toLowerCase();
    const filteredOrders = dummyOrders.filter((order) =>
      order.id.toLowerCase().includes(searchTerm)
    );
    setOrders(filteredOrders);
  };

  return (
    <main className="content overflow-scroll px-5 py-3">
      <h1 className="h3 my-3">
        <strong>Orders</strong>
      </h1>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill">
              <div className="row mb-3">
                <div className="col-sm-5">
                  <form className="form-inline d-flex">
                    <div className="col-md-6">
                      <input
                        className="form-control mr-sm-2"
                        type="search"
                        placeholder="Search"
                        aria-label="Search"
                        value={searchValue}
                        onChange={(e) => setSearchValue(e.target.value)}
                      />
                    </div>
                    <div className="col-md-6 px-3 d-flex">
                      <label
                        htmlFor="status"
                        className="d-flex align-items-center me-3"
                      >
                        Status:
                      </label>
                      <select
                        className="form-control"
                        name="status"
                        value={selectedOrderStatus}
                        onChange={(e) => setSelectedOrderStatus(e.target.value)}
                      >
                        {config.order_status.statuses.map((status, index) => (
                          <option key={index} value={status}>
                            {status}
                          </option>
                        ))}
                      </select>
                    </div>
                  </form>
                </div>
                <div className="col-sm-7">
                  <button className="btn btn-info float-end">Export</button>
                  <Link to={"#"} className="me-2 btn btn-success float-end">
                    Add New Order
                  </Link>
                </div>
              </div>

              <table id="products-table" className="table table-hover my-0">
                <thead>
                  <tr>
                    <th>
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          style={{
                            width: "20px",
                            height: "20px",
                            padding: "0",
                          }}
                          type="checkbox"
                          value=""
                          onChange={handleSelectAll}
                        />
                      </div>
                    </th>
                    <th>Order ID</th>
                    <th>Date</th>
                    <th>Payment Status</th>
                    <th>Total</th>
                    <th>Payment Method</th>
                    <th>Order Status</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map((order, index) => (
                    <tr
                      key={index}
                      className={
                        selectedItems.includes(order.id) ? "table-active" : ""
                      }
                    >
                      <td>
                        <div className="form-check">
                          <input
                            className="form-check-input"
                            style={{
                              width: "20px",
                              height: "20px",
                              padding: "0",
                            }}
                            type="checkbox"
                            value={order.id}
                            checked={selectedItems.includes(order.id)}
                            onChange={(e) => handleSelectItem(e, order.id)}
                          />
                        </div>
                      </td>
                      <td>#{order.id}</td>
                      <td>{order.date}</td>
                      <td>
                        <span
                          className={
                            config.paymentStatusClasses[order.payment_status] ||
                            "badge bg-secondary"
                          }
                        >
                          {order.payment_status || "Unknown"}
                        </span>
                      </td>
                      <td>{order.total}</td>
                      <td>{order.payment_method}</td>
                      <td>
                        <span
                          className={
                            config.order_status.classes[order.status] ||
                            "badge bg-secondary"
                          }
                        >
                          {order.status || "Unknown"}
                        </span>
                      </td>
                      <td>
                        <button
                          className="btn"
                          onClick={() => handleViewItem(order.id)}
                        >
                          <FontAwesomeIcon icon={faEye} />
                        </button>
                        <button
                          className="btn"
                          onClick={() => handleUpdateItem(order.id)}
                        >
                          <FontAwesomeIcon icon={faPenToSquare} />
                        </button>
                        <button
                          className="btn"
                          onClick={() => handleDeleteItem(order.id)}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <Pagination />
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
