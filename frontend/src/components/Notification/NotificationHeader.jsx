import styles from "./notification-header.module.css";

export default function NotificationHeader({
  filter,
  setFilter,
  markAllAsRead,
  unreadCount,
}) {
  return (
    <div className={styles.header}>
      <div className="row align-items-center">
        <div className="col-md-6">
          <h1 className={styles.title}>
            Notifications
            {unreadCount > 0 && (
              <span className={styles.badge}>{unreadCount}</span>
            )}
          </h1>
        </div>
        <div className="col-md-6 d-flex justify-content-md-end mt-3 mt-md-0">
          <div className={styles.actions}>
            <button
              className={`btn ${styles.markReadBtn}`}
              onClick={markAllAsRead}
              disabled={unreadCount === 0}
            >
              Mark all as read
            </button>
            <div className={styles.filterContainer}>
              <select
                className={`form-select ${styles.filterSelect}`}
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
              >
                <option value="all">All notifications</option>
                <option value="unread">Unread</option>
              </select>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
