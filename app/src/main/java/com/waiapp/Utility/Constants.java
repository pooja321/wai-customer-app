package com.waiapp.Utility;

/**
 * Created by keviv on 16/06/2016.
 */
public class Constants {
    //firebase nodes
    public static final String FIREBASE_CHILD_ONLINE_RESOURCE = "Online-Resource";
    public static final String FIREBASE_CHILD_ONLINE_COOK_GEO = "Online-Cook-Geo";
    public static final String FIREBASE_CHILD_ONLINE_WASH_GEO = "Online-Wash-Geo";
    public static final String FIREBASE_CHILD_ONLINE_CLEAN_GEO = "Online-Clean-Geo";
    public static final String FIREBASE_CHILD_RESOURCES = "Resources";
    public static final String FIREBASE_CHILD_USERS = "Users";
    public static final String FIREBASE_CHILD_COOKING = "Cooking";
    public static final String FIREBASE_CHILD_WASHING = "Washing";
    public static final String FIREBASE_CHILD_CLEANING = "Cleaning";
    public static final String FIREBASE_CHILD_USER_ORDERS = "User-Orders";
    public static final String FIREBASE_CHILD_RESOURCE_ORDERS = "Resource-Orders";
    public static final String FIREBASE_CHILD_ORDER = "Order";
    public static final String FIREBASE_CHILD_ORDER_AMOUNT = "OrderAmount";
    public static final String FIREBASE_CHILD_ADDRESS = "Address";
    public static final String FIREBASE_CHILD_RESOURCE_ORDER_DETAIL = "Resource-Order-Detail";
    public static final String FIREBASE_CHILD_USER_ORDER_DETAIL = "User-Order-Detail";
    public static final String FIREBASE_CHILD_ORDER_DETAILS = "Order-Details";

    //firebase properties
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    public static final String FIREBASE_PROPERTY_ORDERTYPE= "orderType";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";
    public static final String FIREBASE_PROPERTY_CUSTOMER_ID = "customerId";

    public static final String ORDER_PROGRESS_STATUS_STARTED = "Started";
    public static final String ORDER_PROGRESS_STATUS_REACHED = "Reached";
    public static final String ORDER_PROGRESS_STATUS_COMPLETED = "Completed";
    public static final String ORDER_PROGRESS_STATUS_PAYMENT_PENDING = "Payment Pending";
    public static final String ORDER_PROGRESS_STATUS_RESOURCE_CONFIRMATION_AWAITED = "Resource Confirmation Awaited";
    public static final String ORDER_PROGRESS_STATUS_RESOURCE_CONFIRMED_ORDER = "Resource Confirmed Order";

    public static final String ORDER_STATUS_ORDERED = "Ordered";
    public static final String ORDER_STATUS_INPROGRESS = "Inprogress";
    public static final String ORDER_STATUS_INCOMPLETE = "Incomplete";
    public static final String ORDER_STATUS_CANCELLED = "Cancelled";
    public static final String ORDER_STATUS_COMPLETED = "Completed";

    public static final String ORDER_TYPE_COOKING = "Cooking";
    public static final String ORDER_TYPE_WASHING = "Washing";
    public static final String ORDER_TYPE_CLEANING = "Cleaning";

    public static final String RESOURCE_STATUS_LOGGED_IN = "LoggedIn";
    public static final String RESOURCE_STATUS_ONLINE = "Online";
    public static final String RESOURCE_STATUS_OFFLINE = "Offline";
    public static final String RESOURCE_STATUS_WORKING = "Working";

    public static final String SORTBY_RATING = "rating";
    public static final String SORTBY_DISTANCE = "distance";
    public static final String FILTERBY_GENDER_MALE = "Male";
    public static final String FILTERBY_GENDER_FEMALE = "Female";
    public static final String ORDER_ID_PREFIX = "O";
    public static final String CUSTOMER_ID_PREFIX = "C";
}
