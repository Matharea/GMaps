package fr.wildcodeschool.gmaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager mLocationManager = null;
    private static final int FINE_LOCATION_REQUEST = 100;
    private LatLng currentPosition;
    private Boolean locationRdy = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkPermission();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    private void checkPermission(){
        // vérification de l'autorisation d'accéder à la position GPS
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

//            // l'autorisation n'est pas acceptée
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                // l'autorisation a été refusée précédemment, on peut prévenir l'utilisateur ici
//            } else {
                // l'autorisation n'a jamais été réclamée, on la demande à l'utilisateur
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_LOCATION_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
 //           }
        } else {
            // TODO : autorisation déjà acceptée, on peut faire une action ici
            initLocation();
        }
    }

    @SuppressLint("MissingPermission")
    public void initLocation(){

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                locationRdy = true;
                showPosition(mMap);
//                Context context = getApplicationContext();
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast= Toast.makeText(context,location.toString(),duration);
//                toast.show();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(FINE_LOCATION_REQUEST == requestCode){
                // cas de notre demande d'autorisation
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO : l'autorisation a été donnée, nous pouvons agir
                initLocation();

            } else {
                // l'autorisation a été refusée :(.
                finish();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (currentPosition != null) {
            showPosition(mMap);
        }
    }

    public void showPosition(GoogleMap mMap){
        mMap.addMarker(new MarkerOptions().position(currentPosition).title("Marker's on me"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
    }
}
