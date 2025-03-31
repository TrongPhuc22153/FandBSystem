import React, { useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";

const FullCalendar = () => {
  const [date, setDate] = useState(new Date());

  return (
    <div className="card flex-fill w-100">
      <div className="card-header">
        <h5 className="card-title mb-0">Calendar</h5>
      </div>
      <div className="card-body d-flex w-100">
        <div className="align-self-center chart chart-lg">
          <Calendar onChange={setDate} value={date}/>
        </div>
      </div>
    </div>
  );
};

export default FullCalendar;
