package com.uisrael.sisplaceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.text.PrecomputedTextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import entidades.VolleySingleton;

public class ActualizarFotoActivity extends AppCompatActivity {

    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;

    TextView user;
    String cedula,nombreUsuario;
    Integer idPersonal;

    RequestQueue request;
    StringRequest stringRequest;
    Bundle datoRecibir;
    ImageButton btnTomarFoto,btnGuardar;
    ImageView campoImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_foto);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_logo_launcher);
       // actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        user=findViewById(R.id.tvUsuario);
        campoImagen= findViewById(R.id.imagenId);
        btnGuardar=findViewById(R.id.imgBtnGuardarFoto);
        btnTomarFoto=findViewById(R.id.imgBtnGuardarFoto);

        datoRecibir=getIntent().getExtras();
        cedula=datoRecibir.getString("usuario");
        nombreUsuario=datoRecibir.getString("nombre");
        idPersonal=datoRecibir.getInt("idpersonal");
        user.setText(nombreUsuario);
        btnGuardar.setVisibility(View.INVISIBLE);

        //
        if (ContextCompat.checkSelfPermission(ActualizarFotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActualizarFotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActualizarFotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        request= Volley.newRequestQueue(getApplicationContext());

        if(solicitaPermisosVersionesSuperiores()){
            btnTomarFoto.setEnabled(true);
        }else{
            btnTomarFoto.setEnabled(false);
        }


    }

    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((ActualizarFotoActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&ActualizarFotoActivity.this.checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }


        if ((shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Utils.MIS_PERMISOS);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==Utils.MIS_PERMISOS){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(ActualizarFotoActivity.this,"Permisos aceptados",Toast.LENGTH_SHORT);
                btnTomarFoto.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ActualizarFotoActivity.this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",ActualizarFotoActivity.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(ActualizarFotoActivity.this,"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ActualizarFotoActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},100);
            }
        });
        dialogo.show();
    }

    public void mostrarDialogoOpciones(View v){
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final  AlertDialog.Builder builder=new AlertDialog.Builder(ActualizarFotoActivity.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto"))
                {
                   tomarFoto();
                }
                else{
                    if(opciones[i].equals("Elegir de Galeria")){
                     Intent intent=new Intent(Intent.ACTION_PICK,
                             MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                     intent.setType("image/");
                     startActivityForResult(intent.createChooser(intent,"Seleccione"),Utils.COD_SELECCIONA);
                   }
                    else{
                        dialogInterface.dismiss();
                    }
                }

            }
        });
        builder.show();
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Método para tomar foto y crear el archivo
  //  static final int REQUEST_TAKE_PHOTO = 1;
    public void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(ActualizarFotoActivity.this, "Error"+ex.getMessage(),Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.uisrael.sisplaceapp.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Utils.COD_FOTO);
            }
        }
    }

//Método para mostrar vista previa en un imageview de la foto tomada
//static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Utils.COD_SELECCIONA:
                Uri miPath=data.getData();
                campoImagen.setImageURI(miPath);
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(ActualizarFotoActivity.this.getContentResolver(),miPath);
                    campoImagen.setImageBitmap(bitmap);
                    btnGuardar.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case  Utils.COD_FOTO:
                File imgPhoto = new File(mCurrentPhotoPath);
                if(imgPhoto.exists()){
                    bitmap = BitmapFactory.decodeFile(imgPhoto.getAbsolutePath());
                    //ImageView myImage = (ImageView)findViewById(R.id.imgView);
                    campoImagen.setImageBitmap(bitmap);
                    btnGuardar.setVisibility(View.VISIBLE);
                }
                break;
       }
       bitmap=redimensionarImagen(bitmap,600,800);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }
    }

    public void cargarWebService(final View v) {

        String url=Utils.DIRECCION_IP+"rest/wsJSONUpdateFotoPersonal.php?";

        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("actualiza")){
                   Toast.makeText(ActualizarFotoActivity.this,"Se ha registrado con exito",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ActualizarFotoActivity.this,"Se ha registrado con éxito",Toast.LENGTH_LONG).show();
                    Log.i("RESPUESTA: ",""+response);

                }
                irInicio(v);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActualizarFotoActivity.this,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
             //   progressBar.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String idper=idPersonal.toString();
                String ced=cedula.toString();
                String imagen=convertirImgString(bitmap);
                String nombre=user.getText().toString().substring(0,5);

                Map<String,String> parametros=new HashMap<>();
                parametros.put("idpersonal",idper);
                parametros.put("cedula",ced);
                parametros.put("nombre",nombre.toString());
                parametros.put("imagen",imagen);
                //Toast.makeText(ActualizarFotoActivity.this, "parametros:"+ced, Toast.LENGTH_LONG).show();

                return parametros;

            }
        };
        request.add(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);
        return imagenString;
    }


    public void irInicio(View v){
        Intent intentEnvio= new Intent(ActualizarFotoActivity.this, InicioActivity.class);
        intentEnvio.putExtra("usuario",cedula);
        intentEnvio.putExtra("nombre",nombreUsuario);
        intentEnvio.putExtra("idpersonal",idPersonal);
        startActivity(intentEnvio);

    }


}
