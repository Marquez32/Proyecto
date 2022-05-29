package com.example.proyecto.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto.FragmentoPantallaPrincipal;
import com.example.proyecto.FragmentoTusEjercicios;
import com.example.proyecto.MenuSlideActivity;
import com.example.proyecto.R;
import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.databinding.FragmentHomeBinding;
import com.example.proyecto.modelos.Imagen;
import com.example.proyecto.modelos.Usuario;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class HomeFragment extends Fragment {

    ConexionSQLiteHelper conn;

    ImageView imBienvenido;
    Button btnBienvenido;

    int idImagen = 0;
    String idUs = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //inicializamos la conexión
        conn = new ConexionSQLiteHelper(getContext(), "bdgimnasio", null, 1);

        imBienvenido = view.findViewById(R.id.im_bienvenido);
        btnBienvenido = view.findViewById(R.id.btn_bienvenida);

        //leemos el id de usuario que se encuentra en el fichero
        leerFichero();
        //seleccionamos el id de la imagen que corresponde al usuario
        seleccionarIdImagenUsuario();
        //buscamos la imagen
        Bitmap b = buscarImagen();
        if(b == null) {
            imBienvenido.setImageResource(R.mipmap.ic_correr);
        }
        else {
            imBienvenido.setImageBitmap(b);
        }

        btnBienvenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para cambiar de un fragmento a otro hacemos una transacción
                Fragment nuevoFragmento = new FragmentoPantallaPrincipal();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(getView().getId(), nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    //Función para destruir la vista
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //Función para seleccionar el id de la imagen que tiene el usuario
    public void seleccionarIdImagenUsuario() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Usuario u = null;
        //pasamos el id a int para lac onsulta sql
        int id = Integer.parseInt(idUs);

        Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_ID_IMAGEN +
                " FROM " + Utilidades.TABLA_USUARIOS +
                " WHERE " + Utilidades.CAMPO_ID + " = " + id, null);

        //Si existe
        if(cursor.moveToNext()) {
            u = new Usuario();
            u.setIdImagen(cursor.getInt(0));
            //obtenemos el id de la imagen
            idImagen = u.getIdImagen();
        }

        //cerramos
        cursor.close();

        db.close();

    }

    //Función para buscar la imagen en la BBDD
    public Bitmap buscarImagen(){
        SQLiteDatabase db = conn.getReadableDatabase();

        Imagen i = null;

        //Buscamos la imagen a partir del id obtenido en la función anterior
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_IMAGENES +
                " WHERE " + Utilidades.CAMPO_ID_IMAGEN + " = " + idImagen , null);
        Bitmap bitmap = null;

        //Si existe una imagen con ese id
        if(cursor.moveToNext()){
            i = new Imagen();
            i.setIdImagen(cursor.getInt(0));

            //obtenemos el id de la imagen
            idImagen = i.getIdImagen();

            //obtenemos la cadena de bytes de la imagen
            byte[] blob = cursor.getBlob(1);

            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            //decodificamos la cadena de bytes
            bitmap = BitmapFactory.decodeStream(bais);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        //cerramos
        db.close();
        return bitmap;
    }

    //Función para leer el id de usuario del fichero
    public void leerFichero() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = getActivity().openFileInput("idUsuario.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            idUs = bufferedReader.readLine();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}