package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import entidades.Evento;
import entidades.VolleySingleton;

public class PorTodosActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    RecyclerView recyclerEventos;
    ArrayList<Evento> listaEventos;
    // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    TextView user;
    Integer idPersonal;
    String cedula,nombreUsuario;
    Bundle datoRecibir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_por_todos);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        listaEventos=new ArrayList<>();

        recyclerEventos = findViewById(R.id.idRecyclerImagen);
        recyclerEventos.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerEventos.setHasFixedSize(true);

        // request= Volley.newRequestQueue(getContext());

        cargarWebService();


        user=findViewById(R.id.tvUsuario);
        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");
        user.setText(nombreUsuario);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Evento evento=null;

        JSONArray json=response.optJSONArray("eventos");

        try {

            for (int i=0;i<json.length();i++){
                evento=new Evento();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                evento.setIdEvento(jsonObject.optInt("ID_EVENTO"));
                evento.setDescripcion(jsonObject.optString("DESCRIPCION"));
                evento.setDireccion(jsonObject.optString("DIRECCION"));
                evento.setFecha(jsonObject.optString("FECHA"));
                evento.setHora(jsonObject.optString("HORA"));
                evento.setResponsable(jsonObject.optString("NOMBRE_RESPONSABLE"));
                evento.setTelefono(jsonObject.optString("CONVENCIONAL_RESPONSABLE"));
                evento.setCelular(jsonObject.optString("CELULAR_RESPONSABLE"));
                evento.setLatitud(jsonObject.optDouble("LATITUD"));
                evento.setLongitud(jsonObject.optDouble("LONGITUD"));
                evento.setRutaLogo(jsonObject.optString("LOGO"));
                Toast.makeText(getApplicationContext(), "eVENTO:" +evento.getDescripcion(), Toast.LENGTH_LONG).show();
                listaEventos.add(evento);
            }

            EventosImagenUrlAdapter adapter=new EventosImagenUrlAdapter(listaEventos, getApplicationContext());
            recyclerEventos.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
        }
    }

    private void cargarWebService() {
        String url="http://192.168.100.244/rest/wsJSONEventosTodosPersonal.php?cedula="+cedula;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void irInicio(View v){
        Intent intentEnvio= new Intent(PorTodosActivity.this, AgendaActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Ir al Inicio",Toast.LENGTH_SHORT).show();
    }


}
