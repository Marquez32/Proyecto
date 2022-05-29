package com.example.proyecto.modelos;

import java.util.Date;

//Modelo Usuario_Ejercicio
public class Usuario_Ejercicio {
    private int id;
    private int idEj;
    private Date fechaRealizado;
    private double peso;
    private int numSeries;

    //Constructores
    public Usuario_Ejercicio() {
    }

    public Usuario_Ejercicio(int id, int idEj, Date fechaRealizado, double peso, int numSeries) {
        this.id = id;
        this.idEj = idEj;
        this.fechaRealizado = fechaRealizado;
        this.peso = peso;
        this.numSeries = numSeries;
    }

    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEj() {
        return idEj;
    }

    public void setIdEj(int idEj) {
        this.idEj = idEj;
    }

    public Date getFechaRealizado() {
        return fechaRealizado;
    }

    public void setFechaRealizado(Date fechaRealizado) {
        this.fechaRealizado = fechaRealizado;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getNumSeries() {
        return numSeries;
    }

    public void setNumSeries(int numSeries) {
        this.numSeries = numSeries;
    }
}
