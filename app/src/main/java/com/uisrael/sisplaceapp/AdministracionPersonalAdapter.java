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
import entidades.Evento;

public class AdministracionPersonalAdapter extends RecyclerView.Adapter<AdministracionPersonalAdapter.AdministracionPersonalHolder> implements  View.OnClickListener{

    List<Evento> listaEventos;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public AdministracionPersonalAdapter(List<Evento> listaEventos, Context context) {
        this.listaEventos = listaEventos;
        this.context = context;
        request= Volley.newRequestQueue(context);
    }

    @Override
    public AdministracionPersonalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.administracion_personal_list,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new AdministracionPersonalHolder(vista);
    }


    @Override
    public void onBindViewHolder(AdministracionPersonalHolder holder, int position) {
        holder.txtIdEvento.setText(listaEventos.get(position).getIdEvento().toString());
        holder.txtDescripcion.setText(listaEventos.get(position).getDescripcion().toString());
        holder.txtDireccion.setText(listaEventos.get(position).getDireccion().toString());
        holder.txtFechaHora.setText(listaEventos.get(position).getFecha().toString()+" - "+listaEventos.get(position).getHora().toString());
        holder.txtResponsable.setText(listaEventos.get(position).getResponsable().toString());
        holder.txtTelefonos.setText(listaEventos.get(position).getTelefono().toString()+" - "+listaEventos.get(position).getCelular().toString());

        
    }
/*
    private void cargarImagenWebService(String logo, final AdministracionPersonalHolder holder) {

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
*/
    @Override
    public int getItemCount() {
        return listaEventos.size();
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

    public class AdministracionPersonalHolder extends RecyclerView.ViewHolder{

        TextView txtIdEvento,txtDescripcion,txtDireccion,txtFechaHora,txtResponsable,txtTelefonos;

        public AdministracionPersonalHolder(View itemView) {
            super(itemView);
            txtIdEvento= (TextView) itemView.findViewById(R.id.txtIdEvento);
            txtDescripcion= (TextView) itemView.findViewById(R.id.txtEvento);
            txtDireccion= (TextView) itemView.findViewById(R.id.txtDireccion);
           // imagen=(ImageView) itemView.findViewById(R.id.idImagenAdmin);
            txtFechaHora= (TextView) itemView.findViewById(R.id.txtFechaHora);
            txtResponsable= (TextView) itemView.findViewById(R.id.txtResponsable);
            txtTelefonos= (TextView) itemView.findViewById(R.id.txtTelefonos);
        }
    }
}
