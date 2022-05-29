package com.example.proyecto.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.proyecto.modelos.Dieta;

import org.junit.Before;
import org.junit.Test;

public class FragmentoDietasTest {

    private Dieta d;

    @Before
    public void setUp() throws Exception {
        d = new Dieta();
    }

    @Test
    public void dietaNotNull() {
        assertNotNull(d);
    }

    @Test
    public void comprobarNombre() {
        String nombre = "paleo";
        d.setNombreDieta(nombre);
        assertEquals(d.getNombreDieta(), nombre);
    }

    @Test
    public void comprobarTipo() {
        String tipo = "proteica";
        d.setTipoDieta(tipo);
        assertEquals(d.getTipoDieta(), tipo);
    }

    @Test
    public void comprobarDescripcion() {
        String descripcion = "muy buena";
        d.setDescripcionDieta(descripcion);
        assertEquals(d.getDescripcionDieta(), descripcion);
    }

    @Test
    public void comprobarObservaciones() {
        String observaciones = "estricta";
        d.setObservacionesDieta(observaciones);
        assertEquals(d.getObservacionesDieta(), observaciones);
    }

}
