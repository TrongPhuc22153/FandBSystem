package com.phucx.phucxfandb.constant;

public class ApiEndpoint {
    private ApiEndpoint() {}

    /**
     * Public APIs accessible to everyone without authentication.
     */
    public static class Public{
        private Public() {}

        public static final String[] GET = {
                "/api/v1/categories",
                "/api/v1/categories/{id}",
                "/api/v1/products/**",
                "/api/v1/discount-types/**",
                "/api/v1/auth/**",
                "/api-docs/**",
                "/swagger-ui/**",
                "/api/v1/tables",
                "/api/v1/tables/table",
                "/chat",
                PAYMENT_METHODS_ENDPOINT,
                PAYMENT_METHOD_BY_ID_ENDPOINT,
                IMAGE_BY_NAME_ENDPOINT,
                RATING_BY_PRODUCT_ID_ENDPOINT
        };
    }

    public static class Auth {
        private Auth(){}

        public static final String[] ALL = {
                "/api/v1/auth/register",
                "/api/v1/auth/forgot",
                "/api/v1/auth/validate",
                LOGIN_AUTH_ENDPOINT,
                RESET_PASSWORD_AUTH_ENDPOINT,
                IMAGES_ENDPOINT
        };
    }

    /**
     * APIs requiring any authenticated user (no specific role).
     */
    public static class Authenticated{
        private Authenticated(){}

        public static final String[] ALL = {
                "/api/v1/topics/**",
                "/api/v1/users/me",
                "/api/v1/profiles/me",
                "/api/v1/address/**",
                "/api/v1/auth/logout",
                "/api/v1/auth/password",
                "/api/v1/notifications/**",
                "/api/v1/orders",
                "/api/v1/reservations",
                ORDER_BY_ID_ENDPOINT,
                RESERVATION_BY_ID_ENDPOINT
        };
    }
    /**
     * APIs restricted to users with the ADMIN role.
     */
    public static class Admin{
        private Admin(){}

        public static final String[] ALL = {
                "/api/v1/categories/**",
                "/api/v1/products/**",
                "/api/v1/discounts/**",
                "/api/v1/tables/**",
                "/api/v1/topics/**",
                "/api/v1/discount-types/**",
                "/api/v1/dine-in/**",
                "/api/v1/employee/reservations/**",
                "/api/v1/kitchen/**",
                "/api/v1/employees/order/**",
                "/api/v1/users",
                "/api/v1/users/{userId}",
                "/api/v1/roles",
                "/api/v1/profiles/{userId}",
        };

        public static final String[] POST = {
                PAYMENT_METHODS_ENDPOINT,
                PAYMENT_METHODS_BULK_ENDPOINT
        };
        public static final String[] PATCH = {
                PAYMENT_METHOD_BY_ID_ENDPOINT,
        };
    }

    /**
     * APIs restricted to users with the CUSTOMER role.
     */
    public static class Customer{
        private Customer(){}

        public static final String[] ALL = {
                "/api/v1/carts/me/**",
                "/api/v1/customers/profile/me/**",
                "/api/v1/customer/reservations/**",
                "/api/v1/customers/order/**",
                "/api/v1/addresses/**",
                "/api/v1/paypal/**",
                USER_RATING_BY_PRODUCT_ID_ENDPOINT,
                RATINGS_ENDPOINT,
                RATING_ID_ENDPOINT
        };
    }

    /**
     * APIs restricted to users with the EMPLOYEE role.
     */
    public static class Employee{
        private Employee(){}

        public static final String[] ALL = {
                "/api/v1/employees/profile/me/**",
                "/api/v1/employee/reservations/**",
                "/api/v1/orders/employees/**"
        };

        public static final String[] PATCH ={
                ORDER_BY_ID_ENDPOINT,
                RESERVATION_BY_ID_ENDPOINT
        };
    }

    public static final String ORDER_BY_ID_ENDPOINT = "/api/v1/orders/{orderId}";

    public static final String RESERVATION_BY_ID_ENDPOINT = "/api/v1/reservations/{reservationId}";

    public static final String RATING_BY_PRODUCT_ID_ENDPOINT = "/api/v1/ratings/products/{productId}";
    public static final String USER_RATING_BY_PRODUCT_ID_ENDPOINT = "/api/v1/ratings/products/{productId}/me";
    public static final String RATINGS_ENDPOINT = "/api/v1/ratings";
    public static final String RATING_ID_ENDPOINT = "/api/v1/ratings/{ratingId}";

    public static final String IMAGES_ENDPOINT = "/api/v1/images";
    public static final String IMAGE_BY_NAME_ENDPOINT = "/api/v1/images/{imageName}";

    public static final String RESET_PASSWORD_AUTH_ENDPOINT = "/api/v1/auth/reset";
    public static final String LOGIN_AUTH_ENDPOINT = "/api/v1/auth/login";

    public static final String PAYMENT_METHODS_ENDPOINT = "/api/v1/payment-methods";
    public static final String PAYMENT_METHOD_BY_ID_ENDPOINT = "/api/v1/payment-methods/{id}";
    public static final String PAYMENT_METHODS_BULK_ENDPOINT = "/api/v1/payment-methods/bulk";

}
