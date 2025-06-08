
import { useMemo, useState } from "react"
import ProductCard from "../ProductCard/ProductCard"
import ReservationSummary from "../ReservationSummary/ReservationSummary"
import styles from "./Step2ProductSelection.module.css"
import { useProducts } from "../../../hooks/productHooks"

export default function Step2ProductSelection({ data, updateData, onNext, onBack }) {
  const [currentPage, setCurrentPage] = useState(0)
  const [searchQuery, setSearchQuery] = useState("");
  const [debouncedSearchQuery, setDebouncedSearchQuery] = useState("");
  const { data: productsData, isLoading, error } = useProducts({
    page: currentPage,
    search: debouncedSearchQuery
  });
  const products = useMemo(() => productsData?.content || [], [productsData]);
  const totalPages = useMemo(() => productsData?.totalPages || 0, [productsData]);

  const updateProductQuantity = (productId, quantity) => {
    const product = products.find((p) => p.productId === productId)
    if (!product) return

    const updatedProducts = [...data.menuItems]
    const existingIndex = updatedProducts.findIndex((p) => p.productId === productId)

    if (quantity === 0) {
      if (existingIndex > -1) {
        updatedProducts.splice(existingIndex, 1)
      }
    } else {
      const productData = {
        productId: product.productId,
        productName: product.productName,
        unitPrice: product.unitPrice,
        quantity,
        picture: product.picture,
      }

      if (existingIndex > -1) {
        updatedProducts[existingIndex] = productData
      } else {
        updatedProducts.push(productData)
      }
    }

    updateData({ menuItems: updatedProducts })
  }

  const getProductQuantity = (productId) => {
    const product = data.menuItems.find((p) => p.productId === productId)
    return product ? product.quantity : 0
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Select Your Products</h2>
        <p>Choose from our delicious menu items</p>
      </div>

      <div className={styles.content}>
        <div className={styles.productsSection}>
          <div className={styles.productsGrid}>
            {products.map((product) => (
              <ProductCard
                key={product.productId}
                product={product}
                quantity={getProductQuantity(product.productId)}
                onQuantityChange={(quantity) => updateProductQuantity(product.productId, quantity)}
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
                {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                  <button
                    key={page}
                    onClick={() => setCurrentPage(page)}
                    className={`${styles.pageNumber} ${currentPage === page ? styles.active : ""}`}
                  >
                    {page}
                  </button>
                ))}
              </div>

              <button
                onClick={() => setCurrentPage((prev) => Math.min(totalPages, prev + 1))}
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
  )
}
