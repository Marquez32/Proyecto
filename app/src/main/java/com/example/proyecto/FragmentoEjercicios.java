package com.example.proyecto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.adaptadores.AdaptadorEjercicios;
import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Ejercicio;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentoEjercicios extends Fragment
        implements SearchView.OnQueryTextListener, AdaptadorEjercicios.OnEjerciciosListener {

    ConexionSQLiteHelper conn;

    SearchView svBuscador;
    EditText etPeso, etFecha, etNumSeries;

    ArrayList<Ejercicio> listaEjercicios = new ArrayList<>();

    RecyclerView.LayoutManager lm;
    RecyclerView rv;

    AdaptadorEjercicios ae;

    int idEj = 0;
    String idUs = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_ejercicios, container, false);

        //inicializamos la conexión
        conn = new ConexionSQLiteHelper(getContext(), "bdgimnasio", null, 1);

        svBuscador = view.findViewById(R.id.sv_buscador);

        etPeso = view.findViewById(R.id.et_peso);
        etFecha = view.findViewById(R.id.et_fechaEjercicios);
        etNumSeries = view.findViewById(R.id.et_numSeries);

        //Formato de la fecha
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        //Obtenemos la fecha actual a partir de la clase calendar
        Date dateObj = calendar.getTime();
        String formattedDate = dtf.format(dateObj);
        etFecha.setText(formattedDate);

        //Función que nos devuelve todos los ejercicios que hay en la BBDD
        consultarListaEjercicios();

        //inicializamos el adaptador
        ae = new AdaptadorEjercicios(listaEjercicios, getContext(), this);

        lm = new LinearLayoutManager(getContext());
        rv = view.findViewById(R.id.rv_ejercicios);
        rv.setLayoutManager(lm);
        rv.setAdapter(ae);

        //Función para inicializar el SearchView
        initListener();

        //obtengo el id de usuario a partir de la variable estática del menú
        idUs = MenuSlideActivity.idUs;

        return view;
    }

    //Función que nos devuelve todos los ejercicios que hay en la base de datos
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

            //añadimos el ejerccio a la lista
            listaEjercicios.add(e);

        }

        //cerramos
        cursor.close();

        db.close();
    }

    //Función para aplicar la implementación del SearchView
    private void initListener() {
        svBuscador.setOnQueryTextListener(this);
    }

    //Función para añadir un ejercicio a un usuario
    public void addUsuarioEjercicio(int posicion) {
        SQLiteDatabase db = conn.getWritableDatabase();

        //obtenemos el peso y el número de series
        String peso = etPeso.getText().toString();
        String numSeries = etNumSeries.getText().toString();

        //obtenemos el id de ejercicio
        idEj = listaEjercicios.get(posicion).getIdEj();

        //parseamos los datos
        double p = 0;
        try {
            p = Double.parseDouble(peso);
        }
        catch(NumberFormatException ex) {
            ex.printStackTrace();
        }

        int nSeries = 0;
        try {
            nSeries = Integer.parseInt(numSeries);
        }
        catch(NumberFormatException ex) {
            ex.printStackTrace();
        }

        //declaramos la variable fecha y cogemos el número hasta el siguiente guión -
        String fecha = etFecha.getText().toString();
        String [] vector = fecha.split("-");

        boolean fechaValida = true;

        //Si el formato de la fecha es correcto (AAAA-MM-dd)
        if(fecha.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
            //casteamos las variables
            int agno = Integer.parseInt(vector[0]);
            int mes = Integer.parseInt(vector[1]);
            int dia = Integer.parseInt(vector[2]);

            //Si no existe ese mes
            if( mes < 1 || mes > 12) {
                fechaValida = false;
                toastError("El mes no puede ser mayor de 12 ni menor de 1");
            }
            //Si no existe ese día
            else if(dia < 1) {
                fechaValida = false;
                toastError("El día tiene que ser como mínimo 1");
            }
            else {
                //meses de enero, marzo, mayo, julio, agosto, octubre y diciembre (31 días)
                if(mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                    if(dia > 31) {
                        fechaValida = false;
                        toastError("No hay más de 31 días en este mes");
                    }
                }
                //meses de abril, junio, septiembre y noviembre (30 días)
                else if(mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                    if(dia > 30) {
                        fechaValida = false;
                        toastError("No hay más de 30 días en este mes");
                    }
                }
                //mes de febrero (28 o 29 días)
                else {
                    //año bisiesto (29 días)
                    if ((agno % 4 == 0) && ((agno % 100 != 0) || (agno % 400 == 0))) {
                        if(dia > 29) {
                            fechaValida = false;
                            toastError("No hay más de 29 días en este mes");
                        }
                    }
                    //año no bisiesto (28 días)
                    else {
                        if(dia > 28) {
                            fechaValida = false;
                            toastError("No hay más de 28 días en este mes");
                        }
                    }
                }
            }

            //Si la fecha es válida insertamos el ejercicio
            if(fechaValida) {
                //metemos los datos a la tabla Usuario_Ejercicio
                ContentValues values = new ContentValues();
                values.put(Utilidades.CAMPO_ID, idUs);
                values.put(Utilidades.CAMPO_ID_EJ, idEj);
                values.put(Utilidades.CAMPO_FECHA_REALIZADO, etFecha.getText().toString());
                values.put(Utilidades.CAMPO_PESO, p);
                values.put(Utilidades.CAMPO_NUM_SERIES, nSeries);

                //Hacemos el insert
                db.insert(Utilidades.TABLA_USUARIOS_EJERCICIOS, null, values);

                toastCorrecto("Ejercicio añadido correctamente");
            }

        }
        else {
            toastError("El formato de la fecha debe ser AAAA-MM-dd");
        }

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //Función que nos da las sugerencias según escribamos en el buscador
    @Override
    public boolean onQueryTextChange(String newText) {
        ae.filtro(newText);
        return false;
    }

    //Interfaz para dar de alta un Ejercicio al Usuario
    @Override
    public void onAltaEjercicio(int posicion) {
        addUsuarioEjercicio(posicion);
    }
}