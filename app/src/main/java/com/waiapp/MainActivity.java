package com.waiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.waiapp.Booking.ListViewFragment;
import com.waiapp.Booking.MapViewFragment;
import com.waiapp.Booking.clean.CleaningFragment;
import com.waiapp.Booking.cook.CookingFragment;
import com.waiapp.Booking.wash.WashingFragment;
import com.waiapp.confirmation.BookingConfirmationActivity;

public class MainActivity extends BaseActivity implements
        MapViewFragment.onAddressSearchClick, ListViewFragment.OnResourceSelectedInterface{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private BottomBar mBottomBar;
    private Toolbar mtoolbar;
    int _membersCount, _mainCourseCount;
    int membersAmount,mainCourseAmount;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("wai","MainActivity oncreate");
        setContentView(R.layout.activity_main);

        mtoolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
        Log.v("wai","show progress dialog box");

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
                    Log.v("wai","new CookingFragment");
                    fragment = new CookingFragment();
                }else if (menuItemId == R.id.bottomBarItemWashing){
                    Log.v("wai","new washingFragment");
                    fragment = new WashingFragment();
                }else if(menuItemId == R.id.bottomBarItemCleaning){
                    Log.v("wai","new cleaningFragment");
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
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.colorAccent));

    }


    public void startAddressSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchAddressActivity.class);
        startActivity(intent);
    }

//    public void onListResourceSelected(String key, ResourceOnline resource, String callingFragment) {
    public void onListResourceSelected(String key, String Name, String callingFragment) {
//        ArrayList<ResourceOnline> resourceList = new ArrayList<>();
//        resourceList.add(resource);
        Intent intent = new Intent(MainActivity.this, BookingConfirmationActivity.class);
        intent.putExtra("resourceKey",key);
        intent.putExtra("resourceName",Name);
        intent.putExtra("fragment_name",callingFragment);
        startActivity(intent);
    }

    @Override
    public void onResourceListdownloadcomplete(Boolean iscomplete) {
        if(iscomplete){
            if(mProgressDialog.isShowing()){
                Log.v("wai","dismiss progress dialog box");
                mProgressDialog.dismiss();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
}
