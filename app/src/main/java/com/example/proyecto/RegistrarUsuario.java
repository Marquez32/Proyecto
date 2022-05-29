package com.example.proyecto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.conexion.ConexionSQLiteHelper;
import com.example.proyecto.conexion.Utilidades;
import com.example.proyecto.modelos.Usuario;

public class RegistrarUsuario extends AppCompatActivity implements FragmentoFecha.OnFechaSeleccionada{


    ConexionSQLiteHelper conn;

    TextView tvFecha;
    EditText etNombre, etApellidos, etNick, etpContrasegna, etpRepetirContrasegna, etCorreo, etTelefono;
    Button btnFechaNacimiento, btnVolver, btnRegistrar;

    String fechaSeleccionada = "0000-00-00";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        //inicializamos la conexión
        conn = new ConexionSQLiteHelper(this, "bdgimnasio", null, 1);

        tvFecha = findViewById(R.id.tv_fecha);
        etNombre = findViewById(R.id.et_nombre);
        etApellidos = findViewById(R.id.et_apellidos);
        etNick = findViewById(R.id.et_Rnick);
        etpContrasegna = findViewById(R.id.etp_Rcontrasegna);
        etpRepetirContrasegna = findViewById(R.id.etp_repetirContrasegna);
        etCorreo = findViewById(R.id.et_correo);
        etTelefono = findViewById(R.id.et_telefono);
        btnFechaNacimiento = findViewById(R.id.btn_fechaNacimiento);
        btnVolver = findViewById(R.id.btn_volver);
        btnRegistrar = findViewById(R.id.btn_registrar);

        //Vamos a un fragmento donde nos sale un calendario para seleccionar la fecha
        btnFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentoFecha ff = new FragmentoFecha();
                ff.show(getSupportFragmentManager(), "ff");
            }
        });

        //Pulsamos el botón y si todos los atributos son correctos se registra el Usuario
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!contrasegnaValida()) {
                    toastError("La contraseña debe tener entre 4 y 12 caracteres");
                }
                if(!contrasegnaCorrecta()) {
                    toastError("Ambas contraseñas deben ser iguales");
                }
                if(!nickValido()) {
                    toastError("El nick no es válido");
                }
                if(!correoValido()) {
                    toastError("El correo debe contener el carácter @");
                }
                if(!fechaCorrecta()) {
                    toastError("Seleccione su fecha de nacimiento");
                }

                if(contrasegnaValida() && contrasegnaCorrecta() && nickValido() && correoValido() && fechaCorrecta()) {
                    altaUsuario();
                    toastCorrecto("Alta realizada");
                }

            }
        });

        //Si pulsamos el botón volvemos a la pantalla de inicio de sesión
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent actividad = new Intent(RegistrarUsuario.this, MainActivity.class);
                startActivity(actividad);
            }
        });
    }

    //Función para dar de alta al Usuario
    public void altaUsuario() {
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();

        //Insertamos los datos en el ContentValues
        values.put(Utilidades.CAMPO_NOMBRE, etNombre.getText().toString());
        values.put(Utilidades.CAMPO_APELLIDOS, etApellidos.getText().toString());
        values.put(Utilidades.CAMPO_NICK, etNick.getText().toString());
        values.put(Utilidades.CAMPO_CONTRASEGNA, etpContrasegna.getText().toString());
        values.put(Utilidades.CAMPO_CORREO, etCorreo.getText().toString());
        values.put(Utilidades.CAMPO_TELEFONO, etTelefono.getText().toString());
        values.put(Utilidades.CAMPO_FECHA_NACIMIENTO, fechaSeleccionada);

        //Hacemos el insert
        db.insert(Utilidades.TABLA_USUARIOS, null, values);

        db.close();
    }

    //Función que comprueba si la contraseña es válida
    public boolean contrasegnaValida() {
        if(etpContrasegna.getText().toString().length() < 4 || etpContrasegna.getText().toString().length() > 12) {
            return false;
        }
        return true;
    }

    //Función que comprueba si la contraseña es correcta
    public boolean contrasegnaCorrecta() {
        if(etpContrasegna.getText().toString().equals(etpRepetirContrasegna.getText().toString())) {
            return true;
        }
        return false;
    }

    //Función que comprueba si el correo es válido
    public boolean correoValido() {
        if(etCorreo.getText().toString().contains("@")) {
            return true;
        }
        return false;
    }

    //Función que comprueba si el nick es válido
    public boolean nickValido() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Usuario u = null;
        //Consulta del cursor
        Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_NICK
                + " FROM " + Utilidades.TABLA_USUARIOS, null);

        //recorremos el cursor
        while(cursor.moveToNext()) {
            u = new Usuario();
            u.setNick(cursor.getString(0));

            //Si ya existe el nick no es válido (devolvemos false)
            if(u.getNick().equals(etNick.getText().toString())) {
                cursor.close();
                return false;
            }

        }

        cursor.close();

        //nick válido
        return true;
    }


    //Función para comprobar si la fecha es correcta
    public boolean fechaCorrecta() {
        if(fechaSeleccionada.equals("0000-00-00")) {
            return false;
        }
        return true;
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

    //Interfaz que nos muestra la fecha que hemos seleccionado en el fragmento
    @Override
    public void onResultadoFecha(int agno, int mes, int dia) {
        fechaSeleccionada = agno + "-" + (mes + 1) + "-" + dia;
        tvFecha.setText(fechaSeleccionada);
    }
}
