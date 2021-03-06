package smartmailbox.keeperbox;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by regueiro on 13/03/17.
 */

public class SolicitudesPendActivity extends Fragment implements Request {
    private String NFC;
    private String localizador;
    private String id_usuario;

    public SolicitudesPendActivity(String NFC, String localizador, String id_usuario){
        this.NFC = NFC;
        this.localizador = localizador;
        this.id_usuario = id_usuario;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        JSONObject json =  new JSONObject();
        try {
            json.put("NFCConsulta",NFC);
            json.put("qlocalizador",localizador);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(SolicitudesPendActivity.this);
        peticion.execute("peticionesPendientes", json.toString());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_solipen_main, container, false);
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response!=null)
            for (int i = 0; i < response.length(); i++) {
                JSONObject row = response.getJSONObject(i);
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                String NFCpeticion = row.getString("NFC");
                String nombre_empresa = row.getString("nombre_empresa");
                String CIF = row.getString("CIF");
                String num_repartidor = row.getString("num_repartidor");
                Fragment fragment = new SolicitudesPendDinamica(nombre + " " + apellidos, NFC, NFCpeticion, localizador,
                        id_usuario, nombre_empresa, CIF, num_repartidor);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.linear_listPend, fragment);
                fragmentTransaction.commit();

            }
    }
}