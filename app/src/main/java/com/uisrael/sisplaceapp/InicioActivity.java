package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import entidades.Personal;

public class InicioActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    TextView user;
    String cedula,nombreUsuario;
    Integer idPersonal;

    Bundle datoRecibir;
    ImageButton btnAgenda,btnInformacion,btnSitios;
    ImageView campoImagen;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressBar progreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //Obtenemos la ActionBar instalada por AppCompatActivity
        ActionBar actionBar = getSupportActionBar();
        //Establecemos el icono en la ActionBar
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);

        btnAgenda=findViewById(R.id.imgBtnAgenda);
        btnInformacion=findViewById(R.id.imgBtnInformacion);
        btnSitios=findViewById(R.id.imgBtnSitios);
        user=findViewById(R.id.tvUsuario);
        campoImagen=findViewById(R.id.imagenId);

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");
        user.setText(nombreUsuario);

        request= Volley.newRequestQueue(getApplicationContext());
        cargarWebService();

    }

    private void cargarWebService(){
         String url=Utils.DIRECCION_IP+"rest/wsJSONConsultarPersonal.php?cedula="+cedula;
         jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
              //   Toast.makeText(getApplicationContext(), "Mensaje"+response, Toast.LENGTH_SHORT).show();
                 Personal personal= new Personal();
                 JSONArray json = response.optJSONArray("personal");
                 JSONObject jsonObject=null;
                 try {
                     jsonObject=json.getJSONObject(0);
                     personal.setApellidosnombres(jsonObject.optString("APELLIDOSNOMBRES"));
                     personal.setCedula(jsonObject.optString("CEDULA"));
                     personal.setId_administracion_zonal(jsonObject.optInt("ID_ADMINISTRACION_ZONAL"));
                     personal.setId_personal(jsonObject.optInt("ID_PERSONAL"));
                     personal.setFoto(jsonObject.optString("FOTO"));
                     user.setText(personal.getApellidosnombres());
                     idPersonal=personal.getId_personal();

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 String urlImagen=Utils.DIRECCION_IP+personal.getFoto();
                 cargarImagen(urlImagen);

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


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se pudo consultar"+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Mensaje"+response, Toast.LENGTH_SHORT).show();
        Personal personal= new Personal();
        JSONArray json = response.optJSONArray("personal");
        JSONObject jsonObject=null;
        try {
            jsonObject=json.getJSONObject(0);
            personal.setApellidosnombres(jsonObject.optString("APELLIDOSNOMBRES"));
            personal.setCedula(jsonObject.optString("CEDULA"));
            personal.setId_administracion_zonal(jsonObject.optInt("ID_ADMINISTRACION_ZONAL"));
            personal.setId_personal(jsonObject.optInt("ID_PERSONAL"));
            personal.setFoto(jsonObject.optString("FOTO"));
            user.setText(personal.getFoto());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void seleccionAgenda(View v){
        Intent intentEnvio= new Intent(InicioActivity.this, AgendaActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
       Toast.makeText(getApplicationContext(),"Seleccionó Agenda-id"+idPersonal,Toast.LENGTH_SHORT).show();
    }


    public void seleccionInformacion(View v){
        Intent intentEnvio= new Intent(InicioActivity.this, InformacionActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó Información-idP"+idPersonal,Toast.LENGTH_SHORT).show();
    }

    public void seleccionSitios(View v){
        Intent intentEnvio= new Intent(InicioActivity.this, SitiosActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó Sitios-id"+idPersonal,Toast.LENGTH_SHORT).show();
    }

}
