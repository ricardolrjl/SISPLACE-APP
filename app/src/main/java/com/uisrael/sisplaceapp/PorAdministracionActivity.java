package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

import entidades.Administracion;
import entidades.Personal;

public class PorAdministracionActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    TextView user;
    Integer idPersonal;
    private Integer idadministracion=0;
    String cedula,nombreUsuario;
    private String nombreAdmin="",rutalogo="";
    Bundle datoRecibir;

    RecyclerView recyclerAdministracion;
    ArrayList<Administracion> listaAdministracion;
    RequestQueue requestQueue;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_por_administracion);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        user=findViewById(R.id.tvUsuario);
        listaAdministracion=new ArrayList<>();
        recyclerAdministracion=findViewById(R.id.idRecyclerImagenAd);
        recyclerAdministracion.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
    //    recyclerAdministracion.setLayoutManager(new GridLayoutManager(this,2));
        recyclerAdministracion.setHasFixedSize(true);
        request=Volley.newRequestQueue(getApplicationContext());

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");

        user.setText(nombreUsuario);
    //    Toast.makeText(getApplicationContext(),"Idpersonal "+idPersonal.toString(),Toast.LENGTH_SHORT).show();

        cargarWebService();


    }

    private void cargarWebService() {
        String url=Utils.DIRECCION_IP+"rest/wsJSONListaAdministracion.php";
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
        Administracion administracion= new Administracion();
        JSONArray json=response.optJSONArray("administracion");
        try {
            for (int i=0;i<json.length();i++){
                administracion=new Administracion();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                administracion.setId_administracion_zonal(jsonObject.optInt("ID_ADMINISTRACION_ZONAL"));
                administracion.setNombre(jsonObject.optString("NOMBRE"));
                administracion.setLogo(jsonObject.optString("LOGO"));
                listaAdministracion.add(administracion);
            }
            AdministracionAdapter adapter=new AdministracionAdapter(listaAdministracion,getApplicationContext());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Seleccionó "+listaAdministracion.get(recyclerAdministracion.getChildAdapterPosition(view)).getNombre(), Toast.LENGTH_SHORT).show();
                    seleccionAdministracion(listaAdministracion.get(recyclerAdministracion.getChildAdapterPosition(view)).getNombre(),listaAdministracion.get(recyclerAdministracion.getChildAdapterPosition(view)).getId_administracion_zonal(),listaAdministracion.get(recyclerAdministracion.getChildAdapterPosition(view)).getLogo());
                }
            });

            recyclerAdministracion.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexion con el servidor"+" "+response, Toast.LENGTH_LONG).show();

        }
    }

    public void seleccionAdministracion(String nombreAdmin,int idadministracion,String rutalogo){
        Intent intentEnvio= new Intent(PorAdministracionActivity.this, AdminPersonalActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idadministracion",idadministracion);
        intentEnvio.putExtra("administracion",nombreAdmin);
        intentEnvio.putExtra("logo",rutalogo);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó "+nombreAdmin,Toast.LENGTH_SHORT).show();
    }

    private void devuelveAdministracion(String nombre){
        String url=Utils.DIRECCION_IP+"rest/wsJSONAdminNombre.php?nombre="+nombre;
        final Administracion administracion= new Administracion();
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray json = response.optJSONArray("administracion");
                JSONObject jsonObject=null;
                try {
                    jsonObject=json.getJSONObject(0);
                    administracion.setId_administracion_zonal(jsonObject.optInt("ID_ADMINISTRACION_ZONAL"));
                    administracion.setNombre(jsonObject.optString("NOMBRE"));
                    administracion.setLogo(jsonObject.optString("LOGO"));
                    nombreAdmin=administracion.getNombre();
                    rutalogo=administracion.getLogo();
                    idadministracion=administracion.getId_administracion_zonal();
                    Toast.makeText(getApplicationContext(),"Seleccionó :"+nombreAdmin+"-"+rutalogo+"-"+idadministracion,Toast.LENGTH_SHORT).show();
                 } catch (JSONException e) {
                    e.printStackTrace();
                }

              }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se pudo consultar"+error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("ERROR",error.toString());
            }
        });
        request.add(jsonObjectRequest);

    }

    public void irInicio(View v){
        Intent intentEnvio= new Intent(PorAdministracionActivity.this, AgendaActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Ir a Agenda",Toast.LENGTH_SHORT).show();
    }



}
