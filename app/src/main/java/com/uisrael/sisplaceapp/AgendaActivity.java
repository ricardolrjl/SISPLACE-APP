package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AgendaActivity extends AppCompatActivity {
    TextView user;
    Integer idPersonal;
    String cedula,nombreUsuario;
    Bundle datoRecibir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
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
      //  Toast.makeText(getApplicationContext(),"Idpersonal:"+idPersonal.toString(),Toast.LENGTH_SHORT).show();
    }

    public void seleccionFechas(View v){
        Intent intentEnvio= new Intent(AgendaActivity.this, PorFechasActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó Fechas",Toast.LENGTH_SHORT).show();
    }

    public void seleccionAdministracion(View v){
        Intent intentEnvio= new Intent(AgendaActivity.this, PorAdministracionActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó Administracion-idP"+idPersonal.toString(),Toast.LENGTH_SHORT).show();
    }

    public void seleccionTodos(View v){
        Intent intentEnvio= new Intent(AgendaActivity.this, PorTodosActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Seleccionó Todos",Toast.LENGTH_SHORT).show();
    }
    public void irInicio(View v){
        Intent intentEnvio= new Intent(AgendaActivity.this, InicioActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        Toast.makeText(getApplicationContext(),"Ir al Inicio",Toast.LENGTH_SHORT).show();
    }
}
