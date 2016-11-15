package com.waiapp.Booking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waiapp.Model.ResourceOnline;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by keviv on 27/06/2016.
 */
public abstract class MapViewFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GeoQueryEventListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    public interface onAddressSearchClick {
        void startAddressSearchActivity();
    }

    private String mJobType;
    private static final int PERMISSION_REQUEST_CODE = 1;

    EditText mAddressSearchEditText;

    // Google client to interact with Google API
    public LocationManager mLocationManager;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private Marker mCenterMarker;
    private MarkerOptions mCenterMarkerOptions;
    private Map<String, Marker> mMapMarkers = new HashMap<>();
    private Map<String, ResourceOnline> mMapResourceList = new HashMap<>();
    DatabaseReference mGeoDatabaseRef, mOnlineResourceDatabaseRef;
    private GeoFire mGeoFire;
    private GeoQuery mGeoQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("wai", "MapViewFragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_map_view, container, false);
        Log.v("wai", "MapViewFragment onCreateView");
        mGeoDatabaseRef = getGeoDatabaseReference();
        mJobType = getJobtype();
        mGeoFire = new GeoFire(mGeoDatabaseRef);
        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);
        final onAddressSearchClick listener = (onAddressSearchClick) getActivity();
//        mAddressSearchEditText = (EditText) view.findViewById(R.id.main_ed_search_address_id);
//        mAddressSearchEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.startAddressSearchActivity();
//            }
//        });
        mCenterMarkerOptions = new MarkerOptions();
