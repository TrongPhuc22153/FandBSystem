import { useState, useCallback, useEffect } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { useReservationTables, useReservationTableActions } from "../../hooks/tableHooks";
import DataTable from "../../components/DataTableManagement/DataTable";
import Pagination from "../../components/Pagination/Pagination";
import {
  ADMIN_ADD_TABLE_URI,
  ADMIN_TABLES_URI,
} from "../../constants/routes";
import { useModal } from "../../context/ModalContext";
import { Badge } from "react-bootstrap";
import { SORTING_DIRECTIONS, TABLE_STATUS_CLASSES } from "../../constants/webConstant";

const AdminTablesPage = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [selectedItems, setSelectedItems] = useState([]);
  const [searchValue, setSearchValue] = useState(
    searchParams.get("searchValue") || ""
  );

  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const { data: tableDataResult, mutate } = useReservationTables({
    page: currentPage,
    search: searchValue,
    direction: SORTING_DIRECTIONS.DESC,
    sortBy: "createdOn",
    isDeleted: null
  });
  const {
    handleUpdateReservationTableStatus,
    updateStatusError,
    updateStatusLoading,
    updateStatusSuccess,
    resetUpdateStatus,
  } = useReservationTableActions();

  const tableData = tableDataResult?.content || [];
  const totalPages = tableDataResult?.totalPages || 0;

  const tableColumns = [
    { key: "tableId", title: "Table ID" },
    { key: "tableNumber", title: "Table Number" },
    { key: "location", title: "Location" },
    {
      key: "status",
      title: "Status",
      render: (table) => (
        <Badge bg={TABLE_STATUS_CLASSES[table.status]}>{table.status}</Badge>
      ),
    },
    { key: "capacity", title: "Capacity" },
    {
      key: "isDeleted",
      title: "Status",
      render: (table) =>
        table.isDeleted ? (
          <Badge bg="danger">Deleted</Badge>
        ) : (
          <Badge bg="success">Active</Badge>
        ),
    },
  ];

  // modal
  const { onOpen } = useModal();

  // delete item
  const handleDeleteConfirm = useCallback(
    async (idToDelete, isDeleted) => {
      if (idToDelete) {
        const success = await handleUpdateReservationTableStatus({
          id: idToDelete, 
          isDeleted: !isDeleted
        });
        if (success) {
          // Optimistically update the cache
          mutate((prevData) => {
            if (!prevData?.content) return prevData;
            return {
              ...prevData,
              content: prevData.content.filter(
                (table) => table.tableId !== idToDelete
              ),
            };
          }, false);
          mutate(); // Trigger background revalidation
          setSelectedItems((prevSelectedItems) =>
            prevSelectedItems.filter((itemId) => itemId !== idToDelete)
          );
          resetUpdateStatus();
        }
      }
    },
    [handleUpdateReservationTableStatus, mutate, resetUpdateStatus]
  );

  const showDeleteConfirmation = useCallback(
    (id, isDeleted) => {
      onOpen({
        title: `${isDeleted ? `Enable` : `Delete`} Table!`,
        message: `Do you want to ${isDeleted ? `enable` : `delete`} this table?`,
        onYes: () => handleDeleteConfirm(id, isDeleted),
      });
    },
    [onOpen, handleDeleteConfirm]
  );

  // update item
  const handleUpdateItem = useCallback(
    (id) => {
      navigate(`${ADMIN_TABLES_URI}/${id}`);
    },
    [navigate]
  );

  // select item
  const handleSelectAll = useCallback(
    (event) => {
      const isChecked = event.target.checked;
      if (isChecked) {
        const allTableIds = tableData.map((table) => table.tableId);
        setSelectedItems(allTableIds);
      } else {
        setSelectedItems([]);
      }
    },
    [tableData]
  );

  const handleSelectItem = useCallback((event, tableId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, tableId]),
      ]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== tableId)
      );
    }
  }, []);

  const debouncedSearch = useCallback(
    (newSearchValue) => {
      searchParams.set("searchValue", newSearchValue);
      setSearchParams(searchParams);
    },
    [setSearchParams]
  );

  const handleSearchInputChange = (e) => {
    const newSearchValue = e.target.value;
    setSearchValue(newSearchValue);
    debouncedSearch(newSearchValue);
  };

  return (
    <main className="content px-5 py-3">
      <h3 className="my-3">
        <strong>Tables</strong>
      </h3>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill p-3">
              <div className="row mb-3">
                <div className="col-sm-5">
                  <Link
                    to={ADMIN_ADD_TABLE_URI}
                    className="btn btn-primary float-start"
                  >
                    <FontAwesomeIcon icon={faPlus} />
                      Add Table
                  </Link>
                </div>
                <div className="col-sm-7">
                  <div className="row">
                    <div className="col-sm-12 col-md-6 mb-2"></div>
                    <div className="col-sm-12 col-md-6 mb-2">
                      <div className="form-group">
                        <input
                          className="form-control me-2"
                          type="search"
                          placeholder="Search"
                          aria-label="Search"
                          value={searchValue}
                          onChange={handleSearchInputChange}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              {updateStatusLoading && (
                <div className="alert alert-info">Deleting table...</div>
              )}
              {updateStatusSuccess && (
                <div className="alert alert-success">{updateStatusSuccess}</div>
              )}
              {updateStatusError?.message && (
                <div className="alert alert-danger">{updateStatusError.message}</div>
              )}

              <DataTable
                data={tableData}
                columns={tableColumns}
                selectedItems={selectedItems}
                handleSelectItem={handleSelectItem}
                handleSelectAll={handleSelectAll}
                handleToggleItem={showDeleteConfirmation}
                handleUpdateItem={handleUpdateItem}
                toggleAttribute="isDeleted"
                uniqueIdKey="tableId"
                deleteLoading={updateStatusLoading}
              />

              {totalPages > 1 && (
                <Pagination
                  totalPages={totalPages}
                  currentPage={currentPage + 1}
                />
              )}
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default AdminTablesPage;