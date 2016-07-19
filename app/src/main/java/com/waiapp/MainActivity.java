package com.waiapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.waiapp.Booking.ListViewFragment;
import com.waiapp.Booking.MapViewFragment;
import com.waiapp.Booking.clean.CleaningFragment;
import com.waiapp.Booking.cook.CookingFragment;
import com.waiapp.Booking.wash.WashingFragment;

public class MainActivity extends BaseActivity implements CookingFragment.OnFragmentInteractionListener,
        CleaningFragment.OnFragmentInteractionListener,WashingFragment.OnFragmentInteractionListener,
        MapViewFragment.onAddressSearchClick, ListViewFragment.OnResourceSelectedInterface{

    public static final String MAP_VIEW_FRAGMENT = "map_view_fragment";
    public static final String LIST_VIEW_FRAGMENT = "list_view_fragment";
    public static final String COOK_FRAGMENT = "cook_fragment";
    public static final String WASH_FRAGMENT = "wash_fragment";
    public static final String CLEAN_FRAGMENT = "clean_fragment";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Switch mMapListSwitch;
    ImageButton mSortFilterImageButton;
    private PopupWindow mPopConfirmationWindow;
    private BottomBar mBottomBar;
    int _membersCount, _mainCourseCount;
    int membersAmount,mainCourseAmount;
    double totalAmount;
    int baseAmount = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _membersCount = 2;
        _mainCourseCount = 2;
        membersAmount = 50;
        mainCourseAmount = 50;

        mBottomBar = BottomBar.attach(findViewById(R.id.main_placeholder), savedInstanceState);
        mBottomBar.noTopOffset();
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
//                if (menuItemId == R.id.bottomBarItemCook){
//                    loadCookFragment();
//                }else if (menuItemId == R.id.bottomBarItemWashing){
//                    loadWashingFragment();
//                }else if(menuItemId == R.id.bottomBarItemCleaning){
//                    loadCleaningFragment();
//                }
            }
        });

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");

//        mSortFilterImageButton = (ImageButton) findViewById(R.id.main_ib_sort_filter);
//        MapViewFragment savedMapFragment = (MapViewFragment) getSupportFragmentManager().findFragmentByTag(MAP_VIEW_FRAGMENT);
//        if (savedMapFragment == null) {
//            Log.v("MainActivity","mapViewFragment1");
//            MapViewFragment mapViewFragment = new MapViewFragment();
//            fragmentManager = getSupportFragmentManager();
//            fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.add(R.id.main_placeholder, mapViewFragment, MAP_VIEW_FRAGMENT);
//            fragmentTransaction.commit();
//        }
//
//        mMapListSwitch = (Switch) findViewById(R.id.main_switch_map_list);
//        if (mMapListSwitch != null) {
//            mMapListSwitch.setChecked(true);
//        }
//        mMapListSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    mMapListSwitch.setText("Map");
//                    LoadMapViewFragment();
//                }else{
//                    mMapListSwitch.setText("List");
//                    loadListViewFragment();
//                }
//            }
//        });
//        mSortFilterImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getSupportFragmentManager();
//                SortFilterDialogFragment sortFilterDialogFragment = SortFilterDialogFragment.newInstance("");
//                sortFilterDialogFragment.show(fm, "fragment_sort_filter");
//
//            }
//        });
    }

    private void LoadMapViewFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        MapViewFragment savedMapFragment = (MapViewFragment) getSupportFragmentManager().findFragmentByTag(MAP_VIEW_FRAGMENT);
        if(savedMapFragment == null) {
            MapViewFragment mapViewFragment = new MapViewFragment();
            fragmentTransaction.replace(R.id.main_placeholder, mapViewFragment, MAP_VIEW_FRAGMENT);
            fragmentTransaction.commit();
        }else{
            fragmentTransaction.replace(R.id.main_placeholder,savedMapFragment, MAP_VIEW_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

//    private void loadListViewFragment() {
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        ListViewFragment savedListFragment = (ListViewFragment) getSupportFragmentManager().findFragmentByTag(LIST_VIEW_FRAGMENT);
//        if (savedListFragment == null){
//            ListViewFragment listViewFragment = new ListViewFragment();
//            fragmentTransaction.replace(R.id.main_placeholder,listViewFragment, LIST_VIEW_FRAGMENT);
//            fragmentTransaction.commit();
//        }else {
//            fragmentTransaction.replace(R.id.main_placeholder,savedListFragment, LIST_VIEW_FRAGMENT);
//            fragmentTransaction.commit();
//        }
//    }


    public void startAddressSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchAddressActivity.class);
        startActivity(intent);
    }

    public void onListResourceSelected(int index) {
//        Toast.makeText(MainActivity.this, "You selected ".concat(String.valueOf(index)), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(MainActivity.this, BookingConfirmationActivity.class);
//        startActivity(intent);
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content));
        startCommentsPopUpView(viewGroup);
    }

    public void startCommentsPopUpView(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.popup_booking_confirmation_layout, null,false);

        // set height depends on the device size
