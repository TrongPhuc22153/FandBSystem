import { PRODUCT_RATING_ENDPOINT, RATINGS_ENDPOINT } from "../constants/api";

export const fetchRating = async ({ productId }) => {
  const response = await fetch(`${PRODUCT_RATING_ENDPOINT}/${productId}`);
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const fetchUserProductRating = async ({ productId, token }) => {
  const response = await fetch(`${PRODUCT_RATING_ENDPOINT}/${productId}/me`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// Create a new rating
export const createRating = async ({ ratingData, token }) => {
  const response = await fetch(RATINGS_ENDPOINT, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(ratingData),
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

// Update an existing rating
export const updateRating = async ({ ratingId, ratingData, token }) => {
  const response = await fetch(`${RATINGS_ENDPOINT}/${ratingId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(ratingData),
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

// Delete a rating
export const deleteRating = async ({ ratingId, token }) => {
  const response = await fetch(`${RATINGS_ENDPOINT}/${ratingId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};