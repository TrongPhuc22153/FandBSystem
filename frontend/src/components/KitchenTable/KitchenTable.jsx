import { useState } from "react"
import OrdersTable from "../OrdersTable/OrdersTable"
import ReservationsTable from "../ReservationsTable/ReservationsTable"
// import MenuItemsTable from "./menu-items-table"
import styles from "./kitchen-table.module.css"

export default function KitchenTable() {
  const [activeTab, setActiveTab] = useState("orders")

  return (
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
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "menuItems" ? "active" : ""}`}
            onClick={() => setActiveTab("menuItems")}
          >
            Menu Items & Inventory
          </button>
        </li>
      </ul>

      <div className={styles.tabContent}>
        {activeTab === "orders" && <OrdersTable />}
        {activeTab === "reservations" && <ReservationsTable />}
        {/* {activeTab === "menuItems" && <MenuItemsTable />} */}
      </div>
    </div>
  )
}
