package com.phucx.phucxfandb.constant;

public class ApiEndpoint {
    private ApiEndpoint() {}

    /**
     * Public APIs accessible to everyone without authentication.
     */
    public static class Public{
        private Public() {}

        public static final String[] GET = {
                CATEGORIES_ENDPOINT,
                CATEGORY_BY_ID_ENDPOINT,
                PRODUCTS_ENDPOINT,
                PRODUCT_BY_ID_ENDPOINT,
                DISCOUNT_TYPES_ENDPOINT,
                DISCOUNT_TYPE_BY_ID_ENDPOINT,
                API_DOCS_ANY_ENDPOINT,
                SWAGGER_UI_ANY_ENDPOINT,
                WEBSOCKET_ENDPOINT,
                TABLES_ENDPOINT,
                TABLE_BY_ID_ENDPOINT,
                TABLES_AVAILABILITY_ENDPOINT,
                TOPICS_ENDPOINT,
                TOPIC_BY_ID_ENDPOINT,
                PAYMENT_METHODS_ENDPOINT,
                PAYMENT_METHOD_BY_ID_ENDPOINT,
                IMAGE_BY_NAME_ENDPOINT,
                RATING_BY_PRODUCT_ID_ENDPOINT,
                DISCOUNT_BY_ID_ENDPOINT,
                TABLE_OCCUPANCIES_ENDPOINT,
                TABLE_OCCUPANCY_BY_ID_ENDPOINT,
        };

        public static final String[] POST = {
                VALIDATE_AUTH_ENDPOINT,
                REGISTER_AUTH_ENDPOINT,
                FORGOT_AUTH_ENDPOINT,
                LOGIN_AUTH_ENDPOINT,
                RESET_PASSWORD_AUTH_ENDPOINT,
        };
    }
    /**
     * APIs requiring any authenticated user (no specific role).
     */
    public static class Authenticated{
        private Authenticated(){}

        public static final String[] ALL = {
                USER_ME_ENDPOINT,
                USER_PASSWORD_ENDPOINT,
                PROFILE_ME_ENDPOINT,
                NOTIFICATIONS_ANY_ENDPOINT,
                IMAGES_ENDPOINT,
                LOGOUT_AUTH_ENDPOINT
        };

        public static final String[] GET = {
                ORDER_BY_ID_ENDPOINT,
                ORDERS_ENDPOINT,
                RESERVATIONS_ENDPOINT,
                RESERVATION_BY_ID_ENDPOINT,
        };

        public static final String[] POST = {
                ORDERS_ENDPOINT,
                RESERVATIONS_ENDPOINT
        };

        public static final String[] PATCH = {
                PAYMENT_BY_ID_ENDPOINT
        };
    }
    /**
     * APIs restricted to users with the ADMIN role.
     */
    public static class Admin{
        private Admin(){}

        public static final String[] ALL = {
                ROLES_ENDPOINT,
                TABLE_BULK_ENDPOINT,
                PRODUCTS_BULK_ENDPOINT,
                TOPICS_BULK_ENDPOINT,
                DISCOUNTS_BY_PRODUCT_ENDPOINT
        };

        public static final String[] GET = {
                USERS_ENDPOINT,
                USER_BY_ID_ENDPOINT,
                PROFILE_BY_ID_ENDPOINT,
        };

        public static final String[] POST = {
                PAYMENT_METHODS_ENDPOINT,
                PAYMENT_METHODS_BULK_ENDPOINT,
                TABLE_BY_ID_ENDPOINT,
                CATEGORIES_ENDPOINT,
                CATEGORIES_BULK_ENDPOINT,
                PRODUCTS_ENDPOINT,
                DISCOUNT_TYPES_ENDPOINT,
                DISCOUNT_TYPES_BULK_ENDPOINT,
                TOPICS_ENDPOINT,
                DISCOUNTS_ENDPOINT,
                USERS_ENDPOINT
        };
        public static final String[] PATCH = {
                PAYMENT_METHOD_BY_ID_ENDPOINT,
                CATEGORY_BY_ID_ENDPOINT,
                PRODUCT_BY_ID_ENDPOINT,
                DISCOUNT_TYPE_BY_ID_ENDPOINT,
                TOPIC_BY_ID_ENDPOINT,
                USER_BY_ID_ENDPOINT
        };
        public static final String[] PUT = {
                TABLE_BY_ID_ENDPOINT,
                CATEGORY_BY_ID_ENDPOINT,
                PRODUCT_BY_ID_ENDPOINT,
                DISCOUNT_BY_ID_ENDPOINT,
        };
    }

