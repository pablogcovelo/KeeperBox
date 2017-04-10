package smartmailbox.keeperbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by regueiro on 10/03/17.
 */

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Request{

    private static final String TAG = "KeeperBox";
    Spinner tipo_usuario;
    ScrollView scrollView_register;
    EditText usuario, contrasena, correo_electronico,idbuzon,nombre, apellidos, cif_empresa,nombre_empresa,
            num_repartidor,pais,ciudad,calle,num_portal,piso,letra_piso, cod_postal;
    Button btn_reg_1,btn_reg_2 ;
   // private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Variable.TOKEN = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, Variable.TOKEN);

        scrollView_register = (ScrollView) findViewById(R.id.scroll_register);
        usuario = (EditText)findViewById(R.id.usuario);
        contrasena  = (EditText)findViewById(R.id.reg_contrasena);
        correo_electronico  = (EditText)findViewById(R.id.correo_electronico);
        idbuzon = (EditText)findViewById(R.id.id_buzon);
        nombre = (EditText)findViewById(R.id.nombre);
        apellidos = (EditText)findViewById(R.id.apellidos);
        cif_empresa = (EditText)findViewById(R.id.cif_empresa);
        nombre_empresa = (EditText)findViewById(R.id.nombre_empresa);
        num_repartidor = (EditText)findViewById(R.id.num_repartidor);
        pais = (EditText)findViewById(R.id.pais);
        ciudad = (EditText)findViewById(R.id.ciudad);
        calle = (EditText)findViewById(R.id.calle);
        num_portal = (EditText)findViewById(R.id.num_portal);
        piso = (EditText)findViewById(R.id.num_piso);
        letra_piso= (EditText)findViewById(R.id.letra_piso);
        cod_postal = (EditText)findViewById(R.id.cod_postal);
        btn_reg_1 = (Button)findViewById(R.id.btn_reg_1);
        btn_reg_2 = (Button)findViewById(R.id.btn_reg_2);

        //progressBar = (ProgressBar) findViewById(R.id.progress);

        this.tipo_usuario = (Spinner) findViewById(R.id.tipo_usuario);

        loadSpinnerTipoUsuario();

        // Boton registro propietario
        btn_reg_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarparametros();
               // progressBar.setVisibility(View.VISIBLE);
                JSONObject json =  new JSONObject();
                try {
                    json.put("qusuario",usuario.getText());
                    json.put("qcorreo_elect",correo_electronico.getText());
                    json.put("qcontrasena",contrasena.getText());
                    json.put("qnombre",nombre.getText());
                    json.put("qapellidos",apellidos.getText());
                    json.put("qNFC","3452asdf"); // TODO
                    json.put("qtipo_usuario","1");
                    json.put("qcod_buzon",idbuzon.getText());
                    json.put("qpais",pais.getText());
                    json.put("qciudad",ciudad.getText());
                    json.put("qcalle",calle.getText());
                    json.put("qnumero",num_portal.getText());
                    json.put("qpiso",piso.getText());
                    json.put("qletra",letra_piso.getText());
                    json.put("qCP",cod_postal.getText());
                    json.put("qnombreEmpresa",JSONObject.NULL);
                    json.put("qCIF",JSONObject.NULL);
                    json.put("qnumeroRepartidor",JSONObject.NULL);
                    json.put("qtoken",Variable.TOKEN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("Registro " + json);
                Peticion peticion = new Peticion(RegisterActivity.this);
                peticion.execute("nuevoUsuario", json.toString());
            }
        });

        // Boton registro repartidor
        btn_reg_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarparametros();
                //progressBar.setVisibility(View.VISIBLE);
                JSONObject json =  new JSONObject();
                try {
                    json.put("usuario",usuario.getText());
                    json.put("correo_elect",correo_electronico.getText());
                    json.put("contrasena",contrasena.getText());
                    json.put("nombre",nombre.getText());
                    json.put("apellidos",apellidos.getText());
                    json.put("NFC","1234");
                    json.put("tipo_usuario","2");
                    json.put("cod_buzon",JSONObject.NULL);
                    json.put("pais",JSONObject.NULL);
                    json.put("ciudad",JSONObject.NULL);
                    json.put("calle",JSONObject.NULL);
                    json.put("numero",JSONObject.NULL);
                    json.put("piso",JSONObject.NULL);
                    json.put("letra",JSONObject.NULL);
                    json.put("CP",JSONObject.NULL);
                    json.put("nombreEmpresa",nombre_empresa.getText());
                    json.put("CIF",cif_empresa.getText());
                    json.put("numeroRepartidor",num_repartidor.getText());
                    json.put("token",Variable.TOKEN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("Registro " + json);
                Peticion peticion = new Peticion(RegisterActivity.this);
                peticion.execute("nuevoUsuario", json.toString());
            }
        });

    }

    private void comprobarparametros() {
        if(usuario.getText().toString().isEmpty()){
            usuario.setError(getResources().getString(R.string.vacio));
            return;
        }
        if(usuario.getText().toString().length() < 4 || usuario.getText().toString().length() > 20){
            usuario.setError(getResources().getString(R.string.tamano));
            return;
        }
        if(correo_electronico.getText().toString().isEmpty()){
            correo_electronico.setError(getResources().getString(R.string.vacio));
            return;
        }
        if(correo_electronico.getText().toString().length() > 50){
            correo_electronico.setError(getResources().getString(R.string.tamano1));
            return;
        }
        if(correo_electronico.getText().toString().contains("@")){
            correo_electronico.setError(getResources().getString(R.string.correo_mal));
            return;
        }
        if(contrasena.getText().toString().isEmpty()){
            contrasena.setError(getResources().getString(R.string.vacio));
            return;
        }
        if(contrasena.getText().toString().length() < 4 || contrasena.getText().toString().length() > 20){
            contrasena.setError(getResources().getString(R.string.tamano));
            return;
        }

        if(tipo_usuario.toString().equals(1)){
            if(idbuzon.getText().toString().isEmpty()){
                idbuzon.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(idbuzon.getText().toString().length() > 45){
                idbuzon.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(nombre.getText().toString().isEmpty()){
                nombre.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(nombre.getText().toString().length() > 20){
                nombre.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(apellidos.getText().toString().isEmpty()){
                apellidos.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(apellidos.getText().toString().length() > 40){
                apellidos.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(pais.getText().toString().isEmpty()){
                pais.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(pais.getText().toString().length() > 45){
                pais.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(ciudad.getText().toString().isEmpty()){
                ciudad.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(ciudad.getText().toString().length() > 40){
                ciudad.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(calle.getText().toString().isEmpty()){
                calle.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(calle.getText().toString().length() > 45){
                calle.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(num_portal.getText().toString().isEmpty()){
                num_portal.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(num_portal.getText().toString().length() > 3){
                try {
                    int nume = Integer.parseInt(num_portal.getText().toString());
                    if(nume > 1000 || nume < 0){
                        num_portal.setError(getResources().getString(R.string.ser_numeros));
                    }
                }catch (Exception e){
                    num_portal.setError(getResources().getString(R.string.ser_numeros));
                    return;
                }
            }
            if(cod_postal.getText().toString().isEmpty()){
                cod_postal.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(cod_postal.getText().toString().length() > 5){
                cod_postal.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(cod_postal.getText().toString().length() <= 5){
                try {
                    int nume = Integer.parseInt(cod_postal.getText().toString());
                    if(nume > 99999 || nume < 10000){
                        cod_postal.setError(getResources().getString(R.string.ser_numeros));
                    }
                }catch (Exception e){
                    cod_postal.setError(getResources().getString(R.string.ser_numeros));
                    return;
                }
            }
        }else{
            if(cif_empresa.getText().toString().isEmpty()){
                cif_empresa.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(cif_empresa.getText().toString().length() > 9){
                cif_empresa.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(nombre_empresa.getText().toString().isEmpty()){
                nombre_empresa.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(nombre_empresa.getText().toString().length() > 45){
                nombre_empresa.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
            if(num_repartidor.getText().toString().isEmpty()){
                num_repartidor.setError(getResources().getString(R.string.vacio));
                return;
            }
            if(num_repartidor.getText().toString().length() > 45){
                num_repartidor.setError(getResources().getString(R.string.tamano_erroneo));
                return;
            }
        }

    }

    private void loadSpinnerTipoUsuario(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipo_usuarios, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.tipo_usuario.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        tipo_usuario.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        if(pos==1){

            idbuzon.setVisibility(View.VISIBLE);
            nombre.setVisibility(View.VISIBLE);
            cif_empresa.setVisibility(View.INVISIBLE);
            apellidos.setVisibility(View.VISIBLE);
            pais.setVisibility(View.VISIBLE);
            ciudad.setVisibility(View.VISIBLE);
            calle.setVisibility(View.VISIBLE);
            piso.setVisibility(View.VISIBLE);
            letra_piso.setVisibility(View.VISIBLE);
            cod_postal.setVisibility(View.VISIBLE);
            btn_reg_1.setVisibility(View.VISIBLE);
            num_portal.setVisibility(View.VISIBLE);
            nombre_empresa.setVisibility(View.INVISIBLE);
            num_repartidor.setVisibility(View.INVISIBLE);
            btn_reg_2.setVisibility(View.INVISIBLE);

            scrollView_register.setOnTouchListener(null);

        }else if (pos==2){

            idbuzon.setVisibility(View.INVISIBLE);
            nombre.setVisibility(View.VISIBLE);
            apellidos.setVisibility(View.VISIBLE);
            pais.setVisibility(View.INVISIBLE);
            ciudad.setVisibility(View.INVISIBLE);
            calle.setVisibility(View.INVISIBLE);
            piso.setVisibility(View.INVISIBLE);
            letra_piso.setVisibility(View.INVISIBLE);
            cod_postal.setVisibility(View.INVISIBLE);
            btn_reg_1.setVisibility(View.INVISIBLE);
            num_portal.setVisibility(View.INVISIBLE);
            cif_empresa.setVisibility(View.VISIBLE);
            nombre_empresa.setVisibility(View.VISIBLE);
            num_repartidor.setVisibility(View.VISIBLE);
            btn_reg_2.setVisibility(View.VISIBLE);
            btn_reg_2.setWillNotDraw(true);
        }else{

            idbuzon.setVisibility(View.INVISIBLE);
            nombre.setVisibility(View.INVISIBLE);
            apellidos.setVisibility(View.INVISIBLE);
            pais.setVisibility(View.INVISIBLE);
            ciudad.setVisibility(View.INVISIBLE);
            calle.setVisibility(View.INVISIBLE);
            piso.setVisibility(View.INVISIBLE);
            num_portal.setVisibility(View.INVISIBLE);
            letra_piso.setVisibility(View.INVISIBLE);
            cod_postal.setVisibility(View.INVISIBLE);
            cif_empresa.setVisibility(View.INVISIBLE);
            nombre_empresa.setVisibility(View.INVISIBLE);
            num_repartidor.setVisibility(View.INVISIBLE);
            btn_reg_2.setVisibility(View.INVISIBLE);
            btn_reg_1.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Callback method to be invoked when the selection disappears from this
        // view. The selection can disappear for instance when touch is
        // activated or when the adapter becomes empty.
    }


    @Override
    public void onRequestCompleted(JSONArray response) throws JSONException {
        // la tarea en segundo plano ya ha terminado. Ocultamos el progreso.
        //progressBar.setVisibility(View.GONE);

        // Cogemos el campo valido de la respuesta JSON
        String valido = null;
        JSONObject row = null;
        for (int i = 0; i < response.length(); i++) {
            row = response.getJSONObject(i);
            valido = row.getString("valido");
        }
        if (valido.equalsIgnoreCase("1")) {
            if(!row.getString("tipo_usuario").equals("2")){
                Intent intent = new Intent(RegisterActivity.this, NavDrawPropActivity.class);
                intent.putExtra("datos", row.toString());
                startActivity(intent);
            }else{
                Intent intent = new Intent(RegisterActivity.this, NavDrawReparActivity.class);
                intent.putExtra("datos", row.toString());
                startActivity(intent);
            }
        }else
            System.out.println("Login incorrecto");
    }
}
