package smartmailbox.keeperbox;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jopea on 02/04/2017.
 */

public class AlertasDinamica extends Fragment implements Request {
    private String mensaje;
    private String NFC;
    private String localizador;
    private String id_usuario;
    Fragment fragment = null;

    public AlertasDinamica(String mensaje, String NFC, String localizador, String id_usuario) {
        this.mensaje = mensaje;
        this.NFC = NFC;
        this.localizador = localizador;
        this.id_usuario = id_usuario;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_alertas, container, false);
        TextView textViewNombre = (TextView) relativeLayout.findViewById(R.id.alertas_nombre);

        switch (mensaje) {
            case "Un usuario acaba de abrir su buzón.":
                textViewNombre.setText(R.string.usuario_abre_buzon);
                textViewNombre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment = new HistorialAccesoActivity(localizador);
                        realizar_consulta("0");
                    }
                });
                break;
            case "Una solicitud pendiente ha sido aprobada.":
                textViewNombre.setText(R.string.solicitud_pen_acep);
                textViewNombre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment = new SolicitudesAceptActivity(NFC);
                        realizar_consulta("1");
                    }
                });
                break;
            case "Una solicitud pendiente ha sido denegada.":
                textViewNombre.setText(R.string.solicitud_pen_dene);
                textViewNombre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment = new SolicitudesRechActivity(NFC);
                        realizar_consulta("2");
                    }
                });
                break;
            case "Nueva solicitud de acceso al buzón.":
                textViewNombre.setText(R.string.nueva_sol_buzon);
                textViewNombre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment = new SolicitudesPendActivity(NFC, localizador, id_usuario);
                        realizar_consulta("3");
                    }
                });
                break;
            case "Se le ha devuelto el permiso para acceder a un buzón":
                textViewNombre.setText(R.string.devuelto_permiso);
                textViewNombre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment = new SolicitudesAceptActivity(NFC);
                        realizar_consulta("1");
                    }
                });
                break;
            case "Se le ha denegado el permiso para acceder a un buzón":
                textViewNombre.setText(R.string.denegado_permiso);
                textViewNombre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment = new SolicitudesRechActivity(NFC);
                        realizar_consulta("2");
                    }
                });
                break;
            case "Se ha registrado un nuevo usuario en el buzón.":
                textViewNombre.setText(R.string.registrado_nuevo_usuario);
                textViewNombre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment = new UsuariosPermitidosActivity(NFC, localizador);
                        realizar_consulta("4");
                    }
                });
                break;
        }
        if (fragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        //layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setPadding(5, 3, 5, 3);
        relativeLayout.setLayoutParams(params);

        // Inflate the layout for this fragment
        return relativeLayout;
    }

    public void realizar_consulta(String tipo) {
        if (!tipo.equals("") || !tipo.isEmpty()) {
            JSONObject json = new JSONObject();
            try {
                json.put("NFCConsulta", NFC);
                json.put("tipo_notificacion", tipo);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Peticion peticion = new Peticion(AlertasDinamica.this);
            peticion.execute("borrarAlertas", json.toString());
        }
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response != null) {

        }
    }
}