    /**
     * APIs restricted to users with the CUSTOMER role.
     */
    public static class Customer{
        private Customer(){}

        public static final String[] ALL = {
                CART_ME_ANY_ENDPOINT,
                CUSTOMER_ME_ENDPOINT,
                ADDRESSES_ANY_ENDPOINT,
                PAYPAL_ANY_ENDPOINT,
                USER_RATING_BY_PRODUCT_ID_ENDPOINT,
                RATINGS_ENDPOINT,
                RATING_ID_ENDPOINT
        };

        public static final String[] PATCH = {

        };
    }

    /**
     * APIs restricted to users with the EMPLOYEE role.
     */
    public static class Employee{
        private Employee(){}

        public static final String[] ALL = {
                EMPLOYEE_ME_ENDPOINT,
                PAYMENTS_ENDPOINT,
        };

        public static final String[] GET = {
                PAYMENT_BY_ID_ENDPOINT
        };

        public static final String[] PATCH ={
                ORDER_BY_ID_ENDPOINT,
                ORDER_BY_ID_ITEM_BY_ID_ENDPOINT,
                TABLE_BY_ID_ENDPOINT,
                TABLE_OCCUPANCY_BY_ID_ENDPOINT,
                RESERVATION_BY_ID_ENDPOINT,
                RESERVATION_BY_ID_ITEM_BY_ID_ENDPOINT
        };

        public static final String[] POST = {
                TABLE_OCCUPANCIES_ENDPOINT,
                ORDER_BY_ID_ITEMS_ENDPOINT,
                RESERVATION_BY_ID_ITEMS_ENDPOINT
        };

        public static final String[] PUT = {
                TABLE_OCCUPANCY_BY_ID_ENDPOINT,
                ORDER_BY_ID_ITEM_BY_ID_ENDPOINT,
                ORDER_BY_ID_ENDPOINT,
                RESERVATION_BY_ID_ITEM_BY_ID_ENDPOINT,
                RESERVATION_BY_ID_ENDPOINT,
        };

        public static final String[] DELETE = {
                ORDER_BY_ID_ITEM_BY_ID_ENDPOINT,
                RESERVATION_BY_ID_ITEM_BY_ID_ENDPOINT
        };
    }

    public static final String PAYPAL_ANY_ENDPOINT = "/api/v1/paypal/**";

    public static final String WEBSOCKET_ENDPOINT = "/chat";

    public static final String API_DOCS_ANY_ENDPOINT =  "/api-docs/**";
    public static final String SWAGGER_UI_ANY_ENDPOINT = "/swagger-ui/**";

    public static final String ROLES_ENDPOINT = "/api/v1/roles";

    public static final String ORDER_BY_ID_ENDPOINT = "/api/v1/orders/{id}";
    public static final String ORDERS_ENDPOINT = "/api/v1/orders";
    public static final String ORDER_BY_ID_ITEMS_ENDPOINT = "/api/v1/orders/{id}/items";
    public static final String ORDER_BY_ID_ITEM_BY_ID_ENDPOINT = "/api/v1/orders/{orderId}/items/{orderItemId}";

    public static final String RESERVATION_BY_ID_ENDPOINT = "/api/v1/reservations/{id}";
    public static final String RESERVATIONS_ENDPOINT = "/api/v1/reservations";
    public static final String RESERVATION_BY_ID_ITEMS_ENDPOINT = "/api/v1/reservations/{id}/items";
    public static final String RESERVATION_BY_ID_ITEM_BY_ID_ENDPOINT = "/api/v1/reservations/{reservationId}/items/{itemId}";

    public static final String TOPICS_ENDPOINT = "/api/v1/topics";
    public static final String TOPIC_BY_ID_ENDPOINT = "/api/v1/topics/{id}";
    public static final String TOPICS_BULK_ENDPOINT = "/api/v1/topics/bulk";

    public static final String NOTIFICATIONS_ANY_ENDPOINT = "/api/v1/notifications/**";

    public static final String ADDRESSES_ANY_ENDPOINT = "/api/v1/addresses/**";

