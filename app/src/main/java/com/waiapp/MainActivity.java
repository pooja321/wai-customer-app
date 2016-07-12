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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ImageButton _PopWindowDismissBT = (ImageButton) inflatedView.findViewById(R.id.confirm_ib_pop_dismiss);
        TextView _totalAmountTextView = (TextView) inflatedView.findViewById(R.id.confirm_tv_total_amount);
        if (_totalAmountTextView != null) {
            _totalAmountTextView.setText("TOTAL: Rs 172.5");
        }
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

        if (_PopWindowDismissBT != null) {
            _PopWindowDismissBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopConfirmationWindow.dismiss();
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
