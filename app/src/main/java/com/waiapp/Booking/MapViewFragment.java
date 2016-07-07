package com.waiapp.Booking;

import android.location.Location;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.waiapp.R;

/**
 * Created by keviv on 27/06/2016.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    public interface onAddressSearchClick {
        void startAddressSearchActivity();
    }

    private GoogleMap mGoogleMap;
    MapView mMapView;
    Location mLocation;
    boolean mapReady = false;
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
        double _lat = 0.00, _lon = 0.00;
        mapReady = true;
        mGoogleMap = googleMap;
        LatLng intensofy = new LatLng(28.4189245, 77.0383607);
        LatLng jmd = new LatLng(28.4204117, 77.0383323);
//
//        if (ContextCompat.checkSelfPermission((MainActivity)getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission((MainActivity)getActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            mGoogleMap.setMyLocationEnabled(true);
//            LocationManager mng = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//            mLocation = mng.getLastKnownLocation(mng.getBestProvider(new Criteria(), false));
//            _lat = mLocation.getLatitude();
//            _lon = mLocation.getLongitude();
//        }
//        LatLng currentPosition = new LatLng(_lat,_lon);
        CameraPosition target = CameraPosition.builder().target(jmd).zoom(14).build();
//        CameraPosition target = CameraPosition.builder().target(currentPosition).zoom(14).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        mGoogleMap.addMarker(new MarkerOptions()
                .title("JMD")
                .snippet("JMD Megapolis")
                .position(jmd).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_dining_black_24dp)));

        final Marker currentPositionMarker = mGoogleMap.addMarker(new MarkerOptions().position(intensofy));
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                // Get the center of the Map.
                LatLng centerOfMap = mGoogleMap.getCameraPosition().target;
                    currentPositionMarker.setPosition(centerOfMap);
            }
        });
    }
}
