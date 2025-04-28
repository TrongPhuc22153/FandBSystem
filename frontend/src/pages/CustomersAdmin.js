import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faEye, faPenToSquare, faTrash } from '@fortawesome/free-solid-svg-icons';
import { Pagination } from '../components/Pagination';
import axios from 'axios';
import { USERS_URL } from '../constants/ApiEndpoints';
import { ROLES } from '../constants/RoleName';
import { useAuth } from '../hooks/AuthContext';
import { getDefaultUser } from '../services/ImageService';

export default function CustomersAdmin() {
  const [customers, setCustomers] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);
  const [searchValue, setSearchValue] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get(`${USERS_URL}?role=${ROLES.CUSTOMER}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        }
      });
      setCustomers(response.data.content);

    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  // Table action handlers (you'll need to implement the actual logic)
  const handleDeleteItem = async (id) => {
    if (window.confirm(`Are you sure you want to delete customer with ID: ${id}?`)) {
      try {
        await axios.delete(`${USERS_URL}/${id}`); // Replace with your actual delete endpoint
        fetchCustomers(); // Refresh the customer list after deletion
        setSelectedItems(prevItems => prevItems.filter(itemId => itemId !== id)); // Remove from selected items
      } catch (error) {
        console.error('Error deleting customer:', error);
        setError('Failed to delete customer.'); // Optionally show an error message to the user
      }
    }
  };

  const handleUpdateItem = (id) => {
    navigate(`/admin/customers/edit/${id}`); // Navigate to the edit customer page
  };

  const handleViewItem = (id) => {
    navigate(`/admin/customers/${id}`); // Navigate to the view customer page
  };

  // Select Items
  const handleSelectAll = (event) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      const allCustomerIds = customers.map((customer) => customer.id);
      setSelectedItems(allCustomerIds);
    } else {
      setSelectedItems([]);
    }
  };

  const handleSelectItem = (event, customerId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, customerId]),
      ]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== customerId)
      );
    }
  };

  const searchCustomers = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get(`${USERS_URL}?search=${searchValue}`); // Adjust the API endpoint for search
      setCustomers(response.data);
    } catch (error) {
      setError(error.message);
      setCustomers([]); // Clear customers on error
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div>Loading customers...</div>;
  }

  if (error) {
    return <div>Error loading customers: {error}</div>;
  }

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
                  <Link to="/admin/customers/add" className="btn btn-primary float-start">
                    <FontAwesomeIcon icon={faPlus} />
                    &nbsp; Add Customers
                  </Link>
                </div>
                <div className="col-sm-7">
                  <Link to="/admin/customers/import" className="btn btn-info float-end">Import</Link>
                  <Link to="/admin/customers/export" className="me-2 btn btn-info float-end">Export</Link>
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
                          checked={selectedItems.length > 0 && selectedItems.length === customers.length}
                        />
                      </div>
                    </th>
                    <th>Customer</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Created Date</th>
                    <th>Enabled</th>
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
                          style={{ height: "50px", width: "50px", borderRadius: "50%" }}
                        />
                        <p>{customer.contact_name}</p>
                      </td>
                      <td>{customer.username}</td>
                      <td>{customer.email}</td>
                      <td>
                        {new Date(customer.created_date).toLocaleString()}
                      </td>
                      <td>{customer.enabled.toString()}</td>
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
              <Pagination />
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}