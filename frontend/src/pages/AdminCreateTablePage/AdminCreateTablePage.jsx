import React, { useState, useCallback, useEffect } from "react";
import { useReservationTableActions } from "../../hooks/tableHooks"; // Adjust the path if needed
import Loading from "../../components/Loading/Loading"; // You might have a generic Loading component
import { useModal } from "../../context/ModalContext"; // Adjust the path if needed
import TableForm from "../../components/TableForm/TableForm"; // Create this component

function AdminCreateTablePage() {
  const [fieldErrors, setFieldErrors] = useState({});
  const [newTable, setNewTable] = useState({
    tableNumber: null,
    capacity: "",
    location: "",
    status: "",
  });

  const { onOpen } = useModal();

  const {
    handleCreateReservationTable,
    createError,
    createLoading,
    createSuccess,
    resetCreate,
  } = useReservationTableActions();

  useEffect(() => {
    setFieldErrors(createError?.fields ?? {});
  }, [createError]);

  useEffect(() => {
    if (createSuccess) {
      const timer = setTimeout(() => {
        resetCreate();
      }, 5000);

      return () => {
        clearTimeout(timer);
      };
    }
  }, [createSuccess, resetCreate]);

  const handleFormSubmit = (event) => {
    event.preventDefault();
    onOpen({
      title: "Create Table!",
      message: "Do you want to continue?",
      onYes: handleCreate,
    });
  };

  const handleCreate = useCallback(async () => {
    const data = {
      tableNumber: newTable.tableNumber,
      capacity: newTable.capacity,
      location: newTable.location,
      status: newTable.status,
    };
    const response = await handleCreateReservationTable(data);
    if (response) {
      setNewTable({ tableNumber: null, capacity: "", location: "", status: "" }); // Reset status
      setFieldErrors({});
    }
  }, [newTable, handleCreateReservationTable]);

  const handleCancelClick = () => {
    setNewTable({ tableNumber: null, capacity: "", location: "", status: "" }); // Reset status
    setFieldErrors({});
    resetCreate();
  };

  const handleInputChange = useCallback((e) => {
    const { name, value } = e.target;
    setNewTable((prevTable) => ({
      ...prevTable,
      [name]: value,
    }));
  }, []);

  if (createLoading) return <Loading />;

  return (
    <div className="container">
      <h1 className="mb-4">Table Management</h1>

      {createSuccess && (
        <div className="alert alert-success">{createSuccess}</div>
      )}

      {createError?.message && (
        <div className="alert alert-danger">{createError.message}</div>
      )}

      <div className="form-section">
        <h2 id="form-title">Add New Table</h2>
        <form id="table-form" onSubmit={handleFormSubmit}>
          <div className="form-container">
            <div className="form-right">
              <TableForm // Create this component
                table={newTable}
                fieldErrors={fieldErrors}
                handleInputChange={handleInputChange}
              />

              <div className="form-buttons">
                <button
                  type="submit"
                  className="btn btn-primary"
                  id="submit-btn"
                  disabled={createLoading}
                >
                  {createLoading ? "Adding Table..." : "Add Table"}
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  id="cancel-btn"
                  onClick={handleCancelClick}
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AdminCreateTablePage;
