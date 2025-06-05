import { useState, useEffect, useMemo, useCallback } from "react";
import styles from "./menu-selection.module.css";
import { useProducts } from "../../hooks/productHooks";
import debounce from "lodash/debounce";
import ErrorDisplay from "../ErrorDisplay/ErrorDisplay";

export default function MenuSelection({
  reservationData,
  updateReservationData,
  onNext,
  onPrevious,
}) {
  const [currentPage, setCurrentPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");
  const [debouncedSearchQuery, setDebouncedSearchQuery] = useState("");
  const { data: productsData, isLoading, error } = useProducts({
    page: currentPage,
    search: debouncedSearchQuery,
  });
  const products = useMemo(() => productsData?.content || [], [productsData]);

  // Debounce search input
  const handleSearchChange = useCallback(() => {
    debounce((value) => {
      setDebouncedSearchQuery(value);
      setCurrentPage(0);
    }, 300)},
    []
  );

  // Store all fetched products across pages
  const [allProducts, setAllProducts] = useState([]);

 // Append or update products when products or search query changes
  useEffect(() => {
    if (products.length > 0) {
      const newProducts = products
        .filter(
          (product) =>
            product.productId &&
            product.productName &&
            product.unitPrice != null &&
            product.description &&
            product.category?.categoryName
        )
        .map((product) => ({
          id: product.productId,
          name: product.productName,
          description: product.description,
          price: product.unitPrice,
          category: product.category.categoryName,
          quantity: 0,
        }));

      setAllProducts((prev) => {
        // Avoid duplicates by filtering out already loaded products
        const existingIds = new Set(prev.map((p) => p.id));
        const uniqueNewProducts = newProducts.filter((p) => !existingIds.has(p.id));
        // Always append unique new products
        return [...prev, ...uniqueNewProducts];
      });
    }
  }, [products, debouncedSearchQuery]);

  // Initialize menu with quantities from reservation data
  const [menu, setMenu] = useState([]);

  // Update menu when allProducts or selectedItems change
  useEffect(() => {
    const updatedMenu = allProducts.map((product) => {
      const existingItem = reservationData.selectedItems.find(
        (item) => item.id === product.id
      );
      return {
        ...product,
        quantity: existingItem ? existingItem.quantity : 0,
      };
    });
    setMenu(updatedMenu);
  }, [allProducts, reservationData.selectedItems]);

  // Filter menu based on search query (client-side fallback)
  const filteredMenu = useMemo(() => {
    if (!searchQuery.trim()) return menu;
    return menu.filter((item) =>
      item.name.toLowerCase().includes(searchQuery.toLowerCase())
    );
  }, [menu, searchQuery]);

  // Group filtered menu items by category
  const menuByCategory = useMemo(() => {
    return filteredMenu.reduce((acc, item) => {
      if (!acc[item.category]) {
        acc[item.category] = [];
      }
      acc[item.category].push(item);
      return acc;
    }, {});
  }, [filteredMenu]);

  // Handle quantity change
  const handleQuantityChange = (id, quantity) => {
    const updatedMenu = menu.map((item) =>
      item.id === id ? { ...item, quantity } : item
    );
    setMenu(updatedMenu);

    // Update selected items in reservation data
    const selectedItems = updatedMenu
      .filter((item) => item.quantity > 0)
      .map((item) => ({
        id: item.id,
        name: item.name,
        price: item.price,
        quantity: item.quantity,
      }));
    updateReservationData({ selectedItems });
  };

  // Calculate total price
  const totalPrice = useMemo(() => {
    return menu
      .filter((item) => item.quantity > 0)
      .reduce((sum, item) => sum + item.price * item.quantity, 0);
  }, [menu]);

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    onNext();
  };

  // Handle search input keydown to prevent form submission
  const handleSearchKeyDown = (e) => {
    if (e.key === "Enter") {
      e.preventDefault(); // Prevent form submission on Enter
    }
  };

  // Handle load more
  const handleLoadMore = () => {
    if (currentPage < productsData?.totalPages - 1) {
      setCurrentPage((prev) => prev + 1);
    }
  };

  // Handle clear search
  const handleClearSearch = () => {
    setSearchQuery("");
    handleSearchChange("");
  };

  if( error?.message ) {
    return <ErrorDisplay message={error.message} />;
  }

  return (
    <form onSubmit={handleSubmit} className={styles.menuForm}>
      <h2 className="mb-4">Select Menu Items</h2>

      <div className="mb-4 d-flex">
        <input
          type="text"
          className="form-control"
          placeholder="Search menu items..."
          value={searchQuery}
          onChange={(e) => {
            setSearchQuery(e.target.value);
            handleSearchChange(e.target.value);
          }}
          onKeyDown={handleSearchKeyDown}
        />
        {searchQuery && (
          <button
            type="button"
            className="btn btn-outline-secondary ms-2"
            onClick={handleClearSearch}
          >
            Clear
          </button>
        )}
      </div>

      {isLoading && currentPage > 0 && (
        <div className="text-center mb-4">Loading more items...</div>
      )}

      {Object.entries(menuByCategory).length === 0 && searchQuery.trim() ? (
        <div className="text-muted">No items match your search.</div>
      ) : (
        Object.entries(menuByCategory).map(([category, items]) => (
          <div key={category} className="mb-4">
            <h3 className={styles.categoryTitle}>{category}</h3>
            <div className="list-group">
              {items.map((item) => (
                <div key={item.id} className={`list-group-item ${styles.menuItem}`}>
                  <div className="d-flex justify-content-between align-items-center">
                    <div>
                      <h5 className="mb-1">{item.name}</h5>
                      <p className="mb-1 text-muted">{item.description}</p>
                      <p className="mb-0 fw-bold">${item.price.toFixed(2)}</p>
                    </div>
                    <div className={styles.quantityControl}>
                      <button
                        type="button"
                        className="btn btn-outline-secondary btn-sm"
                        onClick={() =>
                          handleQuantityChange(item.id, Math.max(0, item.quantity - 1))
                        }
                      >
                        -
                      </button>
                      <span className={styles.quantity}>{item.quantity}</span>
                      <button
                        type="button"
                        className="btn btn-outline-secondary btn-sm"
                        onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                      >
                        +
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))
      )}

      {currentPage < productsData?.totalPages - 1 && (
        <div className="mb-4 text-center">
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={handleLoadMore}
            disabled={isLoading}
          >
            {isLoading ? "Loading..." : "Load More"}
          </button>
        </div>
      )}

      <div className={styles.orderSummary}>
        <h4>Order Summary</h4>
        <p className="fw-bold">Total: ${totalPrice.toFixed(2)}</p>
        <p className="text-muted">
          {reservationData.selectedItems.length === 0
            ? "No items selected yet"
            : `${reservationData.selectedItems.reduce(
                (sum, item) => sum + item.quantity,
                0
              )} items selected`}
        </p>
      </div>

      <div className="d-flex justify-content-between mt-4">
        <button
          type="button"
          className="btn btn-outline-secondary"
          onClick={onPrevious}
        >
          Back to Reservation Details
        </button>
        <button type="submit" className="btn btn-primary">
          Next: Confirm Reservation
        </button>
      </div>
    </form>
  );
}