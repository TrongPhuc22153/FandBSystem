import { useCallback, useMemo, useState } from "react";
import ProductCard from "../ProductCard/ProductCard";
import ReservationSummary from "../ReservationSummary/ReservationSummary";
import styles from "./Step2ProductSelection.module.css";
import { useProducts } from "../../../hooks/productHooks";
import ErrorDisplay from "../../ErrorDisplay/ErrorDisplay";
import { debounce } from "lodash";
import { useSearchParams } from "react-router-dom";
import { Search } from "lucide-react";

export default function Step2ProductSelection({
  data,
  updateData,
  onNext,
  onBack,
}) {
  const [currentPage, setCurrentPage] = useState(0);
  const [searchParams, setSearchParams] = useSearchParams();
  const [searchValue, setSearchValue] = useState("");
  const { data: productsData, error: productsError } = useProducts({
    page: currentPage,
    search: searchValue,
  });

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

  const products = useMemo(() => productsData?.content || [], [productsData]);
  const totalPages = useMemo(
    () => productsData?.totalPages || 0,
    [productsData]
  );

  const updateProductQuantity = (productId, quantity) => {
    const product = products.find((p) => p.productId === productId);
    if (!product) return;

    const updatedProducts = [...data.menuItems];
    const existingIndex = updatedProducts.findIndex(
      (p) => p.productId === productId
    );

    if (quantity === 0) {
      if (existingIndex > -1) {
        updatedProducts.splice(existingIndex, 1);
      }
    } else {
      const productData = {
        productId: product.productId,
        productName: product.productName,
        unitPrice: product.unitPrice,
        quantity,
        picture: product.picture,
      };

      if (existingIndex > -1) {
        updatedProducts[existingIndex] = productData;
      } else {
        updatedProducts.push(productData);
      }
    }

    updateData({ menuItems: updatedProducts });
  };

  const getProductQuantity = (productId) => {
    const product = data.menuItems.find((p) => p.productId === productId);
    return product ? product.quantity : 0;
  };

  if (productsError?.message) {
    return <ErrorDisplay message={productsError.message} />;
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Select Your Products</h2>
        <p>Choose from our delicious menu items</p>
      </div>

      <div className="mb-2">
        <div className={styles.searchContainer}>
          <Search className={styles.searchIcon} />
          <input
            type="text"
            placeholder="Search tables..."
            className={styles.searchInput}
            value={searchValue}
            onChange={handleSearchInputChange}
          />
        </div>
      </div>

      <div className={styles.content}>
        <div className={styles.productsSection}>
          <div className={styles.productsGrid}>
            {products.map((product) => (
              <ProductCard
                key={product.productId}
                product={product}
                quantity={getProductQuantity(product.productId)}
                onQuantityChange={(quantity) =>
                  updateProductQuantity(product.productId, quantity)
                }
              />
            ))}
          </div>

          {totalPages > 1 && (
            <div className={styles.pagination}>
              <button
                onClick={() => setCurrentPage((prev) => Math.max(1, prev - 1))}
                disabled={currentPage === 1}
                className={styles.paginationButton}
              >
                Previous
              </button>

              <div className={styles.pageNumbers}>
                {Array.from({ length: totalPages }, (_, i) => i + 1).map(
                  (page) => (
                    <button
                      key={page}
                      onClick={() => setCurrentPage(page)}
                      className={`${styles.pageNumber} ${
                        currentPage === page ? styles.active : ""
                      }`}
                    >
                      {page}
                    </button>
                  )
                )}
              </div>

              <button
                onClick={() =>
                  setCurrentPage((prev) => Math.min(totalPages, prev + 1))
                }
                disabled={currentPage === totalPages}
                className={styles.paginationButton}
              >
                Next
              </button>
            </div>
          )}
        </div>

        <div className={styles.summarySection}>
          <ReservationSummary data={data} />
        </div>
      </div>

      <div className={styles.actions}>
        <button onClick={onBack} className={styles.backButton}>
          Back
        </button>
        <button onClick={onNext} className={styles.nextButton}>
          Continue to Overview
        </button>
      </div>
    </div>
  );
}
