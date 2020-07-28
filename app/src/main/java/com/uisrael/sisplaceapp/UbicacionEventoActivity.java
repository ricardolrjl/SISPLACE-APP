package com.uisrael.sisplaceapp;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UbicacionEventoActivity extends FragmentActivity implements OnMapReadyCallback {

    Integer idPersonal;
    Integer idevento=0;
    String cedula,nombreUsuario,nombreEvento,direccionEvento;
    Double latitud,longitud;
    Bundle datoRecibir;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_evento);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");
        idevento=datoRecibir.getInt("idevento");
        nombreEvento=datoRecibir.getString("nombreEvento");
        direccionEvento = datoRecibir.getString("direccion");
        latitud=datoRecibir.getDouble("latitud");
        longitud=datoRecibir.getDouble("longitud");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng evento = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(evento).title(nombreEvento).snippet(direccionEvento));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(evento,15));



    }
}
