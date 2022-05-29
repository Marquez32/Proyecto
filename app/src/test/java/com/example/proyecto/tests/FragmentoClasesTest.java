package com.example.proyecto.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.proyecto.modelos.Clase;

import org.junit.Before;
import org.junit.Test;

public class FragmentoClasesTest {

    private Clase c;

    @Before
    public void setUp() throws Exception {
        c = new Clase();
    }

    @Test
    public void claseNotNull() {
        assertNotNull(c);
    }

    @Test
    public void comprobarNombre() {
        String nombre = "ciclo";
        c.setNombreClase(nombre);
        assertEquals(c.getNombreClase(), nombre);
    }

    @Test
    public void comprobarNivel() {
        int nivel = 2;
        c.setNivel(nivel);
        assert(c.getNivel() == nivel);
    }

    @Test
    public void comprobarDiaSemana() {
        String diaSemana = "lunes";
        c.setDiaSemana(diaSemana);
        assertEquals(c.getDiaSemana(), diaSemana);
    }

    @Test
    public void comprobarHoraInicio() {
        String horaInicio = "8:00";
        c.setHoraInicio(horaInicio);
        assertEquals(c.getHoraInicio(), horaInicio);
    }

    @Test
    public void comprobarHoraFin() {
        String horaFin = "15:00";
        c.setHoraFin(horaFin);
        assertEquals(c.getHoraFin(), horaFin);
    }

}
