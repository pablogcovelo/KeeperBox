package smartmailbox.keeperbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
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

    final Button loginButton = (Button) findViewById(R.id.btn_login);
    final Button registerButton = (Button) findViewById(R.id.btn_register);
    final EditText emailUser = (EditText) findViewById(R.id.input_email_user);
    final EditText password = (EditText) findViewById(R.id.input_password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        try {
            Class.forName(DRIVER).newInstance();

            connection = DriverManager.getConnection(URL + DBNAME, USERNAME, PASSWORD);
            if (!connection.isClosed()) {
                Log.d(TAG, "Se ha establecido conexion");
            }
        } catch (InstantiationException e) {
            System.exit(0);
        } catch (IllegalAccessException e) {
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.exit(0);
        } catch (SQLException e) {
            System.exit(0);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Codigo que carge la actividad de registro*/
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            return;
        }
    }

    /**
     * Permite validar los datos introducidos por el ususario en los campos
     * Usuario/Emali y Contraseña.
     *
     * @return
     */
    public boolean validate() {
        boolean valid = false;

        try {
            statement = connection.createStatement();

            String email_user = emailUser.getText().toString();
            String psswd = password.getText().toString();

            try {
                if (!connection.isClosed()) {
                    if (email_user.isEmpty()) {
                        emailUser.setError("Email o nombre de usuario incorrecto");
                        valid = false;
                    } else {
                        CallableStatement cstmt = connection.prepareCall("{call comprobarUsuario(?,?)}");
                        cstmt.setString(1,email_user);
                        cstmt.setString(2,psswd);
                        cstmt.execute();
                        rs = cstmt.getResultSet();

                        emailUser.setError(null);
                    }

                    if (psswd.isEmpty()) {
                        password.setError("La contraseña introducida no es valida");
                        valid = false;
                    } else {
                        password.setError(null);
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
