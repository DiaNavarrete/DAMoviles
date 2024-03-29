package co.edu.unal.mapa;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String EXTRA_R = "co.edu.unal.radius";
    private String key ;
    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private static final int REQUEST_CODE = 101;
    private Marker baseMarker;
    private SearchView mSearchView;
    private Circle circle;
    private int km;
    private LatLng p;
    private String texttoFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        key=getString(R.string.key);
        Log.i("MAPI",key);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Places.initialize(getApplicationContext(), key);
        placesClient = Places.createClient(this);
        mSearchView= findViewById(R.id.search);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("MAPI",s);
                texttoFind=s;
                setNearbyPlacesV2(s);
                mMap.addCircle(new CircleOptions()
                        .center(p)
                        .radius(km));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        fetchLastLocation();
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            System.out.println("No permisos");
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation=location;
                    Toast.makeText(getApplicationContext(),
                            currentLocation.getLatitude() + " " +currentLocation.getLongitude(),
                            Toast.LENGTH_SHORT).show();

                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    private void setNearbyPlaces(){
        List<Place.Field> placeFields= new ArrayList<>();
        placeFields.add(Place.Field.NAME);
        placeFields.add(Place.Field.LAT_LNG);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
        Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Place p = placeLikelihood.getPlace();
                        mMap.addMarker(new MarkerOptions().position(p.getLatLng()).title(p.getName()));
                        Log.i("MAPI", String.format("Place '%s' is located : %s",
                                placeLikelihood.getPlace().getName(),
                                p.getLatLng().toString()));
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("MAPI", "Place not found: " + apiException.getStatusCode());
                        Toast.makeText(getApplicationContext(),
                                "Error " + + apiException.getStatusCode(),
                                Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void setNearbyPlacesV2(String stringfind){
        Log.e("MAPI", "nearby function");
        p =new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                //baseMarker.getPosition();
    //    StringBuilder googlePlaceURL= new StringBuilder("https://maps.googleapis.com/maps/@api=1&");
   /*     StringBuilder googlePlaceURL= new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
        googlePlaceURL.append("latlng="+p.latitude+","+p.longitude+"&sensor=false");*/
        StringBuilder googlePlaceURL= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceURL.append("location=" + p.latitude+","+p.longitude);
        googlePlaceURL.append("&radius="+(km-500));
        if(stringfind!="")  googlePlaceURL.append("&keyword="+stringfind);
        googlePlaceURL.append("&key="+ key);

        Object dataTransfer[]= new Object[2];
        dataTransfer[0]=mMap;
        dataTransfer[1]=googlePlaceURL.toString();
        mMap.clear();
        GetNearbyPlacesData getNearbyPlacesData= new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        final LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

       // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
       // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        baseMarker=mMap.addMarker(new MarkerOptions().position(latLng)
                                            .draggable(true)
                                .visible(false)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        km = getIntent().getIntExtra(EXTRA_R,1);
        Log.e("MAPI", "Radio: " + km);
     /*   circle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(km));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {  }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (marker.equals(baseMarker)) {
                    LatLng ll=marker.getPosition();
                    Toast.makeText(MapsActivity.this, ll.latitude + " " +ll.longitude, Toast.LENGTH_SHORT).show();
                    circle.setCenter(ll);
                    //PutPlaces();
                }

            }
        });*/
        mMap.setMyLocationEnabled(true);
        Log.e("MAPI", "My location");
        setNearbyPlacesV2("");
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }
}
