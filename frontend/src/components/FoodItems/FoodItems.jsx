import styles from "./FoodItems.module.css"
import { getPrimaryProductImage } from "../../utils/imageUtils"

export default function FoodItems({ items, onAddToOrder }) {
  return (
    <div className={styles.foodGrid}>
      {items.map((food) => (
        <div key={food.productId} className={styles.foodCard}>
          <div className={styles.imageContainer}>
            <img
              src={getPrimaryProductImage(food.image)}
              alt={food.productName}
              fill
              style={{ objectFit: "cover" }}
            />
          </div>
          <div className={styles.foodContent}>
            <h5 className={styles.foodTitle}>{food.productName}</h5>
            <p className={styles.foodDescription}>{food.description}</p>
            <div className={styles.foodFooter}>
              <span className={styles.foodPrice}>${food.unitPrice.toFixed(2)}</span>
              <button className={styles.addButton} onClick={() => onAddToOrder(food)}>
                Add to Order
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}