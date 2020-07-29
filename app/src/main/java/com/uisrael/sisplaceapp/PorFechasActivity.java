package com.uisrael.sisplaceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import entidades.Evento;

public class PorFechasActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    TextView user;
    EditText editFechaDesde,editFechaHasta;
    Integer idPersonal;
    String cedula,nombreUsuario,fechadesde,fechahasta;
    Bundle datoRecibir;

    RecyclerView recyclerFechas;
    ArrayList<Evento> listaEventos;
    RequestQueue requestQueue;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    static final int DATE_ID2 = 1;
    Calendar C = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_por_fechas);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        user=findViewById(R.id.tvUsuario);
        editFechaDesde=findViewById(R.id.etFechaDesde);
        editFechaHasta=findViewById(R.id.etFechaHasta);

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");
        user.setText(nombreUsuario);

        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);

        listaEventos=new ArrayList<>();
        recyclerFechas = findViewById(R.id.idRecyclerFechas);
        recyclerFechas.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        //  recyclerEventos.setLayoutManager(new GridLayoutManager(this,2));
        recyclerFechas.setHasFixedSize(true);
        request= Volley.newRequestQueue(getApplicationContext());

        editFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(DATE_ID);
            }
        });

        editFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(DATE_ID2);
            }
        });


    }
    private void colocar_fecha(int id) {
        String mes=String.valueOf(mMonthIni + 1);
        mes=mes.trim().length()==1?'0'+mes:mes;
        String dia=String.valueOf(mDayIni);
        dia=dia.trim().length()==1?'0'+dia:dia;
        if(id==DATE_ID)
          editFechaDesde.setText(mYearIni +"-" + mes + "-" + dia + " ");
        if(id==DATE_ID2)
            editFechaHasta.setText(mYearIni +"-" +mes+ "-" + dia + " ");
    }



    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYearIni = year;
                    mMonthIni = monthOfYear;
                    mDayIni = dayOfMonth;
                    colocar_fecha(DATE_ID);

                }

            };

    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYearIni = year;
                    mMonthIni = monthOfYear;
                    mDayIni = dayOfMonth;
                    colocar_fecha(DATE_ID2);

                }

            };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYearIni, sMonthIni, sDayIni);
            case DATE_ID2:
                return new DatePickerDialog(this, mDateSetListener2, sYearIni, sMonthIni, sDayIni);


        }
        return null;
    }

    public void cargarWebServiceFechas(View v) {
        fechadesde=editFechaDesde.getText().toString();
        fechahasta=editFechaHasta.getText().toString();
        String url=Utils.DIRECCION_IP+"rest/wsJSONEventosFechasPersonal.php?cedula="+cedula+"&fechadesde="+fechadesde.trim()+"&fechahasta="+fechahasta.trim();
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    public void irInicio(View v){
        Intent intentEnvio= new Intent(PorFechasActivity.this, InicioActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);
        //  Toast.makeText(getApplicationContext(),"Ir al Inicio",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Evento evento= new Evento();
        JSONArray json=response.optJSONArray("administracion");
        try {
            for (int i=0;i<json.length();i++){
                evento= new Evento();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                evento.setIdEvento(jsonObject.optInt("ID_EVENTO"));
                evento.setIdAdministracionEvento(jsonObject.optInt("ID_ADMINISTRACION_ZONAL"));
                evento.setDescripcion(jsonObject.optString("DESCRIPCION"));
                evento.setDireccion(jsonObject.optString("DIRECCION"));
                evento.setFecha(jsonObject.optString("FECHA"));
                evento.setHora(jsonObject.optString("HORA"));
                evento.setResponsable(jsonObject.optString("NOMBRE_RESPONSABLE"));
                evento.setTelefono(jsonObject.optString("CONVENCIONAL_RESPONSABLE"));
                evento.setCelular(jsonObject.optString("CELULAR_RESPONSABLE"));
                evento.setRutaLogo(jsonObject.optString("LOGO"));
                evento.setLongitud(jsonObject.optDouble("LONGITUD"));
                evento.setLatitud(jsonObject.optDouble("LATITUD"));

                listaEventos.add(evento);
            }
            EventoPersonalAdapter adapter=new EventoPersonalAdapter(listaEventos,getApplicationContext());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //        Toast.makeText(getApplicationContext(), "Seleccionó "+listaEventos.get(recyclerEventos.getChildAdapterPosition(view)).getDireccion(), Toast.LENGTH_SHORT).show();
                    seleccionAdministracion(listaEventos.get(recyclerFechas.getChildAdapterPosition(view)).getDescripcion(),listaEventos.get(recyclerFechas.getChildAdapterPosition(view)).getIdEvento(),listaEventos.get(recyclerFechas.getChildAdapterPosition(view)).getLongitud(),listaEventos.get(recyclerFechas.getChildAdapterPosition(view)).getLatitud(),listaEventos.get(recyclerFechas.getChildAdapterPosition(view)).getDireccion());
                }
            });

            recyclerFechas.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexion con el servidor"+" "+response, Toast.LENGTH_LONG).show();

        }
    }

    public void seleccionAdministracion(String nombreEvento,int idEvento,Double longitud,Double latitud,String direccion){
        Intent intentEnvio= new Intent(PorFechasActivity.this, UbicacionEventoActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idevento",idEvento);
        intentEnvio.putExtra("idpersonal",idPersonal);
        intentEnvio.putExtra("nombreEvento",nombreEvento);
        intentEnvio.putExtra("longitud",longitud);
        intentEnvio.putExtra("latitud",latitud);
        intentEnvio.putExtra("direccion",direccion);
        startActivity(intentEnvio);
        //    Toast.makeText(getApplicationContext(),"Seleccionó "+nombreEvento,Toast.LENGTH_SHORT).show();
    }
}
