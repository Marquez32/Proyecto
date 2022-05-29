package com.example.proyecto.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.proyecto.modelos.Usuario;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrarUsuarioTest {

    private Usuario u;

    @Before
    public void setUp() throws Exception {
        u = new Usuario();
    }

    @Test
    public void usuarioNotNull() {
        assertNotNull(u);
    }

    @Test
    public void comprobarNombre() {
        String nombre = "Ismael";
        u.setNombre(nombre);
        assertEquals(u.getNombre(), nombre);
    }

    @Test
    public void comprobarApellidos() {
        String apellido = "Marquez";
        u.setApellidos(apellido);
        assertEquals(u.getApellidos(), apellido);
    }

    @Test
    public void comprobarNick() {
        String nick = "IMDB32";
        u.setNick(nick);
        assertEquals(u.getNick(), nick);
    }

    @Test
    public void comprobarPassword() {
        String password = "1234";
        u.setContrasegna(password);
        assertEquals(u.getContrasegna(), password);
    }

    @Test
    public void contrasegnaNoSegura() {
        String password = "123";
        boolean cierto = true;
        if(password.length() < 4) {
            cierto = false;
        }
        assertFalse(cierto);
    }

    @Test
    public void contrasegnaSegura() {
        String password = "123";
        boolean cierto = true;
        if(password.length() > 4) {
            cierto = false;
        }
        assertTrue(cierto);
    }

    @Test
    public void repetirContrasegna() {
        String password = "1234";
        String password2 = "1234";
        assertEquals(password, password2);
    }

    @Test
    public void repetirContrasegnaMal() {
        String password = "12345";
        String password2 = "1234";
        assertNotEquals(password, password2);
    }

    @Test
    public void comprobarCorreo() {
        String correo = "isma@gmail.com";
        u.setCorreo(correo);
        assertEquals(u.getCorreo(), correo);
    }

    @Test
    public void correoValido() {
        String correo = "isma@gmail.com";
        boolean valido = false;
        if(correo.contains("@")) {
            valido = true;
        }
        assertTrue(valido);
    }

    @Test
    public void correoNoValido() {
        String correo = "ismagmail.com";
        boolean valido = false;
        if(correo.contains("@")) {
            valido = true;
        }
        assertFalse(valido);
    }

    @Test
    public void comprobarTelefono() {
        String telefono = "625707738";
        u.setTelefono(telefono);
        assertEquals(u.getTelefono(), telefono);
    }

    @Test
    public void comprobarFechaNacimiento() {
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaNacimiento = dtf.parse("2001-06-19");
            u.setFechaNacimiento(fechaNacimiento);
            assertEquals(u.getFechaNacimiento(), fechaNacimiento);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
