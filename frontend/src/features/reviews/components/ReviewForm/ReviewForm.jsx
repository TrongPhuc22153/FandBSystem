import { useCallback, useEffect, useState } from "react";
import styles from "./ReviewForm.module.css";
import { useModal } from "../../../../shared/context/ModalContext";
import { useAlert } from "../../../../shared/context/AlertContext";
import { mutate } from "swr";
import { PRODUCT_RATING_ENDPOINT } from "../../../../shared/constants/api";
import {useDispatch, useSelector} from "react-redux";
import {resetCreateState} from "../../slices/reviewSlice";
import {createUserReview} from "../../thunks/reviewThunk";

const ReviewForm = ({ review, productId, onCreateOrUpdateUserReview }) => {
  const [initialReview, setInitialReview] = useState({
    rating: 0,
    comment: "",
  });
  const [hover, setHover] = useState(0);
  const [submitted, setSubmitted] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const {
    success: createSuccess,
    error: createError,
  } = useSelector((state) => state.reviews.create);
  const dispatch = useDispatch();

  useEffect(() => {
    setInitialReview({
      rating: review.score || 0,
      comment: review.comment || "",
    });
  }, [review.score, review.comment]);

  useEffect(() => {
    setFieldErrors(createError?.fields ?? {});
  }, [createError]);

  useEffect(() => {
    if (createSuccess) {
      showNewAlert({
        message: createSuccess,
        action: () => dispatch(resetCreateState()),
      });
      dispatch(resetCreateState())
    }
  }, [createSuccess, showNewAlert, dispatch]);

  const handleCreateOrUpdate = useCallback(async () => {
    const ratingData = {
      productId: productId,
      score: initialReview.rating,
      comment: initialReview.comment,
    };
    const data = await dispatch(createUserReview(ratingData));
    if (createUserReview.fulfilled.match(data)) {
      setHover(0);
      await mutate(`${PRODUCT_RATING_ENDPOINT}/${productId}`)
      onCreateOrUpdateUserReview?.();
    }
  }, [dispatch, initialReview, productId, onCreateOrUpdateUserReview]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onOpen({
      title: "Submit review",
      message: "Do you want to submit this review?",
      onYes: handleCreateOrUpdate,
    });
  };

  // input change
  const handleCommentChange = (event) => {
    setInitialReview((prev) => ({
      ...prev,
      comment: event.target.value,
    }));
  };

  const handleRatingChange = (index) => {
    setInitialReview((prev) => ({
      ...prev,
      rating: index,
    }));
  };

  if (submitted) {
    return (
      <div className="alert alert-success" role="alert">
        Thank you for your review!
        <button className="btn btn-link" onClick={() => setSubmitted(false)}>
          Write another review
        </button>
      </div>
    );
  }

  return (
    <>
      <div className={`card ${styles["review-card"]}`}>
        <div className={styles["card-header"]}>
          <h3>Write a Review</h3>
        </div>
        <div className={styles["card-body"]}>
          <form onSubmit={handleSubmit}>
            {createError?.message && (
              <div className="alert alert-danger">{createError.message}</div>
            )}

            <div className="mb-3">
              <label className="form-label">Rating</label>
              <div className={styles["star-rating"]}>
                {[...Array(5)].map((_, index) => {
                  index += 1;
                  return (
                    <button
                      type="button"
                      key={index}
                      className={`btn ${
                        index <= (hover || initialReview.rating)
                          ? styles["text-warning"]
                          : styles["text-secondary"]
                      }`}
                      onClick={() => handleRatingChange(index)}
                      onMouseEnter={() => setHover(index)}
                      onMouseLeave={() => setHover(review.rating)}
                    >
                      <span>&#9733;</span>
                    </button>
                  );
                })}
              </div>
              {fieldErrors?.score &&
                fieldErrors.score.map((error, index) => (
                  <div key={index} className="error-message text-danger">
                    {error}
                  </div>
                ))}
            </div>

            <div className="mb-3">
              <label htmlFor="comment" className="form-label">
                Your Review
              </label>
              <textarea
                className={`form-control ${styles["form-control"]} ${
                  fieldErrors?.comment ? "is-invalid" : ""
                }`}
                id="comment"
                rows="4"
                value={initialReview.comment}
                onChange={handleCommentChange}
                placeholder="Share your experience with this product..."
              ></textarea>
              {fieldErrors?.comment &&
                fieldErrors.comment.map((error, index) => (
                  <div key={index} className="error-message text-danger">
                    {error}
                  </div>
                ))}
            </div>

            <button
              type="submit"
              className={`btn btn-primary ${styles["btn-primary"]}`}
            >
              Submit Review
            </button>
          </form>
        </div>
      </div>
      {submitted && (
        <div
          className={`alert alert-success ${styles["alert-success"]}`}
          role="alert"
        >
          Thank you for your review!
          <button
            className={`btn btn-link ${styles["btn-link"]}`}
            onClick={() => setSubmitted(false)}
          >
            Write another review
          </button>
        </div>
      )}
    </>
  );
};

export default ReviewForm;