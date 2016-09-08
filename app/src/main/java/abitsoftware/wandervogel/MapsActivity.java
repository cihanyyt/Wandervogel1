package abitsoftware.wandervogel;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Address adr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = new Intent(this, GPSTrackerActivity.class);
        startActivityForResult(intent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Button b = (Button) findViewById(R.id.buttonMap);
        Geocoder geoCoder = new Geocoder(this);
        List<Address> list = null;

        Double longitude = 41.0082;
        Double latitude = 28.9784;

        if(requestCode == 1){
            Bundle extras = data.getExtras();
            latitude = extras.getDouble("Longitude");
            longitude = extras.getDouble("Latitude");
        }

        LatLng mapCenter = new LatLng(longitude, latitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));


        try {
            list = geoCoder.getFromLocation(longitude, latitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null & list.size() > 0) {
            b.setText(list.get(0).getAdminArea().toString());
        }

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(mapCenter)
                .zoom(13)
                .bearing(90)
                .build();

        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);

        // Polylines are useful for marking paths and routes on the map.
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(41.0082, 28.9784))  // Sydney
                .add(new LatLng(41.0082, 20.9784))  // Fiji
                .add(new LatLng(40.0082, 20.9784))  // Hawaii
                .add(new LatLng(40.0082, 28.9784))  // Mountain View
        );


    }
}
