const API_URL = "http://localhost:8080"


export const LOGIN_URL = `${API_URL}/api/v1/auth/login`;
export const LOGOUT_URL = `${API_URL}/api/v1/auth/logout`;
export const REGISTER_URL = `${API_URL}/api/v1/auth/register`;

export const AUTHENTICATED_USER_URL = `${API_URL}/api/v1/users/me`;
export const USERS_URL = `${API_URL}/api/v1/users`;

export const EMPLOYEE_USER_PROFILE_URL = `${API_URL}/api/v1/employees/profile/me`;


export const CUSTOMER_USER_PROFILE_URL = `${API_URL}/api/v1/customers/profile/me`;

export const USER_CART_URL = `${API_URL}/api/v1/carts/me`;
export const UPDATE_ITEMS_CART_URL = `${API_URL}/api/v1/carts/me/items`;


export const TABLES_URL = `${API_URL}/api/v1/tables`;


export const CATEGORIES_URL =  `${API_URL}/api/v1/categories`;
export const PRODUCT_URL =  `${API_URL}/api/v1/products`;