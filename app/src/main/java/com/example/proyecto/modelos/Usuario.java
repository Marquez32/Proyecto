package com.example.proyecto.modelos;

import java.util.Date;

//Modelo Usuario
public class Usuario {
    private int id;
    private String nombre;
    private String apellidos;
    private String nick;
    private String contrasegna;
    private String correo;
    private String telefono;
    private Date fechaNacimiento;
    private int idDieta;
    private int idImagen;

    //Constructores
    public Usuario() {
    }

    public Usuario(int id, String nombre, String apellidos, String nick, String contrasegna, String correo, String telefono, Date fechaNacimiento, int idDieta, int idImagen) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nick = nick;
        this.contrasegna = contrasegna;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.idDieta = idDieta;
        this.idImagen = idImagen;
    }

    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContrasegna() {
        return contrasegna;
    }

    public void setContrasegna(String contrasegna) {
        this.contrasegna = contrasegna;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(int idDieta) {
        this.idDieta = idDieta;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }
}
