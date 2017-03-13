package smartmailbox.keeperbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by regueiro on 13/03/17.
 */

public class SolicitudesPendActivity extends Fragment {

    public SolicitudesPendActivity(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_solipen_main, container, false);

        /**Crear lista de cosas para comprobar la creación dinámica de vistas**/


    }

}
