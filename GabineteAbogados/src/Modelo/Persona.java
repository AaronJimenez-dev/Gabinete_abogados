package Modelo;

import java.util.ArrayList;

public class Persona {
    private String dni;
    private String nombre;
    private String apellido;
    private String direccion;

    public Persona(String dni, String nombre, String apellido, String direccion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
    }
    public Persona(){}

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "DNI = " + getDni() +
                "\nNombre = " + getNombre() +
                "\nApellido = " + getApellido() +
                "\nDireccion = " + getDireccion();
    }
}
