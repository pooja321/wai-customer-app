package com.maidit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.maidit.Booking.MapViewFragment;

public class MainActivity extends BaseActivity implements MapViewFragment.onAddressSearchClick {

    public static final String MAP_VIEW_FRAGMENT = "map_view_fragment";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Switch mMapListSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapViewFragment mapViewFragment = new MapViewFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_placeholder, mapViewFragment, MAP_VIEW_FRAGMENT);
        fragmentTransaction.commit();

        mMapListSwitch = (Switch) findViewById(R.id.main_switch_map_list);
        if (mMapListSwitch != null) {
            mMapListSwitch.setChecked(true);
        }
        mMapListSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mMapListSwitch.setText("Map");
                }else{
                    mMapListSwitch.setText("List");
                }
            }
        });
    }

    @Override
    public void startAddressSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchAddressActivity.class);
        startActivity(intent);
    }
}
