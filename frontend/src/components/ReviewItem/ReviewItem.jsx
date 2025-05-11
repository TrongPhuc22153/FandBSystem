import React from "react";
import RatingDisplay from "../RatingDisplay/RatingDisplay";
import { getImageSrc } from "../../utils/imageUtils";

const ReviewItem = ({ review }) => {
  return (
    <div className="col-lg-6 d-flex flex-wrap gap-3">
      <div className="col-md-2">
        <div className="image-holder">
          <img
            src={getImageSrc(review?.profile?.picture)}
            alt="review"
            className="img-fluid rounded-circle"
          />
        </div>
      </div>
      <div className="col-md-8">
        <div className="review-content">
          <RatingDisplay rating={review.score} />
          <div className="review-header">
            <span className="author-name text-black fw-bold">
              {review?.profile.user.username || "Anonymous"}
            </span>
            <span className="review-date">– {review.lastUpdatedOn}</span>
          </div>
          <p>{review.comment || ""}</p>
        </div>
      </div>
    </div>
  );
};

export default ReviewItem;
