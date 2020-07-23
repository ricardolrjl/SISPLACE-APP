package com.uisrael.sisplaceapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;
import entidades.Administracion;

public class AdministracionAdapter extends RecyclerView.Adapter<AdministracionAdapter.AdministracionHolder> implements  View.OnClickListener{

    List<Administracion> listaAdministracion;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public AdministracionAdapter(List<Administracion> listaAdministracion, Context context) {
        this.listaAdministracion = listaAdministracion;
        this.context = context;
        request= Volley.newRequestQueue(context);
    }

    @Override
    public AdministracionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.administracion_list,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new AdministracionHolder(vista);
    }


    @Override
    public void onBindViewHolder(AdministracionHolder holder, int position) {
        holder.txtIdAdministracion.setText(listaAdministracion.get(position).getId_administracion_zonal().toString());
     //   holder.txtNombre.setText(listaAdministracion.get(position).getNombre().toString());
    //    holder.txtRutalLogo.setText(listaAdministracion.get(position).getLogo().toString());
       if (listaAdministracion.get(position).getLogo()!=null){
            //
           cargarImagenWebService(listaAdministracion.get(position).getLogo(),holder);
       }else{
            holder.imagen.setImageResource(R.drawable.usuarioblue);
        }
        
    }

    private void cargarImagenWebService(String logo, final AdministracionHolder holder) {

        String urlImagen=Utils.DIRECCION_IP+logo;
        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaAdministracion.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class AdministracionHolder extends RecyclerView.ViewHolder{

        TextView txtIdAdministracion,txtNombre,txtRutalLogo;
        ImageView imagen;

        public AdministracionHolder(View itemView) {
            super(itemView);
            txtIdAdministracion= (TextView) itemView.findViewById(R.id.txtIdAdministracion);
        //    txtNombre= (TextView) itemView.findViewById(R.id.txtNombre);
         //   txtRutalLogo= (TextView) itemView.findViewById(R.id.txtRutaLogo);
            imagen=(ImageView) itemView.findViewById(R.id.idImagenAdmin);

        }
    }
}
