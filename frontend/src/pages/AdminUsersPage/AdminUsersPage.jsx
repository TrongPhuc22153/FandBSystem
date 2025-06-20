import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState, useCallback, useEffect, useMemo } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { ADMIN_ADD_USER_URI, ADMIN_USERS_URI } from "../../constants/routes";
import Pagination from "../../components/Pagination/Pagination";
import { useUsers, useUserActions } from "../../hooks/userHook";
import DataTable from "../../components/DataTableManagement/DataTable";
import { Badge } from "react-bootstrap";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";
import { ROLE_CLASSES } from "../../constants/webConstant";
import { debounce } from "lodash";

const AdminUsersPage = () => {
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

  const { data: userDataResult, mutate } = useUsers({
    page: currentPage,
    search: searchValue,
    
  });

  const userColumns = [
    { key: "username", title: "Username" },
    { key: "email", title: "Email" },
    { key: "firstName", title: "First Name" },
    { key: "lastName", title: "Last Name" },
    {
      key: "roles",
      title: "Roles",
      render: (user) =>
        user.roles ? (
          user.roles.map((role, index) => (
            <Badge
              className="me-2"
              bg={ROLE_CLASSES[role]}
              key={`${role}--${index}`}
            >
              {role}
            </Badge>
          ))
        ) : (
          <Badge bg="danger">No Roles</Badge>
        ),
    },
    {
      key: "enabled",
      title: "Enabled",
      render: (user) =>
        user.enabled ? (
          <Badge bg="success">Yes</Badge>
        ) : (
          <Badge bg="danger">No</Badge>
        ),
    },
  ];

  const {
    handleDeleteUser,
    deleteError,
    deleteLoading,
    deleteSuccess,
    resetDelete,
  } = useUserActions();

  const userData = useMemo(
    () => userDataResult?.content || [],
    [userDataResult]
  );
  const totalPages = userDataResult?.totalPages || 0;

  // alert
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (deleteSuccess) {
      showNewAlert({ message: deleteSuccess, action: resetDelete });
    }
  }, [deleteSuccess, showNewAlert, resetDelete]);

  // modal
  const { onOpen } = useModal();

  // delete user
  const handleDeleteConfirm = useCallback(
    async (idToDelete, enabled) => {
      if (idToDelete) {
        const success = await handleDeleteUser(idToDelete, !enabled);
        if (success) {
          // Optimistically update the cache
          mutate((prevData) => {
            if (!prevData?.content) return prevData;
            return {
              ...prevData,
              content: prevData.content.filter(
                (user) => user.id !== idToDelete
              ),
            };
          }, false);
          mutate(); // Trigger background revalidation
          setSelectedItems((prevSelectedItems) =>
            prevSelectedItems.filter((itemId) => itemId !== idToDelete)
          );
        }
      }
    },
    [handleDeleteUser, mutate]
  );

  const showDeleteConfirmation = useCallback(
    (id, enabled) => {
      onOpen({
        title: `${enabled ? "Delete" : "Enable"} User!`,
        message: `Do you want to ${enabled ? "delete" : "enable"} this user?`,
        onYes: () => handleDeleteConfirm(id, enabled),
      });
    },
    [onOpen, handleDeleteConfirm]
  );

  // update item
  const handleViewItem = useCallback(
    (id) => {
      navigate(`${ADMIN_USERS_URI}/${id}`);
    },
    [navigate]
  );

  const handleSelectAll = useCallback(
    (event) => {
      const isChecked = event.target.checked;
      if (isChecked) {
        const allUserIds = userData.map((user) => user.id);
        setSelectedItems(allUserIds);
      } else {
        setSelectedItems([]);
      }
    },
    [userData]
  );

  const handleSelectItem = useCallback((event, userId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, userId]),
      ]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== userId)
      );
    }
  }, []);

  const debouncedSearch = useCallback(() =>
    debounce((newSearchValue) => {
      searchParams.set("searchValue", newSearchValue);
      searchParams.set("page", "1");
      setSearchParams(searchParams);
      setCurrentPage(0);
    }, 300),
    [setSearchParams, searchParams]
  );

  const handleSearchInputChange = (e) => {
    const newSearchValue = e.target.value;
    setSearchValue(newSearchValue);
    debouncedSearch(newSearchValue);
  };

  return (
    <main className="content px-5 py-3">
      <h3 className="my-3">
        <strong>Users</strong>
      </h3>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill p-3">
              <div className="row mb-3">
                <div className="col-sm-5">
                  <Link
                    to={ADMIN_ADD_USER_URI}
                    className="btn btn-primary float-start"
                  >
                    <FontAwesomeIcon icon={faPlus} />  Add User
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

              {deleteLoading && (
                <div className="alert alert-info">Deleting user...</div>
              )}
              {deleteError?.message && (
                <div className="alert alert-danger">{deleteError.message}</div>
              )}

              <DataTable
                data={userData}
                columns={userColumns}
                selectedItems={selectedItems}
                deleteLoading={deleteLoading}
                handleSelectItem={handleSelectItem}
                handleSelectAll={handleSelectAll}
                hadnleViewItem={handleViewItem}
                handleToggleItem={showDeleteConfirmation}
                toggleAttribute="enabled"
                uniqueIdKey="userId"
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

export default AdminUsersPage;
