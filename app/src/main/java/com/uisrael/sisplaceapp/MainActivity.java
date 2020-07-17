package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ///para las variables
    EditText etUsuario,etClave;
    String nombreUsuario;
    /////

    ListView mListView;
    List<String> mLista= new ArrayList<>();
    ArrayAdapter mAdapter;

    //SANDRITA
    String ws = "http://192.168.100.85/rest/postpostgres.php";
    //Victor
    //String ws = "http://192.168.100.26/rest/postpostgres.php";
    //RICARDO
    //String ws = "http://192.168.100.26/rest/postpostgres.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsuario=findViewById(R.id.etUsuario);
        etClave=findViewById(R.id.etClave);
    }

    public String devuelveClave(String usuario){
        String clave="";
        //SANDRITA
        //String ws = "http://192.168.100.85/rest/postpostgres.php";
        //RICARDO
        //String ws = "http://192.168.100.26/rest/postpostgres.php";

        StrictMode.ThreadPolicy politica= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL(ws);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json;

            while((inputLine = in.readLine()) !=null){
                response.append(inputLine);
            }
            json=response.toString();
            JSONArray array=null;
            array= new JSONArray(json);
            String username="";

            for (int i=0; i<array.length(); i++){
                JSONObject objeto = array.getJSONObject(i);
                username=objeto.optString("username");
                if(username.equalsIgnoreCase(usuario)) {
                    clave = objeto.optString("clave");
                    nombreUsuario=objeto.optString("nombre");
                }
            }
        }
        catch (MalformedURLException e  ) {
            Toast.makeText(getApplicationContext(),"error"+e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"error"+e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"error"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return clave;
    }

    public String devuelveNombre(String usuario){
        String nombreUser = "No existe nombre";
        //String ws = "http://192.168.100.85/rest/postpostgres.php";
        StrictMode.ThreadPolicy politica= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL(ws);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json;

            while((inputLine = in.readLine()) !=null){
                response.append(inputLine);
            }
            json=response.toString();
            JSONArray array=null;
            array= new JSONArray(json);
            String username="";

            for (int i=0; i<array.length(); i++){
                JSONObject objeto = array.getJSONObject(i);
                username=objeto.optString("username");
                if(username.equalsIgnoreCase(usuario)) {
                    nombreUser=objeto.optString("nombre");
                }
            }
        }
        catch (MalformedURLException e  ) {
            Toast.makeText(getApplicationContext(),"error"+e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"error"+e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"error"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return nombreUser;
    }

    public void envioUsuarioPassword(View v){
        String clave="";
        Intent intentEnvio= new Intent(MainActivity.this, InicioActivity.class);
        clave=devuelveClave(etUsuario.getText().toString().trim());
        if(etClave.getText().toString().equals(clave) && !(etClave.getText().toString().isEmpty())){
            nombreUsuario=devuelveNombre(etUsuario.getText().toString().trim());
            intentEnvio.putExtra("usuario",etUsuario.getText().toString());
            intentEnvio.putExtra("nombre",nombreUsuario);
            Toast.makeText(getApplicationContext(),"Usuario Correcto.",Toast.LENGTH_SHORT).show();
            startActivity(intentEnvio);
        }else{
            Toast.makeText(getApplicationContext(),"Datos incorrectos. Favor Verifique",Toast.LENGTH_SHORT).show();

        }
    }
}
