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

public class UsuariosRegistradosActivity extends Fragment implements Request{

    String localizador;
    String NFCPropietario;

    public UsuariosRegistradosActivity(String NFC, String localizador){
        this.localizador = localizador;
        this.NFCPropietario = NFC;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        JSONObject json = new JSONObject();
        try {
            json.put("localizador",localizador);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(UsuariosRegistradosActivity.this);
        peticion.execute("todosUsuarios", json.toString());

        return inflater.inflate(R.layout.activity_usuariosregistrados, container, false);
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response != null)
            for (int i = 0; i < response.length(); i++) {
                JSONObject row = response.getJSONObject(i);
                String NFC = row.getString("NFC");
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                String permisos = row.getString("permiso");
                String tipo_usuario = row.getString("tipo_usuario");
                String nombre_empresa = row.getString("nombre_empresa");
                String CIF = row.getString("CIF");
                String num_repartidor = row.getString("num_repartidor");
                Fragment fragment = new UsuariosRegistradosDinamica(localizador,nombre + " " + apellidos, NFC, permisos,
                        NFCPropietario, tipo_usuario, nombre_empresa, CIF, num_repartidor);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.linear_usu_registrados, fragment);
                fragmentTransaction.commit();
            }
    }
}
