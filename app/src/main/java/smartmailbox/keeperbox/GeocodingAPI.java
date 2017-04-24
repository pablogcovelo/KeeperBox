package smartmailbox.keeperbox;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

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

/**
 * Created by pablo on 22/4/17.
 */

public class GeocodingAPI {

    private static final String TAG = "GeocodingAPI Debugging";

    public ArrayList<LatLng> getLatLong(ArrayList<String> waypoints) throws ExecutionException, InterruptedException {
        ArrayList<LatLng> latlng = new ArrayList<>();
        ArrayList<String> urls;

        urls = setURLs(waypoints);
        for(int i = 0; i < urls.size(); i++) {
            String jsonContent = (new DownloadJSON().execute(urls.get(i))).get();
            if (!jsonContent.isEmpty()) {
                List<GeocodingGSONFormat.ResultsBean> resultsBean;

                //Create Gson object and get the values from JSON url body (for this using DirectionsGSONFormat class)
                Gson gson = new Gson();
                GeocodingGSONFormat responseObj = gson.fromJson(jsonContent, GeocodingGSONFormat.class);

                resultsBean = responseObj.getResults();
                for (int x = 0; x < resultsBean.size(); x++) {
                    double lat = responseObj.getResults().get(x).getGeometry().getLocation().getLat();
                    double lng = responseObj.getResults().get(x).getGeometry().getLocation().getLng();
                    latlng.add(new LatLng(lat, lng));
                }
            }
        }
        return latlng;
    }

    private ArrayList<String> setURLs(ArrayList<String> waypoints) {
        ArrayList<String> url = new ArrayList<>();

        for(int i = 0; i < waypoints.size(); i++) {
            // Waypoints
            String str_waypoints = "address=" + waypoints.get(i);

            // API key
            String str_key = "key=AIzaSyAXyisVJOOorrztQAOpNSVJSwbc7XSM3cs";

            // Building the parameters to the web service
            String parameters = str_waypoints + "&" + str_key;

            // Output format
            String output = "json";

            // Building the url to the web service
            url.add("https://maps.googleapis.com/maps/api/geocode/" + output + "?" + parameters);
        }

        return url;
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
            return null;
        }
    }
}
