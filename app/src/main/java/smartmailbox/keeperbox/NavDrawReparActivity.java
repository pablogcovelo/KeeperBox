package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by regueiro on 13/03/17.
 */

public class NavDrawReparActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar appbar;
    boolean inicio = true;
    String localizador = "cdefghij";
    String NFC = "7890asdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbarrep);

        appbar = (Toolbar) findViewById(R.id.appbarRep);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutRep);
        navView = (NavigationView) findViewById(R.id.navviewRep);

        if (inicio) {
            inicio = false;
            Fragment fragment = new SolicitarPermisoActivity();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frameRep, fragment).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.solicitar_acceso));
        }

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (item.getItemId()) {
                            case R.id.solicitar_permiso:
                                fragment = new SolicitarPermisoActivity();
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
                                fragment = new MapaRutasActivity();
                                fragmentTransaction = true;
                                break;
                            case R.id.alertas:
                                //fragment = new AlertasActivity(NFC, localizador);
                                fragmentTransaction = true;
                                break;
                            case R.id.ajustes:
                                fragment = new AjustesReparActivity();
                                fragmentTransaction = true;
                                break;
                        }

                        if (fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frameRep, fragment).commit();

                            item.setChecked(true);
                            getSupportActionBar().setTitle(item.getTitle());
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }

                }
        );


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
        Fragment fragment = new SolicitarPermisoActivity();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frameRep, fragment).commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.solicitud_acceso));
    }

}
