package smartmailbox.keeperbox;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity Debugging";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;

    private static final LatLng ORIGIN = new LatLng(42.170178, -8.687878);
    private static final LatLng DESTINATION = new LatLng(42.170178, -8.687878);

    Polyline polyline;
    private List<LatLng> polyLinePoints = new ArrayList<>();

    private DirectionsURL directionsurl = new DirectionsURL();
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         * Situamos el boton de geolocalizacion abajo a la derecha
         */
        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);

        /**
         * Añadimos una "Search ToolBar" al mapa para realizar busquedas
         */
        /*PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                LatLng latlng = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });*/
    }

    /**
     * Trazamos por el mapa la ruta que debe seguir el repartidor para realizar la entrega de los paquetes
     * y añadimos un marcador en cada uno de los puntos en los que tiene que dejar un paquete
     *
     * @param lat
     * @param lng
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void TraceRoute(double lat, double lng) throws ExecutionException, InterruptedException {
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));

        String urlResponse = directionsurl.getURL(ORIGIN, DESTINATION);
        Log.d(TAG, urlResponse);
        // Trigger Async Task (onPreExecute method)
        String jsonContent = (new DownloadJSON().execute(urlResponse)).get();
        if (!jsonContent.isEmpty()) {
            mMap.clear();
            DecodePolyline decodePolyline = new DecodePolyline();
            polyLinePoints = decodePolyline.getPolyLine(jsonContent);

            if (polyline != null) {
                polyline.remove();
            }

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < polyLinePoints.size(); i++) {
                polylineOptions.add(polyLinePoints.get(i));
            }

            polyline = mMap.addPolyline(polylineOptions);

            //Put new marker on destination point
            mMap.addMarker(new MarkerOptions().position(new LatLng(42.235587, -8.719975)));
            mMap.addMarker(new MarkerOptions().position(new LatLng(42.234845, -8.705945)));
            mMap.addMarker(new MarkerOptions().position(new LatLng(42.219278, -8.732695)));
        }
    }

    /**
     * Uses AsyncTask to create a task away from the main UI thread. This task takes a
     * URL string and uses it to create an HttpUrlConnection. Once the connection
     * has been established, the AsyncTask downloads the contents of the webpage as
     * an InputStream. Finally, the InputStream is converted into a string, which is
     * splayed in the UI by the AsyncTask's onPostExecute method.
     */
    private class DownloadJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            StringBuilder jsonResult = new StringBuilder();

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                String linea;
                while ((linea = reader.readLine()) != null) {
                    jsonResult.append(linea);
                }

                return jsonResult.toString();

            } catch (MalformedURLException e) {
                Log.d(TAG, e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            } finally {
                urlConnection.disconnect();
            }
            Log.d(TAG, jsonResult.toString());
            return null;
        }
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        /**
         * Obtenemos la posicion actual del usuario  e iniciamos el mapa en dicha posicion
         */
        int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);

// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MapsActivity.this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MapsActivity.this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        Log.d("Location: ", String.valueOf(location));
        LatLng originPoint = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originPoint,15));
        /*LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));*/

        try {
            TraceRoute(ORIGIN.latitude, ORIGIN.longitude);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}