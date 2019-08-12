package be.pxl.mobiledevelopmentproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleSuperMarketActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private int ProximityRadius = 2000;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private Location lastLocation;
    private LatLng latLng;
    private Marker currentUserLocationMarker;
    private double latitude, longitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_super_market);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }


    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        enableMyLocation();
    }

    public void findSupermarkets(View view) {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (network_enabled) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lastLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(lastLocation!=null){
                longitude = lastLocation.getLongitude();
                latitude = lastLocation.getLatitude();
            }
        }

        System.out.println("--------------------------------------" + lastLocation + "---------");
        System.out.println(mMap);
        if(mMap!=null && longitude != 0.0 && latitude != 0.0){
            String supermarket = "supermarket";
            Object transferData[] = new Object[2];
            GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
            String url = getUrl(latitude, longitude, supermarket);
            transferData[0]=mMap;
            transferData[1]=url;


            getNearbyPlaces.execute(transferData);
            Toast.makeText(this, "Looking for Nearby Supermarkets...", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Showing Nearby Supermarkets...", Toast.LENGTH_SHORT).show();
        }

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private String getUrl(double latitudeSupermarket, double longitudeSupermarket, String nearbyPlace) {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=").append(latitudeSupermarket).append(",").append(longitudeSupermarket);
        googleURL.append("&radius=").append(ProximityRadius);
        googleURL.append("&type=").append(nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyD75cSlJq19-74-vj7sj-p5jyfKXokRGW0");


        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());

        return googleURL.toString();
    }

    @Override
    public void onLocationChanged(Location location){
        /*
        System.out.println("CHANGED LOCATION--------------------------------------------------------------");
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if (currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("user Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null)
        {
            mFusedLocationProviderClient.removeLocationUpdates(new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult){
                    super.onLocationResult(locationResult);
                }
            });
        }

        */

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult){
                    super.onLocationResult(locationResult);
                }
            }, getMainLooper());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }
}
