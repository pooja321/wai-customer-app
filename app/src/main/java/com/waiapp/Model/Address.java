package com.waiapp.Model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by keviv on 03/08/2016.
 */
public class Address extends RealmObject implements Serializable {

    private String addressName;
    private String addressType;
    private String addressId;
    private String houseNo;
    private String AreaName;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private static final long serialVersionUID = 1L;

    public Address()
    {


    }


    public Address(String addressName,String addressId, String addressType, String houseNo, String areaName, String landmark, String city, String state, String country, String pincode ) {
        this.addressName = addressName;
        this.addressType = addressType;
        this.addressId = addressId;
        AreaName = areaName;
        this.city = city;
        this.country = country;
        this.houseNo = houseNo;
        this.landmark = landmark;
        this.pincode = pincode;
        this.state = state;


    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

}
