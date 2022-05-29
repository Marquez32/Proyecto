package com.example.proyecto.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.proyecto.modelos.Ejercicio;

import org.junit.Before;
import org.junit.Test;

public class FragmentoEjerciciosTest {

    private Ejercicio e;

    @Before
    public void setUp() throws Exception {
        e = new Ejercicio();
    }

    @Test
    public void ejercicioNotNull() {
        assertNotNull(e);
    }

    @Test
    public void comprobarNombreEj() {
        String nombre = "futbol";
        e.setNombreEj(nombre);
        assertEquals(e.getNombreEj(), nombre);
    }

    @Test
    public void comprobarZonaTrabajada() {
        String zonaTrabajada = "pecho";
        e.setZonaTrabajada(zonaTrabajada);
        assertEquals(e.getZonaTrabajada(), zonaTrabajada);
    }

    @Test
    public void comprobarIntensidad() {
        String intensidad = "media";
        e.setIntensidad(intensidad);
        assertEquals(e.getIntensidad(), intensidad);
    }

}
