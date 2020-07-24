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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import entidades.Evento;
import entidades.VolleySingleton;

public class PorTodosActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    RecyclerView recyclerEventos;
    ArrayList<Evento> listaEventos;
    RequestQueue requestQueue;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    TextView user;
    Integer idPersonal,idEvento;
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

        user=findViewById(R.id.tvUsuario);
        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");
        user.setText(nombreUsuario);

        listaEventos=new ArrayList<>();
        recyclerEventos = findViewById(R.id.idRecyclerEventos);
        recyclerEventos.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        //  recyclerEventos.setLayoutManager(new GridLayoutManager(this,2));
        recyclerEventos.setHasFixedSize(true);
        request= Volley.newRequestQueue(getApplicationContext());

        cargarWebService();
    }

    private void cargarWebService() {
        String url=Utils.DIRECCION_IP+"rest/wsJSONEventosTodosPersonal.php?cedula="+cedula;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Evento evento= new Evento();
        JSONArray json=response.optJSONArray("administracion");
        try {
            for (int i=0;i<json.length();i++){
                evento= new Evento();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                evento.setIdEvento(jsonObject.optInt("ID_EVENTO"));
                evento.setIdAdministracionEvento(jsonObject.optInt("ID_ADMINISTRACION_ZONAL"));
                evento.setDescripcion(jsonObject.optString("DESCRIPCION"));
                evento.setDireccion(jsonObject.optString("DIRECCION"));
                evento.setFecha(jsonObject.optString("FECHA"));
                evento.setHora(jsonObject.optString("HORA"));
                evento.setResponsable(jsonObject.optString("NOMBRE_RESPONSABLE"));
                evento.setTelefono(jsonObject.optString("CONVENCIONAL_RESPONSABLE"));
                evento.setCelular(jsonObject.optString("CELULAR_RESPONSABLE"));
                evento.setRutaLogo(jsonObject.optString("LOGO"));
                evento.setLongitud(jsonObject.optDouble("LONGITUD"));
                evento.setLatitud(jsonObject.optDouble("LATITUD"));

                listaEventos.add(evento);
            }
           EventoPersonalAdapter adapter=new EventoPersonalAdapter(listaEventos,getApplicationContext());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            //        Toast.makeText(getApplicationContext(), "Seleccionó "+listaEventos.get(recyclerEventos.getChildAdapterPosition(view)).getDireccion(), Toast.LENGTH_SHORT).show();
                    seleccionAdministracion(listaEventos.get(recyclerEventos.getChildAdapterPosition(view)).getDescripcion(),listaEventos.get(recyclerEventos.getChildAdapterPosition(view)).getIdEvento(),listaEventos.get(recyclerEventos.getChildAdapterPosition(view)).getLongitud(),listaEventos.get(recyclerEventos.getChildAdapterPosition(view)).getLatitud());
                }
            });

            recyclerEventos.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexion con el servidor"+" "+response, Toast.LENGTH_LONG).show();

        }
    }

    public void seleccionAdministracion(String nombreEvento,int idEvento,Double longitud,Double latitud){
        Intent intentEnvio= new Intent(PorTodosActivity.this, UbicacionActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idevento",idEvento);
        intentEnvio.putExtra("idpersonal",idPersonal);
        intentEnvio.putExtra("nombreEvento",nombreEvento);
        intentEnvio.putExtra("longitud",longitud);
        intentEnvio.putExtra("latitud",latitud);
        startActivity(intentEnvio);
    //    Toast.makeText(getApplicationContext(),"Seleccionó "+nombreEvento,Toast.LENGTH_SHORT).show();
    }


    public void irInicio(View v){
        Intent intentEnvio= new Intent(PorTodosActivity.this, AgendaActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
     //   Toast.makeText(getApplicationContext(),"Ir al Inicio",Toast.LENGTH_SHORT).show();
    }


}
