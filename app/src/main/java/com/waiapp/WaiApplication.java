package com.waiapp;

import android.app.Application;

/**
 * Created by keviv on 18/06/2016.
 */
public class WaiApplication extends Application {

    private Boolean orderPending;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Boolean getOrderPending() {
        return orderPending;
    }

    public void setOrderPending(Boolean orderPending) {
        this.orderPending = orderPending;
    }
}
