package smartmailbox.keeperbox;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by regueiro on 13/03/17.
 */

public class NavDrawPropActivity extends AppCompatActivity implements Request{

    private static final String TAG = "KeeperBox";
    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar appbar;
    boolean inicio = true;

    private JSONObject parametros;
    private String NFC;
    private String localizador;
    private String id_usuario ;
    private String token_recibido;
    private String nombre;
    private String apellidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbarprop);

        // Parametros
        String datos = getIntent().getExtras().getString("datos");
        try {
            if (datos != null) {

                parametros = new JSONObject(datos);
                NFC = parametros.getString("NFC");
                localizador = parametros.getString("localizador");
                id_usuario = parametros.getString("id_usuario");
                token_recibido = parametros.getString("token");
                nombre = parametros.getString("nombre");
                apellidos = parametros.getString("apellidos");
                Variable.tipo_propietario = Integer.parseInt(parametros.getString("tipo_usuario"));

                Variable.TOKEN = FirebaseInstanceId.getInstance().getToken();
                if(!token_recibido.equals(Variable.TOKEN)){
                    Log.d(TAG, Variable.TOKEN);
                    JSONObject json =  new JSONObject();
                    try {
                        json.put("qNFC",NFC);
                        json.put("qtoken",Variable.TOKEN);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Peticion peticion = new Peticion(NavDrawPropActivity.this);
                    peticion.execute("actualizarToken", json.toString());
                }
            }

            appbar = (Toolbar) findViewById(R.id.appbar);
            setSupportActionBar(appbar);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            navView = (NavigationView) findViewById(R.id.navview);

            consultarAlertas();

            if (inicio) {
                inicio = false;
                Fragment fragment = new SolicitudesPendActivity(NFC, localizador, id_usuario);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                getSupportActionBar().setTitle(getResources().getString(R.string.solicitudesPendientes));
            }

            navView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            TextView textView = (TextView) findViewById(R.id.nombre_propietario_header);
                            textView.setText(nombre + " " + apellidos);
                            consultarAlertas();
                            boolean fragmentTransaction = false;
                            Fragment fragment = null;

                            switch (item.getItemId()) {
                                case R.id.solicitudesPendientes:
                                    fragment = new SolicitudesPendActivity(NFC, localizador, id_usuario);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.usuariospermitidos:
                                    fragment = new UsuariosPermitidosActivity(NFC, localizador);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.usuariosregistrados:
                                    fragment = new UsuariosRegistradosActivity(NFC, localizador);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.historialacceso:
                                    fragment = new HistorialAccesoActivity(localizador);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.registrar_nuevousuario:
                                    fragment = new SolicitarPermisoActivity(NFC);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.alertas:
                                    fragment = new AlertasActivity(NFC, localizador, id_usuario);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.ajustes:
                                    fragment = new AjustesActivity(localizador);
                                    fragmentTransaction = true;
                                    break;
                            }

                            if (fragmentTransaction) {
                                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

                                item.setChecked(true);
                                getSupportActionBar().setTitle(item.getTitle());
                            }

                            drawerLayout.closeDrawers();
                            return true;
                        }

                    }
            );
        }catch (JSONException e) {
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

    public void onBackPressed(){
        Fragment fragment = new SolicitudesPendActivity(NFC, localizador, id_usuario);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.solicitudesPendientes));
    }

    public void consultarAlertas(){
        JSONObject json =  new JSONObject();
        try {
            json.put("NFCConsulta", NFC);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(NavDrawPropActivity.this);
        peticion.execute("listaAlertas", json.toString());
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        boolean esavisos = false;
        if (response!=null) {
            for (int i = 0; i < response.length(); i++) {

                try{
                    JSONObject row = response.getJSONObject(i);
                    String mensaje_alerta = row.getString("mensaje_alerta");
                    if(!mensaje_alerta.isEmpty() || !mensaje_alerta.equals("null")){
                        esavisos = true;
                    }
                    break;
                }catch (JSONException e){
                }
            }
            if (esavisos) {
                setMenuCounter(R.id.alertas, response.length());
            }
        }else{
            setMenuCounter(R.id.alertas, 0);
        }
    }

    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navView.getMenu().findItem(itemId).getActionView();
        if(count == 0){
            view.setVisibility(View.INVISIBLE);
            view.setText("");
        }else{
            view.setVisibility(View.VISIBLE);
            view.setText(Integer.toString(count));
        }

    }
}
