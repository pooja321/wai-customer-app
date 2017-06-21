package customer.thewaiapp.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import customer.thewaiapp.com.Booking.ListViewFragment;
import customer.thewaiapp.com.Booking.MapViewFragment;
import customer.thewaiapp.com.Booking.clean.CleaningFragment;
import customer.thewaiapp.com.Booking.cook.CookingFragment;
import customer.thewaiapp.com.Booking.wash.WashingFragment;
import customer.thewaiapp.com.confirmation.BookingConfirmationActivity;

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
}
