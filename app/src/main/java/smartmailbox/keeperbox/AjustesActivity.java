package smartmailbox.keeperbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by regueiro on 3/04/17.
 */

class AjustesActivity extends Fragment{

    String localizador;
    String[] lista_objetos = {getResources().getString(R.string.localizador),
            getResources().getString(R.string.idioma),
            getResources().getString(R.string.cerrar_sesion)};

    public AjustesActivity(String localizador) {
        this.localizador = localizador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_ajustes, container, false);
        ListView lista = (ListView) v.findViewById(R.id.ajustes_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, lista_objetos);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int posicion = position;
                String itemValue = (String) lista.getItemAtPosition(posicion);
                switch (view.getId()){
                    case R.string.localizador:
                        AlertDialog.Builder builder_localizador = new AlertDialog.Builder(getContext());
                        builder_localizador.setMessage(getString(R.string.mensaje_localizador) + "\n" + localizador)
                                .setTitle(getString(R.string.cabecera_localizador))
                                .setCancelable(false)
                                .setNeutralButton(getString(R.string.accept),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert_localizador = builder_localizador.create();
                        alert_localizador.show();
                        break;
                    case R.string.idioma:
                        final CharSequence[] items = {getString(R.string.ingles), getString(R.string.castellano),
                            getString(R.string.gallego)};

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(getString(R.string.elige_idioma));
                        builder.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Locale idioma = new Locale("es","ES");
                                switch (item){
                                    case 0:
                                        idioma = new Locale("en", "EN");
                                        break;
                                    case 1:
                                        idioma = new Locale("es", "ES");
                                        break;
                                    case 2:
                                        idioma = new Locale("gl", "GL");
                                        break;
                                }
                                Locale.setDefault(idioma);
                                Configuration config = new Configuration();
                                config.locale = idioma;
                                getActivity().getBaseContext().getResources().updateConfiguration(config,
                                        getActivity().getBaseContext().getResources().getDisplayMetrics());
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    case R.string.cerrar_sesion:
                        AlertDialog.Builder builder_sesion = new AlertDialog.Builder(getContext());
                        builder_sesion.setMessage(getString(R.string.mensaje_cierre_sesion))
                                .setTitle(getString(R.string.advertencia))
                                .setCancelable(false)
                                .setNegativeButton(getString(R.string.cancelar),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        })
                                .setPositiveButton(getString(R.string.continuar),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                getActivity().finish();
                                            }
                                        });
                        AlertDialog alert_sesion = builder_sesion.create();
                        alert_sesion.show();
                        break;
                }
            }
        });

        return v;
    }

}
