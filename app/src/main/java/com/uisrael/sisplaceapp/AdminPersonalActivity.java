package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import entidades.Administracion;
import entidades.Evento;
import entidades.Personal;

public class AdminPersonalActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener  {
    TextView user,viewAdministracion;
    Integer idAdministracion;
    Integer idPersonal,idEvento;
    String cedula,nombreUsuario,administracion,rutalogo;
    Bundle datoRecibir;
    ImageView campoImagen;
    RequestQueue request;
    RecyclerView recyclerAdministracionPersonal;
    ArrayList<Evento> listaEventos;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_personal);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        campoImagen=findViewById(R.id.imagenIdAdmin);
        user=findViewById(R.id.tvUsuario);

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idAdministracion=datoRecibir.getInt("idadministracion");
        administracion=datoRecibir.getString("administracion");
        rutalogo=datoRecibir.getString("logo");
        idPersonal=datoRecibir.getInt("idpersonal");

        user.setText(nombreUsuario);

        String urlImagen=Utils.DIRECCION_IP+rutalogo;

        listaEventos=new ArrayList<>();
        recyclerAdministracionPersonal=findViewById(R.id.idRecyclerAdmPersonal);
        recyclerAdministracionPersonal.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        //    recyclerAdministracion.setLayoutManager(new GridLayoutManager(this,2));
        recyclerAdministracionPersonal.setHasFixedSize(true);
        request=Volley.newRequestQueue(getApplicationContext());
        cargarImagen(urlImagen);

        cargarWebService();
    }

    private void cargarWebService() {
        String url=Utils.DIRECCION_IP+"rest/wsJSONEventosAdminPersonal.php?cedula="+cedula+"&idadministracion="+idAdministracion.toString();
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    private void cargarImagen(String urlImagen) {
        urlImagen=urlImagen.replace(" ","%20");
        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                campoImagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }

    public void irInicio(View v){
        Intent intentEnvio= new Intent(AdminPersonalActivity.this, PorAdministracionActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
      //  Toast.makeText(getApplicationContext(),"Ir a Administracion",Toast.LENGTH_SHORT).show();
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
                evento.setDescripcion(jsonObject.optString("DESCRIPCION"));
                evento.setDireccion(jsonObject.optString("DIRECCION"));
                evento.setFecha(jsonObject.optString("FECHA"));
                evento.setHora(jsonObject.optString("HORA"));
                evento.setResponsable(jsonObject.optString("NOMBRE_RESPONSABLE"));
                evento.setTelefono(jsonObject.optString("CONVENCIONAL_RESPONSABLE"));
                evento.setCelular(jsonObject.optString("CELULAR_RESPONSABLE"));
                evento.setLatitud(jsonObject.optDouble("LATITUD"));
                evento.setLongitud(jsonObject.optDouble("LONGITUD"));
                listaEventos.add(evento);
            }
            AdministracionPersonalAdapter adapter=new AdministracionPersonalAdapter(listaEventos,getApplicationContext());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //    Toast.makeText(getApplicationContext(), "Seleccionó "+listaEventos.get(recyclerAdministracionPersonal.getChildAdapterPosition(view)).getLatitud(), Toast.LENGTH_SHORT).show();
                    seleccionEvento(listaEventos.get(recyclerAdministracionPersonal.getChildAdapterPosition(view)).getDescripcion(),listaEventos.get(recyclerAdministracionPersonal.getChildAdapterPosition(view)).getIdEvento(),listaEventos.get(recyclerAdministracionPersonal.getChildAdapterPosition(view)).getLongitud(),listaEventos.get(recyclerAdministracionPersonal.getChildAdapterPosition(view)).getLatitud());
                }
            });

            recyclerAdministracionPersonal.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexion con el servidor"+" "+response, Toast.LENGTH_LONG).show();

        }
    }

    public void seleccionEvento(String nombreEvento,int idEvento,Double longitud,Double latitud){
        Intent intentEnvio= new Intent(AdminPersonalActivity.this, UbicacionActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idevento",idEvento);
        intentEnvio.putExtra("idpersonal",idPersonal);
        intentEnvio.putExtra("nombreEvento",nombreEvento);
        intentEnvio.putExtra("longitud",longitud);
        intentEnvio.putExtra("latitud",latitud);
        startActivity(intentEnvio);
      //  Toast.makeText(getApplicationContext(),"Seleccionó "+latitud,Toast.LENGTH_SHORT).show();
    }
}
