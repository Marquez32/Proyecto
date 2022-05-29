package com.example.proyecto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Dieta;


public class FragmentoDietas extends Fragment {

    ConexionSQLiteHelper conn;

    TextView tvDescripcionDieta, tvNombreDieta, tvObservacionesDieta;
    Spinner spDietas;
    Button btnAgnadirDieta, btnBorrarDieta;
    ImageView imDieta, imDieta2;

    String dietaSeleccionada = "";
    int idDieta = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmento_dietas, container, false);

        //Inicializamos la conexión
        conn = new ConexionSQLiteHelper(getContext(), "bdgimnasio", null, 1);

        tvDescripcionDieta = view.findViewById(R.id.tv_descripcionDieta);
        tvNombreDieta = view.findViewById(R.id.tv_nombreDieta);
        tvObservacionesDieta = view.findViewById(R.id.tv_observacionesDieta);
        spDietas = view.findViewById(R.id.sp_dietas);
        btnAgnadirDieta = view.findViewById(R.id.btn_agnadirDieta);
        btnBorrarDieta = view.findViewById(R.id.btn_borrarDieta);
        imDieta = view.findViewById(R.id.im_dieta);
        imDieta2 = view.findViewById(R.id.im_dieta2);

        //Inicializamos el spinner con los tipos de dietas
        String[] tipoDieta = new String[] {"hipocalorica", "por puntos", "paleo",
                "proteica", "detox"};

        //Inicializamos el adaptador del spinner
        ArrayAdapter<String> adaptadorTipoDieta = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, tipoDieta);

        spDietas.setAdapter(adaptadorTipoDieta);

        //Al pulsar un item del spinner
        spDietas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //obtenemos la dieta seleccionada
                dietaSeleccionada = adapterView.getItemAtPosition(i).toString();
                //Función que nos devuelve información del tipo de dieta seleccionada
                consultarTipoDieta();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAgnadirDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Función para asignar una dieta al usuario
                asignarDieta();
                toastCorrecto("Dieta asignada con éxito");
            }
        });

        btnBorrarDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inicializamos el Dialog
                AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
                ventana.setTitle("Dieta");
                ventana.setMessage("¿Seguro que quieres eliminar tu dieta?");
                //SI pulsamos que sí
                ventana.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Borramos la dieta de la BBDD
                        borrarDieta();
                        toastCorrecto("Dieta eliminada con éxito");
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
        });

        return view;
    }

    //Función que nos devuelve información del tipo de dieta seleccionada
    public void consultarTipoDieta() {
        tvNombreDieta.setText("");
        tvDescripcionDieta.setText("");
        tvObservacionesDieta.setText("");

        SQLiteDatabase db = conn.getReadableDatabase();
        Dieta d = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_DIETAS +
                " WHERE " + Utilidades.CAMPO_TIPO_DIETA + " LIKE '" + dietaSeleccionada + "'", null);

        if(cursor.moveToNext()) {
            d = new Dieta();
            d.setIdDieta(cursor.getInt(0));
            d.setNombreDieta(cursor.getString(1));
            d.setTipoDieta(cursor.getString(2));
            d.setDescripcionDieta(cursor.getString(3));
            d.setObservacionesDieta(cursor.getString(4));

            //mostramos la información
            tvNombreDieta.setText(d.getNombreDieta());
            tvDescripcionDieta.setText(d.getDescripcionDieta());
            tvObservacionesDieta.setText(d.getObservacionesDieta());

            //obtenemos el id de la dieta
            idDieta = d.getIdDieta();

            //Según el tipo de dieta
            switch (d.getTipoDieta()) {
                //mostramos una imagen u otra
                //Con View.Gone hacemos una imagen invisible
                //Con View.Visible hacemos una imagen visible
                case "hipocalorica":
                    imDieta.setImageResource(R.mipmap.hipocalorica);
                    imDieta2.setVisibility(View.GONE);
                    break;
                case "por puntos":
                    imDieta.setImageResource(R.mipmap.porpuntos);
                    imDieta2.setVisibility(View.VISIBLE);
                    imDieta2.setImageResource(R.mipmap.puntosdieta);
                    break;
                case "paleo":
                    imDieta.setImageResource(R.mipmap.paleo);
                    imDieta2.setVisibility(View.GONE);
                    break;
                case "proteica":
                    imDieta.setImageResource(R.mipmap.proteica);
                    imDieta2.setVisibility(View.GONE);
                    break;
                case "detox":
                    imDieta.setImageResource(R.mipmap.dieta_detox);
                    imDieta2.setVisibility(View.GONE);
                    break;
            }

        }

        //cerramos
        cursor.close();

        db.close();
    }

    //Función que asigna una dieta al usuario
    public void asignarDieta() {
        SQLiteDatabase db = conn.getWritableDatabase();
        //obtengo el id de usuario a partir de la variable estática del menú
        String idUs = MenuSlideActivity.idUs;
        //Ejecuto la sentencia sql
        db.execSQL("UPDATE " + Utilidades.TABLA_USUARIOS +
                " SET " + Utilidades.CAMPO_ID_DIETA + " = " + idDieta +
                " WHERE " + Utilidades.CAMPO_ID + " = " + idUs);

        //cerramos
        db.close();
    }

    //Función para borrar una dieta de la BBDD
    public void borrarDieta() {
        SQLiteDatabase db = conn.getWritableDatabase();
        //obtengo el id de usuario a partir de la variable estática del menú
        String idUs = MenuSlideActivity.idUs;
        //Ejecuto la sentencia sql
        db.execSQL("UPDATE " + Utilidades.TABLA_USUARIOS +
                " SET " + Utilidades.CAMPO_ID_DIETA + " = null " +
                " WHERE " + Utilidades.CAMPO_ID + " = " + idUs);

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