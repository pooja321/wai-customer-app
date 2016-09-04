package com.waiapp.Model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by keviv on 21/07/2016.
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;
    private String orderType;
    private String customerId;
    private String resourceId;
    private String customerAddressId;
    private String orderStatus;
    private String orderProgressStatus;
    private String paymentMode;
    private double OrderAmount;
    private HashMap<String, Object> orderCreationTime;
    private HashMap<String, Object> orderbookingTime;
    private HashMap<String, Object> serviceStartTime;
    private HashMap<String, Object> serviceEndTime;
    private Boolean feedbackProvided;

    public Order(){

    }

    public Order(String orderId, String orderType, String customerId, String resourceId, String customerAddressId, String orderStatus,
                 String orderProgressStatus, String paymentMode, double orderAmount, HashMap<String, Object> orderCreationTime, HashMap<String, Object> orderbookingTime,
                 HashMap<String, Object> serviceStartTime, HashMap<String, Object> serviceEndTime, Boolean feedbackProvided) {
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

    public Boolean getFeedbackProvided() {
        return feedbackProvided;
    }

    public void setFeedbackProvided(Boolean feedbackProvided) {
        this.feedbackProvided = feedbackProvided;
    }

    public double getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        OrderAmount = orderAmount;
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

    public HashMap<String, Object> getOrderbookingTime() {
        return orderbookingTime;
    }

    public void setOrderbookingTime(HashMap<String, Object> orderbookingTime) {
        this.orderbookingTime = orderbookingTime;
    }

    public HashMap<String, Object> getOrderCreationTime() {
        return orderCreationTime;
    }

    public void setOrderCreationTime(HashMap<String, Object> orderCreationTime) {
        this.orderCreationTime = orderCreationTime;
    }

    public HashMap<String, Object> getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(HashMap<String, Object> serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public HashMap<String, Object> getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(HashMap<String, Object> serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }
}
