package smartmailbox.keeperbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Request {
    private static final String TAG = "LoginActivity";

    private ProgressBar progressBar;
    private EditText user_email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        if (!validate()) {
            onLoginFailed();
            return;
        }

        Intent intent = new Intent(LoginActivity.this, NavDrawReparActivity.class);
        //intent.putExtra("datos", row.toString());
        startActivity(intent);

       /* JSONObject json =  new JSONObject();
        try {
            json.put("usuario",useremail);
            json.put("correo",useremail);
            json.put("contrasena",passwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        progressBar.setVisibility(View.VISIBLE);

        Peticion peticion = new Peticion(LoginActivity.this);
        peticion.execute("comprobarUsuario", json.toString());*/
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Fallo en el registro", Toast.LENGTH_LONG);
    }

    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        // la tarea en segundo plano ya ha terminado. Ocultamos el progreso.
        //progressBar.setVisibility(View.GONE);
        JSONObject row = null;
        // Cogemos el campo valido de la respuesta JSON
        String valido = null;
        for (int i = 0; i < response.length(); i++) {
            row = response.getJSONObject(i);
            valido = row.getString("valido");
        }

        if (valido.equalsIgnoreCase("1")) {
            System.out.println("Login correcto");
            Intent intent = new Intent(LoginActivity.this, NavDrawPropActivity.class);
            intent.putExtra("datos", row.toString());
            startActivity(intent);
        }
        else
            System.out.println("Login incorrecto");
    }

    /**
     * Permite validar los datos introducidos por el ususario en los campos
     * Usuario/Emali y Contraseña.
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
