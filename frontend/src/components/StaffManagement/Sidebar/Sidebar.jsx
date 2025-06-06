
import { Link, useLocation } from "react-router-dom"
import styles from "./Sidebar.module.css"

const Sidebar = ({ isOpen, onToggle }) => {
  const location = useLocation()

  const menuItems = [
    { path: "/", label: "Dashboard", icon: "📊" },
    { path: "/staff", label: "Staff List", icon: "👥" },
    { path: "/shifts", label: "Shifts", icon: "📅" },
    { path: "/payroll", label: "Payroll", icon: "💰" },
    { path: "/performance", label: "Performance", icon: "📈" },
  ]

  return (
    <>
      <div className={`${styles.sidebar} ${isOpen ? styles.open : ""}`}>
        <div className={styles.header}>
          <h2>F&B Manager</h2>
          <button className={styles.closeBtn} onClick={onToggle}>
            ×
          </button>
        </div>
        <nav className={styles.nav}>
          {menuItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`${styles.navItem} ${location.pathname === item.path ? styles.active : ""}`}
              onClick={() => window.innerWidth <= 768 && onToggle()}
            >
              <span className={styles.icon}>{item.icon}</span>
              {item.label}
            </Link>
          ))}
        </nav>
      </div>
      {isOpen && <div className={styles.overlay} onClick={onToggle}></div>}
      <button className={styles.menuToggle} onClick={onToggle}>
        ☰
      </button>
    </>
  )
}

export default Sidebar