    public static final String PRODUCTS_ENDPOINT = "/api/v1/products";
    public static final String PRODUCT_BY_ID_ENDPOINT = "/api/v1/products/{id}";
    public static final String PRODUCTS_BULK_ENDPOINT = "/api/v1/products/bulk";

    public static final String CATEGORIES_ENDPOINT = "/api/v1/categories";
    public static final String CATEGORY_BY_ID_ENDPOINT = "/api/v1/categories/{id}";
    public static final String CATEGORIES_BULK_ENDPOINT = "/api/v1/categories/bulk";

    public static final String USERS_ENDPOINT = "/api/v1/users";
    public static final String USER_BY_ID_ENDPOINT = "/api/v1/users/{id}";
    public static final String USER_ME_ENDPOINT = "/api/v1/users/me";
    public static final String USER_PASSWORD_ENDPOINT = "/api/v1/users/password";

    public static final String CART_ME_ANY_ENDPOINT = "/api/v1/carts/me/**";

    public static final String CUSTOMER_ME_ENDPOINT = "/api/v1/customers/me";

    public static final String EMPLOYEE_ME_ENDPOINT = "/api/v1/employees/me";

    public static final String PROFILE_BY_ID_ENDPOINT = "/api/v1/profiles/{id}";
    public static final String PROFILE_ME_ENDPOINT = "/api/v1/profiles/me";

    public static final String DISCOUNT_TYPES_ENDPOINT = "/api/v1/discount-types";
    public static final String DISCOUNT_TYPE_BY_ID_ENDPOINT = "/api/v1/discount-types/{id}";
    public static final String DISCOUNT_TYPES_BULK_ENDPOINT = "/api/v1/discount-types/bulk";

    public static final String DISCOUNTS_ENDPOINT = "/api/v1/discounts";
    public static final String DISCOUNT_BY_ID_ENDPOINT = "/api/v1/discounts/{id}";
    public static final String DISCOUNTS_BY_PRODUCT_ENDPOINT = "/api/v1/discounts/products/{id}";

    public static final String RATING_BY_PRODUCT_ID_ENDPOINT = "/api/v1/ratings/products/{productId}";
    public static final String USER_RATING_BY_PRODUCT_ID_ENDPOINT = "/api/v1/ratings/products/{productId}/me";
    public static final String RATINGS_ENDPOINT = "/api/v1/ratings";
    public static final String RATING_ID_ENDPOINT = "/api/v1/ratings/{ratingId}";

    public static final String IMAGES_ENDPOINT = "/api/v1/images";
    public static final String IMAGE_BY_NAME_ENDPOINT = "/api/v1/images/{imageName}";

    public static final String RESET_PASSWORD_AUTH_ENDPOINT = "/api/v1/auth/reset";
    public static final String LOGIN_AUTH_ENDPOINT = "/api/v1/auth/login";
    public static final String FORGOT_AUTH_ENDPOINT = "/api/v1/auth/forgot";
    public static final String REGISTER_AUTH_ENDPOINT = "/api/v1/auth/register";
    public static final String VALIDATE_AUTH_ENDPOINT = "/api/v1/auth/validate";
    public static final String LOGOUT_AUTH_ENDPOINT = "/api/v1/auth/logout";

    public static final String TABLES_ENDPOINT = "/api/v1/tables";
    public static final String TABLES_AVAILABILITY_ENDPOINT = "/api/v1/tables/availability";
    public static final String TABLE_BY_ID_ENDPOINT = "/api/v1/tables/{id}";
    public static final String TABLE_BULK_ENDPOINT = "/api/v1/tables/bulk";

    public static final String TABLE_OCCUPANCIES_ENDPOINT = "/api/v1/table-occupancies";
    public static final String TABLE_OCCUPANCY_BY_ID_ENDPOINT = "/api/v1/table-occupancies/{id}";

    public static final String PAYMENT_METHODS_ENDPOINT = "/api/v1/payment-methods";
    public static final String PAYMENT_METHOD_BY_ID_ENDPOINT = "/api/v1/payment-methods/{id}";
    public static final String PAYMENT_METHODS_BULK_ENDPOINT = "/api/v1/payment-methods/bulk";

    public static final String PAYMENTS_ENDPOINT = "/api/v1/payments";
    public static final String PAYMENT_BY_ID_ENDPOINT = "/api/v1/payments/{id}";
}
