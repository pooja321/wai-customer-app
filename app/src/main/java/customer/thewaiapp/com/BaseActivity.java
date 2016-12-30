package customer.thewaiapp.com;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import customer.thewaiapp.com.Login.LoginActivity;
import customer.thewaiapp.com.Model.User;
import customer.thewaiapp.com.Order.OrderHistoryActivity;
import customer.thewaiapp.com.Profile.ProfileActivity;
import io.realm.Realm;
import io.realm.RealmResults;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Realm mRealm;

    User user;

    @Override
    public void setContentView(int layoutResID) {

        mRealm = Realm.getDefaultInstance();
        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) drawer.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawer);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View Nav_View = navigationView.getHeaderView(0);
        TextView nav_Username = (TextView) Nav_View.findViewById(R.id.navheader_userName);
        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.v("wai", "<<<<<<<<<<<<<UserEmail from Base Activity>>>>>>>>" + UserEmail);
        RealmResults<User> UserResults = mRealm.where(User.class).equalTo("Email", UserEmail).findAll();
        Log.v("wai", "<<<<<<<<<<<<<UserResults from Base Activity>>>>>>>>>" + UserResults);
        if (UserResults.size() > 0) {
            user = UserResults.get(0);
            if (user != null) {
                String firstname = user.getFirstName();
                String lastname = user.getLastName();
                nav_Username.setText(String.format("%s %s", firstname, lastname));
            }
        }

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
}