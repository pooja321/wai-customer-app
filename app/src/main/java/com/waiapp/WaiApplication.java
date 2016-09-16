package com.waiapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by keviv on 18/06/2016.
 */
public class WaiApplication extends Application {

    private Boolean orderPending;
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public Boolean getOrderPending() {
        return orderPending;
    }

    public void setOrderPending(Boolean orderPending) {
        this.orderPending = orderPending;
    }
}
