package smartmailbox.keeperbox;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by regueiro on 4/04/17.
 */

public class SolicitudesRepartidorDinamica extends Fragment {
    String nombre_apellidos;
    String permiso;
    public SolicitudesRepartidorDinamica(String s, String permiso) {
        this.nombre_apellidos = s;
        this.permiso = permiso;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_lista_solicitudes, container, false);
        TextView textView = (TextView) relativeLayout.findViewById(R.id.lista_solicitudes_textView);
        ImageView imageview = (ImageView) relativeLayout.findViewById(R.id.lista_solicitudes_imageView);
        if(permiso.equals("1")){
            imageview.setImageResource(R.drawable.imagen_verde);
        }else if(permiso.equals("2")){
            imageview.setImageResource(R.drawable.imagen_gris);
        }else if(permiso.equals("3")){
            imageview.setImageResource(R.drawable.imagen_roja);
        }

        textView.setText(nombre_apellidos);
        return relativeLayout;
    }
}
