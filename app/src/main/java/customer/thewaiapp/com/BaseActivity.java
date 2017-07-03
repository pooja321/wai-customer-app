package customer.thewaiapp.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import customer.thewaiapp.com.Login.LoginActivity;
import customer.thewaiapp.com.Model.User;
import customer.thewaiapp.com.Order.OrderHistoryActivity;
import customer.thewaiapp.com.Profile.ProfileActivity;
import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.Utility.NetworkChangeReceiver;
import customer.thewaiapp.com.Utility.Utilities;
import io.realm.Realm;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Realm mRealm;
    User user;
    String UID;
    private DatabaseReference mDatabase;
    NetworkChangeReceiver networkChangeReceiver;
    @Override
    public void setContentView(int layoutResID) {

        mRealm = Realm.getDefaultInstance();
        networkChangeReceiver = new NetworkChangeReceiver();
        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) drawer.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawer);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View Nav_View = navigationView.getHeaderView(0);
        final TextView nav_Username = (TextView) Nav_View.findViewById(R.id.navheader_userName);
        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(Constants.FIREBASE_CHILD_USERS).child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("Wai123", "Data: " + dataSnapshot);
                if (dataSnapshot.getValue() != null) {
                user = dataSnapshot.getValue(User.class);
                nav_Username.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(user);
                mRealm.commitTransaction();
                }
                else {
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                    HashMap<String, Object> timestampChanged = new HashMap<>();
                    timestampChanged.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                    String userId = Utilities.generateCustomerId();
                    SharedPreferences prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);
                    String UserEmail = prefs.getString("Email",null);
                    String FirstName = prefs.getString("FName",null);
                    String LastName = prefs.getString("LName",null);
                    String Gender = prefs.getString("Gender",null);
                    String Uid = prefs.getString("Uid",null);
                    final User user = new User(userId, UserEmail,FirstName,Gender, LastName,0, timestampChanged, timestampJoined);
                    mRealm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(user);
                        }
                    });
                    mDatabase.child(Constants.FIREBASE_CHILD_USERS).child(Uid).setValue(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if (useToolbar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }
        setUpNavView();
    }

    protected void setUpNavView() {

        navigationView.setNavigationItemSelectedListener(this);

        if (useDrawerToggle()) {
            actionBarDrawerToggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(actionBarDrawerToggle);
        } else if (useToolbar() && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    protected boolean useDrawerToggle() {
        return true;
    }

    protected boolean useToolbar() {
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_history) {
            startActivity(new Intent(this, OrderHistoryActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        else if (id == R.id.nav_terms) {
            startActivity(new Intent(this, TermsAndConditionsActivity.class));
        }
        else if (id == R.id.nav_newbooking) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_logout) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                Toast.makeText(BaseActivity.this, "Please Login First", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeReceiver, new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
}