//        mCenterMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCenterMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black_36dp));
        return view;

    }

    public abstract DatabaseReference getGeoDatabaseReference();

    public abstract String getJobtype();

    private void buildAlertMessageNoGps() {
        Log.v("wai", "MapViewFragment buildAlertMessageNoGps");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean checkPermission() {
        Log.v("wai", "MapViewFragment checkPermission");
        int result = ContextCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        Log.v("wai", "MapViewFragment requestPermission");
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getActivity().getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        }
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("wai", "Permission Granted");
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.v("wai", "MapViewFragment buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("wai", "MapViewFragment onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("wai", "MapViewFragment onResume");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("wai", "MapViewFragment onStart");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("wai", "MapViewFragment onStop");
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
        if (mGeoQuery != null) {
            mGeoQuery.removeAllListeners();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v("wai", "MapViewFragment onMapReady");
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setInfoWindowAdapter(new ResourceInfoMarkerWindow());

        LatLng centerOfMap = mGoogleMap.getCameraPosition().target;
        mCenterMarkerOptions.position(centerOfMap);
        mCenterMarker = mGoogleMap.addMarker(mCenterMarkerOptions);
        if (!checkPermission()) {
            Log.v("wai", "no location permission");
            requestPermission();
        } else {
            Log.v("wai", "location permission granted");
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
        GeoLocation mGeoLocation = new GeoLocation(centerOfMap.latitude, centerOfMap.longitude);
        mGeoQuery = mGeoFire.queryAtLocation(mGeoLocation, 3);
        mGeoQuery.addGeoQueryEventListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v("wai", "MapViewFragment onConnected");
        LocationRequest mLocationRequest = new LocationRequest();
        int UPDATE_INTERVAL = 10000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        int FATEST_INTERVAL = 5000;
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int DISPLACEMENT = 10;
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("wai", "MapViewFragment onLocationChanged");
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onCameraMove() {
        LatLng centerOfMap = mGoogleMap.getCameraPosition().target;
        mCenterMarker.setPosition(centerOfMap);
        mGeoQuery.setCenter(new GeoLocation(centerOfMap.latitude, centerOfMap.longitude));
        mGeoQuery.setRadius(3);
//        LatLng centermarkerposition = mCenterMarker.getPosition();
//        String position = String.format("%.2f %.2f", centermarkerposition.latitude, centermarkerposition.longitude );
//        Log.v("wai", "onCameraMove center marker position: " + position);
//        Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        Log.v("wai", "onKeyEntered: " + String.format("%.2f %.2f", location.latitude, location.longitude));
        int resId = 0;
        switch (mJobType) {
            case Constants.FIREBASE_CHILD_CLEANING:
                resId = getResources().getIdentifier("ic_person_black_24dp", "drawable", "com.waiapp");
                break;
            case Constants.FIREBASE_CHILD_WASHING:
                resId = getResources().getIdentifier("ic_hanger_black_24dp", "drawable", "com.waiapp");
                break;
            case Constants.FIREBASE_CHILD_COOKING:
                resId = getResources().getIdentifier("ic_local_dining_black_24dp", "drawable", "com.waiapp");
                break;

        }
        Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                .icon(BitmapDescriptorFactory.fromResource(resId)));
        this.mMapMarkers.put(key, marker);
        Log.v("wai", "key: " + key);
        getResourceData(key, marker.getId());
    }

    private void getResourceData(final String key, final String id) {
        Log.v("wai", "getResourceDatamarker id: " + key);
        mOnlineResourceDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_ONLINE_RESOURCE).child(mJobType).child(key);
        mOnlineResourceDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("wai", "onDataChange dataSnapshot: " + dataSnapshot.toString());
                mMapResourceList.put(id, dataSnapshot.getValue(ResourceOnline.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onKeyExited(String key) {
        // Remove any old marker
        Marker marker = this.mMapMarkers.get(key);
        if (marker != null) {
            marker.remove();
            this.mMapMarkers.remove(key);
            mMapResourceList.remove(marker.getId());
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        // Move the marker
        Marker marker = mMapMarkers.get(key);
        if (marker != null) {
            this.animateMarkerTo(marker, location.latitude, location.longitude);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        if (markerHashMap.containsValue(marker)){
//            Toast.makeText(getActivity(), "marker found", Toast.LENGTH_SHORT).show();
//        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getActivity(), "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    // Animation handler for old APIs without animation support
    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed / DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    class ResourceInfoMarkerWindow implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            Log.v("wai","getInfoWindow: " + marker.getId());
            if (!(marker.getId().equals("m0"))) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.list_resource_item, null);
                render(marker, view);
                return view;
            }
            return null;
        }

        private void render(Marker marker, View view) {
            TextView mTextViewName, mTextViewResourceRating;
            ImageView mImageViewResourcePic, mImageViewGenderIcon;
            Uri profilePicUri = null;

            mTextViewName = (TextView) view.findViewById(R.id.list_item_name);
            mTextViewResourceRating = (TextView) view.findViewById(R.id.list_item_rating);
            mImageViewResourcePic = (ImageView) view.findViewById(R.id.list_item_profilePic);
            mImageViewGenderIcon = (ImageView) view.findViewById(R.id.list_item_gender);

            String id = marker.getId();
            Log.v("wai", "render id: " + id);
            ResourceOnline resource = mMapResourceList.get(id);

            String _fullName = resource.getName();
            if (resource.getPicture() != null) {
                profilePicUri = Uri.parse(resource.getPicture());
                Glide.with(view.getContext()).load(profilePicUri).placeholder(R.drawable.beforeafter).into(mImageViewResourcePic);
            } else {
                Glide.with(view.getContext()).load(R.drawable.beforeafter).into(mImageViewResourcePic);
            }
            mTextViewName.setText(_fullName);
            mTextViewResourceRating.setText(String.valueOf(resource.getRating()));
            Glide.with(view.getContext()).load(profilePicUri).placeholder(R.drawable.beforeafter).into(mImageViewResourcePic);
            switch (resource.getGender()) {
                case ("Male"): {
                    mImageViewGenderIcon.setImageResource(R.drawable.human_male);
                    mImageViewGenderIcon.setColorFilter(Color.rgb(33, 150, 243));
                    break;
                }
                case ("Female"): {
                    mImageViewGenderIcon.setImageResource(R.drawable.human_female);
                    mImageViewGenderIcon.setColorFilter(Color.rgb(233, 30, 99));
                    break;
                }
            }

        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
