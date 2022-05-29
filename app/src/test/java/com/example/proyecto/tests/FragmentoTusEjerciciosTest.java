package com.example.proyecto.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.proyecto.modelos.Usuario_Ejercicio;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentoTusEjerciciosTest {

    private Usuario_Ejercicio ue;

    @Before
    public void setUp() throws Exception {
        ue = new Usuario_Ejercicio();
    }

    @Test
    public void usuarioEjercicioNotNull() {
        assertNotNull(ue);
    }

    @Test
    public void comprobarFechaRealizado() {
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaRealizado = dtf.parse("2001-06-19");
            ue.setFechaRealizado(fechaRealizado);
            assertEquals(ue.getFechaRealizado(), fechaRealizado);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void comprobarPeso() {
        double peso = 20;
        ue.setPeso(peso);
        assert(ue.getPeso() == peso);
    }

    @Test
    public void comprobarNumSeries() {
        int numSeries = 3;
        ue.setNumSeries(numSeries);
        assert(ue.getNumSeries() == numSeries);
    }


}
