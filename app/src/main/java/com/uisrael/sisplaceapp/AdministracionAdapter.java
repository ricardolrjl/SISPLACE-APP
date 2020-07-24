package com.uisrael.sisplaceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import entidades.Administracion;

public class AdministracionAdapter extends RecyclerView.Adapter<AdministracionAdapter.AdministracionHolder>{

    List<Administracion> listaAdministracion;

    public AdministracionAdapter(List<Administracion> listaAdministracion) {
        this.listaAdministracion = listaAdministracion;
    }

    @Override
    public AdministracionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.administracion_list,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new AdministracionHolder(vista);
    }


    @Override
    public void onBindViewHolder(AdministracionHolder holder, int position) {
        holder.txtIdAdministracion.setText(listaAdministracion.get(position).getId_administracion_zonal().toString());
        holder.txtNombre.setText(listaAdministracion.get(position).getNombre().toString());
        holder.txtRutalLogo.setText(listaAdministracion.get(position).getLogo().toString());
    }

    @Override
    public int getItemCount() {
        return listaAdministracion.size();
    }

    public class AdministracionHolder extends RecyclerView.ViewHolder{

        TextView txtIdAdministracion,txtNombre,txtRutalLogo;

        public AdministracionHolder(View itemView) {
            super(itemView);
            txtIdAdministracion= (TextView) itemView.findViewById(R.id.txtIdAdministracion);
            txtNombre= (TextView) itemView.findViewById(R.id.txtNombre);
            txtRutalLogo= (TextView) itemView.findViewById(R.id.txtRutaLogo);

        }
    }
}
