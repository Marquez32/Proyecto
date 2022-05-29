package com.example.proyecto.adaptadores;

import android.content.Context;
import android.os.Build;
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
import com.example.proyecto.modelos.Clase;
import com.example.proyecto.modelos.Ejercicio;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AdaptadorClases extends RecyclerView.Adapter<AdaptadorClases.ContenedorClases>
            implements View.OnClickListener{

    ArrayList<Clase> listaClases;
    ArrayList<Clase> originalListaClases;
    Context context;
    OnClasesListener onClasesListener;

    private View.OnClickListener listener;

    //Constructor
    public AdaptadorClases(ArrayList<Clase> listaClases, Context context, OnClasesListener onClasesListener) {
        this.listaClases = listaClases;
        this.context = context;
        this.onClasesListener = onClasesListener;
        originalListaClases = new ArrayList<>();
        originalListaClases.addAll(listaClases);
    }

    @NonNull
    @Override
    public ContenedorClases onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflamos la vista e inicializamos el setOnClickListener
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clases, parent, false);
        view.setOnClickListener(this);
        return new AdaptadorClases.ContenedorClases(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContenedorClases holder, int position) {
        //sacamos por pantalla los datos correspondientes
        holder.tvHoraInicio.setText(listaClases.get(position).getHoraInicio());
        holder.tvHoraFin.setText(listaClases.get(position).getHoraFin());
        holder.tvDiaSemana.setText(listaClases.get(position).getDiaSemana());
        holder.tvNombreClase.setText(listaClases.get(position).getNombreClase());

        //según sea un nivel u otro
        switch (listaClases.get(position).getNivel()) {
            //mostramos una imagen dependiendo del nivel
            case 1:
                holder.imNivel.setImageResource(R.mipmap.ic_fondoazul);
                break;
            case 2:
                holder.imNivel.setImageResource(R.mipmap.ic_fondo_amarillo);
                break;
            case 3:
                holder.imNivel.setImageResource(R.mipmap.ic_fondo_rojo);
                break;
        }
    }

    //Devolvemos el tamaño de la lista
    @Override
    public int getItemCount() {
        return listaClases.size();
    }

    //Función para realizar las búsquedas por nombre con el SearchView
    public void filtro(final String cadenaBusqueda) {
        if(cadenaBusqueda.length() == 0) {
            //limpiamos la lista y le añadimos la lista original
            listaClases.clear();
            listaClases.addAll(originalListaClases);
        }
        else {
            //limpiamos la lista
            listaClases.clear();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Uso del lenguaje Lambda para cumplir la condición del nombre
                ArrayList<Clase> coleccion = (ArrayList<Clase>) originalListaClases.stream()
                        .filter(i -> i.getNombreClase().toLowerCase().contains(cadenaBusqueda))
                        .collect(Collectors.toList());
                //añadimos los datos a la lista
                listaClases.addAll(coleccion);
            }
            else {
                //limpiamos la lista
                listaClases.clear();
                //Buscamos en la lista original y si cumple la condición lo añadimos a la lista
                for(Clase c: originalListaClases) {
                    if(c.getNombreClase().toLowerCase().contains(cadenaBusqueda)) {
                        listaClases.add(c);
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
    public interface OnClasesListener {
        public void OnAltaClase(int posicion);
    }

    //Clase contenedora
    public class ContenedorClases extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView tvHoraInicio, tvHoraFin, tvDiaSemana, tvNombreClase;
        ImageView imNivel;

        //Constructor
        public ContenedorClases(@NonNull View itemView) {
            super(itemView);
            tvHoraInicio = itemView.findViewById(R.id.tv_horaInicio);
            tvHoraFin = itemView.findViewById(R.id.tv_horaFin);
            tvDiaSemana = itemView.findViewById(R.id.tv_diaSemana);
            tvNombreClase = itemView.findViewById(R.id.tv_nombreClase);
            imNivel = itemView.findViewById(R.id.im_nivel);

            itemView.setOnCreateContextMenuListener(this);
        }

        //Ejecutamos la función a partir de la posición obtenida en el adaptador
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            onClasesListener.OnAltaClase(getAdapterPosition());
            return true;
        }

        //Inicializamos los items del menú
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem itemAlta = contextMenu.add(Menu.NONE, 1, 1, "Añadir clase");
            itemAlta.setOnMenuItemClickListener(this);
        }
    }
}
