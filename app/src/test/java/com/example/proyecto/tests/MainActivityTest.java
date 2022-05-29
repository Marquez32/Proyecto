package com.example.proyecto.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.annotation.NonNull;

import com.example.proyecto.MainActivity;
import com.example.proyecto.modelos.Usuario;

import org.junit.Before;
import org.junit.Test;

public class MainActivityTest {

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

}
