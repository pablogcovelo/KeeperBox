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

public class SolicitarPermisoActivity extends Fragment implements Request{

    TextView informativo;
    String NFC;
    NfcAdapter nfcAdapter;

    public SolicitarPermisoActivity(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_solici_perm, container, false);
        nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        EditText localizador_solicitado = (EditText) v.findViewById(R.id.editText_solici_perm);
        informativo = (TextView) v.findViewById(R.id.informativo_solici_perm);
        Button registrar = (Button) v.findViewById(R.id.button_solici_perm);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nfcAdapter != null && nfcAdapter.isEnabled()){
                   // if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getActivity().getIntent().getAction())) {
                        Tag tag = getActivity().getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
                        NFC = tag.getId().toString();
                  //  }
                    JSONObject json = new JSONObject();
                    try {
                        json.put("qNFC",NFC);
                        json.put("qlocalizador",localizador_solicitado.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Peticion peticion = new Peticion(SolicitarPermisoActivity.this);
                    peticion.execute("nuevaPeticion", json.toString());
                }else{
                    startNfcSettingsActivity();
                }
            }
        });

        return v;
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response != null) {
            String valido = response.getString(0);
            if (valido.contains("error")) {
                informativo.setText(getResources().getString(R.string.error_registro_local_erroneo));
            }
        }
    }

    public void startNfcSettingsActivity() {
        Toast.makeText(getContext(), getResources().getString(R.string.activar_nfc), Toast.LENGTH_LONG).show();
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }
}