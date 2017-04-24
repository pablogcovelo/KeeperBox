package smartmailbox.keeperbox;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by regueiro on 7/04/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    public static final String TAG = "KeeperBox";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Variable.TOKEN = FirebaseInstanceId.getInstance().getToken();
    }
}
