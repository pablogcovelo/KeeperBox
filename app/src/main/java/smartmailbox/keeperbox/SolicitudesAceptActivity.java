package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by regueiro on 13/03/17.
 */

public class SolicitudesAceptActivity extends Fragment implements Request{

    String NFC;

    public SolicitudesAceptActivity(String NFC){
        this.NFC = NFC;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_lista_solicitudes, container, false);

        JSONObject json = new JSONObject();
        try {
            json.put("NFCConsulta", NFC);
            json.put("tipo_solicitud", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(SolicitudesAceptActivity.this);
        peticion.execute("listarSolicitudesRep", json.toString());

        return v;
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response != null)
            for (int i = 0; i < response.length(); i++) {
                JSONObject row = response.getJSONObject(i);
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                Fragment fragment = new SolicitudesRepartidorDinamica(nombre + " " + apellidos, "1");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.linear_lista_solicitudes, fragment);
                fragmentTransaction.commit();
            }
    }
}