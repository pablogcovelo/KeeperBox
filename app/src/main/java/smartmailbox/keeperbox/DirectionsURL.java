package smartmailbox.keeperbox;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pablo on 9/4/17.
 */

public class DirectionsURL {

    public String getURL(LatLng originPoint, LatLng destPoint, String waypoints) {
        // Origin route
        String str_origin = "origin=" + originPoint.latitude + "," + originPoint.longitude;

        // Destination route
        String str_dest = "destination=" + destPoint.latitude + "," + destPoint.longitude;

        // Waypoints
        String str_waypoints = "waypoints=" + waypoints;

        // API key
        String str_key = "key=AIzaSyAXyisVJOOorrztQAOpNSVJSwbc7XSM3cs";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + str_waypoints + "&" + str_key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }
}