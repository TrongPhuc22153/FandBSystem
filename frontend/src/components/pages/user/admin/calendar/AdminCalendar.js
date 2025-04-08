import Calendar from "react-calendar";
import { useState } from "react";
import "react-calendar/dist/Calendar.css"; // Import default styles for react-calendar

// Custom CSS to increase font size
const customStyles = `
  .react-calendar {
    font-size: 16px; /* Adjust this value to change the overall font size */
  }
  .react-calendar__tile {
    font-size: 16px; /* Specific to day tiles */
  }
  .react-calendar__month-view__days__day {
    font-size: 16px; /* Day numbers */
  }
  .react-calendar__navigation__label {
    font-size: 18px; /* Month/Year label in navigation */
  }
  .react-calendar__navigation__arrow {
    font-size: 20px; /* Navigation arrows */
  }
`;

const dummyReservations = [
  {
    id: "RES001",
    customer: {
      name: "John Doe",
      email: "johndoe@example.com",
      phone: "+1234567890",
    },
    reservationDetails: {
      date: "2025-04-10",
      time: "18:00",
      numberOfGuests: 4,
      specialRequests: "Window seat, birthday cake",
      tableNumber: "T1",
    },
    order: [
      { itemId: "FD1001", name: "Grilled Salmon", quantity: 2, price: 18.99 },
      { itemId: "FD2003", name: "Caesar Salad", quantity: 1, price: 7.5 },
      { itemId: "DR3002", name: "Lemonade", quantity: 4, price: 3.0 },
    ],
    totalAmount: 54.48,
    status: "Confirmed",
  },
  {
    id: "RES002",
    customer: {
      name: "Jane Smith",
      email: "janesmith@example.com",
      phone: "+1987654321",
    },
    reservationDetails: {
      date: "2025-04-10",
      time: "19:30",
      numberOfGuests: 2,
      specialRequests: "Quiet table, vegetarian meal",
      tableNumber: "T2",
    },
    order: [
      {
        itemId: "FD1002",
        name: "Vegetable Stir Fry",
        quantity: 2,
        price: 12.0,
      },
      { itemId: "DR3001", name: "Orange Juice", quantity: 2, price: 2.5 },
    ],
    totalAmount: 27.0,
    status: "Pending",
  },
  {
    id: "RES003",
    customer: {
      name: "Alice Johnson",
      email: "alicejohnson@example.com",
      phone: "+1122334455",
    },
    reservationDetails: {
      date: "2025-04-12",
      time: "20:00",
      numberOfGuests: 6,
      specialRequests: "Table near the entrance, no garlic",
      tableNumber: "T3",
    },
    order: [
      { itemId: "FD1003", name: "Chicken Alfredo", quantity: 4, price: 16.99 },
      { itemId: "FD2002", name: "Garlic Bread", quantity: 2, price: 5.0 },
      { itemId: "DR3003", name: "Coke", quantity: 6, price: 2.0 },
    ],
    totalAmount: 91.94,
    status: "Confirmed",
  },
  {
    id: "RES004",
    customer: {
      name: "Bob Brown",
      email: "bobbrown@example.com",
      phone: "+1230987654",
    },
    reservationDetails: {
      date: "2025-04-14",
      time: "17:30",
      numberOfGuests: 3,
      specialRequests: "Outdoor seating",
      tableNumber: "T4",
    },
    order: [
      { itemId: "FD1005", name: "Cheeseburger", quantity: 3, price: 9.99 },
      { itemId: "DR3004", name: "Iced Tea", quantity: 3, price: 2.5 },
    ],
    totalAmount: 38.47,
    status: "Completed",
  },
  {
    id: "RES005",
    customer: {
      name: "Emily Davis",
      email: "emilydavis@example.com",
      phone: "+4455667788",
    },
    reservationDetails: {
      date: "2025-04-15",
      time: "19:00",
      numberOfGuests: 2,
      specialRequests: "Anniversary celebration",
      tableNumber: "T5",
    },
    order: [
      { itemId: "FD1004", name: "Steak", quantity: 2, price: 25.99 },
      { itemId: "DR3002", name: "Lemonade", quantity: 2, price: 3.0 },
    ],
    totalAmount: 58.98,
    status: "Completed",
  },
];

