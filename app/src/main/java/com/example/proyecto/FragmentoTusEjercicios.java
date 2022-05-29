package com.example.proyecto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.adaptadores.AdaptadorEjercicios;
import com.example.proyecto.adaptadores.AdaptadorUsuarioEjercicios;
import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Ejercicio;
import com.example.proyecto.modelos.Usuario_Ejercicio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentoTusEjercicios extends Fragment {
    ConexionSQLiteHelper conn;

    EditText etFecha;
    Button btnConsultar, btnBorrar;

    ArrayList<Usuario_Ejercicio> listaUsuarioEjercicios = new ArrayList<>();
    ArrayList<Ejercicio> listaEjercicios = new ArrayList<>();

    RecyclerView.LayoutManager lm;
    RecyclerView rv;

    AdaptadorUsuarioEjercicios aue;

    String idUs = "";
    int pos = 0;
    int idEj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmento_tus_ejercicios, container, false);

        //Inicializamos la conexión
        conn = new ConexionSQLiteHelper(getContext(), "bdgimnasio", null, 1);

        etFecha = view.findViewById(R.id.et_fecha);
        btnConsultar = view.findViewById(R.id.btn_consultar);
        btnBorrar = view.findViewById(R.id.btn_borrarEj);

        //Formato de la fecha
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        //Obtenemos la fecha actual a partir de la clase calendar
        Date dateObj = calendar.getTime();
        String formattedDate = dtf.format(dateObj);
        etFecha.setText(formattedDate);

        //obtengo el id de usuario a partir de la variable estática del menú
        idUs = MenuSlideActivity.idUs;

        //Función que nos devuelve todos los ejercicios que hay en la BBDD
        consultarListaEjercicios();

        lm = new LinearLayoutManager(getContext());
        rv = view.findViewById(R.id.rv_tusEjercicios);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inicializamos la lista de Ejercicios del Usuario
                listaUsuarioEjercicios = new ArrayList<>();
                consultarTusEjerciciosPorFecha();
                //Inicializamos el adaptador
                aue = new AdaptadorUsuarioEjercicios(listaUsuarioEjercicios, listaEjercicios,
                        getContext());
                rv.setLayoutManager(lm);
                rv.setAdapter(aue);
                //Al clickar en un elemento del adaptador
                aue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //obtenemos la posición de la lista
                        pos = rv.getChildAdapterPosition(view);
                        //a partir de esa posición obtenemos el id
                        idEj = listaUsuarioEjercicios.get(pos).getIdEj();
                        String nomEj = "";
                        //Buscamos en la lista total de Ejercicios
                        for(Ejercicio e: listaEjercicios) {
                            //Si el id coincide con el id de la lista total
                            if(e.getIdEj() == idEj) {
                                nomEj = e.getNombreEj();
                            }
                        }
                        Toast.makeText(getContext(), "Has seleccionado el ejercicio: " +
                                nomEj, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Hacemos un diálogo de confirmación
                    AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
                    ventana.setTitle("Ejercicios");
                    ventana.setMessage("¿Seguro que quieres eliminar el ejercicio?");
                    //Si pulsas que sí
                    ventana.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //borras el ejercicio de la BBDD y del ArrayList
                            borrarEjercicio();
                            listaUsuarioEjercicios.remove(pos);
                            toastCorrecto("El ejercicio se ha eliminado correctamente");
                            //actualizamos el adaptador
                            aue.notifyDataSetChanged();
                        }
                    });
                    //Si pulsas que no
                    ventana.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            toastError("Has cancelado la operación");
                        }
                    });
                    //IMPORTANTE para que se muestre el dialog
                    ventana.show();
                }
                catch(IndexOutOfBoundsException e){
                    e.printStackTrace();
                    toastError("No has seleccionado ningún ejercicio");
                }

            }
        });

        return view;
    }

    //Función para consultar los ejercicios por la fecha de realización
    public void consultarTusEjerciciosPorFecha() {

        //pasamos el id a int para la consulta sql
        int id = Integer.parseInt(idUs);

        SQLiteDatabase db = conn.getReadableDatabase();
        Usuario_Ejercicio ue = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_USUARIOS_EJERCICIOS +
                " WHERE " + Utilidades.CAMPO_ID + " = " + id + " AND " +
                Utilidades.CAMPO_FECHA_REALIZADO + " LIKE '" + etFecha.getText().toString() + "'", null);

        //Formato de la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //Mientras haya siguiente
        while(cursor.moveToNext()) {
            ue = new Usuario_Ejercicio();
            ue.setId(cursor.getInt(0));
            ue.setIdEj(cursor.getInt(1));
            try {
                ue.setFechaRealizado(sdf.parse(cursor.getString(2)));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            ue.setPeso(cursor.getDouble(3));
            ue.setNumSeries(cursor.getInt(4));

            //añadimos el ejercicio
            listaUsuarioEjercicios.add(ue);
        }

        //cerramos
        cursor.close();
    }

    //Función para consultar la lista total de ejercicios
    public void consultarListaEjercicios() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Ejercicio e = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_EJERCICIOS, null);

        //Mientras haya siguiente
        while(cursor.moveToNext()) {
            e = new Ejercicio();
            e.setIdEj(cursor.getInt(0));
            e.setNombreEj(cursor.getString(1));
            e.setZonaTrabajada(cursor.getString(2));
            e.setIntensidad(cursor.getString(3));

            //añadimos el ejercicio a la lista
            listaEjercicios.add(e);

        }

        //cerramos
        cursor.close();
    }

    //Función para borrar un ejercicio de un usuario
    public void borrarEjercicio() {
        SQLiteDatabase db = conn.getWritableDatabase();

        //obtenemos el id de usuario y el id del ejercicio
        int idUs = listaUsuarioEjercicios.get(pos).getId();
        int idEj = listaUsuarioEjercicios.get(pos).getIdEj();
        //sentencia sql
        db.execSQL("DELETE FROM " + Utilidades.TABLA_USUARIOS_EJERCICIOS +
                " WHERE " + Utilidades.CAMPO_ID + " = " + idUs + " AND " +
                Utilidades.CAMPO_ID_EJ + " = " + idEj + " AND " +
                Utilidades.CAMPO_FECHA_REALIZADO + " LIKE '" + etFecha.getText().toString() + "'");
        //cerramos
        db.close();
    }

    //Función que nos devuelve un toast personalizado
    public void toastCorrecto(String mensaje) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, null);
        TextView txt = view.findViewById(R.id.txtMessage1);
        txt.setText(mensaje);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    //Función que nos devuelve un toast de error personalizado
    public void toastError(String mensaje) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_mistake, null);
        TextView txt = view.findViewById(R.id.txtMessage2);
        txt.setText(mensaje);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

}