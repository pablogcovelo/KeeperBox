package smartmailbox.keeperbox;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pablo on 9/4/17.
 */

public class DirectionsURL {
    /**
     * Waypoints de prueba
     */
    private static final String STREET1 = "Rua+Urzaiz";
    private static final String NUMBER1 = "8";
    private static final String CITY1 = "Vigo";

    private static final String STREET2 = "Rua+da+Travesia+de+Vigo";
    private static final String NUMBER2 = "51";
    private static final String CITY2 = "Vigo";

    private static final String STREET3 = "Avenia+de+Castrelos";
    private static final String NUMBER3 = "22";
    private static final String CITY3 = "Vigo";
    /**
     * Final Waypoints de prueba
     */
    String waypoints = STREET1+",+"+NUMBER1+",+"+CITY1+"|"+STREET2+",+"+NUMBER2+",+"+CITY2+"|"+STREET3+",+"+NUMBER3+",+"+CITY3;
    public String getURL(LatLng originPoint, LatLng destPoint) {
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