//        mPopConfirmationWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 400, true );
        mPopConfirmationWindow = new PopupWindow(inflatedView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT );
        // set a background drawable with rounders corners
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPopConfirmationWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.fb_popup_bg, getTheme()));
        }
        // make it focusable to show the keyboard to enter in `EditText`
        mPopConfirmationWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        mPopConfirmationWindow.setOutsideTouchable(true);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        mPopConfirmationWindow.showAtLocation(view, Gravity.BOTTOM, 0,100);
        ImageButton _PopWindowDismissBT = (ImageButton) inflatedView.findViewById(R.id.booking_ib_pop_dismiss);
        final TextView _TextViewMembersCount = (TextView) inflatedView.findViewById(R.id.booking_tv_members_count);
        final TextView _TextViewMainCourseCount = (TextView) inflatedView.findViewById(R.id.booking_tv_maincourse_count);
        final TextView _TextViewMembersAmount = (TextView) inflatedView.findViewById(R.id.booking_tv_members_price);
        final TextView _TextViewMainCourseAmount = (TextView) inflatedView.findViewById(R.id.booking_tv_maincourse_price);
        final TextView _TextViewBaseAmount = (TextView) inflatedView.findViewById(R.id.booking_tv_base_price);
        final TextView _TextViewServiceTaxAmount = (TextView) inflatedView.findViewById(R.id.booking_tv_service_tax_amount);
        final TextView _TextViewTotalAmount = (TextView) inflatedView.findViewById(R.id.booking_tv_total_amount);
        Button _ButtonIncrementMembers = (Button) inflatedView.findViewById(R.id.booking_bt_members_count_increment);
        Button _ButtonDecrementMembers = (Button) inflatedView.findViewById(R.id.booking_bt_members_count_decrement);
        Button _ButtonIncrementMainCourse = (Button) inflatedView.findViewById(R.id.booking_bt_maincourse_count_increment);
        Button _ButtonDecrementMainCourse = (Button) inflatedView.findViewById(R.id.booking_bt_maincourse_count_decrement);

        _ButtonIncrementMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _membersCount = _membersCount + 1;
                _TextViewMembersCount.setText(String.valueOf(_membersCount));
                if(_membersCount >= 2){
                    membersAmount =_membersCount * 50;
                }else{
                    membersAmount = 50;}

                _TextViewMembersAmount.setText(String.valueOf(membersAmount));
                int tempAmount = baseAmount + membersAmount + mainCourseAmount;
                double serviceTaxAmount = tempAmount*.125;
                _TextViewServiceTaxAmount.setText(String.valueOf(serviceTaxAmount));
                totalAmount = (serviceTaxAmount+tempAmount);
                _TextViewTotalAmount.setText(String.valueOf(totalAmount));
            }
        });
        _ButtonDecrementMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_membersCount > 2){
                    _membersCount = _membersCount - 1;
                    membersAmount =_membersCount * 50;
                }else{membersAmount = 50;}
                _TextViewMembersCount.setText(String.valueOf(_membersCount));

//                if(_membersCount >= 2){
//                    membersAmount =_membersCount * 50;
//                }else{membersAmount = 50;}

                _TextViewMembersAmount.setText(String.valueOf(membersAmount));
                int tempAmount = baseAmount + membersAmount + mainCourseAmount;
                double serviceTaxAmount = tempAmount*.125;
                _TextViewServiceTaxAmount.setText(String.valueOf(serviceTaxAmount));
                totalAmount = (serviceTaxAmount+tempAmount);
                _TextViewTotalAmount.setText(String.valueOf(totalAmount));
            }
        });
        _ButtonIncrementMainCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mainCourseCount = _mainCourseCount + 1;
                _TextViewMainCourseCount.setText(String.valueOf(_mainCourseCount));
                if(_mainCourseCount >= 2){
                    mainCourseAmount =_mainCourseCount * 50;
                }else{mainCourseAmount = 50;}

                _TextViewMainCourseAmount.setText(String.valueOf(mainCourseAmount));
                int tempAmount = baseAmount + membersAmount + mainCourseAmount;
                double serviceTaxAmount = tempAmount*.125;
                _TextViewServiceTaxAmount.setText(String.valueOf(serviceTaxAmount));
                totalAmount = (serviceTaxAmount+tempAmount);
                _TextViewTotalAmount.setText(String.valueOf(totalAmount));
            }
        });
        _ButtonDecrementMainCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_mainCourseCount >2) {
                    _mainCourseCount = _mainCourseCount - 1;
                    membersAmount =_mainCourseCount * 50;
                }else{
                    mainCourseAmount = 50;
                }
                _TextViewMainCourseCount.setText(String.valueOf(_mainCourseCount));
//                if(_mainCourseCount >= 2){
//                    membersAmount =_mainCourseCount * 50;
//                }else{mainCourseAmount = 50;}

                _TextViewMainCourseAmount.setText(String.valueOf(mainCourseAmount));
                int tempAmount = baseAmount + membersAmount + mainCourseAmount;
                double serviceTaxAmount = tempAmount*.125;
                _TextViewServiceTaxAmount.setText(String.valueOf(serviceTaxAmount));
                totalAmount = (serviceTaxAmount+tempAmount);
                _TextViewTotalAmount.setText(String.valueOf(totalAmount));
            }
        });

        if (_PopWindowDismissBT != null) {
            _PopWindowDismissBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopConfirmationWindow.dismiss();
                    _membersCount = 2;
                    _mainCourseCount = 2;
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
