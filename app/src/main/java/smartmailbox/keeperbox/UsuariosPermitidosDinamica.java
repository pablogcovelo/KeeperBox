package smartmailbox.keeperbox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Fragment;
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

public class UsuariosPermitidosDinamica extends Fragment implements Request {
    String localizador;
    String nombre;
    String NFC;
    private String NFCPropietario;
    private String tipo_usuario;
    ToggleButton toggleButton;

    @SuppressLint("ValidFragment")
    public UsuariosPermitidosDinamica(String localizador, String nombre, String NFC, String NFCPropietario, String tipo_usuario) {
        this.localizador = localizador;
        this.nombre = nombre;
        this.NFC = NFC;
        this.NFCPropietario = NFCPropietario;
        this.tipo_usuario = tipo_usuario;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_usu_permi, container, false);
        TextView textView = (TextView) relativeLayout.findViewById(R.id.usupermi_textView);
        toggleButton = (ToggleButton) relativeLayout.findViewById(R.id.usupermi_toggleButton);
        toggleButton.setTag(NFC);
        if (Variable.tipo_propietario < Integer.parseInt(tipo_usuario)) {
            toggleButton.setEnabled(true);
            toggleButton.setChecked(true);
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleButton.setEnabled(false);
                    if (toggleButton.isChecked()) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("qlocalizador", localizador);
                            json.put("qNFC", NFC);
                            json.put("qNFCPropietario", NFCPropietario);
                            json.put("qpermiso", 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Peticion peticion = new Peticion(UsuariosPermitidosDinamica.this);
                        peticion.execute("manejoPermisos", json.toString());
                    } else {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("qlocalizador", localizador);
                            json.put("qNFC", NFC);
                            json.put("qNFCPropietario", NFCPropietario);
                            json.put("qpermiso", 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Peticion peticion = new Peticion(UsuariosPermitidosDinamica.this);
                        peticion.execute("manejoPermisos", json.toString());
                    }
                }
            });
        } else {
            toggleButton.setChecked(true);
            toggleButton.setEnabled(false);
        }
        textView.setText(nombre);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setLayoutParams(params);

        return relativeLayout;
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if(response != null) {
            toggleButton.setEnabled(true);
            JSONObject row = response.getJSONObject(0);
            String NFCerroneo = row.getString("error");
            if (!NFCerroneo.contains("correcto")) {

                System.out.println("Ha ocurrido un error con el NFC: " + NFCerroneo);
            }
        }
    }
}