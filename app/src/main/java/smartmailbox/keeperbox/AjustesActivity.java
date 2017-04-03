package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by regueiro on 3/04/17.
 */

class AjustesActivity extends Fragment {

    String localizador;
    String[] opciones = {"Localizador", "Cerrar Sesión"};

    public AjustesActivity(String localizador){
        this.localizador = localizador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  = inflater.inflate(R.layout.activity_ajustes, container, false);

      //  ListAdapter listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opciones);
        ListView listview = (ListView) v.findViewById(R.id.listview_ajustes);
        //listview.setAdapter(listadapter);

        return v;
    }
}
