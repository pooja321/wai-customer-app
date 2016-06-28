package com.maidit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.maidit.Booking.ListViewFragment;
import com.maidit.Booking.MapViewFragment;

public class MainActivity extends BaseActivity implements MapViewFragment.onAddressSearchClick, ListViewFragment.OnResourceSelectedInterface {

    public static final String MAP_VIEW_FRAGMENT = "map_view_fragment";
    public static final String LIST_VIEW_FRAGMENT = "list_view_fragment";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Switch mMapListSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Toast.makeText(MainActivity.this, "You selected ".concat(String.valueOf(index)), Toast.LENGTH_SHORT).show();
    }
}
