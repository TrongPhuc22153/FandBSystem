import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faPenToSquare, faTrash } from '@fortawesome/free-solid-svg-icons';
import { TABLES_URL } from '../constants/ApiEndpoints'; // Assuming you have your API endpoints defined
import { Pagination } from '../components/Pagination'; // Assuming you have a Pagination component
import { useAuth } from '../hooks/AuthContext';

export default function TablesAdmin() {
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pageable, setPageable] = useState({
    pageNumber: 0,
    totalPages: 0,
  });
  const [searchParams] = useSearchParams();
  const currentPage = parseInt(searchParams.get('page') || '0');
  const { token } = useAuth();

  useEffect(() => {
    const fetchTables = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await axios.get(TABLES_URL, {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
          }
        });
        const data = res.data;
        setTables(data.content);
        setPageable({
          pageNumber: data.pageable.pageNumber,
          totalPages: data.totalPages,
        });

        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchTables();
  }, []);

  const handleDeleteTable = async (id) => {
    if (window.confirm('Are you sure you want to delete this table?')) {
      try {
        await axios.delete(`${TABLES_URL}/${id}`);
        setTables(tables.filter((table) => table.id !== id));
      } catch (err) {
        setError(err.message);
        // Optionally, show an error message to the user
      }
    }
  };

  if (loading) {
    return <p>Loading tables...</p>;
  }

  if (error) {
    return <p>Error fetching tables: {error}</p>;
  }

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
                  <Link to="/admin/tables/add" className="btn btn-primary float-start">
                    <FontAwesomeIcon icon={faPlus} />
                    &nbsp; Add Tables
                  </Link>
                </div>
                <div className="col-sm-7">
                  <Link to="/admin/tables/import" className="btn btn-info float-end">Import</Link>
                  <Link to="/admin/tables/export" className="me-2 btn btn-info float-end">Export</Link>
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
                        <Link to={`/admin/tables/edit/${table.id}`} className="btn btn-sm btn-primary me-2">
                          <FontAwesomeIcon icon={faPenToSquare} /> Edit
                        </Link>
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => handleDeleteTable(table.id)}
                        >
                          <FontAwesomeIcon icon={faTrash} /> Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <Pagination totalPages={pageable.totalPages} currentPage={currentPage} />
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}