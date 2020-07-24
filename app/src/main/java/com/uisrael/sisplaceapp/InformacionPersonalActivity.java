package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import entidades.Evento;
import entidades.Personal;

public class InformacionPersonalActivity extends AppCompatActivity   implements Response.Listener<JSONObject>,Response.ErrorListener{

    ImageView campoImagen;
    Button btnActualizar;
    Integer idPersonal;
    String cedula,nombreUsuario;
    Bundle datoRecibir;
    TextView user,nombre,fechanac;
    EditText direccion,telefono,celular,correo;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        campoImagen=findViewById(R.id.imagenId);
        user=findViewById(R.id.tvUsuario);
        nombre=findViewById(R.id.tvNombre);
        fechanac=findViewById(R.id.tvFechaNacimiento);
        direccion=findViewById(R.id.etDireccion);
        telefono=findViewById(R.id.etTelefono);
        celular=findViewById(R.id.etCelular);
        correo=findViewById(R.id.etCorreo);
        btnActualizar=findViewById(R.id.btnActualizar);

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");

        user.setText(cedula);
        nombre.setText(nombreUsuario);

        request= Volley.newRequestQueue(getApplicationContext());
        cargarWebService();

    }

    private void cargarWebService() {
        String url=Utils.DIRECCION_IP+"rest/wsJSONConsultarPersonal.php?cedula="+cedula;
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


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        String url=Utils.DIRECCION_IP+"rest/wsJSONConsultarPersonal.php?cedula="+cedula;
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                    personal.setFecha_nacimiento(jsonObject.optString("FECHA_NACIMIENTO"));
                    personal.setDireccion(jsonObject.optString("DIRECCION"));
                    personal.setTelefono_convecional(jsonObject.optString("TELEFONO_CONVENCIONAL"));
                    personal.setCelular(jsonObject.optString("TELEFONO_CELULAR"));
                    personal.setCorreo_electronico(jsonObject.optString("CORREO_ELECTRONICO"));
                    user.setText(personal.getApellidosnombres());
                    idPersonal=personal.getId_personal();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String urlImagen=Utils.DIRECCION_IP+personal.getFoto();
                cargarImagen(urlImagen);
                fechanac.setText(personal.getFecha_nacimiento());
                direccion.setText(personal.getDireccion());
                telefono.setText(personal.getTelefono_convecional());
                celular.setText(personal.getCelular());
                correo.setText(personal.getCorreo_electronico());
                user.setText(personal.getCedula());


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

    public void actualizarInformacion(View v){
       String url=Utils.DIRECCION_IP+"rest/wsJSONUpdatePersonal.php?";
       stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("actualiza")){
                     Toast.makeText(getApplicationContext(),"Se ha Actualizado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se ha Actualizado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String cedula=user.getText().toString();
                String vdireccion=direccion.getText().toString();
                String vtelefono=telefono.getText().toString();
                String vcelular=celular.getText().toString();
                String vcorreo=correo.getText().toString();

                Map<String,String> parametros=new HashMap<>();
                parametros.put("cedula",cedula);
                parametros.put("direccion",vdireccion);
                parametros.put("telefono",vtelefono);
                parametros.put("celular",vcelular);
                parametros.put("correo",vcorreo);
                return parametros;
            }
        };
       request.add(stringRequest);
    }


    public void irInicio(View v){
        Intent intentEnvio= new Intent(InformacionPersonalActivity.this, InicioActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
      //  Toast.makeText(getApplicationContext(),"Ir a Agenda",Toast.LENGTH_SHORT).show();
    }
}
