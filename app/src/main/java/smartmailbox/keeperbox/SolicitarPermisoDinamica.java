package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by regueiro on 9/04/17.
 */

class SolicitarPermisoDinamica extends Fragment implements Request{

    String NFC, localizador_solicitado, pais, ciudad, calle, numero, piso, letra, CP;
    Button boton_buzon;

    public SolicitarPermisoDinamica(String NFC, String localizador_respuesta, String pais_respuesta, String ciudad_respuesta, String calle_respuesta, String numero_respuesta, String piso_respuesta, String letra_respuesta, String cp_respuesta) {

        this.NFC = NFC;
        this.localizador_solicitado = localizador_respuesta;
        this.pais = pais_respuesta;
        this.ciudad = ciudad_respuesta;
        this.calle = calle_respuesta;
        this.numero = numero_respuesta;
        this.piso = piso_respuesta;
        this.letra = letra_respuesta;
        this.CP = cp_respuesta;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dinamica_solicitar_permiso, container, false);
        TextView informacion_buzon = (TextView) v.findViewById(R.id.textview_solicitar_permiso);
        boton_buzon = (Button) v.findViewById(R.id.button_solicitar_permiso);
        String info_buzon = getResources().getString(R.string.localizador) + ": " +
                localizador_solicitado + "\n" + getResources().getString(R.string.pais) + ": " + pais
                + " " + getResources().getString(R.string.ciudad)+ ": " + ciudad + "\n" +
                getResources().getString(R.string.calle) +": " + calle + "\n" +
                getResources().getString(R.string.num_portal)+ ": " + numero + " " + getResources().getString(R.string.letra_piso)
                +": " + piso + " " + getResources().getString(R.string.letra_piso) + ": " + letra +
                getResources().getString(R.string.cod_postal) + ": " + CP;
        informacion_buzon.setText(info_buzon);

        boton_buzon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boton_buzon.setText(getResources().getText(R.string.enviando));
                boton_buzon.setEnabled(false);
                JSONObject json = new JSONObject();
                try {
                    json.put("qNFC", NFC);
                    json.put("qlocalizador", localizador_solicitado);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Peticion peticion = new Peticion(SolicitarPermisoDinamica.this);
                peticion.execute("nuevaPeticion", json.toString());
            }
        });

        return v;
    }


    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if(response != null){
            boton_buzon.setText(getResources().getText(R.string.enviado));
            boton_buzon.setEnabled(false);
        }

    }
}
