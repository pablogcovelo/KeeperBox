package smartmailbox.keeperbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static Connection connection = null;
    private final static String URL = "jdbc:mysql://localhost:3306/";
    private final static String DBNAME = "KeeperBox";
    private final static String DRIVER = "com.mysql.jdbc.Driver";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "natacion";

    private Statement statement;
    private ResultSet rs;
   /* final Button loginButton = (Button) findViewById(R.id.btn_login);
    final Button registerButton = (Button) findViewById(R.id.btn_register);
    final EditText emailUser = (EditText) findViewById(R.id.input_email_user);
    final EditText password = (EditText) findViewById(R.id.input_password);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     /*   try {
        Button registerButton = (Button) findViewById(R.id.btn_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });/*
        try {
            Class.forName(DRIVER).newInstance();

            connection = DriverManager.getConnection(URL + DBNAME, USERNAME, PASSWORD);
            if (!connection.isClosed()) {
                Log.d(TAG, "Se ha establecido conexion");
            }
        } catch (InstantiationException e) {
            Log.d(TAG, e.getMessage());
            System.exit(0);
        } catch (IllegalAccessException e) {
            Log.d(TAG, e.getMessage());
            System.exit(0);
        } catch (ClassNotFoundException e) {
            Log.d(TAG, e.getMessage());
            System.exit(0);
        } catch (SQLException e) {
            Log.d(TAG, e.getMessage());
            System.exit(0);
        }*/

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
                /**Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);*/
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

      /*  if (!validate()) {
            onLoginFailed();
            return;
        }*/

        Intent intent = new Intent(LoginActivity.this, NavDrawActivity.class);
        startActivity(intent);
        if (!validate()) {
            onLoginFailed();
            return;
        }

        /**Intent intent = new Intent(this, Usuario/RepartidorActivity.class);
        startActivity(intent);*/
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Fallo en el registro", Toast.LENGTH_LONG);
    }

    /**
     * Permite validar los datos introducidos por el ususario en los campos
     * Usuario/Emali y Contraseña.
     *
     * @return
     */
    public boolean validate() {
        final EditText emailUser = (EditText) findViewById(R.id.input_email_user);
        final EditText password = (EditText) findViewById(R.id.input_password);

        boolean valid = false;

        String email_user = emailUser.getText().toString();
        String psswd = password.getText().toString();

        try {
            statement = connection.createStatement();

        String email_user = emailUser.getText().toString();
        String psswd = password.getText().toString();

        try {
      /*  try {
            statement = connection.createStatement();

            try {
                if (!connection.isClosed()) {
                    CallableStatement cstmt = connection.prepareCall("{call comprobarUsuario(?,?)}");
                    cstmt.setString(1, email_user);
                    cstmt.setString(2, psswd);
                    cstmt.execute();
                    rs = cstmt.getResultSet();
                    while (rs.next()) {
                        if (rs.getString("valido").equalsIgnoreCase("0")) {
                            emailUser.setError("Email/Usuario o contraseña incorrectos");
                            password.setError("Email/Usuario o contraseña incorrectos");
                            valid = false;
                        } else {
                            emailUser.setError(null);
                            password.setError(null);
                            valid = true;
                        }
                    }
                }
            } catch (SQLException e) {
                Toast.makeText(this, "Sin conexión. Intentelo mas tarde", Toast.LENGTH_SHORT).show();
                return valid;
            } finally {
                return valid;
            }
        } catch (SQLException e) {
            Toast.makeText(this, "Sin conexión. Intentelo mas tarde", Toast.LENGTH_SHORT).show();
            return valid;
        } finally {
            return valid;
        }

    }

}
