package com.waiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.waiapp.Booking.ListViewFragment;
import com.waiapp.Booking.MapViewFragment;
import com.waiapp.Booking.clean.CleaningFragment;
import com.waiapp.Booking.cook.CookingFragment;
import com.waiapp.Booking.wash.WashingFragment;
import com.waiapp.Model.Resource;
import com.waiapp.confirmation.BookingConfirmationActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements
        MapViewFragment.onAddressSearchClick, ListViewFragment.OnResourceSelectedInterface{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private BottomBar mBottomBar;
    private Toolbar mtoolbar;
    int _membersCount, _mainCourseCount;
    int membersAmount,mainCourseAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtoolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);

        _membersCount = 2;
        _mainCourseCount = 2;
        membersAmount = 50;
        mainCourseAmount = 50;

//        mBottomBar = BottomBar.attach(findViewById(R.id.main_placeholder), savedInstanceState);
        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.main_coordinatorLayout),findViewById(R.id.main_placeholder), savedInstanceState);
        mBottomBar.noTopOffset();
        mBottomBar.setMaxFixedTabs(2);
        mBottomBar.noNavBarGoodness();
        mBottomBar.useFixedMode();
        mBottomBar.setItems(R.menu.mainactivity_bottombar);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {

            Fragment fragment;
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemCook){
                    fragment = new CookingFragment();
                }else if (menuItemId == R.id.bottomBarItemWashing){
                    fragment = new WashingFragment();
                }else if(menuItemId == R.id.bottomBarItemCleaning){
                    fragment = new CleaningFragment();
                }
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_placeholder, fragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");

    }


    public void startAddressSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchAddressActivity.class);
        startActivity(intent);
    }

    public void onListResourceSelected(String key, Resource resource, String callingFragment) {
        ArrayList<Resource> resourceList = new ArrayList<>();
        resourceList.add(resource);
        Intent intent = new Intent(MainActivity.this, BookingConfirmationActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("resource",resource);
        intent.putExtra("fragment_name",callingFragment);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
}
