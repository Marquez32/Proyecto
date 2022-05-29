package com.example.proyecto.tests;

import static org.junit.Assert.assertNotNull;

import com.example.proyecto.modelos.Usuario_Clase;

import org.junit.Before;
import org.junit.Test;

public class FragmentoTusClasesTest {

    private Usuario_Clase uc;

    @Before
    public void setUp() throws Exception {
        uc = new Usuario_Clase();
    }

    @Test
    public void usuarioClaseNotNull() {
        assertNotNull(uc);
    }
}
