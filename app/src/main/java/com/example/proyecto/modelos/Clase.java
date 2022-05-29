package com.example.proyecto.modelos;

//Modelo Clase
public class Clase {
    private int idClase;
    private String nombreClase;
    private int nivel;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;

    //Constructores
    public Clase() {
    }

    public Clase(int idClase, String nombreClase, int nivel, String diaSemana, String horaInicio, String horaFin) {
        this.idClase = idClase;
        this.nombreClase = nombreClase;
        this.nivel = nivel;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    //Getters y Setters
    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
}
