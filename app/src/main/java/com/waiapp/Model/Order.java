package com.waiapp.Model;

/**
 * Created by keviv on 21/07/2016.
 */
public class Order  {
    private String orderId;
    private String orderType;
    private String customerId;
    private String resourceId;
    private String customerAddressId;
    private String orderStatus;
    private String orderProgressStatus;
    private String paymentMode;
    private String OrderAmount;
    private String orderCreationTime;
    private String orderbookingTime;
    private String serviceStartTime;
    private String serviceEndTime;
    private String feedbackProvided;

    public Order(){

    }

    public Order(String orderId, String orderType, String customerId, String resourceId, String customerAddressId, String orderStatus,
                 String orderProgressStatus, String paymentMode,String orderAmount, String orderCreationTime, String orderbookingTime,
                 String serviceStartTime, String serviceEndTime, String feedbackProvided) {
        this.customerAddressId = customerAddressId;
        this.customerId = customerId;
        this.feedbackProvided = feedbackProvided;
        OrderAmount = orderAmount;
        this.orderbookingTime = orderbookingTime;
        this.orderCreationTime = orderCreationTime;
        this.orderId = orderId;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.paymentMode = paymentMode;
        this.resourceId = resourceId;
        this.serviceEndTime = serviceEndTime;
        this.serviceStartTime = serviceStartTime;
        this.orderProgressStatus = orderProgressStatus;
    }

    public String getOrderProgressStatus() {
        return orderProgressStatus;
    }

    public void setOrderProgressStatus(String orderProgressStatus) {
        this.orderProgressStatus = orderProgressStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCustomerAddressId() {
        return customerAddressId;
    }

    public void setCustomerAddressId(String customerAddressId) {
        this.customerAddressId = customerAddressId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFeedbackProvided() {
        return feedbackProvided;
    }

    public void setFeedbackProvided(String feedbackProvided) {
        this.feedbackProvided = feedbackProvided;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getOrderbookingTime() {
        return orderbookingTime;
    }

    public void setOrderbookingTime(String orderbookingTime) {
        this.orderbookingTime = orderbookingTime;
    }

    public String getOrderCreationTime() {
        return orderCreationTime;
    }

    public void setOrderCreationTime(String orderCreationTime) {
        this.orderCreationTime = orderCreationTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(String serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public String getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }
}
