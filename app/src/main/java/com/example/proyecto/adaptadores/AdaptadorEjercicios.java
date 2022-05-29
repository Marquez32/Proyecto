package com.example.proyecto.adaptadores;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.modelos.Ejercicio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdaptadorEjercicios extends RecyclerView.Adapter<AdaptadorEjercicios.ContenedorEjercicios>
            implements View.OnClickListener{

    ArrayList<Ejercicio> listaEjercicios;
    ArrayList<Ejercicio> originalListaEjercicios;
    Context contexto;
    OnEjerciciosListener onEjerciciosListener;

    private View.OnClickListener listener;

    //Constructor
    public AdaptadorEjercicios(ArrayList<Ejercicio> listaEjercicios, Context contexto, OnEjerciciosListener onEjerciciosListener) {
        this.listaEjercicios = listaEjercicios;
        this.contexto = contexto;
        this.onEjerciciosListener = onEjerciciosListener;
        this.originalListaEjercicios = new ArrayList<>();
        originalListaEjercicios.addAll(listaEjercicios);
    }

    @NonNull
    @Override
    public ContenedorEjercicios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflamos la vista e inicializamos el setOnClickListener
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio, parent, false);
        view.setOnClickListener(this);
        return new AdaptadorEjercicios.ContenedorEjercicios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContenedorEjercicios holder, int position) {
        //sacamos por pantalla los datos correspondientes
        holder.tvNombreEjercicio.setText(listaEjercicios.get(position).getNombreEj());
        //según sea una zona del cuerpo u otra mostramos una imagen
        switch (listaEjercicios.get(position).getZonaTrabajada()) {
            case "pierna":
                holder.imagenEjercicio.setImageResource(R.mipmap.ic_pierna);
                break;
            case "espalda":
                holder.imagenEjercicio.setImageResource(R.mipmap.ic_espalda);
                break;
            case "pecho":
                holder.imagenEjercicio.setImageResource(R.mipmap.ic_pecho);
                break;
            case "brazo":
                holder.imagenEjercicio.setImageResource(R.mipmap.ic_brazo);
                break;
            default:
                switch (listaEjercicios.get(position).getNombreEj()) {
                    case "futbol":
                        holder.imagenEjercicio.setImageResource(R.mipmap.ic_futbol);
                        break;
                    case "baloncesto":
                        holder.imagenEjercicio.setImageResource(R.mipmap.ic_deporte2);
                        break;
                    case "tenis":
                    case "padel":
                        holder.imagenEjercicio.setImageResource(R.mipmap.ic_tenis);
                        break;
                    case "natacion":
                        holder.imagenEjercicio.setImageResource(R.mipmap.ic_natacion);
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
        return listaEjercicios.size();
    }

    //Función para realizar las búsquedas por nombre con el SearchView
    public void filtro(final String cadenaBusqueda) {
        if(cadenaBusqueda.length() == 0) {
            //limpiamos la lista y le añadimos la lista original
            listaEjercicios.clear();
            listaEjercicios.addAll(originalListaEjercicios);
        }
        else {
            //limpiamos la lista
            listaEjercicios.clear();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Uso del lenguaje Lambda para cumplir la condición del nombre
                ArrayList<Ejercicio> coleccion = (ArrayList<Ejercicio>) originalListaEjercicios.stream()
                        .filter(i -> i.getNombreEj().toLowerCase().contains(cadenaBusqueda))
                        .collect(Collectors.toList());
                //añadimos los datos a la lista
                listaEjercicios.addAll(coleccion);
            }
            else {
                //limpiamos la lista
                listaEjercicios.clear();
                //Buscamos en la lista original y si cumple la condición lo añadimos a la lista
                for(Ejercicio e: originalListaEjercicios) {
                    if(e.getNombreEj().toLowerCase().contains(cadenaBusqueda)) {
                        listaEjercicios.add(e);
                    }
                }
            }
        }

        //notificamos los cambios
        notifyDataSetChanged();

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

    //Interfaz
    public interface OnEjerciciosListener {
        public void onAltaEjercicio(int posicion);
    }

    //Clase contenedora
    public class ContenedorEjercicios extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView tvNombreEjercicio;
        ImageView imagenEjercicio;

        //Constructor
        public ContenedorEjercicios(@NonNull View itemView) {
            super(itemView);
            tvNombreEjercicio = itemView.findViewById(R.id.tv_nombreEjercicio);
            imagenEjercicio = itemView.findViewById(R.id.im_ejercicio);

            itemView.setOnCreateContextMenuListener(this);
        }

        //Ejecutamos la función a partir de la posición obtenida en el adaptador
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            onEjerciciosListener.onAltaEjercicio(getAdapterPosition());
            return true;
        }

        //Inicializamos los items del menú
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem itemAlta = contextMenu.add(Menu.NONE, 1, 1, "Añadir ejercicio");
            itemAlta.setOnMenuItemClickListener(this);
        }
    }
}

