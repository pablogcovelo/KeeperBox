package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by regueiro on 26/03/2017.
 */

public class UsuariosPermitidosDinamica extends Fragment {
    String nombre;

    public UsuariosPermitidosDinamica(String nombre){
        this.nombre = nombre;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_usu_permi, container, false);
        TextView textView = (TextView) relativeLayout.findViewById(R.id.usupermi_textView);
        textView.setText(nombre);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setPadding(5, 3, 5, 3);
        relativeLayout.setLayoutParams(params);

        return relativeLayout;
    }
}