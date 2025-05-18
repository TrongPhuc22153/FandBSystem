package com.phucx.phucxfandb.constant;

public class ApiEndpoint {

    public static final String ORDER_BY_ID_ENDPOINT = "/api/v1/orders/{orderId}";

    public static final String RESERVATION_BY_ID_ENDPOINT = "/api/v1/reservations/{reservationId}";

    public static final String RATING_BY_PRODUCT_ID_ENDPOINT = "/api/v1/ratings/products/{productId}";
    public static final String USER_RATING_BY_PRODUCT_ID_ENDPOINT = "/api/v1/ratings/products/{productId}/me";
    public static final String RATINGS_ENDPOINT = "/api/v1/ratings";
    public static final String RATING_ID_ENDPOINT = "/api/v1/ratings/{ratingId}";

    public static final String IMAGES_ENDPOINT = "/api/v1/images";
    public static final String IMAGE_BY_NAME_ENDPOINT = "/api/v1/images/{imageName}";
    /**
     * Public APIs accessible to everyone without authentication.
     */
    public static final String[] PUBLIC_API = {
            "/api/v1/categories",
            "/api/v1/categories/{id}",
            "/api/v1/products/**",
            "/api/v1/discount-types/**",
            "/api/v1/payment-methods/**",
            "/api/v1/auth/**",
            "/api-docs/**",
            "/swagger-ui/**",
            "/api/v1/feedback",
            "/api/v1/tables",
            "/api/v1/tables/table",
            "/chat",
            IMAGE_BY_NAME_ENDPOINT,
            RATING_BY_PRODUCT_ID_ENDPOINT

    };

    public static final String[] AUTH_API = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            IMAGES_ENDPOINT
    };

    /**
     * APIs requiring any authenticated user (no specific role).
     */
    public static final String[] AUTHENTICATED_USER_API = {
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

    /**
     * APIs restricted to users with the ADMIN role.
     */
    public static final String[] ADMIN_API = {
            "/api/v1/categories/**",
            "/api/v1/payment-methods/**",
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

    public static final String[] WAITER_API = {
            "/api/v1/waiter/**",
    };
    public static final String[] RECEPTIONIST_API = {
            "/api/v1/receptionist/**"
    };
    public static final String[] CHEF_API = {
            "/api/v1/chef/**",
    };

    /**
     * APIs restricted to users with the CUSTOMER role.
     */
    public static final String[] CUSTOMER_API = {
            "/api/v1/carts/me/**",
            "/api/v1/customers/profile/me/**",
            "/api/v1/customer/reservations/**",
            "/api/v1/customers/order/**",
            "/api/v1/feedback/**",
            "/api/v1/addresses/**",
            USER_RATING_BY_PRODUCT_ID_ENDPOINT,
            RATINGS_ENDPOINT,
            RATING_ID_ENDPOINT
    };

    /**
     * APIs restricted to users with the EMPLOYEE role.
     */
    public static final String[] EMPLOYEE_API = {
            "/api/v1/employees/profile/me/**",
            "/api/v1/employee/reservations/**",
            "/api/v1/orders/employees/**"
    };

}
