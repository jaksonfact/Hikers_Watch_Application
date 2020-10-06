package com.example.hikers_watch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {


    LocationManager locationManager;

    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();

        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }
    }

    public void updateLocationInfo (Location location){
        Log.i("LocationInfo", location.toString());

        TextView latTextView = (TextView) findViewById(R.id.latTextView);
        TextView lonTextView = (TextView) findViewById(R.id.longTextView);
        TextView altTextView = (TextView) findViewById(R.id.altTextView);
        TextView accTextView = (TextView) findViewById(R.id.accTextView);

        latTextView.setText("Latitude : " + location.getLatitude());
        lonTextView.setText("Longtitude : " + location.getLongitude());
        altTextView.setText("Altitude : " + location.getAltitude());
        accTextView.setText("Accuracy : " + location.getAccuracy());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try{
            String address = "Could not find address";
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
               Log.i("lisr",""+listAddress.size());
            if (listAddress != null && listAddress.size() >0){
                Log.i("PlaceInfo", listAddress.get(0).toString());
                address = "Address: \n";
                if (listAddress.get(0).getSubThoroughfare() != null){
                    address += listAddress.get(0).getSubThoroughfare() + " ";

                }
                if (listAddress.get(0).getSubThoroughfare() != null){
                    address += listAddress.get(0).getSubThoroughfare() + "\n";

                }
                if (listAddress.get(0).getSubLocality() != null){
                    address += listAddress.get(0).getSubLocality() + "\n";
                }
                if (listAddress.get(0).getLocality() != null){
                    address += listAddress.get(0).getLocality() + "\n";
                }
                if (listAddress.get(0).getPostalCode() != null){
                    address += listAddress.get(0).getPostalCode() + "\n";
                }
                if (listAddress.get(0).getCountryName() != null){
                    address += listAddress.get(0).getCountryName() + "\n";
                }
            }

            TextView addressTextView =  (TextView) findViewById(R.id.addressTextView);
            addressTextView.setText(address);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
            @Override
            public void  onStatusChanged (String s, int i, Bundle bundle){

            }
            @Override
            public  void onProviderEnabled(String s) {

            }
            @Override
            public void onProviderDisabled(String s) {

          }
        };

        if (Build.VERSION.SDK_INT < 23){
            startListening();
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null){
                    updateLocationInfo(location);
                }
            }
        }

    }
}