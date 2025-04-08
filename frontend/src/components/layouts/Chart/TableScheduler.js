import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { useState, useCallback } from "react";
import config from "../../../config/WebConfig";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus } from "@fortawesome/free-solid-svg-icons";

const TableScheduler = ({ reservations = [], tables = [] }) => {
  const initialEvents = reservations.map((reservation) => ({
    id: reservation.id || Math.random().toString(36).substring(2),
    title: reservation.table || "Reservation",
    start: reservation.startTime,
    end: reservation.endTime,
    backgroundColor:
      config.tableColors[reservation.table] || config.tableColors["Default"],
    borderColor:
      config.tableColors[reservation.table] || config.tableColors["Default"],
    extendedProps: {
      customer: reservation.customer,
      table: reservation.table,
    },
  }));

  const [events, setEvents] = useState(initialEvents);

  const handleDateSelect = useCallback(
    (selectionInfo) => {
      const table = prompt("Enter table number (e.g., 'Table 1'):");
      if (table && tables.includes(table)) {
        const newStart = new Date(selectionInfo.startStr);
        const newEnd = new Date(selectionInfo.endStr);

        // Check for overlapping reservations
        const hasConflict = events.some(
          (event) =>
            event.extendedProps.table === table &&
            new Date(event.start) < newEnd &&
            new Date(event.end) > newStart
        );

        if (hasConflict) {
          alert("This table is already booked during this time!");
          return;
        }

        const customer = prompt("Enter customer name:");
        if (customer) {
          setEvents((currentEvents) => [
            ...currentEvents,
            {
              id: Math.random().toString(36).substring(2),
              title: table,
              start: selectionInfo.startStr,
              end: selectionInfo.endStr,
              backgroundColor:
                config.tableColors[table] || config.tableColors["Default"],
              borderColor:
                config.tableColors[table] || config.tableColors["Default"],
              extendedProps: {
                customer,
                table,
              },
            },
          ]);
        }
      } else {
        alert("Invalid table number!");
      }
    },
    [tables, events] // Add events to dependencies
  );

  const handleEventChange = useCallback((changeInfo) => {
    setEvents((currentEvents) =>
      currentEvents.map((event) =>
        event.id === changeInfo.event.id
          ? {
              ...event,
              start: changeInfo.event.start,
              end: changeInfo.event.end,
            }
          : event
      )
    );
  }, []);

  // Custom render for day cells in month view
  const renderDayCellContent = (dayRenderInfo) => {
    const date = dayRenderInfo.date;
    const eventCount = events.filter((event) => {
      const eventStart = new Date(event.start);
      const eventEnd = new Date(event.end || event.start); // Handle events without end time
      return (
        eventStart.toDateString() === date.toDateString() ||
        (eventStart < date && eventEnd > date)
      );
    }).length;

    return (
      <div className="fc-daygrid-day-number">
        {dayRenderInfo.dayNumberText}
        {eventCount > 0 && (
          <span className="event-count">{` (${eventCount})`}</span>
        )}
      </div>
    );
  };

  return (
    <>
      <div className="table-scheduler row">
        {/* <div className="row mb-3">
          <div className="col-12">
            <button className="btn btn-danger">
              <FontAwesomeIcon icon={faPlus} /> Create new event
            </button>
          </div>
        </div> */}
        <div className="row">
          <div className="col-12">
            <FullCalendar
              plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
              initialView="timeGridWeek"
              headerToolbar={{
                start: "today prev,next",
                center: "title",
                end: "dayGridMonth,timeGridWeek,timeGridDay",
              }}
              buttonText={{
                today: "Today",
                month: "Month",
                week: "Week",
                day: "Day",
              }}
              events={events}
              selectable={true}
              editable={true}
              height="auto"
              select={handleDateSelect}
              eventChange={handleEventChange}
              eventClick={(clickInfo) => {
                if (
                  window.confirm(
                    `Delete reservation for ${clickInfo.event.extendedProps.customer} at ${clickInfo.event.title}?`
                  )
                ) {
                  setEvents((currentEvents) =>
                    currentEvents.filter(
                      (event) => event.id !== clickInfo.event.id
                    )
                  );
                }
              }}
              slotMinTime="08:00:00"
              slotMaxTime="23:00:00"
              slotDuration="00:30:00"
              allDaySlot={false}
              eventContent={(eventInfo) => (
                <>
                  <b>{eventInfo.event.title}</b>
                  <br />
                  <i>{eventInfo.event.extendedProps.customer}</i>
                </>
              )}
              dayCellContent={renderDayCellContent} // Custom content for month view
              dayMaxEvents={0} // Prevents event list from showing in month view
            />
          </div>
        </div>
      </div>
    </>
  );
};

export default TableScheduler;
