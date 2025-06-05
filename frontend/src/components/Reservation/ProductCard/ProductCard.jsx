import { useState } from "react"
import styles from "./ProductCard.module.css"


export default function ProductCard({ product, quantity, onQuantityChange }) {
  const [imageLoaded, setImageLoaded] = useState(false)

  const incrementQuantity = () => {
    onQuantityChange(quantity + 1)
  }

  const decrementQuantity = () => {
    if (quantity > 0) {
      onQuantityChange(quantity - 1)
    }
  }

  const handleQuantityInput = (e) => {
    const value = Number.parseInt(e.target.value) || 0
    if (value >= 0) {
      onQuantityChange(value)
    }
  }

  return (
    <div className={styles.card}>
      <div className={styles.imageContainer}>
        <img
          src={product.picture || "/placeholder.svg"}
          alt={product.productName}
          className={`${styles.image} ${imageLoaded ? styles.loaded : ""}`}
          onLoad={() => setImageLoaded(true)}
        />
        {!imageLoaded && <div className={styles.imagePlaceholder} />}
      </div>

      <div className={styles.content}>
        <h3 className={styles.name}>{product.productName}</h3>
        <p className={styles.description}>{product.description}</p>
        <div className={styles.price}>${product.unitPrice.toFixed(2)}</div>

        <div className={styles.quantitySection}>
          <span className={styles.quantityLabel}>Quantity:</span>
          <div className={styles.quantityControls}>
            <button onClick={decrementQuantity} disabled={quantity === 0} className={styles.quantityButton}>
              −
            </button>
            <input
              type="number"
              value={quantity}
              onChange={handleQuantityInput}
              className={styles.quantityInput}
              min="0"
            />
            <button onClick={incrementQuantity} className={styles.quantityButton}>
              +
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
