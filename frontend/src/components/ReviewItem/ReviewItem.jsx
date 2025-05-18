import { formatDate } from "../../utils/datetimeUtils";
import styles from "./ReviewItem.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import RatingStar from "../RatingStar/RatingStar";

const ReviewItem = ({ review }) => {
  const user = review.profile?.user;

  return (
    <div className={styles["review-item"]}>
      <div className={styles["review-header"]}>
        <div className={styles["user-info"]}>
          {review.customer?.profile?.picture ? (
            <img
              src={review.customer?.profile.picture}
              className={styles["user-avatar"]}
              alt={user?.username || "User"}
            />
          ) : (
            <FontAwesomeIcon className={styles["user-avatar"]} icon={faUser} />
          )}
          <div className={styles["user-details"]}>
            <h5 className={styles.username}>{review.customer?.profile.user.username}</h5>
            <span className={styles["review-date"]}>
              {formatDate(review.createdAt)}
            </span><br />
            {review.createdAt !== review.lastModifiedAt &&
              <span className={styles["review-date"]}>
                <b>Last modified -</b> {formatDate(review.lastModifiedAt)}
              </span>
            }
          </div>
        </div>
        <RatingStar score={review.score}/>
      </div>
      <div className={styles["review-content"]}>
        <p className={styles["review-text"]}>{review.comment}</p>
      </div>
    </div>
  );
};

export default ReviewItem;