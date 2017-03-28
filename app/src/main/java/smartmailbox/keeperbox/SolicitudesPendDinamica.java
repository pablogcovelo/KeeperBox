package smartmailbox.keeperbox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jopea on 26/03/2017.
 */

public class SolicitudesPendDinamica extends Fragment {
    String nombre;
    @SuppressLint("ValidFragment")
    public SolicitudesPendDinamica(String nombre){
        this.nombre = nombre;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.dinamica_soli_pend, container, false);
        TextView textView = (TextView) relativeLayout.findViewById(R.id.solpen_text);
        textView.setText(nombre);
//layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setPadding(5, 3, 5, 3);
        relativeLayout.setLayoutParams(params);

        /*ScrollView scrollView = (ScrollView)getView().findViewById(R.id.scroll_listPend);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });*/
        ///////
        // Inflate the layout for this fragment
        return relativeLayout;//inflater.inflate(R.layout.dinamica_soli_pend, container, false);
    }
}
