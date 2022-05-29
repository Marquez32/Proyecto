package com.example.proyecto.conexion;

public class Utilidades {
    //Constantes tabla Usuarios
    public static final String TABLA_USUARIOS = "usuarios";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_APELLIDOS = "apellidos";
    public static final String CAMPO_NICK = "nick";
    public static final String CAMPO_CONTRASEGNA = "contrasegna";
    public static final String CAMPO_CORREO = "correo";
    public static final String CAMPO_TELEFONO = "telefono";
    public static final String CAMPO_FECHA_NACIMIENTO = "fechaNacimiento";

    //Tabla Dietas
    public static final String TABLA_DIETAS = "dietas";
    public static final String CAMPO_ID_DIETA = "idDieta";

    //Tabla Imágenes
    public static final String TABLA_IMAGENES = "imagenes";
    public static final String CAMPO_ID_IMAGEN = "idImagen";

    public static final String CREAR_TABLA_USUARIOS = "CREATE TABLE " + TABLA_USUARIOS +
            "(" + CAMPO_ID + " integer primary key autoincrement, " +
            CAMPO_NOMBRE + " text not null, " +
            CAMPO_APELLIDOS + " text not null, " +
            CAMPO_NICK + " text not null unique, " +
            CAMPO_CONTRASEGNA + " text not null, " +
            CAMPO_CORREO + " text, " +
            CAMPO_TELEFONO + " text, " +
            CAMPO_FECHA_NACIMIENTO + " date, " +
            CAMPO_ID_DIETA + " integer, " +
            CAMPO_ID_IMAGEN + " integer, " +
            "foreign key (" + CAMPO_ID_DIETA + ") references " + TABLA_DIETAS + "(" + CAMPO_ID_DIETA + ")," +
            "foreign key (" + CAMPO_ID_IMAGEN + ") references " + TABLA_IMAGENES + "(" + CAMPO_ID_IMAGEN + "));" ;

//--------------------------------------------------------------------------------
    //Constantes tabla Ejercicios

    public static final String TABLA_EJERCICIOS = "ejercicios";
    public static final String CAMPO_ID_EJ = "idEj";
    public static final String CAMPO_NOMBRE_EJ = "nombreEj";
    public static final String CAMPO_ZONA_TRABAJADA = "zonaTrabajada";
    public static final String CAMPO_INTENSIDAD = "intensidad";

    public static final String CREAR_TABLA_EJERCICIOS = "CREATE TABLE " + TABLA_EJERCICIOS +
            "(" + CAMPO_ID_EJ + " integer primary key autoincrement, " +
            CAMPO_NOMBRE_EJ + " text not null, " +
            CAMPO_ZONA_TRABAJADA + " text not null, " +
            CAMPO_INTENSIDAD + " text not null);";
    //--------------------------------------------------------------------------------
    //Constantes tabla Usuario_Ejercicios

    public static final String TABLA_USUARIOS_EJERCICIOS = "usuarios_ejercicios";
    public static final String CAMPO_FECHA_REALIZADO = "fechaRealizado";
    public static final String CAMPO_PESO = "peso";
    public static final String CAMPO_NUM_SERIES = "series";

    public static final String CREAR_TABLA_USUARIOS_EJERCICIOS = "CREATE TABLE " + TABLA_USUARIOS_EJERCICIOS +
            "(" + CAMPO_ID + " integer, " +
            CAMPO_ID_EJ + " integer, " +
            CAMPO_FECHA_REALIZADO + " date, " +
            CAMPO_PESO + " double, " +
            CAMPO_NUM_SERIES + " integer, " +
            "foreign key (" + CAMPO_ID + ") references " + TABLA_USUARIOS + "(" + CAMPO_ID + ") " +
            "foreign key (" + CAMPO_ID_EJ + ") references " + TABLA_EJERCICIOS + "(" + CAMPO_ID_EJ + "))";

    //--------------------------------------------------------------------------------
    //Constantes tabla Clases

    public static final String TABLA_CLASES = "clases";
    public static final String CAMPO_ID_CLASE = "idClase";
    public static final String CAMPO_NOMBRE_CLASE = "nombreClase";
    public static final String CAMPO_NIVEL = "nivel";
    public static final String CAMPO_DIA_SEMANA = "diaSemana";
    public static final String CAMPO_HORA_INICIO = "horaInicio";
    public static final String CAMPO_HORA_FIN = "horaFin";

    public static final String CREAR_TABLA_CLASES = "CREATE TABLE " + TABLA_CLASES +
            "(" + CAMPO_ID_CLASE + " integer primary key autoincrement, " +
            CAMPO_NOMBRE_CLASE + " text not null, " +
            CAMPO_NIVEL + " integer not null, " +
            CAMPO_DIA_SEMANA + " text not null, " +
            CAMPO_HORA_INICIO + " text not null, " +
            CAMPO_HORA_FIN + " text not null);";

    //--------------------------------------------------------------------------------
    //Constantes tabla Usuario_Clases
    public static final String TABLA_USUARIOS_CLASES = "usuarios_clases";
    public static final String CREAR_TABLA_USUARIOS_CLASES = "CREATE TABLE " + TABLA_USUARIOS_CLASES +
            "(" + CAMPO_ID + " integer, " +
            CAMPO_ID_CLASE + " integer, " +
            "foreign key (" + CAMPO_ID + ") references " + TABLA_USUARIOS + "(" + CAMPO_ID + ") " +
            "foreign key (" + CAMPO_ID_CLASE + ") references " + TABLA_CLASES + "(" + CAMPO_ID_CLASE + "));";

    //--------------------------------------------------------------------------------
    //Constantes tabla Dietas

    public static final String CAMPO_NOMBRE_DIETA = "nombreDieta";
    public static final String CAMPO_TIPO_DIETA = "tipoDieta";
    public static final String CAMPO_DESCRIPCION_DIETA = "descripcionDieta";
    public static final String CAMPO_OBSERVACIONES_DIETA = "observacionesDieta";

    public static final String CREAR_TABLA_DIETAS = "CREATE TABLE " + TABLA_DIETAS +
            "(" + CAMPO_ID_DIETA + " integer primary key autoincrement, " +
            CAMPO_NOMBRE_DIETA + " text not null, " +
            CAMPO_TIPO_DIETA + " text not null, " +
            CAMPO_DESCRIPCION_DIETA + " text not null, " +
            CAMPO_OBSERVACIONES_DIETA + " text);";

    //--------------------------------------------------------------------------------
    //Constantes tabla Imagenes

    public static final String CAMPO_IMG = "img";

    public static final String CREAR_TABLA_IMAGENES = "CREATE TABLE " + TABLA_IMAGENES +
            "(" + CAMPO_ID_IMAGEN + " integer primary key autoincrement, " +
            CAMPO_IMG + " blob);";

}
