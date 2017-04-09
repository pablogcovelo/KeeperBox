package smartmailbox.keeperbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.Locale;

/**
 * Created by regueiro on 4/04/17.
 */

public class AjustesReparActivity extends Fragment {

    public AjustesReparActivity() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_ajustes, container, false);

        String[] lista_objetos = {getResources().getString(R.string.idioma),
                getResources().getString(R.string.cerrar_sesion)};

        ListView lista = (ListView) v.findViewById(R.id.ajustes_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, lista_objetos);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int posicion = position;
                switch (posicion) {
                    case 0:
                        final CharSequence[] items = {getString(R.string.ingles), getString(R.string.castellano),
                                getString(R.string.gallego)};

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(getString(R.string.elige_idioma));
                        Resources res = getActivity().getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        android.content.res.Configuration conf = res.getConfiguration();
                        builder.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Locale idioma = new Locale("es", "ES");
                                switch (item) {
                                    case 0:
                                        conf.setLocale(new Locale("en"));
                                        break;
                                    case 1:
                                        conf.setLocale(new Locale("es"));
                                        break;
                                    case 2:
                                        conf.setLocale(new Locale("gl"));
                                        break;
                                }
                                res.updateConfiguration(conf, dm);
                               /* Locale.setDefault(idioma);
                                Configuration config = new Configuration();
                                config.locale = idioma;
                                getActivity().getBaseContext().getResources().updateConfiguration(config,
                                        getActivity().getBaseContext().getResources().getDisplayMetrics());*/
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    case 1:
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