package com.example.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Dieta;
import com.example.proyecto.modelos.Usuario;


public class FragmentoPantallaPrincipal extends Fragment {

    ConexionSQLiteHelper conn;

    TextView tvDieta, tvDieta2, tvLink;
    ImageView imMisEjercicios, imMisClases;

    Button btnAgnadeEjercicios, btnMisEjercicios, btnAgnadeClases, btnMisClases, btnDietas;

    String idUs = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmento_pantalla_principal, container, false);

        //inicializamos la conexión
        conn = new ConexionSQLiteHelper(getContext(), "bdgimnasio", null, 1);

        tvDieta = view.findViewById(R.id.tv_principalDieta);
        tvDieta2 = view.findViewById(R.id.tv_principalDieta2);
        tvLink = view.findViewById(R.id.tv_link);
        imMisEjercicios = view.findViewById(R.id.im_misEjercicios);
        imMisClases = view.findViewById(R.id.im_misClases);
        btnAgnadeEjercicios = view.findViewById(R.id.btn_agnadeEjercicios);
        btnMisEjercicios = view.findViewById(R.id.btn_misEjercicios);
        btnAgnadeClases = view.findViewById(R.id.btn_agnadeClases);
        btnMisClases = view.findViewById(R.id.btn_misClases);
        btnDietas = view.findViewById(R.id.btn_dietas);

        imMisEjercicios.setImageResource(R.mipmap.principal_ejercicios);
        imMisClases.setImageResource(R.mipmap.misclases);

        //obtengo el id de usuario a partir de la variable estática del menú
        idUs = MenuSlideActivity.idUs;

        btnAgnadeEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para cambiar de un fragmento a otro hacemos una transacción
                Fragment nuevoFragmento = new FragmentoEjercicios();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(getView().getId(), nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnMisEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para cambiar de un fragmento a otro hacemos una transacción
                Fragment nuevoFragmento = new FragmentoTusEjercicios();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(getView().getId(), nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnAgnadeClases.setOnClickListener(new View.OnClickListener() {
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

        btnMisClases.setOnClickListener(new View.OnClickListener() {
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

        btnDietas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para cambiar de un fragmento a otro hacemos una transacción
                Fragment nuevoFragmento = new FragmentoDietas();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(getView().getId(), nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Función para ver la dieta que está siguiendo el usuario
        consultoDieta();

        return view;
    }

    //Función para ver la dieta que está siguiendo el usuario
    public void consultoDieta() {
        SQLiteDatabase db = conn.getWritableDatabase();
        Usuario u = null;

        //pasamos el id a int para la consulta sql
        int id = Integer.parseInt(idUs);
        int idDieta = 0;

        Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_ID_DIETA +
                " FROM " + Utilidades.TABLA_USUARIOS +
                " WHERE " + Utilidades.CAMPO_ID + " = " + id, null);

        if(cursor.moveToNext()) {
            u = new Usuario();
            u.setIdDieta(cursor.getInt(0));
            //obtenemos el id de la dieta
            idDieta = u.getIdDieta();
        }

        //Si el id de la dieta no es 0
        if(idDieta != 0) {
            Dieta d = null;
            //obtenemos la dieta del usuario
            Cursor cursor2 = db.rawQuery("SELECT " + Utilidades.CAMPO_TIPO_DIETA +
                    " FROM " + Utilidades.TABLA_DIETAS +
                    " WHERE " + Utilidades.CAMPO_ID_DIETA + " = " + idDieta, null);

            String tipoDieta = "";

            if (cursor2.moveToNext()) {
                d = new Dieta();
                d.setTipoDieta(cursor2.getString(0));
                //obtenemos el tipo de dieta
                tipoDieta = d.getTipoDieta();
            }

            tvDieta.setText("Estas siguiendo la dieta " + tipoDieta + ".");
            tvDieta2.setText("Para obtener más información sobre esta dieta consulte eñ siguiente link: ");

            //según sea un tipo de dieta u otra
            String link = "";
            switch (tipoDieta) {
                case "hipocalorica":
                    link = "https://www.clara.es/belleza/cuerpo/dieta-hipocalorica_13897";
                    break;
                case "por puntos":
                    link = "https://www.merca2.es/2019/06/22/dieta-puntos-asi-hace/";
                    break;
                case "paleo":
                    link = "https://middlesexhealth.org/learning-center/espanol/articulos/dieta-paleo-qu-es-y-por-qu-es-tan-popular";
                    break;
                case "proteica":
                    link = "https://www.clara.es/belleza/cuerpo/dieta-proteica_16765";
                    break;
                case "detox":
                    link = "https://www.dietox.es/que-es-la-dieta-detox-y-beneficios/";
                    break;
                default:
                    link = "";
            }

            //Si el link existe
            if (!link.equals("")) {
                tvLink.setText(link);
                //Para subrayar el texto
                tvLink.setPaintFlags(tvLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                String finalLink = link;
                tvLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Para redirigir a una página web
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalLink));
                        startActivity(intent);
                    }
                });
            }

        }
        else {
            tvDieta.setText("No sigues ninguna dieta");
            tvDieta2.setText("Si quiere seguir una dieta vaya al apartado de dietas");
            tvLink.setText("");
        }

        //cerramos
        db.close();
    }
}