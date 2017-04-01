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

public class UsuariosPermitidosActivity extends Fragment implements Request {

    String cod_buzon = "asdf1234";

    public UsuariosPermitidosActivity() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        JSONObject json = new JSONObject();
        try {
            json.put("id_buzon", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(UsuariosPermitidosActivity.this);
        peticion.execute("usuariosPermitidos", json.toString());

        return inflater.inflate(R.layout.activity_usuariospermit, container, false);
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException{
        String valido = null;

        if (response != null)
            for (int i = 0; i < response.length(); i++) {
                JSONObject row = response.getJSONObject(i);
                String NFC = row.getString("NFC");
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                Fragment fragment = new UsuariosPermitidosDinamica(NFC + " " + nombre + " " + apellidos);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.linear_usu_permi, fragment);
                fragmentTransaction.commit();
            }

    }
}
