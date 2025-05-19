const API_BASE_URL = "http://localhost:8080";
const WEBSOCKET_URL = "ws://localhost:8080/chat";
export const WEBSOCKET_ENDPOINT = `${WEBSOCKET_URL}`;

export const PRODUCTS_ENDPOINT = `${API_BASE_URL}/api/v1/products`;

export const CATEGORIES_ENDPOINT = `${API_BASE_URL}/api/v1/categories`;

export const PRODUCT_RATING_ENDPOINT = `${API_BASE_URL}/api/v1/ratings/products`;
export const RATINGS_ENDPOINT = `${API_BASE_URL}/api/v1/ratings`;

const AUTH_PATH = `${API_BASE_URL}/api/v1/auth`
export const REGISTER_ENDPOINT = `${AUTH_PATH}/register`;
export const LOGIN_ENDPOINT = `${AUTH_PATH}/login`;
export const LOGOUT_ENDPOINT = `${AUTH_PATH}/logout`;
export const FORGOT_PASSWORD_ENDPOINT = `${AUTH_PATH}/forgot`;
export const VALIDATE_TOKEN_ENDPOINT = `${AUTH_PATH}/validate`;
export const RESET_PASSWORD_ENDPOINT = `${AUTH_PATH}/reset`;

export const USER_ENDPOINT = `${API_BASE_URL}/api/v1/users/me`;
export const USERS_ENDPOINT = `${API_BASE_URL}/api/v1/users`;
export const USER_PASSWORD_ENDPOINT = `${API_BASE_URL}/api/v1/users/password`;

export const USER_CART_ENDPOINT = `${API_BASE_URL}/api/v1/carts/me`;
export const USER_CART_ITEMS_ENDPOINT = `${API_BASE_URL}/api/v1/carts/me/items`;

export const USER_PROFILE_ENDPOINT = `${API_BASE_URL}/api/v1/profiles/me`;
export const USER_PROFILES_ENDPOINT = `${API_BASE_URL}/api/v1/profiles`;

export const USER_NOTIFICATIONS_ENDPOINT = `${API_BASE_URL}/api/v1/notifications/me`;
export const USER_NOTIFICATION_ENDPOINT = (id) => `${API_BASE_URL}/api/v1/notifications/${id}/me`;

export const SHIPPING_ADDRESS_ENDPOINT = `${API_BASE_URL}/api/v1/addresses`;
export const USER_SHIPPING_ADDRESS_ENDPOINT = `${API_BASE_URL}/api/v1/addresses/me`;

export const IMAGE_ENDPOINT = `${API_BASE_URL}/api/v1/images`;

export const ROLES_ENDPOINT = `${API_BASE_URL}/api/v1/roles`;

export const ORDERS_ENDPOINT = `${API_BASE_URL}/api/v1/orders`;

export const RESERVATION_TABLES_ENDPOINT = `${API_BASE_URL}/api/v1/tables`;

export const RESERVATIONS_ENDPOINT = `${API_BASE_URL}/api/v1/reservations`;

export const CUSTOMER_PROFILES_ENDPOINT = `${API_BASE_URL}/api/v1/customers/profile`;
export const CUSTOMER_PROFILE_ENDPOINT = `${API_BASE_URL}/api/v1/customers/profile/me`;

export const EMPLOYEE_PROFILES_ENDPOINT = `${API_BASE_URL}/api/v1/employees/profile`;
export const EMPLOYEE_PROFILE_ENDPOINT = `${API_BASE_URL}/api/v1/employees/profile/me`;

export const DINE_IN_ENDPOINT = `${API_BASE_URL}/api/v1/dine-in`

export const KITCHEN_ENPOINT = `${API_BASE_URL}/api/v1/kitchen`
export const KITCHEN_ORDERS_ENPOINT = `${KITCHEN_ENPOINT}/orders`
export const KITCHEN_RESERVATIONS_ENDPOINT = `${KITCHEN_ENPOINT}/reservations`;