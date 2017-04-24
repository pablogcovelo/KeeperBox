package smartmailbox.keeperbox;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
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

public class SolicitudesPendDinamica extends Fragment implements Request {
    private String nombre;
    private String NFC;
    private String NFCpeticion;
    private String localizador;
    private String id_usuario;
    private String nombre_empresa;
    private String CIF;
    private String num_repartidor;

    public SolicitudesPendDinamica(String nombre, String NFC, String NFCpeticion, String localizador, String id_usuario, String nombre_empresa, String CIF, String num_repartidor) {
        this.nombre = nombre;
        this.NFC = NFC;
        this.NFCpeticion = NFCpeticion;
        this.localizador = localizador;
        this.id_usuario = id_usuario;
        this.nombre_empresa = nombre_empresa;
        this.CIF = CIF;
        this.num_repartidor = num_repartidor;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View InputFragmentView = inflater.inflate(R.layout.dinamica_soli_pend, container, false);
        Button rejectButton = (Button) InputFragmentView.findViewById(R.id.solpen_btn_reject);
        Button aceptButton = (Button) InputFragmentView.findViewById(R.id.solpen_btn_acept);

        rejectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resolverSolicitud("2");
            }
        });
        aceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resolverSolicitud("1");

            }
        });
        TextView textView = (TextView) InputFragmentView.findViewById(R.id.solpen_text);
        if (nombre_empresa.isEmpty() || CIF.isEmpty() || num_repartidor.isEmpty()) {
            textView.setText(nombre);
        } else {
            textView.setText(nombre + "\n" + getResources().getString(R.string.nombre_empresa) + ": " + nombre_empresa +
                    "\n" + getResources().getString(R.string.cif_empresa) + ": " + CIF + "\n" + getResources().getString(R.string.num_rapartidor) +
                    ": " + num_repartidor);
        }


        //layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        InputFragmentView.setPadding(5, 3, 5, 3);
        InputFragmentView.setLayoutParams(params);

        // Inflate the layout for this fragment
        return InputFragmentView;
    }

    public void resolverSolicitud(String aceptar) {
        JSONObject json = new JSONObject();
        try {
            json.put("qNFC", NFC);
            json.put("qlocalizador", localizador);
            json.put("qNFCPeticion", NFCpeticion);
            json.put("acepRech", aceptar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Peticion peticion = new Peticion(SolicitudesPendDinamica.this);
        peticion.execute("resolverPetPend", json.toString());
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response != null) {
            Fragment fragment = new SolicitudesPendActivity(NFC, localizador, id_usuario);
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment).commit();
        }
    }
}