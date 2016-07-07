package com.waiapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.waiapp.Login.LoginActivity;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {

        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base,null);
        FrameLayout activityContainer = (FrameLayout) drawer.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawer);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.inflateHeaderView(R.layout.nav_header_base);
//        LinearLayout navHeaderbase = (LinearLayout) findViewById(R.id.nav_header_base);
////        navHeaderbase.setOnClickListener(new View.OnClickListener(){
////            public void onClick(View v) {
////                Intent intent = new Intent(BaseActivity.this, ProfileAcitivity.class);
////                startActivity(intent);
////            }
////        });

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if (useToolbar())  {
            setSupportActionBar(toolbar);
        }
        else   {
            toolbar.setVisibility(View.GONE);
        }
        setUpNavView();
    }

    protected void setUpNavView() {

        navigationView.setNavigationItemSelectedListener(this);

        if( useDrawerToggle()) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        } else if (useToolbar() && getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }
    protected boolean useDrawerToggle()
    {
        return true;
    }

    protected boolean useToolbar()
    {
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_history) {

        } else if (id == R.id.nav_address) {

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_chat_with_us) {

        } else if (id == R.id.nav_invite_and_earn) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }else if (id == R.id.nav_logout) {
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
}
