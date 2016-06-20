package com.maidit;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    GoogleMap m_map;
    boolean mapReady=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap map){
        mapReady=true;
        m_map = map;
        LatLng intensofy = new LatLng(28.4189245,77.0383607);
//        LatLng sprinklr = new LatLng(28.4189245,78.0383607);
//        LatLng sabmiller = new LatLng(28.4189245,79.0383607);
        LatLng jmd = new LatLng(28.4204117,77.0383323);
        CameraPosition target = CameraPosition.builder().target(jmd).zoom(14).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

        m_map.addMarker(new MarkerOptions()
                .title("JMD")
                .snippet("JMD Megapolis")
                .position(jmd).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_dining_black_24dp)));

        m_map.addMarker(new MarkerOptions()
                .title("Intensofy Solutions")
                .snippet("Iris tech Park")
                .position(intensofy));
//        m_map.addMarker(new MarkerOptions()
//                .title("Sprinklr")
//                .snippet("Iris tech Park")
//                .position(sprinklr));
//        m_map.addMarker(new MarkerOptions()
//                .title("SABMiller")
//                .snippet("Iris tech Park")
//                .position(sabmiller).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_dining_black_24dp)));
    }
}
