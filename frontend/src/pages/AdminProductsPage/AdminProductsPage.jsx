import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState, useCallback, useEffect, useMemo } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import {
  ADMIN_ADD_PRODUCT_URI,
  ADMIN_PRODUCTS_URI,
} from "../../constants/routes";
import { getImageSrc } from "../../utils/imageUtils";
import Pagination from "../../components/Pagination/Pagination";
import { useProducts, useProductActions } from "../../hooks/productHooks";
import DataTable from "../../components/DataTableManagement/DataTable";
import { useModal } from "../../context/ModalContext";
import { Badge } from "react-bootstrap";

const AdminProductsPage = () => {
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

  const { data: productDataResult, mutate } = useProducts({
    page: currentPage,
    search: searchValue,
    sortBy: "createdAt",
    direction: "DESC",
    isDeleted: null,
  });

  const {
    handleDeleteProduct,
    deleteError,
    deleteLoading,
    deleteSuccess,
    resetDelete,
  } = useProductActions();

  const productData = useMemo(
    () => productDataResult?.content || [],
    [productDataResult]
  );
  const totalPages = productDataResult?.totalPages || 0;

  const productColumns = [
    {
      key: "image",
      title: "Image",
      render: (product) => (
        <img
          className="rounded img-icon"
          src={product.picture || getImageSrc()}
          alt={product.productName}
          style={{ height: "50px" }}
        />
      ),
    },
    { key: "productName", title: "Product" },
    {
      key: "category",
      title: "Category",
      render: (product) => product.category?.categoryName,
    },
    { key: "unitPrice", title: "Price" },
    { key: "unitsInStock", title: "Quantity" },
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

  // delete product
  const handleDeleteConfirm = useCallback(
    async (idToDelete, isDeleted) => {
      if (idToDelete) {
        const success = await handleDeleteProduct(idToDelete, !isDeleted);
        if (success) {
          // Optimistically update the cache
          mutate((prevData) => {
            if (!prevData?.content) return prevData;
            return {
              ...prevData,
              content: prevData.content.filter(
                (product) => product.id !== idToDelete
              ),
            };
          }, false);
          mutate();
          setSelectedItems((prevSelectedItems) =>
            prevSelectedItems.filter((itemId) => itemId !== idToDelete)
          );
          resetDelete();
        }
      }
    },
    [handleDeleteProduct, mutate, resetDelete]
  );

  const showDeleteConfirmation = useCallback(
    (id, isDeleted) => {
      onOpen({
        title: `${isDeleted ? "Enable" : "Delete"} Product!`,
        message: `Do you want to ${
          isDeleted ? "enable" : "delete"
        } this product?`,
        onYes: () => handleDeleteConfirm(id, isDeleted),
      });
    },
    [onOpen, handleDeleteConfirm]
  );

  // update
  const handleUpdateItem = useCallback(
    (id) => {
      navigate(`${ADMIN_PRODUCTS_URI}/${id}`);
    },
    [navigate]
  );

  const handleSelectAll = useCallback(
    (event) => {
      const isChecked = event.target.checked;
      if (isChecked) {
        const allFoodIds = productData.map((product) => product.id);
        setSelectedItems(allFoodIds);
      } else {
        setSelectedItems([]);
      }
    },
    [productData]
  );

  const handleSelectItem = useCallback((event, productId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, productId]),
      ]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== productId)
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
        <strong>Products</strong>
      </h3>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill p-3">
              <div className="row mb-3">
                <div className="col-sm-5">
                  <Link
                    to={ADMIN_ADD_PRODUCT_URI}
                    className="btn btn-primary float-start"
                  >
                    <FontAwesomeIcon icon={faPlus} />
                    &nbsp; Add Products
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

              {deleteSuccess && (
                <div className="alert alert-success">{deleteSuccess}</div>
              )}
              {deleteError?.message && (
                <div className="alert alert-danger">{deleteError.message}</div>
              )}

              <DataTable
                data={productData}
                columns={productColumns}
                selectedItems={selectedItems}
                handleToggleItem={showDeleteConfirmation}
                handleUpdateItem={handleUpdateItem}
                handleSelectAll={handleSelectAll}
                handleSelectItem={handleSelectItem}
                deleteLoading={deleteLoading}
                toggleAttribute="isDeleted"
                uniqueIdKey="productId"
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

export default AdminProductsPage;
