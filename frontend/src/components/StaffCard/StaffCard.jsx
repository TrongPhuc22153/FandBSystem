import styles from "./StaffCard.module.css"

function StaffCard({ member, tables, getTableDetails }) {
  const assignedTables = member.tables
    .map((tableId) => getTableDetails(tableId))
    .filter(Boolean);

  return (
    <div className={styles.staffCard}>
      <div className={styles.cardHeader}>
        <div>
          <h3 className={styles.staffName}>{member.name}</h3>
          <p className={styles.staffSection}>Section: {member.section}</p>
        </div>
        <span
          className={`${styles.badge} ${
            assignedTables.length > 0
              ? styles.activeBadge
              : styles.inactiveBadge
          }`}
        >
          {assignedTables.length} tables
        </span>
      </div>

      {assignedTables.length > 0 ? (
        <div className={styles.assignedTables}>
          <h4 className={styles.assignedTitle}>Assigned Tables:</h4>
          <div className={styles.tableGrid}>
            {assignedTables.map((table) => (
              <div key={table.id} className={styles.tableItem}>
                <div className={styles.tableName}>Table {table.number}</div>
                <div className={styles.tableDetails}>
                  {table.seats} seats | Since {table.occupiedAt}
                </div>
              </div>
            ))}
          </div>
        </div>
      ) : (
        <p className={styles.noTables}>No tables currently assigned</p>
      )}
    </div>
  );
}
export default StaffCard;