package com.example.proyecto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.adaptadores.AdaptadorClases;
import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Clase;
import com.google.type.DateTime;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FragmentoClases extends Fragment implements SearchView.OnQueryTextListener, AdaptadorClases.OnClasesListener{

    ConexionSQLiteHelper conn;

    SearchView svClases;
    EditText etFecha;
    Button btnFiltroFecha, btnIrTusClases;

    ArrayList<Clase> listaClases = new ArrayList<>();
    RecyclerView.LayoutManager lm;
    RecyclerView rv;

    AdaptadorClases ac;

    int diaSemana = 0;

    String idUs = "";
    int idClase = 0;
    int pos = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmento_clases, container, false);

        //inicializamos la conexión
        conn = new ConexionSQLiteHelper(getContext(), "bdgimnasio", null, 1);

        svClases = view.findViewById(R.id.sv_clases);
        etFecha = view.findViewById(R.id.et_fechaClases);
        btnFiltroFecha = view.findViewById(R.id.btn_filtrarPorFecha);
        btnIrTusClases = view.findViewById(R.id.btn_irTusClases);

        //Función que nos devuelve todas las clases de la BBDD
        consultarClases();
        //obtengo el id de usuario a partir de la variable estática del menú
        idUs = MenuSlideActivity.idUs;

        //Inicializamos el adaptador
        ac = new AdaptadorClases(listaClases, getContext(), this);

        lm = new LinearLayoutManager(getContext());
        rv = view.findViewById(R.id.rv_clases);
        rv.setLayoutManager(lm);
        rv.setAdapter(ac);

        //Formato de la fecha
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        //Obtenemos la fecha actual a partir de clase Calendar
        Date dateObj = calendar.getTime();
        String formattedDate = dtf.format(dateObj);
        etFecha.setText(formattedDate);

        //Función para aplicar la implementación del SearchView
        initListener();

        //Con esta función obtenemos el día de la semana a partir de una fecha determinada
        btnFiltroFecha.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //declaramos la variable fecha
                String fecha = etFecha.getText().toString();
                try {
                    Date f = dtf.parse(fecha);
                    calendar.setTime(f);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //cogemos el número hasta el siguiente guión -
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
                        if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                            if (dia > 31) {
                                fechaValida = false;
                                toastError("No hay más de 31 días en este mes");
                            }
                        }
                        //meses de abril, junio, septiembre y noviembre (30 días)
                        else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                            if (dia > 30) {
                                fechaValida = false;
                                toastError("No hay más de 30 días en este mes");
                            }
                        }
                        //mes de febrero (28 o 29 días)
                        else {
                            //año bisiesto (29 días)
                            if ((agno % 4 == 0) && ((agno % 100 != 0) || (agno % 400 == 0))) {
                                if (dia > 29) {
                                    fechaValida = false;
                                    toastError("No hay más de 29 días en este mes");
                                }
                            }
                            //año no bisiesto (28 días)
                            else {
                                if (dia > 28) {
                                    fechaValida = false;
                                    toastError("No hay más de 28 días en este mes");
                                }
                            }
                        }
                    }
                }
                else {
                    fechaValida = false;
                    toastError("El formato de la fecha debe ser AAAA-MM-dd");
                }

                //Si la fecha es válida
                if(fechaValida) {
                    diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
                    //obtenemos el día de la semana a partir del número devuelto
                    filtrarPorFecha();
                    ac.notifyDataSetChanged();
                }

            }
        });

        btnIrTusClases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para cambiar de un fragmento a otro hacemos una transacción
                Fragment nuevoFragmento = new FragmentoTusClases();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(getView().getId(), nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

    //Función que nos devuelve todas las clases de la BBDD
    public void consultarClases() {
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

    //Función para obtener el día de la semana de una fecha determinada
    public void filtrarPorFecha() {
        String dia = "";
        switch (diaSemana) {
            case 1:
                dia = "domingo";
                break;
            case 2:
                dia = "lunes";
                break;
            case 3:
                dia = "martes";
                break;
            case 4:
                dia = "miercoles";
                break;
            case 5:
                dia = "jueves";
                break;
            case 6:
                dia = "viernes";
                break;
            case 7:
                dia = "sabado";
                break;
        }

        //limpiamos la lista de clases
        listaClases.clear();

        SQLiteDatabase db = conn.getReadableDatabase();
        Clase c = null;
        //consulta sql
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_CLASES +
                " WHERE " + Utilidades.CAMPO_DIA_SEMANA + " LIKE '" + dia + "'", null);

        //mientras haya siguiente
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

    //Función para dar de alta una clase a un usuario
    public void altaUsuarioClase(int posicion) {
        pos = posicion;

        //obetenemos el id de la clase a partir de su posición
        idClase = listaClases.get(pos).getIdClase();

        SQLiteDatabase db = conn.getWritableDatabase();

        //Metemos los valores a la tabla Usuario_Clase
        ContentValues values = new ContentValues();

        values.put(Utilidades.CAMPO_ID, idUs);
        values.put(Utilidades.CAMPO_ID_CLASE, idClase);

        //Hacemos el insert
        db.insert(Utilidades.TABLA_USUARIOS_CLASES, null, values);

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

    //Función para aplicar la implementación del SearchView
    private void initListener() {
        svClases.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String textoNuevo) {
        return false;
    }

    //Función que nos da las sugerencias según escribamos en el buscador
    @Override
    public boolean onQueryTextChange(String textoNuevo) {
        ac.filtro(textoNuevo);
        return false;
    }

    //Interfaz para dar de alta una clase a un usuario
    @Override
    public void OnAltaClase(int posicion) {
        altaUsuarioClase(posicion);
        toastCorrecto("La clase se ha añadido correctamente");
    }
}