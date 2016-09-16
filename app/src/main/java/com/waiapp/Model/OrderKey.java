package com.waiapp.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by keviv on 15/09/2016.
 */
public class OrderKey extends RealmObject {
    @PrimaryKey
    private String id;
    private String Orderkey;

    public OrderKey(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
