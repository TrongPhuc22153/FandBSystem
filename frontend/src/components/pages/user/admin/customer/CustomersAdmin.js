import { useState } from "react";
import config from "../../../../../config/WebConfig";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link, useNavigate } from "react-router";
import {
  faEye,
  faPenToSquare,
  faPlus,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";
import { getDefaultUser } from "../../../../../services/ImageService";
import { Pagination } from "../../../../layouts/Pagination";

const dummyData = [
  {
    id: "C001",
    contact_name: "John Doe",
    phone: "(123) 456-7890",
    email: "john.doe@example.com",
    created_date: "2025-03-25T10:00:00Z",
    status: "Active",
    image: "https://example.com/images/john_doe.jpg",
  },
  {
    id: "C002",
    contact_name: "Jane Smith",
    phone: "(234) 567-8901",
    email: "jane.smith@example.com",
    created_date: "2025-03-28T14:30:00Z",
    status: "Deactive",
    image: "https://example.com/images/jane_smith.jpg",
  },
  {
    id: "C003",
    contact_name: "Robert Brown",
    phone: "(345) 678-9012",
    email: "robert.brown@example.com",
    created_date: "2025-03-20T09:15:00Z",
    status: "Deactive",
    image: "https://example.com/images/robert_brown.jpg",
  },
  {
    id: "C004",
    contact_name: "Emily White",
    phone: "(456) 789-0123",
    email: "emily.white@example.com",
    created_date: "2025-03-22T16:45:00Z",
    status: "Active",
    image: "https://example.com/images/emily_white.jpg",
  },
  {
    id: "C005",
    contact_name: "Michael Johnson",
    phone: "(567) 890-1234",
    email: "michael.johnson@example.com",
    created_date: "2025-03-30T11:30:00Z",
    status: "Deactive",
    image: "https://example.com/images/michael_johnson.jpg",
  },
  {
    id: "C006",
    contact_name: "Sarah Connor",
    phone: "(678) 901-2345",
    email: "sarah.connor@example.com",
    created_date: "2025-03-21T08:30:00Z",
    status: "Active",
    image: "https://example.com/images/sarah_connor.jpg",
  },
  {
    id: "C007",
    contact_name: "David Miller",
    phone: "(789) 012-3456",
    email: "david.miller@example.com",
    created_date: "2025-03-26T12:00:00Z",
    status: "Deactive",
    image: "https://example.com/images/david_miller.jpg",
  },
  {
    id: "C008",
    contact_name: "Sophia Lopez",
    phone: "(890) 123-4567",
    email: "sophia.lopez@example.com",
    created_date: "2025-03-27T14:00:00Z",
    status: "Active",
    image: "https://example.com/images/sophia_lopez.jpg",
  },
  {
    id: "C009",
    contact_name: "James Wilson",
    phone: "(901) 234-5678",
    email: "james.wilson@example.com",
    created_date: "2025-03-23T09:45:00Z",
    status: "Deactive",
    image: "https://example.com/images/james_wilson.jpg",
  },
  {
    id: "C010",
    contact_name: "Olivia Martinez",
    phone: "(012) 345-6789",
    email: "olivia.martinez@example.com",
    created_date: "2025-03-24T11:15:00Z",
    status: "Active",
    image: "https://example.com/images/olivia_martinez.jpg",
  },
  {
    id: "C011",
    contact_name: "Daniel Anderson",
    phone: "(123) 456-7891",
    email: "daniel.anderson@example.com",
    created_date: "2025-03-29T10:30:00Z",
    status: "Deactive",
    image: "https://example.com/images/daniel_anderson.jpg",
  },
  {
    id: "C012",
    contact_name: "Emma Thomas",
    phone: "(234) 567-8902",
    email: "emma.thomas@example.com",
    created_date: "2025-03-31T15:45:00Z",
    status: "Active",
    image: "https://example.com/images/emma_thomas.jpg",
  },
  {
    id: "C013",
    contact_name: "William Harris",
    phone: "(345) 678-9013",
    email: "william.harris@example.com",
    created_date: "2025-03-19T13:00:00Z",
    status: "Deactive",
    image: "https://example.com/images/william_harris.jpg",
  },
];

export default function CustomersAdmin() {
  const [customers, setCustomers] = useState(dummyData);
  const [selectedItems, setSelectedItems] = useState([]);
  const [searchValue, setSearchValue] = useState("");
  const navigate = useNavigate();

  //  Table action
  const handleDeleteItem = (id) => {};
  const handleUpdateItem = (id) => {};
  const handleViewItem = (id) => {};

  // Select Items
  const handleSelectAll = (event) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      // Select all: Get all customer IDs and update state
      const allcustomerIds = customers.map((customer) => customer.id);
      setSelectedItems(allcustomerIds);
    } else {
      // Deselect all: Clear the selected items state
      setSelectedItems([]);
    }
  };

  const handleSelectItem = (event, customerId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      // Add the customerId to the selected list (prevent duplicates just in case)
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, customerId]),
      ]);
    } else {
      // Remove the customerId from the selected list
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== customerId)
      );
    }
  };

  const searchCustomers = (e) => {
    e.preventDefault();
    const searchTerm = searchValue.toLowerCase();
    const filteredCustomers = dummyData.filter((customer) =>
      customer.contact_name.toLowerCase().includes(searchTerm)
    );
    setCustomers(filteredCustomers);
  };

  return (
    <main className="content overflow-scroll px-5 py-3">
      <h1 className="h3 my-3">
        <strong>Customers</strong>
      </h1>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill">
              <div className="row mb-3">
                <div className="col-sm-5">
                  <Link className="btn btn-primary float-start">
                    <FontAwesomeIcon icon={faPlus} />
                    &nbsp; Add Customers
                  </Link>
                </div>
                <div className="col-sm-7">
                  <Link className="btn btn-info float-end">Import</Link>
                  <Link className="me-2 btn btn-info float-end">Export</Link>
                </div>
              </div>
              <div className="row mb-3">
                <div className="col-md-8"></div>
                <div className="col-md-4">
                  <form className="form-inline d-flex" onSubmit={searchCustomers}>
                    <input
                      className="form-control mr-sm-2 me-2"
                      type="search"
                      placeholder="Search"
                      aria-label="Search"
                      value={searchValue}
                      onChange={(e) => setSearchValue(e.target.value)}
                    />
                    <button
                      className="btn btn-outline-success my-2 my-sm-0"
                      type="submit"
                    >
                      Search
                    </button>
                  </form>
                </div>
              </div>

              <table id="products-table" className="table table-hover my-0">
                <thead>
                  <tr>
                    <th>
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          style={{
                            width: "20px",
                            height: "20px",
                            padding: "0",
                          }}
                          type="checkbox"
                          value=""
                          onChange={handleSelectAll}
                        />
                      </div>
                    </th>
                    <th>Customer</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th>Created Date</th>
                    <th>Status</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {customers.map((customer, index) => (
                    <tr
                      key={index}
                      className={
                        selectedItems.includes(customer.id)
                          ? "table-active"
                          : ""
                      }
                    >
                      <td>
                        <div className="form-check">
                          <input
                            className="form-check-input"
                            style={{
                              width: "20px",
                              height: "20px",
                              padding: "0",
                            }}
                            type="checkbox"
                            value={customer.id}
                            checked={selectedItems.includes(customer.id)}
                            onChange={(e) => handleSelectItem(e, customer.id)}
                          />
                        </div>
                      </td>
                      <td className="d-flex">
                        <img
                          className="me-3"
                          src={customer.image || getDefaultUser()}
                          alt={customer.contact_name}
                          style={{ minHeight: "40px" }}
                        />
                        <p>{customer.contact_name}</p>
                      </td>
                      <td>{customer.phone}</td>
                      <td>{customer.email}</td>
                      <td>
                        {new Date(customer.created_date).toLocaleString()}
                      </td>
                      <td>{customer.status}</td>
                      <td>
                        <span
                          className={
                            config.customerStatusClasses[customer.status] ||
                            "badge bg-secondary"
                          }
                        >
                          {customer.status || "Unknown"}
                        </span>
                      </td>
                      <td>
                        <button
                          className="btn"
                          onClick={() => handleViewItem(customer.id)}
                        >
                          <FontAwesomeIcon icon={faEye} />
                        </button>
                        <button
                          className="btn"
                          onClick={() => handleUpdateItem(customer.id)}
                        >
                          <FontAwesomeIcon icon={faPenToSquare} />
                        </button>
                        <button
                          className="btn"
                          onClick={() => handleDeleteItem(customer.id)}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <Pagination/>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
