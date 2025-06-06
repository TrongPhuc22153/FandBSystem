import { useState, useEffect, useMemo, useCallback } from "react";
import styles from "./kitchen-table.module.css";
import { useProductActions, useProducts } from "../../../hooks/productHooks";
import ErrorDisplay from "../../ErrorDisplay/ErrorDisplay";
import { SORTING_DIRECTIONS } from "../../../constants/webConstant";
import Pagination from "../../Pagination/Pagination";
import { useSearchParams } from "react-router-dom";
import { useAlert } from "../../../context/AlertContext";
import { useModal } from "../../../context/ModalContext";
import { useCategories } from "../../../hooks/categoryHooks";
import { debounce } from "lodash";
import { Search } from "lucide-react";

export default function MenuItemsTable() {
  const [menuItems, setMenuItems] = useState([]);
  const [seletedAvailable, setSelectedAvailable] = useState(null);
  const [selectedCategoryId, setSelectedCategoryId] = useState(null);

  const [searchParams, setSearchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [searchValue, setSearchValue] = useState(
    searchParams.get("searchValue") || ""
  );

  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  const debouncedSearch = useCallback(
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

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const {
    handleDeleteProduct,
    deleteError,
    resetDelete,
    handleUpdateProductQuantity,
    updateQuantityError,
    updateQuantitySuccess,
    resetUpdateQuantity,
  } = useProductActions();

  const { showNewAlert } = useAlert();
  const { onOpen } = useModal();

  const { data: categoriesData } = useCategories();

  const { data: productsData, error: productsError } = useProducts({
    sortBy: "productId",
    direction: SORTING_DIRECTIONS.ASC,
    categoryId: selectedCategoryId,
    page: currentPage,
    size: 20,
    isDeleted: seletedAvailable,
    search: searchValue,
  });

  const categories = useMemo(
    () => categoriesData?.content || [],
    [categoriesData]
  );

  const products = useMemo(() => productsData?.content || [], [productsData]);
  const totalPages = useMemo(
    () => productsData?.totalPages || 0,
    [productsData]
  );

  useEffect(() => {
    if (deleteError?.message) {
      showNewAlert({
        message: deleteError.message,
        variant: "danger",
        action: resetDelete,
      });
    }
    if (updateQuantityError?.message) {
      showNewAlert({
        message: updateQuantityError.message,
        variant: "danger",
        action: resetUpdateQuantity,
      });
    }
  }, [
    showNewAlert,
    deleteError,
    resetDelete,
    updateQuantityError,
    resetUpdateQuantity,
  ]);

  useEffect(() => {
    if (updateQuantitySuccess) {
      showNewAlert({
        message: updateQuantitySuccess,
        action: resetUpdateQuantity,
      });
    }
  }, [showNewAlert, updateQuantitySuccess, resetUpdateQuantity]);

  useEffect(() => {
    setMenuItems(products);
  }, [products]);

  const updateMenuItemStock = (itemId, newStock) => {
    setMenuItems((prevItems) =>
      prevItems.map((item) =>
        item.productId === itemId
          ? {
              ...item,
              unitsInStock: newStock,
              isDeleted: newStock <= 0,
            }
          : item
      )
    );
  };

  const updateProductQuantity = useCallback(
    async (itemId) => {
      const menuItem = menuItems.find((item) => item.productId === itemId);
      await handleUpdateProductQuantity(itemId, menuItem.unitsInStock);
    },
    [handleUpdateProductQuantity, menuItems]
  );

  const showConfirmUpdateMenuItem = (itemId) => {
    onOpen({
      title: "Update product",
      message: "Do you want to update this product?",
      onYes: () => updateProductQuantity(itemId),
    });
  };

  const toggleAvailability = async (itemId) => {
    const newIsDeleted = menuItems.find((item) => item.productId === itemId)
      .isDeleted
      ? false
      : true;
    setMenuItems((prevItems) =>
      prevItems.map((item) =>
        item.productId === itemId ? { ...item, isDeleted: newIsDeleted } : item
      )
    );
    await handleDeleteProduct(itemId, newIsDeleted);
  };

  const getStockStatusClass = (item) => {
    if (item.unitsInStock <= 0) return "bg-danger";
    if (item.unitsInStock < item.minimumStock) return "bg-warning";
    return "bg-success";
  };

  if (productsError?.message) {
    return <ErrorDisplay message={productsError.message} />;
  }

  return (
    <div>
      <div className="d-flex justify-content-between mb-3 align-items-center">
        <h3>Menu Items & Inventory</h3>
        <div className="d-flex gap-2 align-items-center">
          <div className="input-group" style={{ maxWidth: "300px" }}>
            <div className={styles.searchContainer}>
              <Search className={styles.searchIcon} />
              <input
                type="text"
                placeholder="Search products..."
                className={styles.searchInput}
                value={searchValue}
                onChange={handleSearchInputChange}
              />
            </div>
          </div>
          <select
            className="form-select"
            value={selectedCategoryId}
            onChange={(e) => setSelectedCategoryId(e.target.value)}
          >
            <option value="">All Categories</option>
            {categories.map((category) => (
              <option key={category.categoryId} value={category.categoryId}>
                {category.categoryName}
              </option>
            ))}
          </select>
          <select
            className="form-select"
            value={seletedAvailable}
            onChange={(e) => setSelectedAvailable(e.target.value)}
          >
            <option value="">All Items</option>
            <option value="false">Available Only</option>
            <option value="true">Unavailable Only</option>
          </select>
        </div>
      </div>

      <div className={`table-responsive ${styles.tableContainer}`}>
        <table className="table table-striped table-hover">
          <thead className="table-dark">
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Category</th>
              <th>Current Stock</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {menuItems.length === 0 ? (
              <tr>
                <td colSpan={7} className="text-center">
                  No menu items found
                </td>
              </tr>
            ) : (
              menuItems.map((item) => (
                <tr key={item.productId} className={styles.menuItemRow}>
                  <td>{item.productId}</td>
                  <td>{item.productName}</td>
                  <td>{item.category.categoryName}</td>
                  <td>
                    <div className="input-group input-group-sm">
                      <button
                        className="btn btn-outline-secondary"
                        onClick={() =>
                          updateMenuItemStock(
                            item.productId,
                            Math.max(0, item.unitsInStock - 1)
                          )
                        }
                        disabled={item.unitsInStock <= 0}
                      >
                        -
                      </button>
                      <input
                        type="number"
                        className="form-control text-center"
                        value={item.unitsInStock}
                        onChange={(e) =>
                          updateMenuItemStock(
                            item.productId,
                            Math.max(0, Number.parseInt(e.target.value) || 0)
                          )
                        }
                        min="0"
                      />
                      <button
                        className="btn btn-outline-secondary"
                        onClick={() =>
                          updateMenuItemStock(
                            item.productId,
                            item.unitsInStock + 1
                          )
                        }
                      >
                        +
                      </button>
                    </div>
                    <small className="d-block text-muted">
                      Min: {item.minimumStock}
                    </small>
                  </td>
                  <td>
                    <span className={`badge ${getStockStatusClass(item)}`}>
                      {item.unitsInStock <= 0
                        ? "Out of Stock"
                        : item.unitsInStock < item.minimumStock
                        ? "Low Stock"
                        : "In Stock"}
                    </span>
                    <div className="form-check form-switch mt-1">
                      <input
                        className="form-check-input"
                        type="checkbox"
                        checked={!item.isDeleted}
                        onChange={() => toggleAvailability(item.productId)}
                        id={`availability-${item.productId}`}
                      />
                      <label
                        className="form-check-label"
                        htmlFor={`availability-${item.productId}`}
                      >
                        {!item.isDeleted ? "Available" : "Unavailable"}
                      </label>
                    </div>
                  </td>
                  <td>
                    <button
                      className="btn btn-sm btn-outline-primary"
                      onClick={() => showConfirmUpdateMenuItem(item.productId)}
                    >
                      Restock
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
        {totalPages > 1 && (
          <Pagination totalPages={totalPages} currentPage={currentPage + 1} />
        )}
      </div>
    </div>
  );
}
