package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.function.DoubleToLongFunction;

import entidades.Administracion;

public class UbicacionActivity extends AppCompatActivity {
    TextView user,txtLatitud,txtLongitud,txtNombreEvento;
    Integer idPersonal;
    Integer idevento=0;
    String cedula,nombreUsuario,nombreEvento;
    Double latitud,longitud;
    Bundle datoRecibir;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        user=findViewById(R.id.tvUsuario);
        txtLatitud=findViewById(R.id.txtLatitud);
        txtLongitud=findViewById(R.id.txtLongitud);
        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");
        idevento=datoRecibir.getInt("idevento");
        nombreEvento=datoRecibir.getString("nombreEvento");
        latitud=datoRecibir.getDouble("latitud");
        longitud=datoRecibir.getDouble("longitud");

        user.setText(nombreUsuario);
        txtLongitud.setText(longitud.toString());
        txtLatitud.setText(latitud.toString());
        txtNombreEvento.setText(nombreEvento);
    }
}
