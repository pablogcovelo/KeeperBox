package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jose on 26/03/2017.
 */

public class SolicitudesPendDinamica extends Fragment implements Request{
    private String nombre;
    private String NFC;
    private String NFCpeticion;
    private String localizador;
    private String id_usuario;

    public SolicitudesPendDinamica(String nombre, String NFC, String NFCpeticion, String localizador, String id_usuario){
        this.nombre = nombre;
        this.NFC = NFC;
        this.NFCpeticion = NFCpeticion;
        this.localizador = localizador;
        this.id_usuario = id_usuario;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View InputFragmentView = inflater.inflate(R.layout.dinamica_soli_pend, container, false);
        Button rejectButton = (Button) InputFragmentView.findViewById(R.id.solpen_btn_reject);
        Button aceptButton = (Button) InputFragmentView.findViewById(R.id.solpen_btn_acept);

        rejectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Boton rechazar");
                resolverSolicitud("2");
            }
        });
        aceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Boton rechazar");
                resolverSolicitud("1");

            }
        });
        TextView textView = (TextView) InputFragmentView.findViewById(R.id.solpen_text);
        textView.setText(nombre);

        //layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        InputFragmentView.setPadding(5, 3, 5, 3);
        InputFragmentView.setLayoutParams(params);

        // Inflate the layout for this fragment
        return InputFragmentView;
    }

    public void resolverSolicitud (String aceptar) {
        JSONObject json =  new JSONObject();
        try {
            json.put("qNFC",NFC);
            json.put("qlocalizador",localizador);
            json.put("qNFCPeticion",NFCpeticion);
            json.put("acepRech",aceptar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(SolicitudesPendDinamica.this);
        peticion.execute("resolverPetPend", json.toString());
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        System.out.println("*** AQUI 300***");
        Fragment fragment = new SolicitudesPendActivity(NFC, localizador, id_usuario);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).commit();
    }
}