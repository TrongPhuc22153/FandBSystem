import { useState, useCallback, useEffect, useMemo } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { useCategories, useCategoryActions } from "../../hooks/categoryHooks";
import DataTable from "../../components/DataTableManagement/DataTable";
import Pagination from "../../components/Pagination/Pagination";
import { debounce } from "lodash";
import {
  ADMIN_ADD_CATEGORY_URI,
  ADMIN_CATEGORIES_URI,
} from "../../constants/routes";
import { getImageSrc } from "../../utils/imageUtils";
import { useModal } from "../../context/ModalContext";
import { Badge } from "react-bootstrap";
import { SORTING_DIRECTIONS } from "../../constants/webConstant";

const AdminCategoriesPage = () => {
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

  const { data: categoryDataResult, mutate } = useCategories({
    page: currentPage,
    search: searchValue,
    direction: SORTING_DIRECTIONS.DESC,
    field: "createdAt",
    isDeleted: null,
  });
  const {
    handleDeleteCategory,
    deleteError,
    deleteLoading,
    deleteSuccess,
    resetDelete,
  } = useCategoryActions();

  const categoryData = useMemo(
    () => categoryDataResult?.content || [],
    [categoryDataResult]
  );
  const totalPages = categoryDataResult?.totalPages || 0;

  const categoryColumns = [
    {
      key: "image",
      title: "Image",
      render: (category) => (
        <img
          className="rounded img-icon"
          src={category.picture || getImageSrc()}
          alt={category.categoryName}
        />
      ),
    },
    { key: "categoryName", title: "Category Name" },
    { key: "description", title: "Description" },
    {
      key: "isDeleted",
      title: "Status",
      render: (product) =>
        product.isDeleted ? (
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
        const success = await handleDeleteCategory(idToDelete, !isDeleted);
        if (success) {
          // Optimistically update the cache
          mutate((prevData) => {
            if (!prevData?.content) return prevData;
            return {
              ...prevData,
              content: prevData.content.filter(
                (category) => category.id !== idToDelete
              ),
            };
          }, false);
          mutate(); // Trigger background revalidation
          setSelectedItems((prevSelectedItems) =>
            prevSelectedItems.filter((itemId) => itemId !== idToDelete)
          );
          resetDelete(); // Reset delete success/error messages
        }
      }
    },
    [handleDeleteCategory, mutate, resetDelete]
  );

  const showDeleteConfirmation = useCallback(
    (id, isDeleted) => {
      onOpen({
        title: `${isDeleted ? `Enable` : `Delete`} Category!`,
        message: `Do you want to ${
          isDeleted ? `enable` : `delete`
        } this category?`,
        onYes: () => handleDeleteConfirm(id, isDeleted),
      });
    },
    [onOpen, handleDeleteConfirm]
  );

  // update item
  const handleUpdateItem = useCallback(
    (id) => {
      navigate(`${ADMIN_CATEGORIES_URI}/${id}`);
    },
    [navigate]
  );

  // select item
  const handleSelectAll = useCallback(
    (event) => {
      const isChecked = event.target.checked;
      if (isChecked) {
        const allCategoryIds = categoryData.map((category) => category.id);
        setSelectedItems(allCategoryIds);
      } else {
        setSelectedItems([]);
      }
    },
    [categoryData]
  );

  const handleSelectItem = useCallback((event, categoryId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, categoryId]),
      ]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== categoryId)
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
        <strong>Categories</strong>
      </h3>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill p-3">
              <div className="row mb-3">
                <div className="col-sm-5">
                  <Link
                    to={ADMIN_ADD_CATEGORY_URI}
                    className="btn btn-primary float-start"
                  >
                    <FontAwesomeIcon icon={faPlus} />
                    &nbsp; Add Category
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
                <div className="alert alert-info">Deleting category...</div>
              )}
              {deleteSuccess && (
                <div className="alert alert-success">{deleteSuccess}</div>
              )}
              {deleteError?.message && (
                <div className="alert alert-danger">{deleteError.message}</div>
              )}

              <DataTable
                data={categoryData}
                columns={categoryColumns}
                selectedItems={selectedItems}
                handleSelectItem={handleSelectItem}
                handleSelectAll={handleSelectAll}
                handleToggleItem={showDeleteConfirmation}
                handleUpdateItem={handleUpdateItem}
                toggleAttribute="isDeleted"
                uniqueIdKey="categoryId"
                deleteLoading={deleteLoading}
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

export default AdminCategoriesPage;
