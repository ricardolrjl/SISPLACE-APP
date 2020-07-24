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

import entidades.Evento;

public class EventoPersonalAdapter extends RecyclerView.Adapter<EventoPersonalAdapter.EventoPersonalHolder> implements  View.OnClickListener{

    List<Evento> listaEventos;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public EventoPersonalAdapter(List<Evento> listaEventos, Context context) {
        this.listaEventos = listaEventos;
        this.context = context;
        request= Volley.newRequestQueue(context);
    }

    @Override
    public EventoPersonalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.eventos_list_image,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new EventoPersonalHolder(vista);
    }


    @Override
    public void onBindViewHolder(EventoPersonalHolder holder, int position) {
        holder.txtIdEvento.setText(listaEventos.get(position).getIdEvento().toString());
        holder.txtDescripcion.setText(listaEventos.get(position).getDescripcion().toString());
        holder.txtDireccion.setText(listaEventos.get(position).getDireccion().toString());
        holder.txtFechaHora.setText(listaEventos.get(position).getFecha().toString());
        holder.txtHora.setText(listaEventos.get(position).getHora().toString());
        holder.txtResponsable.setText(listaEventos.get(position).getResponsable().toString());
        holder.txtTelefonos.setText(listaEventos.get(position).getTelefono().toString()+" - "+listaEventos.get(position).getCelular().toString());
        if (listaEventos.get(position).getRutaLogo()!=null){
            //
            cargarImagenWebService(listaEventos.get(position).getRutaLogo(),holder);
        }else{
            holder.imagen.setImageResource(R.drawable.usuarioblue);
        }

        
    }

    private void cargarImagenWebService(String logo, final EventoPersonalHolder holder) {

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

    public class EventoPersonalHolder extends RecyclerView.ViewHolder{

        TextView txtIdEvento,txtDescripcion,txtDireccion,txtFechaHora,txtResponsable,txtTelefonos,txtHora;
        ImageView imagen;

        public EventoPersonalHolder(View itemView) {
            super(itemView);
            txtIdEvento= (TextView) itemView.findViewById(R.id.idEvento);
            txtDescripcion= (TextView) itemView.findViewById(R.id.idDescripcion);
            txtDireccion= (TextView) itemView.findViewById(R.id.idDireccion);
            imagen=(ImageView) itemView.findViewById(R.id.idImagenEvento);
            txtFechaHora= (TextView) itemView.findViewById(R.id.idFecha);
            txtHora= (TextView) itemView.findViewById(R.id.idHora);
            txtResponsable= (TextView) itemView.findViewById(R.id.idResponsable);
            txtTelefonos= (TextView) itemView.findViewById(R.id.idTelefonos);
        }
    }
}
