package com.example.proyecto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.adaptadores.AdaptadorUsuarioClases;
import com.example.proyecto.adaptadores.AdaptadorUsuarioEjercicios;
import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Clase;
import com.example.proyecto.modelos.Usuario_Clase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class FragmentoTusClases extends Fragment implements AdaptadorUsuarioClases.OnUsuarioClasesListener{

    ConexionSQLiteHelper conn;

    Spinner spDiaSemana;
    Button btnIrClases;

    ArrayList<Usuario_Clase> listaUsuarioClases = new ArrayList<>();
    ArrayList<Clase> listaClases = new ArrayList<>();

    RecyclerView.LayoutManager lm;
    RecyclerView rv;

    AdaptadorUsuarioClases auc;

    String idUs = "";
    String diaSeleccionado = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmento_tus_clases, container, false);

        //inicializamos la conexión
        conn = new ConexionSQLiteHelper(getContext(), "bdgimnasio", null, 1);

        spDiaSemana = view.findViewById(R.id.sp_diaSemana);
        btnIrClases = view.findViewById(R.id.btn_irClases);

        //obtengo el id de usuario a partir de la variable estática del menú
        idUs = MenuSlideActivity.idUs;

        //Función para obtener todas las clases de la BBDD
        consultarListaClases();
        //Función para consultar las clases que tenemos según el día que sea
        consultarClasesPorDiaSemana();

        lm = new LinearLayoutManager(getContext());
        rv = view.findViewById(R.id.rv_tusClases);

        //inicializamos el adaptador
        auc = new AdaptadorUsuarioClases(listaUsuarioClases, listaClases, getContext(), this);
        rv.setLayoutManager(lm);
        rv.setAdapter(auc);

        //Inicializamos el calendar y obtenemos el día de la semana actual
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_WEEK);

        String[] diaSemana;

        //Según sea un día u otro mostramos el spinner
        switch (dia) {
            case 3:
                diaSemana = new String[] {"martes", "miercoles", "jueves", "viernes", "lunes"};
                break;
            case 4:
                diaSemana = new String[] {"miercoles", "jueves", "viernes", "lunes", "martes"};
                break;
            case 5:
                diaSemana = new String[] {"jueves", "viernes", "lunes", "martes", "miercoles"};
                break;
            case 6:
                diaSemana = new String[] {"viernes", "lunes", "martes", "miercoles", "jueves"};
                break;
            default:
                diaSemana = new String[] {"lunes", "martes", "miercoles", "jueves", "viernes"};

        }

        //Inicializamos el adaptador del spinner
        ArrayAdapter<String> adaptadorDiaSemana = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, diaSemana);

        spDiaSemana.setAdapter(adaptadorDiaSemana);

        //Al clickar en una opción
        spDiaSemana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //guardamos el día seleccionado
                diaSeleccionado = adapterView.getItemAtPosition(i).toString();
                //consultamos las clases que tenemos ese día de la semana
                consultarClasesPorDiaSemana();
                auc.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Esto sirve para ir de un fragmento a otro
        btnIrClases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para cambiar de un fragmento a otro hacemos una transacción
                Fragment nuevoFragmento = new FragmentoClases();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(getView().getId(), nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

    //Función para obtener todas las clases de la BBDD
    public void consultarListaClases() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Clase c = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_CLASES, null);

        //Mientras haya siguiente
        while(cursor.moveToNext()) {
            c = new Clase();
            c.setIdClase(cursor.getInt(0));
            c.setNombreClase(cursor.getString(1));
            c.setNivel(cursor.getInt(2));
            c.setDiaSemana(cursor.getString(3));
            c.setHoraInicio(cursor.getString(4));
            c.setHoraFin(cursor.getString(5));

            //añadimos la clase a la lista
            listaClases.add(c);

        }

        //cerramos
        cursor.close();

        db.close();
    }

    //Función para consultar las clases que tenemos en un día de la semana en concreto
    public void consultarClasesPorDiaSemana() {
        //limpiamos la lista
        listaUsuarioClases.clear();

        //pasamos el id a int para la consulta sql
        int id = Integer.parseInt(idUs);

        SQLiteDatabase db = conn.getReadableDatabase();
        Usuario_Clase uc = null;

        //consulta sql con unión entre tablas
        //(Por eso no he utilizado la clase Utilidades ya que es muy incómodo)
        String consultaSql = "SELECT uc.id, c.idClase FROM usuarios_clases uc JOIN clases c " +
                "ON uc.idClase = c.idClase WHERE c.diaSemana = '" + diaSeleccionado + "' AND " +
                "uc.id = " + id;

        Cursor cursor = db.rawQuery(consultaSql, null);
        //Mientras haya siguiente
        while(cursor.moveToNext()) {
            uc = new Usuario_Clase();
            uc.setId(cursor.getInt(0));
            uc.setIdClase(cursor.getInt(1));

            //añadimos la clase al usuario
            listaUsuarioClases.add(uc);
        }

        //cerramos
        cursor.close();

        db.close();

    }

    //Función para borrar una clase del usuario
    public void borrarClase(int posicion) {
        SQLiteDatabase db = conn.getWritableDatabase();

        //Obtenemos el id del usuario y el id de la clase
        int id = listaUsuarioClases.get(posicion).getId();
        int idClase = listaUsuarioClases.get(posicion).getIdClase();

        //ejecutamos la sentencia sql
         db.execSQL("DELETE FROM " + Utilidades.TABLA_USUARIOS_CLASES +
         " WHERE " + Utilidades.CAMPO_ID + " = " + id + " AND " + Utilidades.CAMPO_ID_CLASE + " = " + idClase + ";");

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

    //Interfaz para borrar una clase de un usuario
    @Override
    public void OnBorrarClase(int posicion) {
        //Creamos un Dialog
        AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
        ventana.setTitle("Clase");
        ventana.setMessage("¿Seguro que quieres eliminar la clase?");
        //SI pulsamos que sí
        ventana.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //borramos la clase de la BBDD y del ArrayList
                borrarClase(posicion);
                listaUsuarioClases.remove(posicion);
                toastCorrecto("Clase eliminada con éxito");
                //actualizamos el adaptador
                auc.notifyDataSetChanged();
            }
        });
        //Si pulsamos que no
        ventana.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toastError("Has cancelado la operación");
            }
        });
        //IMPORTANTE para que se muestre el Dialog
        ventana.show();
    }

}