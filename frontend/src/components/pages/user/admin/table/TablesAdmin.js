import { faPenToSquare } from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Pagination } from "../../../../layouts/Pagination";
import { Link } from "react-router";
import { faPlus } from "@fortawesome/free-solid-svg-icons";

const tables = [
  { id: 1, tableNumber: "T1", capacity: 2, location: "Indoor" },
  { id: 2, tableNumber: "T2", capacity: 4, location: "Indoor" },
  { id: 3, tableNumber: "T3", capacity: 4, location: "Patio" },
  { id: 4, tableNumber: "T4", capacity: 6, location: "Indoor" },
  { id: 5, tableNumber: "T5", capacity: 2, location: "Balcony" },
  { id: 6, tableNumber: "T6", capacity: 8, location: "Private Room" },
  { id: 7, tableNumber: "T7", capacity: 4, location: "Indoor" },
  { id: 8, tableNumber: "T8", capacity: 6, location: "Patio" },
  { id: 9, tableNumber: "T9", capacity: 2, location: "Balcony" },
  { id: 10, tableNumber: "T10", capacity: 10, location: "VIP Lounge" },
];

export default function TablesAdmin() {
  return (
    <main className="content overflow-scroll px-5 py-3">
      <h1 className="h3 my-3">
        <strong>Tables</strong>
      </h1>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill">
              <div className="row mb-3">
                <div className="col-sm-5">
                  <Link className="btn btn-primary float-start">
                    <FontAwesomeIcon icon={faPlus} />
                    &nbsp; Add Tables
                  </Link>
                </div>
                <div className="col-sm-7">
                  <Link className="btn btn-info float-end">Import</Link>
                  <Link className="me-2 btn btn-info float-end">Export</Link>
                </div>
              </div>

              <table className="table table-striped table-hover">
                <thead>
                  <tr>
                    <th>Table Number</th>
                    <th>Capacity</th>
                    <th>Location</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {tables.map((table) => (
                    <tr key={table.id}>
                      <td>{table.tableNumber}</td>
                      <td>{table.capacity}</td>
                      <td>{table.location}</td>
                      <td>
                        <button className="btn btn-sm btn-primary me-2">
                          <FontAwesomeIcon icon={faPenToSquare} /> Edit
                        </button>
                        <button className="btn btn-sm btn-danger">
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <Pagination />
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
