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

public class NavDrawActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar appbar;
    boolean inicio = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navView = (NavigationView) findViewById(R.id.navview);

        if(inicio){
            inicio = false;
            //Fragment fragment = new SolicitudesPendActivity();SolicitudesPendDinamica
            //Fragment fragment = new SolicitudesPendDinamica();
            Fragment fragment = new SolicitudesPendActivity();
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
                                fragment = new SolicitudesPendActivity();
                                fragmentTransaction = true;
                                break;
                            case R.id.usuariospermitidos:
                                fragment = new UsuariosPermitidosActivity();
                                fragmentTransaction = true;
                                break;
                            case R.id.usuariosregistrados:
                                fragment = new UsuariosRegistradosActivity();
                                fragmentTransaction = true;
                                break;
                            case R.id.historialacceso:
                                fragment = new HistorialAccesoActivity();
                                fragmentTransaction = true;
                                break;
                            case R.id.registrar_nuevousuario:
                               //fragment = new Fragment5();
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
        Fragment fragment = new SolicitudesPendActivity();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.solicitudesPendientes));
    }

}
