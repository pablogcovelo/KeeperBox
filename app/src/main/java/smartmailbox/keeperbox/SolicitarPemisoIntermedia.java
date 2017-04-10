package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by regueiro on 9/04/17.
 */

public class SolicitarPemisoIntermedia extends Fragment {

    String NFC,localizador_respuesta, pais_respuesta,ciudad_respuesta,calle_respuesta,numero_respuesta,piso_respuesta,letra_respuesta,cp_respuesta;

    public SolicitarPemisoIntermedia(String nfc, String localizador_respuesta, String pais_respuesta, String ciudad_respuesta, String calle_respuesta, String numero_respuesta, String piso_respuesta, String letra_respuesta, String cp_respuesta) {
        this.NFC = nfc;
        this.localizador_respuesta = localizador_respuesta;
        this.pais_respuesta = pais_respuesta;
        this.ciudad_respuesta = ciudad_respuesta;
        this.calle_respuesta = calle_respuesta;
        this.numero_respuesta = numero_respuesta;
        this.piso_respuesta = piso_respuesta;
        this.letra_respuesta = letra_respuesta;
        this.cp_respuesta = cp_respuesta;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_solipen_main, container, false);

        rellenar();

        return v;
    }

    public void rellenar(){
        Fragment fragment = new SolicitarPermisoDinamica(NFC,localizador_respuesta, pais_respuesta, ciudad_respuesta,
                calle_respuesta,numero_respuesta,piso_respuesta, letra_respuesta,cp_respuesta);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linear_listPend, fragment);
        fragmentTransaction.commit();
    }

    public void onBackPressed(){
        Fragment fragment = new SolicitarPermisoActivity(NFC);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(Variable.tipo_propietario == 1){
            fragmentTransaction.replace(R.id.content_frame, fragment);
        }else if(Variable.tipo_propietario == 2){
            fragmentTransaction.replace(R.id.content_frameRep, fragment);
        }
    }
}
