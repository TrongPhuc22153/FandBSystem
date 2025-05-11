import { useState } from "react";
import OrdersTable from "../../components/OrdersTable/OrdersTable";
import ReservationsTable from "../../components/ReservationsTable/ReservationsTable";
import styles from "./kitchen-table.module.css"

export default function EmployeeKitchenPage() {
  const [activeTab, setActiveTab] = useState("orders")
  
  return (
    <main className="container-fluid py-4">
      <h1 className="mb-4">Kitchen Management System</h1>
      <div className={styles.kitchenTable}>
      <ul className="nav nav-tabs mb-4">
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "orders" ? "active" : ""}`}
            onClick={() => setActiveTab("orders")}
          >
            Incoming Orders
          </button>
        </li>
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "reservations" ? "active" : ""}`}
            onClick={() => setActiveTab("reservations")}
          >
            Reservations
          </button>
        </li>
        {/* <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "menuItems" ? "active" : ""}`}
            onClick={() => setActiveTab("menuItems")}
          >
            Menu Items & Inventory
          </button>
        </li> */}
      </ul>

      <div className={styles.tabContent}>
        {activeTab === "orders" && <OrdersTable />}
        {activeTab === "reservations" && <ReservationsTable />}
        {/* {activeTab === "menuItems" && <MenuItemsTable />} */}
      </div>
    </div>
    </main>
  );
}
