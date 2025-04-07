import Calendar from "react-calendar";
import { useState } from "react";
const dummyReservations = [
    {
        id: 'RES001',
        customer: {
            name: 'John Doe',
            email: 'johndoe@example.com',
            phone: '+1234567890',
        },
        reservationDetails: {
            date: '2025-04-10',
            time: '18:00',
            numberOfGuests: 4,
            specialRequests: 'Window seat, birthday cake',
        },
        order: [
            { itemId: 'FD1001', name: 'Grilled Salmon', quantity: 2, price: 18.99 },
            { itemId: 'FD2003', name: 'Caesar Salad', quantity: 1, price: 7.50 },
            { itemId: 'DR3002', name: 'Lemonade', quantity: 4, price: 3.00 },
        ],
        totalAmount: 54.48,
        status: 'Confirmed',
    },
    {
        id: 'RES002',
        customer: {
            name: 'Jane Smith',
            email: 'janesmith@example.com',
            phone: '+1987654321',
        },
        reservationDetails: {
            date: '2025-04-10',
            time: '19:30',
            numberOfGuests: 2,
            specialRequests: 'Quiet table, vegetarian meal',
        },
        order: [
            { itemId: 'FD1002', name: 'Vegetable Stir Fry', quantity: 2, price: 12.00 },
            { itemId: 'DR3001', name: 'Orange Juice', quantity: 2, price: 2.50 },
        ],
        totalAmount: 27.00,
        status: 'Pending',
    },
    {
        id: 'RES003',
        customer: {
            name: 'Alice Johnson',
            email: 'alicejohnson@example.com',
            phone: '+1122334455',
        },
        reservationDetails: {
            date: '2025-04-12',
            time: '20:00',
            numberOfGuests: 6,
            specialRequests: 'Table near the entrance, no garlic',
        },
        order: [
            { itemId: 'FD1003', name: 'Chicken Alfredo', quantity: 4, price: 16.99 },
            { itemId: 'FD2002', name: 'Garlic Bread', quantity: 2, price: 5.00 },
            { itemId: 'DR3003', name: 'Coke', quantity: 6, price: 2.00 },
        ],
        totalAmount: 91.94,
        status: 'Confirmed',
    },
    {
        id: 'RES004',
        customer: {
            name: 'Bob Brown',
            email: 'bobbrown@example.com',
            phone: '+1230987654',
        },
        reservationDetails: {
            date: '2025-04-14',
            time: '17:30',
            numberOfGuests: 3,
            specialRequests: 'Outdoor seating',
        },
        order: [
            { itemId: 'FD1005', name: 'Cheeseburger', quantity: 3, price: 9.99 },
            { itemId: 'DR3004', name: 'Iced Tea', quantity: 3, price: 2.50 },
        ],
        totalAmount: 38.47,
        status: 'Confirmed',
    },
    {
        id: 'RES005',
        customer: {
            name: 'Emily Davis',
            email: 'emilydavis@example.com',
            phone: '+4455667788',
        },
        reservationDetails: {
            date: '2025-04-15',
            time: '19:00',
            numberOfGuests: 2,
            specialRequests: 'Anniversary celebration',
        },
        order: [
            { itemId: 'FD1004', name: 'Steak', quantity: 2, price: 25.99 },
            { itemId: 'DR3002', name: 'Lemonade', quantity: 2, price: 3.00 },
        ],
        totalAmount: 58.98,
        status: 'Confirmed',
    },
];
export default function AdminCalendarPage() {

    const [events, setEvents] = useState({
        '2025-04-07': ['Event 1', 'Event 2'],
        '2025-04-15': ['Event 3'],
    });
    const [date, setDate] = useState(new Date());
    const tileContent = ({ date, view }) => {
        const dateString = date.toISOString().split('T')[0]; // Format date to 'YYYY-MM-DD'
        if (events[dateString]) {
            return (
                <ul>
                    {events[dateString].map((event, index) => (
                        <li key={index}>{event}</li>
                    ))}
                </ul>
            );
        }
        return null;
    };


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

    const [selectedDate, setSelectedDate] = useState(new Date());
    const [eventsForSelectedDate, setEventsForSelectedDate] = useState([]);
    const reservationsByDate = groupByDate(dummyReservations);
    return (
        <main className="content overflow-scroll px-5 py-3">
            <h1 className="h3 my-3">
                <strong>Calendar</strong>
            </h1>
            <div className="container-fluid p-0">
                <div className="row">
                    <div className="col-md-4">

                    </div>
                    <div className="col-md-8">
                        <div className="card flex-fill w-100">
                            <div className="card-body d-flex w-100">
                                <div className="align-self-center chart chart-lg">
                                    <Calendar onChange={setDate} value={date} tileContent={tileContent} />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div style={{ marginTop: '20px' }}>
                <h3>Selected Date: {selectedDate.toLocaleDateString()}</h3>
                {eventsForSelectedDate.length > 0 ? (
                    <ul>
                        {eventsForSelectedDate.map((reservation, index) => (
                            <li key={index}>
                                <strong>{reservation.customer.name}</strong> - {reservation.reservationDetails.time}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No reservations for this date.</p>
                )}
            </div>
        </main>
    )
}