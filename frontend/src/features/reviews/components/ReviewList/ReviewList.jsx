import ReviewItem from "../ReviewItem/ReviewItem";
import styles from "./ReviewList.module.css";

const ReviewList = ({ reviews, averageScore, totalReviews  }) => {
  const roundedAverage = Math.round(averageScore * 10) / 10;

  return (
    <div className={styles["review-list-container"]}>
      <h3 className={styles["review-list-title"]}>Customer Reviews</h3>

      <div className={styles["review-summary"]}>
        <div className={styles["average-rating"]}>
          <span className={styles["rating-number"]}>{roundedAverage}</span>
          <div className={styles["rating-stars"]}>
            {[...Array(5)].map((_, index) => (
              <span
                key={index}
                className={`${styles.star} ${
                  index < Math.floor(averageScore) ? styles.filled : ""
                }`}
              >
                &#9733;
              </span>
            ))}
          </div>
          <span className={styles["rating-count"]}>
            Based on {totalReviews} reviews
          </span>
        </div>
      </div>

      <div className={styles.reviews}>
        {reviews.map((review, index) => (
          <ReviewItem key={index} review={review} />
        ))}
      </div>
    </div>
  );
};

export default ReviewList;
