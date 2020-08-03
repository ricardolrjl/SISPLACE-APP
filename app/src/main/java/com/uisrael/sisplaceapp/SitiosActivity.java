package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SitiosActivity extends AppCompatActivity {
    TextView user;
    String cedula,nombreUsuario;
    Bundle datoRecibir;
    Integer idPersonal;
    ImageButton imw;

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

        imw=findViewById(R.id.imgBtnWhatsapp);
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
   //     Toast.makeText(getApplicationContext(),"Ir al Inicio",Toast.LENGTH_SHORT).show();
    }

    public void irYoutube(View v){
        String YoutubeId = "yt://MN0nU_su5u17xqHnhs2JSQ";
        String urlPage = "https://www.youtube.com/channel/UCUguziC608WwBWWSkcJZX1Q";

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YoutubeId)));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Aplicación no instalada",Toast.LENGTH_SHORT).show();
            //Abre url de pagina.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
        }
    }
    public void irTwiter(View v){
        String TwiterId = "tw://page/235467633241041";
        String urlPage = "https://twitter.com/agentesdequito?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TwiterId)));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Aplicación no instalada",Toast.LENGTH_SHORT).show();
            //Abre url de pagina.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
        }
    }
    public void irPagina(View v){

        String urlPage = "https://cuerpodeagentesdecontrolquito.gob.ec/";

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Aplicación no instalada",Toast.LENGTH_SHORT).show();
            //Abre url de pagina.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
        }
    }

    public void irWhatsapp(View v){
/*
        try {
            Uri uri = Uri.parse("smsto:"+"+593987342976");
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("web.whatsapp.com");
            startActivity(i);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Aplicación no instalada",Toast.LENGTH_SHORT).show();
        }

*/
try {
    Uri sms_uri = Uri.parse("smsto:+" + "593992850775");
    Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
    sms_intent.putExtra("sms_body", "Mi nombre es "+nombreUsuario+" ,necesito ayuda con ");
    startActivity(sms_intent);
}
catch (Exception e){
    Uri sms_uri = Uri.parse("smsto:+" + "593992850775");
    Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
    sms_intent.putExtra("sms_body", "Mi nombre es "+nombreUsuario+" ,necesito ayuda con ");
    startActivity(sms_intent);
}





    }
}
