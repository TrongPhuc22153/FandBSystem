import styles from "./FoodItems.module.css";
import { useState } from "react";

export default function FoodItems({ food, onAddToOrder }) {
  const [imageLoaded, setImageLoaded] = useState(false)

  return (
    <div className={styles.card}>
      <div className={styles.imageContainer}>
        <img
          src={food.picture || "/placeholder.svg"}
          alt={food.productName}
          className={`${styles.image} ${imageLoaded ? styles.loaded : ""}`}
          onLoad={() => setImageLoaded(true)}
        />
        {!imageLoaded && <div className={styles.imagePlaceholder} />}
      </div>

      <div className={styles.content}>
        <h3 className={styles.name}>{food.productName}</h3>
        <p className={styles.description}>{food.description}</p>
        <div className={styles.price}>${food.unitPrice.toFixed(2)}</div>

        <div className={styles.quantitySection}>
          <span className={styles.quantityLabel}></span>
          <div className={styles.quantityControls}>
            <button
              className={styles.addButton}
              onClick={() => onAddToOrder(food)}
            >
              Add to Order
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
