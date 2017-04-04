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
 * Created by jopea on 02/04/2017.
 */

public class AlertasDinamica extends Fragment {
    String nombre;

    public AlertasDinamica(String nombre){
        this.nombre = nombre;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_alertas, container, false);
        TextView textViewNombre = (TextView) relativeLayout.findViewById(R.id.alertas_nombre);
        textViewNombre.setText(nombre);

        //layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setPadding(5, 3, 5, 3);
        relativeLayout.setLayoutParams(params);

        // Inflate the layout for this fragment
        return relativeLayout;
    }
}
