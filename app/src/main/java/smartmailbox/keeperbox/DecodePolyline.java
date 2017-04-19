package smartmailbox.keeperbox;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pablo on 9/4/17.
 */

public class DecodePolyline {
    private static final String TAG = "DecodePolyline Debugging";

    private List<DirectionsGSONFormat.RoutesBean> routesBean = null;
    private List<DirectionsGSONFormat.RoutesBean.LegsBean> legsBean = null;
    private List<DirectionsGSONFormat.RoutesBean.LegsBean.StepsBean> steps = null;
    private DirectionsGSONFormat.RoutesBean.LegsBean.StepsBean.PolylineBean polylineBean = null;
    private ArrayList<LatLng> route = new ArrayList<>();

    public List<LatLng> getPolyLine(String json) {
        //Create Gson object and get the values from JSON url body (for this using DirectionsGSONFormat class)
        Gson gson = new Gson();
        DirectionsGSONFormat responseObj = gson.fromJson(json, DirectionsGSONFormat.class);

        routesBean = responseObj.getRoutes();

        for (int i = 0; i < routesBean.size(); i++) {
            legsBean = responseObj.getRoutes().get(i).getLegs();
            for(int x = 0; x < legsBean.size(); x++) {
                steps = responseObj.getRoutes().get(i).getLegs().get(x).getSteps();
                for (int j = 0; j < steps.size(); j++) {
                    polylineBean = responseObj.getRoutes().get(i).getLegs().get(x).getSteps().get(j).getPolyline();
                    ArrayList<LatLng> arr = decodePolyline();
                    for(int y = 0; y < arr.size(); y++) {
                        route.add(arr.get(y));
                    }
                }
            }
        }
        return route;
    }

    public ArrayList<LatLng> decodePolyline() {
        // Decode PolyLine
        ArrayList<LatLng> polyline = new ArrayList<>();
        int len = polylineBean.getPoints().length();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = polylineBean.getPoints().charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polylineBean.getPoints().charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            polyline.add(new LatLng(lat / 100000d, lng / 100000d));
        }
        return polyline;
    }
}
