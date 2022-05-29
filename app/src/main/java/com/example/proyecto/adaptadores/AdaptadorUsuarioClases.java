package com.example.proyecto.adaptadores;

import android.content.Context;
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
import com.example.proyecto.modelos.Usuario_Clase;

import java.util.ArrayList;

public class AdaptadorUsuarioClases extends RecyclerView.Adapter<AdaptadorUsuarioClases.ContenedorUsuarioClases>
            implements View.OnClickListener{

    ArrayList<Usuario_Clase> listaUsuarioClases;
    ArrayList<Clase> listaClases;
    Context context;
    OnUsuarioClasesListener onUsuarioClasesListener;

    private View.OnClickListener listener;

    //Constructor
    public AdaptadorUsuarioClases(ArrayList<Usuario_Clase> listaUsuarioClases, ArrayList<Clase> listaClases, Context context, OnUsuarioClasesListener onUsuarioClasesListener) {
        this.listaUsuarioClases = listaUsuarioClases;
        this.listaClases = listaClases;
        this.context = context;
        this.onUsuarioClasesListener = onUsuarioClasesListener;
    }

    @NonNull
    @Override
    public ContenedorUsuarioClases onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflamos la vista e inicializamos el setOnClickListener
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clases, parent, false);
        view.setOnClickListener(this);
        return new AdaptadorUsuarioClases.ContenedorUsuarioClases(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContenedorUsuarioClases holder, int position) {
        //sacamos por pantalla los datos correspondientes
        int idClase = listaUsuarioClases.get(position).getIdClase();

        String nombreClase = "", diaSemana = "", horaInicio = "", horaFin = "";
        int nivel = 0;

        //Buscamos los datos en la lista donde se encuentran todas las clases
        for(Clase c: listaClases) {
            if(c.getIdClase() == idClase) {
                nombreClase = c.getNombreClase();
                diaSemana = c.getDiaSemana();
                nivel = c.getNivel();
                horaInicio = c.getHoraInicio();
                horaFin = c.getHoraFin();
            }
        }

        holder.tvNombreClase.setText(nombreClase);
        holder.tvDiaSemana.setText(diaSemana);
        holder.tvHoraInicio.setText(horaInicio);
        holder.tvHoraFin.setText(horaFin);

        //según sea un nivel u otro mostramos una imagen
        switch (nivel) {
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
        return listaUsuarioClases.size();
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
    public interface OnUsuarioClasesListener {
        public void OnBorrarClase(int posicion);
    }

    //Clase contenedora
    public class ContenedorUsuarioClases extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        TextView tvHoraInicio, tvHoraFin, tvDiaSemana, tvNombreClase;
        ImageView imNivel;

        //Constructor
        public ContenedorUsuarioClases(@NonNull View itemView) {
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
            onUsuarioClasesListener.OnBorrarClase(getAdapterPosition());
            return true;
        }

        //Inicializamos los items del menú
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem itemBorrar = contextMenu.add(Menu.NONE, 1, 1, "Borrar clase");
            itemBorrar.setOnMenuItemClickListener(this);
        }
    }

}
