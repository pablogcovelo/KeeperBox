package smartmailbox.keeperbox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by regueiro on 26/03/2017.
 */

public class UsuariosPermitidosDinamica extends Fragment implements Request{
    String localizador;
    String nombre;
    String NFC;
    ToggleButton toggleButton;
    @SuppressLint("ValidFragment")
    public UsuariosPermitidosDinamica(String localizador, String nombre, String NFC){
        this.localizador = localizador;
        this.nombre = nombre;
        this.NFC = NFC;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_usu_permi, container, false);
        TextView textView = (TextView) relativeLayout.findViewById(R.id.usupermi_textView);
        toggleButton = (ToggleButton) relativeLayout.findViewById(R.id.usupermi_toggleButton);
        toggleButton.setTag(NFC);
        toggleButton.setChecked(true);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton.setEnabled(false);
                if(toggleButton.isChecked()){
                    JSONObject json = new JSONObject();
                    try {
                        json.put("qlocalizador", localizador);
                        json.put("qNFC", NFC);
                        json.put("qpermiso", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("AQUI2"+json);
                    Peticion peticion = new Peticion(UsuariosPermitidosDinamica.this);
                    peticion.execute("manejoPermisos", json.toString());
                }else{
                    JSONObject json = new JSONObject();
                    try {
                        json.put("qlocalizador", localizador);
                        json.put("qNFC", NFC);
                        json.put("qpermiso", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("AQUI2"+json);
                    Peticion peticion = new Peticion(UsuariosPermitidosDinamica.this);
                    peticion.execute("manejoPermisos", json.toString());
                }
            }
        });
        textView.setText(nombre);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setLayoutParams(params);

        return relativeLayout;
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        toggleButton.setEnabled(true);
        String valido = response.getString(0);
        if(valido.contains("error")){
            String NFCerroneo = valido.split(":")[1];

            System.out.println("Ha ocurrido un error con el NFC: " + NFCerroneo);
        }
    }
}