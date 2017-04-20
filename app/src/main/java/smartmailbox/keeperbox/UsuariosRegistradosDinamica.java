package smartmailbox.keeperbox;

import android.os.Bundle;
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
 * Created by regueiro on 31/03/17.
 */

public class UsuariosRegistradosDinamica extends Fragment implements Request{

    String localizador;
    String nombre;
    String NFC;
    String permisos;
    String NFCPropietario;
    String tipo_usuario;
    String nombre_empresa;
    String CIF;
    String num_repartidor;
    ToggleButton toggleButton;

    public UsuariosRegistradosDinamica(String localizador, String nombre, String NFC, String permisos, String NFCPropietario,
                                       String tipo_usuario, String nombre_empresa, String CIF, String num_repartidor) {
        this.localizador = localizador;
        this.nombre = nombre;
        this.NFC = NFC;
        this.permisos = permisos;
        this.NFCPropietario = NFCPropietario;
        this.tipo_usuario = tipo_usuario;
        this.nombre_empresa = nombre_empresa;
        this.CIF = CIF;
        this.num_repartidor = num_repartidor;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_usu_permi, container, false);
        TextView textView = (TextView) relativeLayout.findViewById(R.id.usupermi_textView);
        toggleButton = (ToggleButton) relativeLayout.findViewById(R.id.usupermi_toggleButton);
        toggleButton.setTag(NFC);
        if(permisos.equals("1")){
            toggleButton.setChecked(true);
        }else{
            toggleButton.setChecked(false);
        }

        if(Variable.tipo_propietario < Integer.parseInt(tipo_usuario)){
            toggleButton.setEnabled(true);
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleButton.setEnabled(false);
                    if(toggleButton.isChecked()){
                        JSONObject json = new JSONObject();
                        try {
                            json.put("qlocalizador", localizador);
                            json.put("qNFC", NFC);
                            json.put("qNFCPropietario", NFCPropietario);
                            json.put("qpermiso", 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Peticion peticion = new Peticion(UsuariosRegistradosDinamica.this);
                        peticion.execute("manejoPermisos", json.toString());
                    }else{
                        JSONObject json = new JSONObject();
                        try {
                            json.put("qlocalizador", localizador);
                            json.put("qNFC", NFC);
                            json.put("qNFCPropietario", NFCPropietario);
                            json.put("qpermiso", 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Peticion peticion = new Peticion(UsuariosRegistradosDinamica.this);
                        peticion.execute("manejoPermisos", json.toString());
                    }
                }
            });

        }else{
            toggleButton.setEnabled(false);
        }

        if(nombre_empresa.isEmpty() || CIF.isEmpty() || num_repartidor.isEmpty()){
            textView.setText(nombre);
        }else{
            textView.setText(nombre + "\n" + getResources().getString(R.string.empresa) + ": " + nombre_empresa +
                    " " + getResources().getString(R.string.cif) + ": " + CIF + "\n" + getResources().getString(R.string.num_repartidor)+
                    ": " + num_repartidor);
        }


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setLayoutParams(params);

        return relativeLayout;
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        toggleButton.setEnabled(true);
        JSONObject row = response.getJSONObject(0);
        String NFCerroneo = row.getString("error");
        if (!NFCerroneo.contains("correcto")) {

            System.out.println("Ha ocurrido un error con el NFC: " + NFCerroneo);
        }
    }
}
