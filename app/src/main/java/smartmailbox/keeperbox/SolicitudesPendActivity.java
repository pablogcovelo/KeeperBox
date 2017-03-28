package smartmailbox.keeperbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by regueiro on 13/03/17.
 */

public class SolicitudesPendActivity extends Fragment implements Request {
    FragmentActivity mContext;
    ViewGroup layout;
    LayoutInflater linflater;
    FrameLayout v;

    public SolicitudesPendActivity(){
        JSONObject json =  new JSONObject();
        try {
            json.put("NFCConsulta","dfsdf");
            json.put("qlocalizador","sdfsdf");
            prueba(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @SuppressLint("ValidFragment")
    public SolicitudesPendActivity(final Context context){
        this.mContext = (FragmentActivity) context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = (FrameLayout) inflater.inflate(R.layout.activity_solipen_main,container, false);
        linflater = inflater;

        JSONObject json =  new JSONObject();
        try {
            json.put("NFCConsulta","dfsdf");
            json.put("qlocalizador","sdfsdf");
            prueba(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_solipen_main, container, false);

        /**Crear lista de cosas para comprobar la creación dinámica de vistas**/


    }

    @Override
    public void onRequestCompleted(JSONObject response) throws JSONException {
        Iterator<?> keys = response.keys();
        while(keys.hasNext() ){
            String key = (String)keys.next();
            System.out.println(response.get(key).toString());
        }
        System.out.println("Login incorrecto");
        Fragment fragment = new SolicitudesPendDinamica("Jose Vilas");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        fragment = new SolicitudesPendDinamica("J. A. Regueiro");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
    public void prueba(JSONObject response) throws JSONException {
        Iterator<?> keys = response.keys();
        while(keys.hasNext() ){
            String key = (String)keys.next();
            System.out.println(response.get(key).toString());
        }

        /*System.out.println("***AQUI 1***");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        //LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_soli_pend, null, false);
        System.out.println("***AQUI 2***");

        TextView textView = (TextView) relativeLayout.findViewById(R.id.solpen_text);
        textView.setText("Probando jejexd");
        System.out.println("***AQUI 3***");

        //ViewGroup layout = null;
        layout = (ViewGroup) v.findViewById(R.id.linear_listPend);
        layout.addView(relativeLayout);*/
        Fragment fragment = new SolicitudesPendDinamica("Ramon Cachondo");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        Fragment fragment2 = new SolicitudesPendDinamica("Pepe Vilas");
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment2).commit();

        //
        //ScrollView scrollView = (ScrollView)mContext.findViewById(R.id.scroll_listPend);
        ScrollView scrollView = (ScrollView)v.findViewById(R.id.scroll_listPend);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        //
        //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();
        /*Fragment fragment2 = new SolicitudesPendDinamica("Pepe Vilas");
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.add(R.id.content_frame, fragment2);
        fragmentTransaction.commit();*/
    }



}
