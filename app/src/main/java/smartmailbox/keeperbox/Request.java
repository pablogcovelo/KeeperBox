package smartmailbox.keeperbox;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jopea on 21/03/2017.
 */

public interface Request {
    void onRequestCompleted(JSONObject response) throws JSONException;
}