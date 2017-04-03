package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by regueiro on 13/03/17.
 */

public class HistorialAccesoActivity extends Fragment implements Request {
    private String intervalo = "week";
    private String localizador;

    public HistorialAccesoActivity(String localizador){
        this.localizador = localizador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View InputFragmentView = inflater.inflate(R.layout.activity_historialacceso, container, false);
        Button monthButton = (Button) InputFragmentView.findViewById(R.id.btn_month);
        Button weekButton = (Button) InputFragmentView.findViewById(R.id.btn_week);

        monthButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { intervalo = "month"; limpiar(); hacerPeticion(); }
        });
        weekButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { intervalo = "week"; limpiar(); hacerPeticion(); }
        });
        hacerPeticion();

        // Inflate the layout for this fragment
        return InputFragmentView;
    }

    public void hacerPeticion() {
        JSONObject json =  new JSONObject();
        try {
            json.put("localizador",localizador);
            json.put("intervalo",intervalo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Peticion peticion = new Peticion(HistorialAccesoActivity.this);
        peticion.execute("listaRegistros", json.toString());
    }

    public void limpiar() {
        System.out.println("*************Hola aqui");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new HistorialAccesoDinamica("", "");

        fragmentTransaction.replace(R.id.linear_historial, new Fragment());
        fragmentTransaction.commit();
        ///int id = fragmentManager.getBackStackEntryCount();//getBackStackEntryAt().getId()
        /*for(int i = 0; i < fragmentManager.getFragments().size(); ++i) {
            System.out.println("*************Hola 00"+i);
            fragmentManager. .remo(fragmentManager.getBackStackEntryAt(i).getId(), fragmentManager.POP_BACK_STACK_INCLUSIVE);
        }*/
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        System.out.println("*** AQUI 300***");
        if (response!=null)
            for (int i = 0; i < response.length(); i++) {
                JSONObject row = response.getJSONObject(i);
                String nombre = row.getString("nombre");
                String apellidos = row.getString("apellidos");
                String fecha = row.getString("fecha");
                Fragment fragment = new HistorialAccesoDinamica(nombre + " " + apellidos, fecha);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.linear_historial, fragment);
                fragmentTransaction.commit();

            }
    }
}