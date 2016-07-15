package com.waiapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.waiapp.Booking.ListViewFragment;
import com.waiapp.Booking.MapViewFragment;

public class MainActivity extends BaseActivity implements MapViewFragment.onAddressSearchClick, ListViewFragment.OnResourceSelectedInterface {

    public static final String MAP_VIEW_FRAGMENT = "map_view_fragment";
    public static final String LIST_VIEW_FRAGMENT = "list_view_fragment";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Switch mMapListSwitch;
    ImageButton mSortFilterImageButton;
    private PopupWindow mPopConfirmationWindow;

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
        mSortFilterImageButton = (ImageButton) findViewById(R.id.main_ib_sort_filter);
        MapViewFragment savedMapFragment = (MapViewFragment) getSupportFragmentManager().findFragmentByTag(MAP_VIEW_FRAGMENT);
        if (savedMapFragment == null) {
            Log.v("MainActivity","mapViewFragment1");
            MapViewFragment mapViewFragment = new MapViewFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_placeholder, mapViewFragment, MAP_VIEW_FRAGMENT);
            fragmentTransaction.commit();
        }

        mMapListSwitch = (Switch) findViewById(R.id.main_switch_map_list);
        if (mMapListSwitch != null) {
            mMapListSwitch.setChecked(true);
        }
        mMapListSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mMapListSwitch.setText("Map");
                    LoadMapViewFragment();
                }else{
                    mMapListSwitch.setText("List");
                    loadListViewFragment();
                }
            }
        });
        mSortFilterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                SortFilterDialogFragment sortFilterDialogFragment = SortFilterDialogFragment.newInstance("");
                sortFilterDialogFragment.show(fm, "fragment_sort_filter");

            }
        });
    }

    private void LoadMapViewFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        MapViewFragment savedMapFragment = (MapViewFragment) getSupportFragmentManager().findFragmentByTag(MAP_VIEW_FRAGMENT);
        if(savedMapFragment == null) {
            Log.v("MainActivity","mapViewFragment2");
            MapViewFragment mapViewFragment = new MapViewFragment();
            fragmentTransaction.replace(R.id.main_placeholder, mapViewFragment, MAP_VIEW_FRAGMENT);
            fragmentTransaction.commit();
        }else{
            Log.v("MainActivity","savedMapFragment");
            fragmentTransaction.replace(R.id.main_placeholder,savedMapFragment, MAP_VIEW_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    private void loadListViewFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ListViewFragment savedListFragment = (ListViewFragment) getSupportFragmentManager().findFragmentByTag(LIST_VIEW_FRAGMENT);
        if (savedListFragment == null){
            Log.v("MainActivity","listViewFragment");
            ListViewFragment listViewFragment = new ListViewFragment();
            fragmentTransaction.replace(R.id.main_placeholder,listViewFragment, LIST_VIEW_FRAGMENT);
//            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else {
            Log.v("MainActivity","savedListFragment");
            fragmentTransaction.replace(R.id.main_placeholder,savedListFragment, LIST_VIEW_FRAGMENT);
//            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void startAddressSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchAddressActivity.class);
        startActivity(intent);
    }

    @Override
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
                Log.v("wai","increment members: " + _membersCount);
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
                Log.v("wai","increment members Amount: " + membersAmount);
                Log.v("wai","increment members temp Amount: " + tempAmount);
                Log.v("wai","increment tax Amount: " + serviceTaxAmount);
                Log.v("wai","increment total Amount: " + totalAmount);
            }
        });
        _ButtonDecrementMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_membersCount > 2){
                    Log.v("wai","decrement members: " + _membersCount);
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
                Log.v("wai","decrement members Amount: " + membersAmount);
                Log.v("wai","decrement members temp Amount: " + tempAmount);
                Log.v("wai","decrement tax Amount: " + serviceTaxAmount);
                Log.v("wai","decrement total Amount: " + totalAmount);
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
}
