package smartmailbox.keeperbox;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by jopea on 21/03/2017.
 */

public interface Request {
    void onRequestCompleted(JSONArray response) throws JSONException;
}