package smartmailbox.keeperbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Request {
    private static final String TAG = "KeeperBox";

    private ProgressBar progressBar;
    private EditText user_email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showGooglePlayServicesStatus();

        progressBar = (ProgressBar) findViewById(R.id.progress);
        user_email = (EditText) findViewById(R.id.input_email_user);
        password = (EditText) findViewById(R.id.input_password);

        final Button loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        final Button registerButton = (Button) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        String useremail = user_email.getText().toString();
        String passwd = password.getText().toString();
        String email;
        String usuarios;

        if(user_email.getText().toString().length() > 50){
            user_email.setError(getResources().getString(R.string.tamano1));
            return;
        }
        if(password.getText().toString().length() > 50){
            password.setError(getResources().getString(R.string.tamano1));
            return;
        }
        if(useremail.contains("@")){
            email = useremail;
            usuarios = "null";
        }else{
            email = "null";
            usuarios = useremail;
        }

        if (!validate()) {
            onLoginFailed();
            return;
        }

       /* Intent intent = new Intent(LoginActivity.this, NavDrawReparActivity.class);
        startActivity(intent);*/

        JSONObject json =  new JSONObject();
        try {
            json.put("qusuario",usuarios);
            json.put("qcorreo_elect",email);
            json.put("qcontrasena",passwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        progressBar.setVisibility(View.VISIBLE);

        Peticion peticion = new Peticion(LoginActivity.this);
        peticion.execute("comprobarUsuario", json.toString());
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Fallo en el registro", Toast.LENGTH_LONG);
    }

    private void showGooglePlayServicesStatus() {
        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();
        int errorCode = apiAvail.isGooglePlayServicesAvailable(this);
        String msg = "Play Services: " + apiAvail.getErrorString(errorCode);
        Log.d(TAG, msg);
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        // la tarea en segundo plano ya ha terminado. Ocultamos el progreso.
        progressBar.setVisibility(View.GONE);
        JSONObject row = null;
        // Cogemos el campo valido de la respuesta JSON
        String valido = null;
        if(response != null) {
            for (int i = 0; i < response.length(); i++) {
                row = response.getJSONObject(i);
                valido = row.getString("valido");
            }

            if (valido.equalsIgnoreCase("1")) {
                System.out.println("Login correcto");

                if (row.getString("tipo_usuario").equals("0") || row.getString("tipo_usuario").equals("1")) {
                    Intent intent = new Intent(LoginActivity.this, NavDrawPropActivity.class);
                    intent.putExtra("datos", row.toString());
                    startActivity(intent);
                } else if (row.getString("tipo_usuario").equals("2")) {
                    Intent intent = new Intent(LoginActivity.this, NavDrawReparActivity.class);
                    intent.putExtra("datos", row.toString());
                    startActivity(intent);
                }
            } else{
                AlertDialog.Builder builder_localizador = new AlertDialog.Builder(this);
                builder_localizador.setMessage(getString(R.string.error_datos))
                        .setTitle(getString(R.string.error_login))
                        .setCancelable(false)
                        .setNeutralButton(getString(R.string.accept),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert_localizador = builder_localizador.create();
                alert_localizador.show();
            }
        }else{
            AlertDialog.Builder builder_localizador = new AlertDialog.Builder(this);
            builder_localizador.setMessage(getString(R.string.error_conexion))
                    .setTitle(getString(R.string.error_red))
                    .setCancelable(false)
                    .setNeutralButton(getString(R.string.accept),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert_localizador = builder_localizador.create();
            alert_localizador.show();
        }
    }

    /**
     * Permite validar los datos introducidos por el ususario en los campos
     * Usuario/Email y Contraseña.
     *
     * @return
     */
    public boolean validate() {
        boolean valid = true;

        String useremail = user_email.getText().toString();
        String passwd = password.getText().toString();

        return valid;
    }
}