export default function AdminCalendarPage() {
  // State for selected date, status filter, and filtered reservations
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [selectedStatus, setSelectedStatus] = useState("All"); // Default to show all statuses
  const [reservationsForSelectedDate, setReservationsForSelectedDate] =
    useState([]);

  // Group reservations by date
  const groupByDate = (reservations) => {
    const grouped = {};
    reservations.forEach((reservation) => {
      const date = reservation.reservationDetails.date;
      if (!grouped[date]) {
        grouped[date] = [];
      }
      grouped[date].push(reservation);
    });
    return grouped;
  };

  const reservationsByDate = groupByDate(dummyReservations);

  // Get unique statuses for the filter dropdown
  const uniqueStatuses = [
    "All",
    ...new Set(dummyReservations.map((res) => res.status)),
  ];

  // Handle date change and update reservations
  const handleDateChange = (newDate) => {
    setSelectedDate(newDate);
    updateReservations(newDate, selectedStatus);
  };

  // Handle status filter change
  const handleStatusChange = (event) => {
    const newStatus = event.target.value;
    setSelectedStatus(newStatus);
    updateReservations(selectedDate, newStatus);
  };

  // Update reservations based on selected date and status
  const updateReservations = (date, status) => {
    const dateString = date.toISOString().split("T")[0];
    let reservations = reservationsByDate[dateString] || [];
    if (status !== "All") {
      reservations = reservations.filter(
        (reservation) => reservation.status === status
      );
    }
    setReservationsForSelectedDate(reservations);
  };

  // Customize calendar tiles to show reservation count
  const tileContent = ({ date, view }) => {
    if (view === "month") {
      const dateString = date.toISOString().split("T")[0];
      const reservationCount = reservationsByDate[dateString]?.length || 0;
      if (reservationCount > 0) {
        return (
          <div style={{ fontSize: "10px", color: "red" }}>
            {reservationCount} {reservationCount === 1 ? "event" : "events"}
          </div>
        );
      }
    }
    return null;
  };

  return (
    <main className="content px-5 py-3">
      {/* Inject custom styles */}
      <style>{customStyles}</style>
      <h1 className="h3 my-3">
        <strong>Calendar</strong>
      </h1>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-md-8 d-flex">
            <div id="event-calendar" className="card flex-fill w-100">
              <div className="card-body">
                  <Calendar
                    onChange={handleDateChange}
                    value={selectedDate}
                    tileContent={tileContent}
                  />
              </div>
            </div>
          </div>
          <div className="col-md-4 d-flex">
            <div id="event-reservation" className="card flex-fill w-100">
              <div className="card-body d-flex flex-column h-100">
                <h5 className="card-title">Filter by Status</h5>
                <select
                  className="form-select mb-3"
                  value={selectedStatus}
                  onChange={handleStatusChange}
                >
                  {uniqueStatuses.map((status) => (
                    <option key={status} value={status}>
                      {status}
                    </option>
                  ))}
                </select>

                <h5>
                  History for{" "}
                  {selectedDate.toLocaleDateString("en-US", {
                    weekday: "long",
                    year: "numeric",
                    month: "long",
                    day: "numeric",
                  })}
                  {selectedStatus !== "All" ? ` (${selectedStatus})` : ""}
                </h5>
                {/* Scrollable reservation history */}
                <div
                  style={{
                    flex: 1,
                    overflowY: "auto",
                  }}
                >
                  {reservationsForSelectedDate.length > 0 ? (
                    <ul style={{ listStyleType: "none", padding: 0 }}>
                      {reservationsForSelectedDate.map((reservation) => (
                        <li
                          key={reservation.id}
                          style={{
                            border: "1px solid #ddd",
                            padding: "10px",
                            marginBottom: "10px",
                            borderRadius: "5px",
                          }}
                        >
                          <strong>{reservation.customer.name}</strong> at{" "}
                          {reservation.reservationDetails.time} <br />
                          <span>
                            Table: {reservation.reservationDetails.tableNumber}
                          </span>
                          <br />
                          <span>
                            Guests:{" "}
                            {reservation.reservationDetails.numberOfGuests}
                          </span>
                          <br />
                          <span>
                            Special Requests:{" "}
                            {reservation.reservationDetails.specialRequests ||
                              "None"}
                          </span>
                          <br />
                          <span>Status: {reservation.status}</span>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p>
                      No reservation history for this date
                      {selectedStatus !== "All"
                        ? ` with status ${selectedStatus}`
                        : ""}
                      .
                    </p>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
