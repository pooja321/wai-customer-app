package com.maidit.Booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maidit.R;

/**
 * Created by keviv on 27/06/2016.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    public interface onAddressSearchClick{
        void startAddressSearchActivity();
    }
    private GoogleMap mGoogleMap;
    MapView mMapView;
    boolean mapReady=false;
    EditText mAddressSearchEditText;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_map_view, container, false);
        MapsInitializer.initialize(this.getActivity());
        mMapView = (MapView) view.findViewById(R.id.main_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        final onAddressSearchClick listener = (onAddressSearchClick) getActivity();
        mAddressSearchEditText = (EditText) view.findViewById(R.id.main_ed_search_address_id);
        mAddressSearchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.startAddressSearchActivity();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapReady=true;
        this.mGoogleMap = googleMap;
        LatLng intensofy = new LatLng(28.4189245,77.0383607);
        LatLng jmd = new LatLng(28.4204117,77.0383323);
        CameraPosition target = CameraPosition.builder().target(jmd).zoom(14).build();
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));

        this.mGoogleMap.addMarker(new MarkerOptions()
                .title("JMD")
                .snippet("JMD Megapolis")
                .position(jmd).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_dining_black_24dp)));

        this.mGoogleMap.addMarker(new MarkerOptions()
                .title("Intensofy Solutions")
                .snippet("Iris tech Park")
                .position(intensofy));

    }
}
