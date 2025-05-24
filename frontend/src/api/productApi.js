import { PRODUCTS_ENDPOINT } from "../constants/api";

export const fetchProducts = async ({
  page = 0,
  size = 10,
  categoryId,
  search,
  isFeatured,
  discontinued,
  isDeleted = false,
  direction = "ASC",
  sortBy = "productName",
}) => {
  const params = new URLSearchParams();
  params.append("page", page.toString());
  params.append("size", size.toString());
  params.append("direction", direction.toString());
  params.append("sortBy", sortBy.toString());
  if (search) {
    params.append("search", search.toString());
  }
  if (isFeatured !== undefined && isFeatured !== null) {
    params.append("isFeature", isFeatured.toString())
  }
  if (isDeleted !== undefined && isDeleted !== null) {
    params.append("isDeleted", isDeleted.toString())
  }
  if (categoryId) {
    params.append("categoryId", categoryId.toString())
  }
  if (discontinued !== undefined && discontinued !== null) {
    params.append("discontinued", discontinued.toString())
  }

  const response = await fetch(`${PRODUCTS_ENDPOINT}?${params.toString()}`);
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const fetchProduct = async ({ productId, isDeleted = false }) => {
  const params = new URLSearchParams();
  if (isDeleted != null && isDeleted != undefined) {
    params.append("isDeleted", isDeleted.toString());
  }
  const response = await fetch(`${PRODUCTS_ENDPOINT}/${productId}?${params.toString()}`);
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};


// Update product
export const updateProduct = async ({ productId, productData, token }) => {
  const response = await fetch(`${PRODUCTS_ENDPOINT}/${productId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(productData),
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

// Create new product
export const createProduct = async ({ productData, token }) => {
  const response = await fetch(PRODUCTS_ENDPOINT, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(productData),
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

// Delete product
export const deleteProduct = async ({ productId, isDeleted, token }) => {
  const response = await fetch(`${PRODUCTS_ENDPOINT}/${productId}`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ isDeleted })
  });

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};