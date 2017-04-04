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
    private String localizador;

    public SolicitudesPendDinamica(String nombre, String NFC, String localizador){
        this.nombre = nombre;
        this.NFC = NFC;
        this.localizador = localizador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View InputFragmentView = inflater.inflate(R.layout.dinamica_soli_pend, container, false);
        Button rejectButton = (Button) InputFragmentView.findViewById(R.id.solpen_btn_reject);
        Button aceptButton = (Button) InputFragmentView.findViewById(R.id.solpen_btn_acept);

        rejectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Boton rechazar");
                resolverSolicitud(false);
            }
        });
        aceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Boton rechazar");
                resolverSolicitud(true);

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

    public void resolverSolicitud (Boolean aceptar) {

        JSONObject json =  new JSONObject();
        try {
            //IN idusuarioProp int, IN idbuzonConsultar int, IN qNFC varchar(10), IN acepRech boolean)
            json.put("idusuarioProp","1"); // TODO: cambiar por localizador ¿?
            json.put("idbuzonConsultar","1");
            json.put("qNFC","asd");
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
        Fragment fragment = new SolicitudesPendActivity(NFC, localizador);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).commit();
     }
}
