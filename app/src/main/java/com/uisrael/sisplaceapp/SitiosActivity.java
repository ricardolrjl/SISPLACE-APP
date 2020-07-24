package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SitiosActivity extends AppCompatActivity {
    TextView user;
    String cedula,nombreUsuario;
    Bundle datoRecibir;
    Integer idPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios);
        //Obtenemos la ActionBar instalada por AppCompatActivity
        ActionBar actionBar = getSupportActionBar();
        //Establecemos el icono en la ActionBar
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        // Activar flecha ir atrás (ir a la Parent Activity declarada en el manifest)
        actionBar.setDisplayHomeAsUpEnabled(true);

        user=findViewById(R.id.tvUsuario);

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");
        user.setText(nombreUsuario);
    }

    public void irFacebook(View v){
        String facebookId = "fb://page/235467633241041";
        String urlPage = "https://www.facebook.com/agentesdequito/";

       try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Aplicación no instalada",Toast.LENGTH_SHORT).show();
            //Abre url de pagina.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
        }
    }

    public void irInicio(View v){
        Intent intentEnvio= new Intent(SitiosActivity.this, InicioActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Ir al Inicio",Toast.LENGTH_SHORT).show();
    }


}
