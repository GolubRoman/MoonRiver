package com.golubroman.golub.weatherv201;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import android.location.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by User on 12.02.2017.
 */

public class LocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location location;
    Geocoder geocoder;
    List<Address> addresses;
    LocationFragmentListener locationFragmentListener;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;
    public interface LocationFragmentListener{
        void getCity(String city);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        locationFragmentListener = (LocationFragmentListener)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();
        }
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    @Override
    public void onConnected(Bundle connectionHint){
        try {
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }catch(SecurityException e){}
        toCity(location.getLongitude(), location.getLatitude());
        startLocationUpdates();
    }
    @Override
    public void onStart() {
        buildGoogleApiClient();
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    protected void startLocationUpdates() {
        try{
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }catch(SecurityException e){

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        toCity(location.getLongitude(), location.getLatitude());
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        ErrorDialogFragment connectionFailed= new ErrorDialogFragment();
        Bundle args=new Bundle();
        args.putInt("connection error №", result.getErrorCode());
        connectionFailed.setArguments(args);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("LocationFragment", "Connection suspended");
        googleApiClient.connect();
    }
    void toCity(double longitude, double latitude){
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0){
            String city = addresses.get(0).getLocality();
            Toast.makeText(getContext(), "Your city: " + city, Toast.LENGTH_SHORT).show();
            locationFragmentListener.getCity(city);
        }
        else Toast.makeText(getContext(), "Cannot find your city", Toast.LENGTH_SHORT).show();

    }
}