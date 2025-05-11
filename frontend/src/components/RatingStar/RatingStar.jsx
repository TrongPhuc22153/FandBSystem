import styles from "./RatingStar.module.css";

export default function RatingStar({ score }) {
  return (
    <div className={styles["review-rating"]}>
      {[...Array(5)].map((_, index) => (
        <span
          key={index}
          className={`${styles.star} ${index < score ? styles.filled : ""}`}
        >
          &#9733;
        </span>
      ))}
    </div>
  );
}
