package com.phucx.phucxfoodshop.constant;

public class MessageQueueConstant {
    // account exchange
    public final static String ACCOUNT_EXCHANGE = "accountservice";
    // shop exchange
    public final static String SHOP_EXCHANGE = "shopservice";
    // product routing key
    public final static String PRODUCT_ROUTING_KEY = "productqueue";
    // discount routing key
    public final static String DISCOUNT_ROUTING_KEY = "discountqueue";
    //  customer routing key
    public final static String CUSTOMER_ROUTING_KEY = "customerqueue";
    //  employee routing key
    public final static String EMPLOYEE_ROUTING_KEY = "employeequeue";
    //  shipper routing key
    public final static String SHIPPER_ROUTING_KEY = "shipperqueue";

    // payment exchange
    public final static String PAYMENT_EXCHANGE = "paymentservice";
    public final static String PAYMENT_ROUTING_KEY = "paymentqueue";
    // notification exchange
    public final static String NOTIFICATION_EXCHANGE = "notificationservice";
    public final static String NOTIFICATION_CUSTOMER_ORDER_ROUTING_KEY = "notificationcustomerorderqueue";
    public final static String NOTIFICATION_EMPLOYEE_ORDER_ROUTING_KEY = "notificationemployeeorderqueue";
}
