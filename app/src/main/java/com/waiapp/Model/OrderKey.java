package com.waiapp.Model;

import io.realm.RealmObject;

/**
 * Created by keviv on 15/09/2016.
 */
public class OrderKey extends RealmObject {
    private String Orderkey;

    public OrderKey(){

    }

    public OrderKey(String orderkey) {
        Orderkey = orderkey;
    }

    public String getOrderkey() {
        return Orderkey;
    }

    public void setOrderkey(String orderkey) {
        Orderkey = orderkey;
    }
}
