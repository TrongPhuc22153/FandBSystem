import { CATEGORIES_ENDPOINT } from "../constants/api";

// GET all categories (paginated)
export const fetchCategories = async ({ page = 0, size = 10, direction = "ASC", field = "categoryName", isDeleted = false }) => {
  const params = new URLSearchParams();
  params.append("page", page.toString())
  params.append("size", size.toString())
  params.append("direction", direction.toString())
  params.append("field", field.toString())
  if (isDeleted != null && isDeleted != undefined) {
    params.append("isDeleted", isDeleted.toString())
  }
  const response = await fetch(
    `${CATEGORIES_ENDPOINT}?${params.toString()}`
  );
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// GET category by ID
export const fetchCategoryById = async (id, isDeleted = false) => {
    const params = new URLSearchParams();
    if(isDeleted!=null && isDeleted!=undefined){
      params.append("isDeleted", isDeleted.toString())
    }
    const response = await fetch(`${CATEGORIES_ENDPOINT}/${id}?${params.toString()}`);
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// POST create new category
export const createCategory = async (categoryData, token) => {
  const response = await fetch(CATEGORIES_ENDPOINT, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(categoryData),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// PUT update category by ID
export const updateCategory = async (id, categoryData, token) => {
  const response = await fetch(`${CATEGORIES_ENDPOINT}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(categoryData),
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

// DELETE category by ID
export const deleteCategory = async (id, isDeleted, token) => {
  const response = await fetch(`${CATEGORIES_ENDPOINT}/${id}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify({ isDeleted })
  });
  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};
