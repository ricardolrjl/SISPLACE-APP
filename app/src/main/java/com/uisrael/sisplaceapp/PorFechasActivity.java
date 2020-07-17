package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PorFechasActivity extends AppCompatActivity {

    TextView user;
    String cedula,nombreUsuario;
    Bundle datoRecibir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_por_fechas);
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
        user.setText(nombreUsuario);
    }
}
