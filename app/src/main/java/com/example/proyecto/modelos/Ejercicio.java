package com.example.proyecto.modelos;

//Modelo Ejercicio
public class Ejercicio {
    private int idEj;
    private String nombreEj;
    private String zonaTrabajada;
    private String intensidad;

    //Constructores
    public Ejercicio() {
    }

    public Ejercicio(int idEj, String nombreEj, String zonaTrabajada, String intensidad) {
        this.idEj = idEj;
        this.nombreEj = nombreEj;
        this.zonaTrabajada = zonaTrabajada;
        this.intensidad = intensidad;
    }

    //Getters y Setters
    public int getIdEj() {
        return idEj;
    }

    public void setIdEj(int idEj) {
        this.idEj = idEj;
    }

    public String getNombreEj() {
        return nombreEj;
    }

    public void setNombreEj(String nombreEj) {
        this.nombreEj = nombreEj;
    }

    public String getZonaTrabajada() {
        return zonaTrabajada;
    }

    public void setZonaTrabajada(String zonaTrabajada) {
        this.zonaTrabajada = zonaTrabajada;
    }

    public String getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(String intensidad) {
        this.intensidad = intensidad;
    }

}
