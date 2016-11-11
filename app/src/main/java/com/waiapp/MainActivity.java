package com.waiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("wai","MainActivity oncreate");
        setContentView(R.layout.activity_main);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.main_bottombar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            Fragment fragment;
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(tabId == R.id.tab_bottomBarItemCook){
                    fragment = new CookingFragment();
                }else if (tabId == R.id.tab_bottomBarItemWashing){
                    fragment = new WashingFragment();
                }else if (tabId == R.id.tab_bottomBarItemCleaning){
                    fragment = new CleaningFragment();
                }
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_placeholder, fragment);
                fragmentTransaction.commit();
            }
        });
    }

    public void startAddressSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchAddressActivity.class);
        startActivity(intent);
    }

    public void onListResourceSelected(String key, String Name, String callingFragment) {
        Intent intent = new Intent(MainActivity.this, BookingConfirmationActivity.class);
        intent.putExtra("resourceKey",key);
        intent.putExtra("resourceName",Name);
        intent.putExtra("fragment_name",callingFragment);
        startActivity(intent);
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
//            startActivity(new Intent(this, SortFilterActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
