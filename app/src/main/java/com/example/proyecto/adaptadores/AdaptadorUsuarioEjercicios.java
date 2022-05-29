package com.example.proyecto.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.modelos.Ejercicio;
import com.example.proyecto.modelos.Usuario_Ejercicio;

import java.util.ArrayList;

public class AdaptadorUsuarioEjercicios extends RecyclerView.Adapter<AdaptadorUsuarioEjercicios.ContenedorUsuarioEjercicios>
        implements View.OnClickListener{

    ArrayList<Usuario_Ejercicio> listaUsuarioEjercicios;
    ArrayList<Ejercicio> listaEjercicios;
    Context contexto;

    private View.OnClickListener listener;

    //Constructor
    public AdaptadorUsuarioEjercicios(ArrayList<Usuario_Ejercicio> listaUsuarioEjercicios, ArrayList<Ejercicio> listaEjercicios, Context contexto) {
        this.listaUsuarioEjercicios = listaUsuarioEjercicios;
        this.listaEjercicios = listaEjercicios;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public ContenedorUsuarioEjercicios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflamos la vista e inicializamos el setOnClickListener
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_ejercicio, parent, false);
        view.setOnClickListener(this);
        return new AdaptadorUsuarioEjercicios.ContenedorUsuarioEjercicios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContenedorUsuarioEjercicios holder, int position) {
        //sacamos por pantalla los datos correspondientes
        //obtenemos los datos de la lista
        int idEj = listaUsuarioEjercicios.get(position).getIdEj();
        double peso = listaUsuarioEjercicios.get(position).getPeso();
        int numSeries = listaUsuarioEjercicios.get(position).getNumSeries();
        String nomEj = "";
        String zonaTrabajada = "";
        //Buscamos los datos en la lista en la que se encuentran todos los ejercicios
        for(Ejercicio e: listaEjercicios) {
            if(e.getIdEj() == idEj) {
                 nomEj = e.getNombreEj();
                 zonaTrabajada = e.getZonaTrabajada();
            }
        }
        holder.tvUsuarioEj.setText(nomEj);
        //Si equivale a esto no mostramos nada
        if(zonaTrabajada.equalsIgnoreCase("todo el cuerpo")) {
            holder.tvPeso.setText("");
            holder.tvNumSeries.setText("");
        }
        else {
            holder.tvPeso.setText("Peso: " + peso);
            holder.tvNumSeries.setText("Nº series: " + numSeries);
        }
        //según la zona del cuerpo mostramos una imagen u otra
        switch (zonaTrabajada) {
            case "pierna":
                holder.imUsuarioEj.setImageResource(R.mipmap.ic_pierna);
                break;
            case "espalda":
                holder.imUsuarioEj.setImageResource(R.mipmap.ic_espalda);
                break;
            case "pecho":
                holder.imUsuarioEj.setImageResource(R.mipmap.ic_pecho);
                break;
            case "brazo":
                holder.imUsuarioEj.setImageResource(R.mipmap.ic_brazo);
                break;
            default:
                switch (nomEj) {
                    case "futbol":
                        holder.imUsuarioEj.setImageResource(R.mipmap.ic_futbol);
                        break;
                    case "baloncesto":
                        holder.imUsuarioEj.setImageResource(R.mipmap.ic_deporte2);
                        break;
                    case "tenis":
                    case "padel":
                        holder.imUsuarioEj.setImageResource(R.mipmap.ic_tenis);
                        break;
                    case "natacion":
                        holder.imUsuarioEj.setImageResource(R.mipmap.ic_natacion);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    //Devolvemos el tamaño de la lista
    @Override
    public int getItemCount() {
        return listaUsuarioEjercicios.size();
    }

    //Función para inicializar el listener
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null) {
            listener.onClick(view);
        }
    }

    //Clase contenedora
    public class ContenedorUsuarioEjercicios extends RecyclerView.ViewHolder{
        TextView tvUsuarioEj, tvPeso, tvNumSeries;
        ImageView imUsuarioEj;

        //Constructor
        public ContenedorUsuarioEjercicios(@NonNull View itemView) {
            super(itemView);
            tvUsuarioEj = itemView.findViewById(R.id.tv_usuarioEjercicio);
            tvPeso = itemView.findViewById(R.id.tv_pesoUsuarioEjercicio);
            tvNumSeries = itemView.findViewById(R.id.tv_numSeriesUsuarioEjercicio);
            imUsuarioEj = itemView.findViewById(R.id.im_usuarioEjercicio);

        }

    }
}
