package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class InicioActivity extends AppCompatActivity {
    TextView user;
    String cedula,nombreUsuario;

    Bundle datoRecibir;
    ImageButton btnAgenda,btnInformacion,btnSitios;
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

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        user.setText(nombreUsuario);
    }

    public void seleccionAgenda(View v){
        Intent intentEnvio= new Intent(InicioActivity.this, AgendaActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó Agenda",Toast.LENGTH_SHORT).show();
    }


    public void seleccionInformacion(View v){
        Intent intentEnvio= new Intent(InicioActivity.this, InformacionActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó Información",Toast.LENGTH_SHORT).show();
    }

    public void seleccionSitios(View v){
        Intent intentEnvio= new Intent(InicioActivity.this, SitiosActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó Sitios",Toast.LENGTH_SHORT).show();
    }
}
