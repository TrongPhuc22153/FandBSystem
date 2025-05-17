import React, { useState, useEffect, useCallback } from "react";
import {
  useReservationTable,
  useReservationTableActions,
} from "../../hooks/tableHooks"; // Adjust the path if needed
import Loading from "../../components/Loading/Loading"; // You might have a generic Loading component
import { useParams } from "react-router-dom";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay"; // You might have a generic ErrorDisplay
import { useModal } from "../../context/ModalContext"; // Adjust the path if needed
import TableForm from "../../components/TableForm/TableForm"; // Use the TableForm component

function AdminUpdateTablePage() {
  const { id } = useParams();
  const [isEditMode, setIsEditMode] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [tableDetails, setTableDetails] = useState({
    tableNumber: null,
    capacity: "",
    location: "",
    status: "",
  });

  const {
    data: tableData,
    isLoading: isTableLoading,
    error,
    mutate: mutateTable,
  } = useReservationTable({ id: id }); // Use the correct hook

  const {
    handleUpdateReservationTable,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate,
  } = useReservationTableActions(); // Use the correct hook

  const { onOpen } = useModal();

  useEffect(() => {
    if (tableData && !isEditMode) {
      setTableDetails({
        tableNumber: tableData.tableNumber || null,
        capacity: tableData.capacity || "",
        location: tableData.location || "",
        status: tableData.status || "",
      });
    }
  }, [tableData, isEditMode]);

  useEffect(() => {
    setFieldErrors(updateError?.fields ?? {});
  }, [updateError]);

  useEffect(() => {
    if (updateSuccess) {
      mutateTable();
      setIsEditMode(false);
      const timer = setTimeout(() => {
        resetUpdate();
      }, 5000);

      return () => {
        clearTimeout(timer);
      };
    }
  }, [updateSuccess, mutateTable, resetUpdate]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setTableDetails((prevDetails) => ({
      ...prevDetails,
      [name]: value,
    }));
  };

  const handleFormSubmit = (event) => {
    event.preventDefault();
    if (!isEditMode) return;
    onOpen({
      title: "Update Table!",
      message: "Do you want to continue?",
      onYes: handleUpdate,
    });
  };

  const handleUpdate = useCallback(async () => {
    const data = {
      tableId: id, // Corrected to use id, not tableId
      tableNumber: tableDetails.tableNumber,
      capacity: tableDetails.capacity,
      location: tableDetails.location,
      status: tableDetails.status,
    };

    const response = await handleUpdateReservationTable(id, data);

    if (response) {
      setIsEditMode(false);
    }
  }, [id, tableDetails, handleUpdateReservationTable]);

  const handleCancelClick = () => {
    setTableDetails({
      tableNumber: tableData?.tableNumber || null,
      capacity: tableData?.capacity || "",
      location: tableData?.location || "",
      status: tableData?.status || "",
    });
    setFieldErrors({});
    resetUpdate();
    setIsEditMode(false);
  };

  if (isTableLoading || updateLoading) return <Loading />;

  if (error) {
    return <ErrorDisplay message={error.message} />;
  }

  return (
    <div className="container">
      <h1>Table Management</h1>

      {updateSuccess && (
        <div className="alert alert-success">{updateSuccess}</div>
      )}

      {updateError?.message && (
        <div className="alert alert-danger">{updateError.message}</div>
      )}

      <div className="form-section">
        <h2 id="form-title">{isEditMode ? "Update Table" : "View Table"}</h2>
        <button
          className="btn btn-warning mb-3"
          onClick={() => setIsEditMode(!isEditMode)}
        >
          {isEditMode ? "Disable Edit" : "Enable Edit"}
        </button>

        <form id="table-form" onSubmit={handleFormSubmit}>
          <div className="form-container">
            <div className="form-right">
              <TableForm
                table={tableDetails}
                handleInputChange={handleInputChange}
                fieldErrors={fieldErrors}
                isEditMode={isEditMode}
              />

              <div className="form-buttons">
                {isEditMode ? (
                  <>
                    <button
                      type="submit"
                      className="btn btn-primary"
                      id="submit-btn"
                      disabled={updateLoading}
                    >
                      {updateLoading ? "Updating Table..." : "Update Table"}
                    </button>
                    <button
                      type="button"
                      className="btn btn-secondary"
                      id="cancel-btn"
                      onClick={handleCancelClick}
                    >
                      Cancel
                    </button>
                  </>
                ) : (
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={handleCancelClick}
                  >
                    Back
                  </button>
                )}
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AdminUpdateTablePage;
