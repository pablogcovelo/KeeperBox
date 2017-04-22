package smartmailbox.keeperbox;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;


/**
 * Created by regueiro on 13/03/17.
 */

public class Peticion extends AsyncTask<String, Object, String> {
    private static final String ip = "keeperbox.ga"; // getIpAddress();

    private Request requestCompleted;

    public Peticion(Request activityContext){
        this.requestCompleted = activityContext;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    /*protected void onPreExecute() {
        // mostramos el círculo de progreso
        progressBar.setVisibility(View.VISIBLE);
    }*/

    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Va a contener la respuesta JSON como un string.
        String forecastJsonStr = null;
        // Parametros
        String procedure = params[0];
        String json = params[1];

        try {
            URL url = new URL("http://" + ip + "/api.php/" + procedure);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("User-Agent", "cliente Android");

            // Incluir lo que se enviará en el body (el JSON) para nuestro servidor REST.
            urlConnection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());

            //Le pasamos el JSON como parametro
            dataOutputStream.write(json.getBytes(StandardCharsets.UTF_8));

            dataOutputStream.flush();
            dataOutputStream.close();

            urlConnection.connect();

            // Podemos comprobar el codigo de la respuesta HTTP
            ////if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            System.out.println("CODIGO: " + urlConnection.getResponseCode());

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                return null;
            }
            forecastJsonStr = buffer.toString();
            return forecastJsonStr;
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        JSONArray json = null;
        try {
            System.out.println("*********** " + s + " ***********");

             if (s!=null) {
                 if (!s.contains("]"))
                     s = "[" + s + "]";
                 json = new JSONArray(s);
             }
            System.out.println("*********** RESPONSE ss ***********");
            System.out.println("*********** " + json + " ***********");

            requestCompleted.onRequestCompleted(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getIpAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress=inetAddress.getHostAddress().toString();
                        Log.e("IP address",""+ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Socket exception in GetIP Address of Utilities", ex.toString());
        }
        return null;
    }
}