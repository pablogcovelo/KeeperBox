package smartmailbox.keeperbox;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jopea on 02/04/2017.
 */

public class AlertasActivity extends Fragment implements Request {
    private String NFC;
    private String localizador;
    private String id_usuario;


    public AlertasActivity(String NFC, String localizador, String id_usuario){
        this.NFC = NFC;
        this.localizador = localizador;
        this.id_usuario = id_usuario;

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        JSONObject json =  new JSONObject();
        try {
            json.put("NFCConsulta", NFC);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(AlertasActivity.this);
        peticion.execute("listaAlertas", json.toString());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_alertas, container, false);
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response!=null)
            for (int i = 0; i < response.length(); i++) {
                JSONObject row = response.getJSONObject(i);
                String mensaje = row.getString("mensaje_alerta");
                Fragment fragment = new AlertasDinamica(mensaje, NFC, localizador, id_usuario);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.linear_alertas, fragment);
                fragmentTransaction.commit();
            }
    }
}
