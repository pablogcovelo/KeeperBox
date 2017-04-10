package smartmailbox.keeperbox;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by regueiro on 13/03/17.
 */

public class SolicitarPermisoActivity extends Fragment implements Request {

    TextView informativo;
    String NFC;
    NfcAdapter nfcAdapter;
    EditText localizador_edittext, pais_edittext, ciudad_edittext, calle_edittext, numero_edittext, piso_edittext, letra_edittext, CP_edittext;
    JSONObject json_primero;


    public SolicitarPermisoActivity(String NFC) {
        this.NFC = NFC;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_solicitar_permiso, container, false);
        localizador_edittext = (EditText) v.findViewById(R.id.locBuzon_solicitarPermiso);
        pais_edittext = (EditText) v.findViewById(R.id.pais_solicitarPermiso);
        ciudad_edittext = (EditText) v.findViewById(R.id.ciudad_solicitarPermiso);
        calle_edittext = (EditText) v.findViewById(R.id.calle_solicitarPermiso);
        numero_edittext = (EditText) v.findViewById(R.id.numero_solicitarPermiso);
        piso_edittext = (EditText) v.findViewById(R.id.piso_solicitarPermiso);
        letra_edittext = (EditText) v.findViewById(R.id.letra_solicitarPermiso);
        CP_edittext = (EditText) v.findViewById(R.id.CP_solicitarPermiso);

        nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        //informativo = (TextView) v.findViewById(R.id.informativo_solici_perm);
        Button registrar = (Button) v.findViewById(R.id.buscar_solicitarPermiso);
        //TODO arreglar esto. Se ha cambiado de layout y enviar más cosillas
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                json_primero = new JSONObject();

                obtenerdatos("NFC",NFC);
                obtenerdatos("qlocalizador",localizador_edittext.getText().toString());
                obtenerdatos("qpais",pais_edittext.getText().toString());
                obtenerdatos("qciudad",ciudad_edittext.getText().toString());
                obtenerdatos("qcalle",calle_edittext.getText().toString());
                obtenerdatos("qnumero",numero_edittext.getText().toString());
                obtenerdatos("qpiso",piso_edittext.getText().toString());
                obtenerdatos("qletra",letra_edittext.getText().toString());
                obtenerdatos("qCP",CP_edittext.getText().toString());

                System.out.println("BuscarBuzon "+ json_primero);

                Peticion peticion = new Peticion(SolicitarPermisoActivity.this);
                peticion.execute("buscarBuzon", json_primero.toString());

            }
        });
        return v;
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response!=null)
            //TODO arreglar esto
            for (int i = 0; i < response.length(); i++) {
                JSONObject row = response.getJSONObject(i);
                String localizador_respuesta = row.getString("localizador");
                String pais_respuesta = row.getString("pais");
                String ciudad_respuesta = row.getString("ciudad");
                String calle_respuesta = row.getString("calle");
                String numero_respuesta = row.getString("numero");
                String piso_respuesta = row.getString("piso");
                String letra_respuesta = row.getString("letra");
                String CP_respuesta = row.getString("CP");
                Fragment fragment = new SolicitarPemisoIntermedia(NFC, localizador_respuesta, pais_respuesta, ciudad_respuesta,
                        calle_respuesta, numero_respuesta, piso_respuesta, letra_respuesta, CP_respuesta);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(Variable.tipo_propietario == 1){
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                }else if(Variable.tipo_propietario == 2){
                    fragmentTransaction.replace(R.id.content_frameRep, fragment);
                }
                fragmentTransaction.commit();

            }
    }

    public void obtenerdatos(String nombre, String valor) {
        try {
            if (valor.isEmpty()) {
                json_primero.put(nombre, JSONObject.NULL);
            } else {
                json_primero.put(nombre, valor);
            }

        } catch (JSONException e) {
            e.printStackTrace();


        }
    }
}