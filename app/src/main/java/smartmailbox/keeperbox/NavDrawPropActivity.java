package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
    String NFC;
    String localizador;
    String id_usuario ;
    String token_recibido;

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
                    System.out.println(json);
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

            if (inicio) {
                inicio = false;
                Fragment fragment = new SolicitudesPendActivity(NFC, localizador, id_usuario);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                getSupportActionBar().setTitle(getResources().getString(R.string.solicitudesPendientes));
            }

            navView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            boolean fragmentTransaction = false;
                            Fragment fragment = null;

                            switch (item.getItemId()) {
                                case R.id.solicitudesPendientes:
                                    fragment = new SolicitudesPendActivity(NFC, localizador, id_usuario);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.usuariospermitidos:
                                    fragment = new UsuariosPermitidosActivity(localizador);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.usuariosregistrados:
                                    fragment = new UsuariosRegistradosActivity(localizador);
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
                                    fragment = new AlertasActivity(NFC, localizador);
                                    fragmentTransaction = true;
                                    break;
                                case R.id.ajustes:
                                    fragment = new AjustesActivity(localizador);
                                    fragmentTransaction = true;
                                    break;
                            }


                            if (fragmentTransaction) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

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
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.solicitudesPendientes));
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        if(response !=null){
            JSONObject row = response.getJSONObject(0);
            if(row.getString("estado").equals("FALSE")){
                Log.d(TAG, "error en la actualización del Token");
            }
        }
    }
}
