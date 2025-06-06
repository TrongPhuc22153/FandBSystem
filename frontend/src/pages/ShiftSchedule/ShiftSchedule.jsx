
import { useState } from "react"
import Card from "../../components/StaffManagement/Card/Card"
import Button from "../../components/StaffManagement/Button/Button"
import styles from "./ShiftSchedule.module.css"

export const mockStaff = [
  {
    id: "1",
    fullName: "John Smith",
    email: "john.smith@restaurant.com",
    phone: "+1 (555) 123-4567",
    gender: "Male",
    dateOfBirth: "1990-05-15",
    jobTitle: "Head Chef",
    department: "Kitchen",
    employmentType: "Full-time",
    hireDate: "2020-01-15",
    workShift: "Morning",
    status: "Active",
    salary: 65000,
    address: "123 Main St, City, State 12345",
    emergencyContact: "Jane Smith - +1 (555) 987-6543",
  },
  {
    id: "2",
    fullName: "Sarah Johnson",
    email: "sarah.johnson@restaurant.com",
    phone: "+1 (555) 234-5678",
    gender: "Female",
    dateOfBirth: "1995-08-22",
    jobTitle: "Server",
    department: "Front of House",
    employmentType: "Part-time",
    hireDate: "2021-03-10",
    workShift: "Evening",
    status: "Active",
    salary: 35000,
    address: "456 Oak Ave, City, State 12345",
    emergencyContact: "Mike Johnson - +1 (555) 876-5432",
  },
  {
    id: "3",
    fullName: "Mike Davis",
    email: "mike.davis@restaurant.com",
    phone: "+1 (555) 345-6789",
    gender: "Male",
    dateOfBirth: "1988-12-03",
    jobTitle: "Host",
    department: "Front of House",
    employmentType: "Full-time",
    hireDate: "2019-11-20",
    workShift: "Split",
    status: "On Leave",
    salary: 40000,
    address: "789 Pine St, City, State 12345",
    emergencyContact: "Lisa Davis - +1 (555) 765-4321",
  },
  {
    id: "4",
    fullName: "Emily Chen",
    email: "emily.chen@restaurant.com",
    phone: "+1 (555) 456-7890",
    gender: "Female",
    dateOfBirth: "1992-07-18",
    jobTitle: "Sous Chef",
    department: "Kitchen",
    employmentType: "Full-time",
    hireDate: "2020-09-05",
    workShift: "Evening",
    status: "Active",
    salary: 55000,
    address: "321 Elm St, City, State 12345",
    emergencyContact: "David Chen - +1 (555) 654-3210",
  },
]

export const mockShifts = [
  {
    id: "1",
    staffId: "1",
    date: "2024-01-15",
    startTime: "06:00",
    endTime: "14:00",
    position: "Head Chef",
  },
  {
    id: "2",
    staffId: "2",
    date: "2024-01-15",
    startTime: "17:00",
    endTime: "23:00",
    position: "Server",
    tableAssignment: "Tables 1-5",
  },
  {
    id: "3",
    staffId: "4",
    date: "2024-01-15",
    startTime: "15:00",
    endTime: "23:00",
    position: "Sous Chef",
  },
]


const ShiftSchedule = () => {
  const [selectedWeek, setSelectedWeek] = useState(new Date())
  const [selectedDepartment, setSelectedDepartment] = useState("")

  const getWeekDates = (date) => {
    const week = []
    const startDate = new Date(date)
    startDate.setDate(date.getDate() - date.getDay())

    for (let i = 0; i < 7; i++) {
      const day = new Date(startDate)
      day.setDate(startDate.getDate() + i)
      week.push(day)
    }
    return week
  }

  const weekDates = getWeekDates(selectedWeek)
  const dayNames = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]

  const getShiftsForDate = (date) => {
    const dateString = date.toISOString().split("T")[0]
    return mockShifts.filter((shift) => shift.date === dateString)
  }

  const getStaffName = (staffId) => {
    const staff = mockStaff.find((s) => s.id === staffId)
    return staff ? staff.fullName : "Unknown"
  }

  const filteredStaff = selectedDepartment
    ? mockStaff.filter((staff) => staff.department === selectedDepartment)
    : mockStaff

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h1>Shift Schedule</h1>
        <div className={styles.controls}>
          <select
            value={selectedDepartment}
            onChange={(e) => setSelectedDepartment(e.target.value)}
            className={styles.select}
          >
            <option value="">All Departments</option>
            <option value="Kitchen">Kitchen</option>
            <option value="Front of House">Front of House</option>
            <option value="Management">Management</option>
            <option value="Bar">Bar</option>
          </select>
          <Button variant="primary">Assign Shift</Button>
        </div>
      </div>

      <Card>
        <div className={styles.weekNavigation}>
          <Button
            variant="secondary"
            onClick={() => {
              const prevWeek = new Date(selectedWeek)
              prevWeek.setDate(selectedWeek.getDate() - 7)
              setSelectedWeek(prevWeek)
            }}
          >
            Previous Week
          </Button>
          <h3>
            Week of {weekDates[0].toLocaleDateString()} - {weekDates[6].toLocaleDateString()}
          </h3>
          <Button
            variant="secondary"
            onClick={() => {
              const nextWeek = new Date(selectedWeek)
              nextWeek.setDate(selectedWeek.getDate() + 7)
              setSelectedWeek(nextWeek)
            }}
          >
            Next Week
          </Button>
        </div>

        <div className={styles.calendar}>
          {weekDates.map((date, index) => {
            const shifts = getShiftsForDate(date)
            const isToday = date.toDateString() === new Date().toDateString()

            return (
              <div key={index} className={`${styles.day} ${isToday ? styles.today : ""}`}>
                <div className={styles.dayHeader}>
                  <div className={styles.dayName}>{dayNames[index]}</div>
                  <div className={styles.dayDate}>{date.getDate()}</div>
                </div>
                <div className={styles.shifts}>
                  {shifts.map((shift) => {
                    const staff = mockStaff.find((s) => s.id === shift.staffId)
                    if (selectedDepartment && staff?.department !== selectedDepartment) {
                      return null
                    }

                    return (
                      <div key={shift.id} className={styles.shift}>
                        <div className={styles.shiftTime}>
                          {shift.startTime} - {shift.endTime}
                        </div>
                        <div className={styles.shiftStaff}>{getStaffName(shift.staffId)}</div>
                        <div className={styles.shiftPosition}>{shift.position}</div>
                        {shift.tableAssignment && <div className={styles.shiftTable}>{shift.tableAssignment}</div>}
                      </div>
                    )
                  })}
                  {shifts.length === 0 && <div className={styles.noShifts}>No shifts assigned</div>}
                </div>
              </div>
            )
          })}
        </div>
      </Card>

      <Card>
        <h3>Staff Availability</h3>
        <div className={styles.staffList}>
          {filteredStaff.map((staff) => (
            <div key={staff.id} className={styles.staffItem}>
              <div className={styles.staffInfo}>
                <div className={styles.staffName}>{staff.fullName}</div>
                <div className={styles.staffRole}>{staff.jobTitle}</div>
              </div>
              <div className={styles.staffStatus}>
                <span className={`${styles.status} ${styles[staff.status.toLowerCase().replace(" ", "")]}`}>
                  {staff.status}
                </span>
              </div>
              <div className={styles.staffShift}>Preferred: {staff.workShift}</div>
            </div>
          ))}
        </div>
      </Card>
    </div>
  )
}

export default ShiftSchedule
