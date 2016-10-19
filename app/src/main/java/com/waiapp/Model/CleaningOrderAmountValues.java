package com.waiapp.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by keviv on 19/10/2016.
 */

public class CleaningOrderAmountValues extends RealmObject {

    @PrimaryKey
    private String OrderId;
    private double totalAmount, serviceTaxAmount;
    private int baseAmount, roomsCount, washroomsCount, utensilbucketCount, roomsAmount, washroomsAmount, utensilbucketAmount;

    public CleaningOrderAmountValues() {
    }

    public CleaningOrderAmountValues(String orderId, int baseAmount, int roomsAmount, int roomsCount, double serviceTaxAmount, double totalAmount, int utensilbucketAmount, int utensilbucketCount, int washroomsAmount, int washroomsCount) {
        this.baseAmount = baseAmount;
        OrderId = orderId;
        this.roomsAmount = roomsAmount;
        this.roomsCount = roomsCount;
        this.serviceTaxAmount = serviceTaxAmount;
        this.totalAmount = totalAmount;
        this.utensilbucketAmount = utensilbucketAmount;
        this.utensilbucketCount = utensilbucketCount;
        this.washroomsAmount = washroomsAmount;
        this.washroomsCount = washroomsCount;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public int getRoomsAmount() {
        return roomsAmount;
    }

    public void setRoomsAmount(int roomsAmount) {
        this.roomsAmount = roomsAmount;
    }

    public int getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(int roomsCount) {
        this.roomsCount = roomsCount;
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

    public int getUtensilbucketAmount() {
        return utensilbucketAmount;
    }

    public void setUtensilbucketAmount(int utensilbucketAmount) {
        this.utensilbucketAmount = utensilbucketAmount;
    }

    public int getUtensilbucketCount() {
        return utensilbucketCount;
    }

    public void setUtensilbucketCount(int utensilbucketCount) {
        this.utensilbucketCount = utensilbucketCount;
    }

    public int getWashroomsAmount() {
        return washroomsAmount;
    }

    public void setWashroomsAmount(int washroomsAmount) {
        this.washroomsAmount = washroomsAmount;
    }

    public int getWashroomsCount() {
        return washroomsCount;
    }

    public void setWashroomsCount(int washroomsCount) {
        this.washroomsCount = washroomsCount;
    }
}
