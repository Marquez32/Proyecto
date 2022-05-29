package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Usuario;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ConexionSQLiteHelper conn;

    TextView tvRegistrar;
    EditText etNick, etpContrasegna;
    Button btnIniciarSesion;

    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializamos conexioón
        conn = new ConexionSQLiteHelper(this, "bdgimnasio", null, 1);

        tvRegistrar = findViewById(R.id.tv_registrar);
        etNick = findViewById(R.id.et_nick);
        etpContrasegna = findViewById(R.id.etp_contrasegna);
        btnIniciarSesion = findViewById(R.id.btn_iniciarSesion);

        //Insertamos datos en la BBDD
        try {
            cargarEjercicios();
            cargarClases();
            cargarDietas();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        //Si pulsamos el textView nos lleva al activity de Registrar Usuario
        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegistrarUsuario.class);
                startActivity(i);
            }
        });

        //Si pulsamos el botón nos lleva dentro de la aplicación si los datos son correctos
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comprobarNickYContrasegna()) {
                    escribirFichero();
                    //Hacemos un intent para cambiar de actividad
                    Intent actividad = new Intent(MainActivity.this, MenuSlideActivity.class);
                    //Pasamos los datos de nick y del correo
                    actividad.putExtra("NICK", u.getNick());
                    actividad.putExtra("CORREO", u.getCorreo());
                    startActivity(actividad);
                }
                else {
                    toastError("El nick o la contraseña son incorrectos");
                }
            }
        });

    }

    //Función con la cual comprobamos si el nick y la contraseña son válidos
    public boolean comprobarNickYContrasegna() {
        SQLiteDatabase db = conn.getReadableDatabase();
        u = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_USUARIOS, null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //Recorremos la tabla con el cursor
        while(cursor.moveToNext()) {
            u = new Usuario();
            u.setId(cursor.getInt(0));
            u.setNombre(cursor.getString(1));
            u.setApellidos(cursor.getString(2));
            u.setNick(cursor.getString(3));
            u.setContrasegna(cursor.getString(4));
            u.setCorreo(cursor.getString(5));
            u.setTelefono(cursor.getString(6));
            try {
                u.setFechaNacimiento(sdf.parse(cursor.getString(7)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Si coinciden ambos devolvemos true
            if(u.getNick().equals(etNick.getText().toString()) &&
                    u.getContrasegna().equals(etpContrasegna.getText().toString())) {
                cursor.close();
                return true;
            }
        }

        cursor.close();

        //No coinciden
        return false;
    }

    //Función para escribir el id de Usuario dentro de un fichero
    public void escribirFichero() {
        String texto = u.getId() + "";
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput("idUsuario.txt", MODE_PRIVATE);
            fileOutputStream.write(texto.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Función que nos devuelve un toast de error personalizado
    public void toastError(String mensaje) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_mistake, (ViewGroup) findViewById(R.id.customToastMistake));
        TextView txt = view.findViewById(R.id.txtMessage2);
        txt.setText(mensaje);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    //cargamos los datos de la tabla Ejercicios
    public void cargarEjercicios() {
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (1, 'futbol', 'todo el cuerpo', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (2, 'baloncesto', 'todo el cuerpo', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (3, 'tenis', 'todo el cuerpo', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (4, 'padel', 'todo el cuerpo', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (5, 'natacion', 'todo el cuerpo', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (6, 'sentadillas', 'pierna', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (7, 'zancadas alternas', 'pierna', 'media')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (8, 'burpees', 'pierna', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (9, 'puente', 'pierna', 'baja')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (10, 'gemelos', 'pierna', 'media')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (11, 'dominadas', 'espalda', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (12, 'remo con barra', 'espalda', 'media')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (13, 'remo con mancuernas', 'espalda', 'baja')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (14, 'peso muerto', 'espalda', 'media')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (15, 'remo con polea baja', 'espalda', 'baja')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (16, 'mancuernas al pecho', 'pecho', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (17, 'press banca inclinado', 'pecho', 'media')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (18, 'flexiones', 'pecho', 'media')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (19, 'aleteo con poleas', 'pecho', 'baja')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (20, 'mancuernas tumbado', 'pecho', 'media')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (21, 'curl de biceps', 'brazo', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (22, 'dip de triceps', 'brazo', 'media')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (23, 'triceps detras de la nuca', 'brazo', 'baja')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (24, 'biceps con mancuerna sentado', 'brazo', 'alta')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_EJERCICIOS +
                " VALUES (25, 'press de hombro', 'brazo', 'media')");

        db.close();
    }

    //Cargamos los datos de la tabla Clases
    public void cargarClases() {
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (1, 'ciclo', 2, 'lunes', '8:00', '9:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (2, 'body pump', 3, 'lunes', '9:30', '10:15')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (3, 'zumba', 2, 'lunes', '10:30', '11:30')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (4, 'body balance', 1, 'lunes', '12:00', '12:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (5, 'yoga', 1, 'lunes', '13:00', '13:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (6, 'pilates', 1, 'lunes', '16:00', '16:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (7, 'gap', 2, 'lunes', '17:00', '17:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (8, 'body combat', 3, 'lunes', '18:00', '18:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (9, 'zumba', 2, 'lunes', '19:00', '19:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (10, 'body pump', 3, 'lunes', '20:00', '20:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (11, 'ciclo', 2, 'lunes', '21:00', '22:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (12, 'pilates', 1, 'martes', '8:00', '9:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (13, 'yoga', 1, 'martes', '9:15', '10:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (14, 'body pump', 3, 'martes', '10:15', '11:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (15, 'gap', 2, 'martes', '11:45', '12:30')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (16, 'espalda sana', 1, 'martes', '13:00', '13:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (17, 'espalda sana', 1, 'martes', '16:00', '16:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (18, 'yoga', 1, 'martes', '17:00', '17:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (19, 'body combat', 3, 'martes', '18:00', '18:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (20, 'pilates', 1, 'martes', '19:00', '19:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (21, 'gap', 2, 'martes', '20:00', '20:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (22, 'body pump', 3, 'martes', '21:00', '21:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (23, 'yoga', 1, 'miercoles', '8:00', '9:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (24, 'zumba', 2, 'miercoles', '9:15', '10:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (25, 'pilates', 1, 'miercoles', '10:15', '11:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (26, 'body combat', 3, 'miercoles', '11:15', '12:15')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (27, 'ciclo', 2, 'miercoles', '12:45', '13:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (28, 'yoga', 1, 'miercoles', '16:00', '16:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (29, 'pilates', 1, 'miercoles', '17:00', '17:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (30, 'body pump', 3, 'miercoles', '18:00', '18:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (31, 'gap', 2, 'miercoles', '19:00', '19:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (32, 'espalda sana', 1, 'miercoles', '20:00', '20:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (33, 'body combat', 3, 'miercoles', '21:00', '21:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (34, 'ciclo', 2, 'jueves', '8:00', '9:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (35, 'gap', 2, 'jueves', '9:15', '10:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (36, 'espalda sana', 1, 'jueves', '10:15', '11:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (37, 'body pump', 3, 'jueves', '11:30', '12:30')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (38, 'pilates', 1, 'jueves', '12:45', '13:30')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (39, 'zumba', 2, 'jueves', '16:00', '16:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (40, 'espalda sana', 2, 'jueves', '17:00', '17:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (41, 'ciclo', 2, 'jueves', '18:00', '18:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (42, 'gap', 2, 'jueves', '19:00', '19:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (43, 'body combat', 3, 'jueves', '20:00', '20:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (44, 'body pump', 3, 'jueves', '21:00', '21:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (45, 'ciclo', 2, 'viernes', '8:00', '9:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (46, 'pilates', 1, 'viernes', '9:15', '10:00')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (47, 'espalda sana', 1, 'viernes', '10:15', '11:15')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (48, 'gap', 2, 'viernes', '11:30', '12:30')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (49, 'body pump', 3, 'viernes', '12:45', '13:30')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (50, 'ciclo', 2, 'viernes', '16:00', '16:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (51, 'espalda sana', 1, 'viernes', '17:00', '17:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (52, 'body combat', 3, 'viernes', '18:00', '18:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (53, 'gap', 2, 'viernes', '19:00', '19:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (54, 'yoga', 1, 'viernes', '20:00', '20:45')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_CLASES +
                " VALUES (55, 'body pump', 3, 'viernes', '21:00', '21:45')");

        db.close();

    }

    //Cargamos los datos de la tabla Dietas
    public void cargarDietas() {
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("INSERT INTO " + Utilidades.TABLA_DIETAS +
                " VALUES (1, 'Dieta Hipocalorica', 'hipocalorica', 'Una dieta hipocalórica " +
                " es una dieta para perder peso en la que se busca consumir menos calorías de " +
                "las que necesita el cuerpo para “funcionar”.\n" +
                "Los alimentos que podemos tomar en esta dieta son fruta, verdura, pescado. carne," +
                "en definitiva productos frescos. En cambio no hay que tomar alimentos que tengan " +
                "pocas vitaminas y minerales, estos alimentos suelen ser muy ricos en azucar." +
                "Tampoco hay que tomar alimentos ultraprocesados.\n', " +
                "'Esta dieta tiene la creencia de que algunos alimentos light no son tan malos" +
                "para nuestro cuerpo. Sin embargo que un alimento sea light no quiere decir " +
                "que tenga menos calorías. Tampoco debemos comer mayor cantidad de un alimento porque " +
                "éste sea light.');" );
        db.execSQL("INSERT INTO " + Utilidades.TABLA_DIETAS +
                " VALUES (2, 'Dieta por Puntos', 'por puntos', 'La dieta de los puntos es una " +
                "dieta flexible y que permite comer prácticamente de todo, ya que no te limita " +
                "la variedad de comida sino que te hace calcular el máximo de puntos.\n" +
                "Para que la dieta tenga éxito no debemos superar los puntos que nos han asignado. ', " +
                "'Para calcular los puntos que te corresponden debes sumar los siguientes parámetros:\n" +
                "')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_DIETAS +
                " VALUES (3, 'Dieta Paleo', 'paleo', 'La dieta paleo se basa en que estamos genéticamente " +
                "adaptados para comer lo que comían nuestros antepasados del Paleolítico: carne, verduras, " +
                "pescado, frutas…, y es mejor evitar lácteos, legumbres y cereales.', 'Es un tipo de " +
                "dieta que ayuda a perder peso o a mantenerte en él, aumenta la sensación de saciedad," +
                "previene de enfermedades y nos proporciona un mejor descanso.')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_DIETAS +
                " VALUES (4, 'Dieta proteica', 'proteica', 'La dieta proteica consiste en un aumento " +
                "considerable en la ingesta de proteínas disminuyendo notoriamente los carbohidratos " +
                "y los lípidos.\n" +
                "Cada comida que se haga durante el día debe incluir un alimento proteico, y lo más " +
                "destacable es que las calorías deben disminuirse progresivamente para que el cuerpo " +
                "no sufra un impacto que ocasione estrés, carencias nutricionales, y lo más temido " +
                "por todos, el efecto rebote.', " +
                "'-Esta dieta es para un tiempo determinado que no debe superar un mes sin extenderse " +
                "ya que se puede poner en riesgo la salud.\n" +
                "-La dieta proteica no es para todas las personas, mucho menos para aquellas que " +
                "sufren de insuficiencias renales, diabetes, hipertensión o personas con obesidad.')");
        db.execSQL("INSERT INTO " + Utilidades.TABLA_DIETAS +
                " VALUES (5, 'Dieta detox', 'detox', 'La dieta detox es un método alimenticio que " +
                "consiste en ayudar a eliminar de nuestro cuerpo todo aquello que no necesita, con " +
                "el objetivo de que su funcionamiento sea mucho más eficiente.', " +
                "'Si sigues la dieta detox podrás obtener los siguientes beneficios: perder peso, " +
                "limpiar tu piel e hidratarla, oxigenar el cerebro, reducir el cansancio, mejorar " +
                "descanso y mejorar el tránsito intestinal.')");

        db.close();
    }

}