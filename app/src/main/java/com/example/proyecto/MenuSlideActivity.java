package com.example.proyecto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Imagen;
import com.example.proyecto.modelos.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.databinding.ActivityMenuSlideBinding;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MenuSlideActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuSlideBinding binding;

    public static String idUs;

    ImageView iconoCabecera;
    TextView textoNick;
    TextView textoCorreo;

    View headView;

    ConexionSQLiteHelper conn;

    Bitmap b;

    int idImagen = 0;
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuSlideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuSlide.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        //inicializamos la conexión
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bdgimnasio", null, 1);

        //leemos el fichero para obtener el id del usuario
        leerFichero();

        navigationView.setItemIconTintList(null);

        //Obtenemos los datos que le pasamos desde el activity de inicio de sesión
        Bundle extras = getIntent().getExtras();

        //Inicilizamos la vista de la cabecera con sus objetos
        headView = navigationView.getHeaderView(0);

        textoNick = headView.findViewById(R.id.titulo_cabecera);
        //mostramos el nick en el textView
        textoNick.setText(extras.getString("NICK"));

        textoCorreo = headView.findViewById(R.id.subtitulo_cabecera);
        //mostramos el correo en el textView
        textoCorreo.setText(extras.getString("CORREO"));

        iconoCabecera = headView.findViewById(R.id.icono_cabecera);

        //Seleccionamos el id del usuario con esta función
        seleccionarIdImagenUsuario();
        //Buscamos la imagen con la función
        b = buscarImagen();
        //Si la imagen es nula mostramos la imagen por defecto
        if(b == null) {
            iconoCabecera.setImageResource(R.mipmap.ic_correr);
        }
        else {
            //mostramos la imagen seleccionada con los parámetros asignados
            iconoCabecera.setImageBitmap(b);
            int ancho = 200;
            int alto = 200;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
            iconoCabecera.setLayoutParams(params);
        }

        // Inicializamos el menú lateral con todas los
        // fragmentos que vamos a mostrar al pulsar el desplegable
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.fragmentoEjercicios,
                R.id.fragmentoTusEjercicios, R.id.fragmentoClases,
                R.id.fragmentoTusClases, R.id.fragmentoDietas, R.id.fragmentoPantallaPrincipal)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_slide);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    //Función para crear el menú de arriba a la derecha
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el menú de arriba a la derecha
        getMenuInflater().inflate(R.menu.menu_slide, menu);
        return true;
    }

    //Función para seleccionar un item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Según el item seleccionado
        switch (item.getItemId()) {
            case R.id.item_cerrarSesion:
                toastCorrecto("Sesión cerrada correctamente");
                //Vamos a la actividad de inicio de sesión
                Intent i = new Intent(MenuSlideActivity.this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.item_fotoPerfil:
                //elegimos la foto de perfil
                elegirFotoGaleria();
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    //Función que sirve para navegar a través del menú lateral
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_slide);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //Función para determinar lo que hace al pulsar el botón de atrás
    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
        super.onBackPressed();
    }

    //Función que lee el fichero para obtener el id del usuario
    public void leerFichero() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("idUsuario.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            idUs = bufferedReader.readLine();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Función que nos devuelve un toast personalizado
    public void toastCorrecto(String mensaje) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.customToast));
        TextView txt = view.findViewById(R.id.txtMessage1);
        txt.setText(mensaje);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    //Función que nos lleva al directorio de imágenes del móvil
    public void elegirFotoGaleria() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Selecciona"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //Seleccionamos una imagen
            Uri miPath = data.getData();
            //Si la imagen no está corrupta la imagen se inserta correctamente
            iconoCabecera.setImageURI(miPath);
            int ancho = 200;
            int alto = 200;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
            iconoCabecera.setLayoutParams(params);

            //Damos caché
            iconoCabecera.setDrawingCacheEnabled(true);

            // Este es el código importante :)
            //Sin esto la vista siempre va a tener una dimensión de 0,0 y el mipmap será null
            iconoCabecera.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            iconoCabecera.layout(0, 0, iconoCabecera.getMeasuredWidth(), iconoCabecera.getMeasuredHeight());

            //Inicilizamos el bitmap con el icono que hemos insertado en la vista de la cabecera
            b = Bitmap.createBitmap(iconoCabecera.getDrawingCache());
            //limpiamos el caché
            iconoCabecera.setDrawingCacheEnabled(false);

            //Guardamos la imagen y actualizamos la foto del usuario
            guardarImagen(b);
            contarId();
            actualizarFotoUsuario();
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }

    }

    //Función para guardar la imagen en la base de datos
    public void guardarImagen(Bitmap bitmap){
        // tamaño del baos depende del tamaño de tus imagenes en promedio
        ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
        //Comprimimos la imagen en el formato PNG
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 , baos);
        byte[] blob = baos.toByteArray();
        // aqui tenemos el byte[] con el imagen comprimido, ahora lo guardaremos en SQLite
        SQLiteDatabase db = conn.getWritableDatabase();

        //Insertamos la imagen en la tabla imagenes
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_IMG, blob);
        db.insert(Utilidades.TABLA_IMAGENES, null, values);
        db.close();

    }

    //Contamos el número de ids de imágenes que hay ya que el último id será la foto de perfil
    //del usuario con el que estemos logeados
    public void contarId() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + Utilidades.TABLA_IMAGENES, null);
        cursor.moveToFirst();

        //Obtenemos el número de id's
        contador = cursor.getInt(0);

        cursor.close();
    }

    //Función para buscar la imagen en la BBDD
    public Bitmap buscarImagen(){
        SQLiteDatabase db = conn.getReadableDatabase();

        Imagen i = null;

        //Consulta para buscar la imagen
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_IMAGENES +
                " WHERE " + Utilidades.CAMPO_ID_IMAGEN + " = " + idImagen , null);
        //Inicializamos el bitmap
        Bitmap bitmap = null;

        if(cursor.moveToNext()){
            i = new Imagen();
            i.setIdImagen(cursor.getInt(0));

            //Obtenemos el id de imagen
            idImagen = i.getIdImagen();

            //Obtenemos la cadena de bytes de la imagen
            byte[] blob = cursor.getBlob(1);

            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            //decodificamos la cadeba de bytes
            bitmap = BitmapFactory.decodeStream(bais);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        //devolvemos el bitmap
        return bitmap;
    }

    //Función para actualizar la foto del usuario
    public void actualizarFotoUsuario() {
        SQLiteDatabase db = conn.getWritableDatabase();
        //Ejecutamos la sentencia SQL para actualizar el usuario
        db.execSQL("UPDATE " + Utilidades.TABLA_USUARIOS +
                " SET " + Utilidades.CAMPO_ID_IMAGEN + " = " + contador +
                " WHERE " + Utilidades.CAMPO_ID + " = " + idUs);

        db.close();
    }

    //Función con la que obtenemos el id de la imagen del usuario
    public void seleccionarIdImagenUsuario() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Usuario u = null;
        //Consulta sql para obtener el id de la imagen
        Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_ID_IMAGEN +
                " FROM " + Utilidades.TABLA_USUARIOS +
                " WHERE " + Utilidades.CAMPO_ID + " = " + idUs, null);

        if(cursor.moveToNext()) {
            u = new Usuario();
            u.setIdImagen(cursor.getInt(0));
            //obtenemos el id de la imagen
            idImagen = u.getIdImagen();
        }

        cursor.close();

        db.close();

    }
}