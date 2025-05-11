import { useState } from "react";
import styles from "./StaffAssignment.module.css";
import StaffCard from "../StaffCard/StaffCard";

export function StaffAssignment({ staff, tables }) {
  // Group staff by section
  const sections = [...new Set(staff.map((s) => s.section))];
  const [activeTab, setActiveTab] = useState("all");

  const getTableDetails = (tableId) => {
    return tables.find((t) => t.id === tableId);
  };

  const getFilteredStaff = () => {
    if (activeTab === "all") return staff;
    return staff.filter(
      (s) => s.section.toLowerCase() === activeTab.toLowerCase()
    );
  };

  return (
    <div className={styles.container}>
      <div className={styles.tabs}>
        <button
          className={`${styles.tab} ${
            activeTab === "all" ? styles.activeTab : ""
          }`}
          onClick={() => setActiveTab("all")}
        >
          All Staff
        </button>
        {sections.map((section) => (
          <button
            key={section}
            className={`${styles.tab} ${
              activeTab === section.toLowerCase() ? styles.activeTab : ""
            }`}
            onClick={() => setActiveTab(section.toLowerCase())}
          >
            {section}
          </button>
        ))}
      </div>

      <div className={styles.staffGrid}>
        {getFilteredStaff().map((member) => (
          <StaffCard
            key={member.id}
            member={member}
            tables={tables}
            getTableDetails={getTableDetails}
          />
        ))}
      </div>
    </div>
  );
}
