package com.example.proyecto.conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//Clase para la conexión a la BBDD
public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    //Constructor
    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Al crear la BBDD
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creamos todas las tablas
        db.execSQL(Utilidades.CREAR_TABLA_DIETAS);
        db.execSQL(Utilidades.CREAR_TABLA_IMAGENES);
        db.execSQL(Utilidades.CREAR_TABLA_USUARIOS);
        db.execSQL(Utilidades.CREAR_TABLA_EJERCICIOS);
        db.execSQL(Utilidades.CREAR_TABLA_USUARIOS_EJERCICIOS);
        db.execSQL(Utilidades.CREAR_TABLA_CLASES);
        db.execSQL(Utilidades.CREAR_TABLA_USUARIOS_CLASES);
    }

    //Al actualizar la versión de la BBDD
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        //borramos todas las tablas
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_DIETAS);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_IMAGENES);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_EJERCICIOS);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_USUARIOS_EJERCICIOS);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_CLASES);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_USUARIOS_CLASES);
        //y las volvemos a crear
        onCreate(db);
    }
}
