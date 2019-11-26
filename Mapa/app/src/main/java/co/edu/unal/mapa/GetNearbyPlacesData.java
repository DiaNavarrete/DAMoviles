package co.edu.unal.mapa;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... objects){

        Log.e("MAPI", "Nearby in background");
        mMap= (GoogleMap)objects[0];
        url=(String)objects[1];
        DownloadURL downloadURL=new DownloadURL();
        try {
            googlePlacesData = downloadURL.readURL(url);
        }catch (IOException e){
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("MAPI", "finally execute");
        List<HashMap<String, String>> nearbyPlaceList=null;
        DataParser parser = new DataParser();
        nearbyPlaceList=parser.parse(s);
        Log.e("MAPI", "size places "+ nearbyPlaceList.size());
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList){
        Log.e("MAPI", "show");
        for(int i =0; i<nearbyPlaceList.size();i++){
            MarkerOptions markerOptions= new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
            String placeName= googlePlace.get("place_name");
            Log.e("MAPI", placeName);
            String vecinity= googlePlace.get("vecinity");
            double lat= Double.parseDouble(googlePlace.get("lat"));
            double lng= Double.parseDouble(googlePlace.get("lng"));
            LatLng latLng= new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " " + vecinity);
            mMap.addMarker(markerOptions);
        }
    }
}
