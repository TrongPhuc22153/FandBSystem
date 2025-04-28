import TableScheduler from "../components/TableScheduler";
import { useState } from "react";

const tableData = ["Table 1", "Table 2", "Table 3", "Table 4", "Table 5"];
export default function AdminTableCalendar() {
  const [tableList, setTableList] = useState([...tableData]);

  const reservationData = [
    {
      id: "1",
      table: "Table 1",
      startTime: "2025-04-10T12:00:00",
      endTime: "2025-04-10T13:30:00",
      customer: "John Doe",
    },
    {
      id: "2",
      table: "Table 2",
      startTime: "2025-04-10T14:00:00",
      endTime: "2025-04-10T15:00:00",
      customer: "Jane Smith",
    },
  ];

  return (
    <main className="content px-5 py-3">
      <h1 className="h3 my-3">
        <strong>Table Calendar</strong>
      </h1>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-md-12 d-flex">
            <div className="card flex-fill w-100">
              <div className="card-body">
                <TableScheduler
                  reservations={reservationData}
                  tables={tableList}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
