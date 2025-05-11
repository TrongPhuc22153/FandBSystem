import styles from "./MenuCategories.module.css";

export default function MenuCategories({
  categories,
  selectedCategoryId,
  onSelectCategory,
}) {
  return (
    <div className={styles.categoryContainer}>
      {categories.map((category) => (
        <button
          key={category.categoryId}
          className={`${styles.categoryButton} ${
            selectedCategoryId == category.categoryId ? styles.selected : ""
          }`}
          onClick={() => onSelectCategory(category.categoryId)}
        >
          {category.categoryName}
        </button>
      ))}
    </div>
  );
}
