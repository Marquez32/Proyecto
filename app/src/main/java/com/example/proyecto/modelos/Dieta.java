package com.example.proyecto.modelos;

import java.util.Date;

//Modelo Dieta
public class Dieta {

    private int idDieta;
    private String nombreDieta;
    private String tipoDieta;
    private String descripcionDieta;
    private String observacionesDieta;

    //Constructores
    public Dieta() {
    }

    public Dieta(int idDieta, String nombreDieta, String tipoDieta, String descripcionDieta, String observacionesDieta) {
        this.idDieta = idDieta;
        this.nombreDieta = nombreDieta;
        this.tipoDieta = tipoDieta;
        this.descripcionDieta = descripcionDieta;
        this.observacionesDieta = observacionesDieta;
    }

    //Getters y Setters
    public int getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(int idDieta) {
        this.idDieta = idDieta;
    }

    public String getNombreDieta() {
        return nombreDieta;
    }

    public void setNombreDieta(String nombreDieta) {
        this.nombreDieta = nombreDieta;
    }

    public String getTipoDieta() {
        return tipoDieta;
    }

    public void setTipoDieta(String tipoDieta) {
        this.tipoDieta = tipoDieta;
    }

    public String getDescripcionDieta() {
        return descripcionDieta;
    }

    public void setDescripcionDieta(String descripcionDieta) {
        this.descripcionDieta = descripcionDieta;
    }

    public String getObservacionesDieta() {
        return observacionesDieta;
    }

    public void setObservacionesDieta(String observacionesDieta) {
        this.observacionesDieta = observacionesDieta;
    }
}
