package smartmailbox.keeperbox;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jose on 31/03/2017.
 */

public class HistorialAccesoDinamica extends Fragment {
    String nombre;
    String fecha;

    public HistorialAccesoDinamica(String nombre, String fecha){
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_historial_acceso, container, false);
        TextView textViewNombre = (TextView) relativeLayout.findViewById(R.id.historial_nombre);
        textViewNombre.setText(nombre);
        TextView textViewFecha = (TextView) relativeLayout.findViewById(R.id.historial_fecha);
        textViewFecha.setText(fecha);

        //layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setPadding(5, 3, 5, 3);
        relativeLayout.setLayoutParams(params);

        // Inflate the layout for this fragment
        return relativeLayout;//inflater.inflate(R.layout.dinamica_soli_pend, container, false);
    }
}
