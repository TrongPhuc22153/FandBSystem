import React from "react";
import { Icon } from "@iconify/react";

const RatingDisplay = ({ rating }) => {
  const fullStars = Math.floor(rating);
  const hasHalfStar = rating % 1 !== 0;
  const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

  return (
    <span className={`rating secondary-font`}>
      {/* Full yellow stars */}
      {[...Array(fullStars)].map((_, i) => (
        <Icon
          key={`star-full-${i}`}
          icon="clarity:star-solid"
          className="text-warning"
        />
      ))}
      {/* Half yellow star */}
      {hasHalfStar && (
        <Icon
          key={`star-half`}
          icon="clarity:half-star-solid"
          className="text-warning"
        />
      )}
      {/* Empty black stars */}
      {[...Array(emptyStars)].map((_, i) => (
        <Icon
          key={`star-empty-${i}`}
          icon="clarity:star-solid"
          className="text-black-50"
        />
      ))}
      <span className="ms-1">({rating.toFixed(1)})</span>
    </span>
  );
};

export default RatingDisplay;
