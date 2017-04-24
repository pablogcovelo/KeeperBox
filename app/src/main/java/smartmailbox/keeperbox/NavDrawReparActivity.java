package smartmailbox.keeperbox;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by regueiro on 13/03/17.
 */

public class NavDrawReparActivity extends AppCompatActivity implements Request {

    private static final String TAG = "KeeperBox";
    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar appbar;
    boolean inicio = true;
    private JSONObject parametros;
    String NFC;
    String token_recibido;
    private String nombre;
    private String apellidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbarrep);
        String datos = getIntent().getExtras().getString("datos");
        try {
            if (datos != null) {

                parametros = new JSONObject(datos);
                NFC = parametros.getString("NFC");
                token_recibido = parametros.getString("token");
                nombre = parametros.getString("nombre");
                apellidos = parametros.getString("apellidos");
                Variable.tipo_propietario = Integer.parseInt(parametros.getString("tipo_usuario"));

                Variable.TOKEN = FirebaseInstanceId.getInstance().getToken();
                if (!token_recibido.equals(Variable.TOKEN)) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("qNFC", NFC);
                        json.put("qtoken", Variable.TOKEN);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Peticion peticion = new Peticion(NavDrawReparActivity.this);
                    peticion.execute("actualizarToken", json.toString());
                }
            }

            appbar = (Toolbar) findViewById(R.id.appbarRep);
            setSupportActionBar(appbar);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutRep);
            navView = (NavigationView) findViewById(R.id.navviewRep);

            consultarAlertas();

            if (inicio) {
                inicio = false;
                Fragment fragment = new SolicitarPermisoActivity(NFC);
                getFragmentManager().beginTransaction().replace(R.id.content_frameRep, fragment).commit();
                getSupportActionBar().setTitle(getResources().getString(R.string.solicitar_acceso));
            }

            navView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            TextView textView = (TextView) findViewById(R.id.nombre_repartidor_header);
                            textView.setText(nombre + " " + apellidos);
                            boolean fragmentTransaction = false;
                            Fragment fragment = null;
                            consultarAlertas();

                            switch (item.getItemId()) {
                                case R.id.solicitar_permiso:
                                    fragment = new SolicitarPermisoActivity(NFC);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.listar_solicitudes:
                                    fragment = new ListaSolicitudesActivity(NFC);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.solicitudes_aceptadas:
                                    fragment = new SolicitudesAceptActivity(NFC);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.solicitudes_pendientes:
                                    fragment = new SolicitudesPendReparActivity(NFC);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.solicitudes_rechazadas:
                                    fragment = new SolicitudesRechActivity(NFC);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.mapa_rutas:
                                    fragment = new MapsActivity(NFC);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.alertas:
                                    fragment = new AlertasActivity(NFC, null, null);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.ajustes:
                                    fragment = new AjustesReparActivity();
                                    fragmentTransaction = true;
                                    break;
                            }

                            if (fragmentTransaction) {
                                getFragmentManager().beginTransaction().replace(R.id.content_frameRep, fragment).commit();
                                item.setChecked(true);
                                getSupportActionBar().setTitle(item.getTitle());
                            }

                            drawerLayout.closeDrawers();
                            return true;
                        }

                    }
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Fragment fragment = new SolicitarPermisoActivity(NFC);
        getFragmentManager().beginTransaction().replace(R.id.content_frameRep, fragment).commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.solicitud_acceso));
    }

    public void consultarAlertas(){
        JSONObject json =  new JSONObject();
        try {
            json.put("NFCConsulta", NFC);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(NavDrawReparActivity.this);
        peticion.execute("listaAlertas", json.toString());
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if (response != null) {
            JSONObject row = response.getJSONObject(0);
        }
    }
}
