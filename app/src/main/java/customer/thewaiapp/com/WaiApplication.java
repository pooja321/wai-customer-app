package customer.thewaiapp.com;

import android.app.Application;

import com.facebook.FacebookSdk;

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
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public Boolean getOrderPending() {
        return orderPending;
    }

    public void setOrderPending(Boolean orderPending) {
        this.orderPending = orderPending;
    }
}
