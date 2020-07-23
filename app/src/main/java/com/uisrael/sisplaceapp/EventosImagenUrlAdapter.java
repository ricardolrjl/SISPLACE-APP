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
import entidades.VolleySingleton;

public class EventosImagenUrlAdapter extends RecyclerView.Adapter<EventosImagenUrlAdapter.EventosHolder>{

    List<Evento> listaEventos;
    RequestQueue request;
    Context context;


    public EventosImagenUrlAdapter(List<Evento> listaEventos, Context context) {
        this.listaEventos = listaEventos;
        this.context=context;
        request= Volley.newRequestQueue(context);
    }

    @Override
    public EventosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.eventos_list_image,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new EventosHolder(vista);
    }

    @Override
    public void onBindViewHolder(EventosHolder holder, int position) {
        holder.txtDescripcion.setText(listaEventos.get(position).getDescripcion().toString());
        holder.txtDireccion.setText(listaEventos.get(position).getDireccion().toString());
        holder.txtFecha.setText(listaEventos.get(position).getFecha().toString());
        holder.txtHora.setText(listaEventos.get(position).getHora().toString());
        holder.txtResponsable.setText(listaEventos.get(position).getResponsable().toString());
        holder.txtCelular.setText(listaEventos.get(position).getCelular().toString());

        if (listaEventos.get(position).getRutaLogo()!=null){
           //
            cargarImagenWebService(listaEventos.get(position).getRutaLogo(),holder);
        }else{
            holder.imagen.setImageResource(R.drawable.usuarioblue);
        }
    }

    private void cargarImagenWebService(String rutaImagen, final EventosHolder holder) {

        String urlImagen=Utils.DIRECCION_IP+rutaImagen;
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
      //  VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class EventosHolder extends RecyclerView.ViewHolder{

        TextView txtIdEvento,txtDescripcion,txtDireccion,txtFecha,txtHora,txtResponsable,txtCelular;
        ImageView imagen;

        public EventosHolder(View itemView) {
            super(itemView);
            txtDescripcion= (TextView) itemView.findViewById(R.id.idDescripcion);
            txtDireccion= (TextView) itemView.findViewById(R.id.idDireccion);
            txtFecha= (TextView) itemView.findViewById(R.id.idFecha);
            txtHora=(TextView) itemView.findViewById(R.id.idHora);
            txtResponsable=(TextView) itemView.findViewById(R.id.idResponsable);
            txtCelular=(TextView) itemView.findViewById(R.id.idCelular);
            imagen=(ImageView) itemView.findViewById(R.id.idImagenEvento);
        }
    }
}
