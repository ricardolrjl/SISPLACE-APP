package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import entidades.Personal;

public class AdminPersonalActivity extends AppCompatActivity {
    TextView user,viewAdministracion;
    Integer idAdministracion;
    Integer idPersonal;
    String cedula,nombreUsuario,administracion,rutalogo;
    Bundle datoRecibir;
    ImageView campoImagen;
    RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_personal);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        campoImagen=findViewById(R.id.imagenId);
        user=findViewById(R.id.tvUsuario);
        viewAdministracion=findViewById(R.id.tvAdmin);

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idAdministracion=datoRecibir.getInt("idadministracion");
        administracion=datoRecibir.getString("administracion");
        rutalogo=datoRecibir.getString("logo");
        idPersonal=datoRecibir.getInt("idpersonal");

        user.setText(nombreUsuario);
        Toast.makeText(getApplicationContext(), "idpersona"+idPersonal, Toast.LENGTH_LONG).show();
        viewAdministracion.setText(idPersonal.toString());

        String urlImagen="http://192.168.100.85/"+rutalogo;
        Toast.makeText(getApplicationContext(), "Url logo:"+urlImagen+"-"+administracion+"-"+idPersonal.toString()+"-"+idAdministracion.toString(), Toast.LENGTH_LONG).show();
        request= Volley.newRequestQueue(getApplicationContext());
      //  cargarImagen(urlImagen);
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
        Toast.makeText(getApplicationContext(),"Ir a Administracion",Toast.LENGTH_SHORT).show();
    }
}
