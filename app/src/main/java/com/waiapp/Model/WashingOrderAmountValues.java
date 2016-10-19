package com.waiapp.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by keviv on 18/10/2016.
 */

public class WashingOrderAmountValues extends RealmObject {

    @PrimaryKey
    private String OrderId;
    private int baseAmount, bucketCount, bucketAmount;
    private double totalAmount, serviceTaxAmount;

    public WashingOrderAmountValues() {
    }

    public WashingOrderAmountValues(String orderId, int baseAmount, int bucketAmount, int bucketCount,  double serviceTaxAmount, double totalAmount) {
        this.baseAmount = baseAmount;
        this.bucketAmount = bucketAmount;
        this.bucketCount = bucketCount;
        OrderId = orderId;
        this.serviceTaxAmount = serviceTaxAmount;
        this.totalAmount = totalAmount;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    public double getServiceTaxAmount() {
        return serviceTaxAmount;
    }

    public void setServiceTaxAmount(double serviceTaxAmount) {
        this.serviceTaxAmount = serviceTaxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public int getBucketCount() {
        return bucketCount;
    }

    public void setBucketCount(int bucketCount) {
        this.bucketCount = bucketCount;
    }

    public int getBucketAmount() {
        return bucketAmount;
    }

    public void setBucketAmount(int bucketAmount) {
        this.bucketAmount = bucketAmount;
    }
}
