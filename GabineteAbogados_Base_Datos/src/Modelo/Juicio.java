package Modelo;

import java.time.LocalDate;

public class Juicio {
    private int id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    /*
        Tramite: no tiene fecha de fin
        Anulado: se ha anulado el juicio
        FInalizado: tiene fecha de finalizacion
    */

    public Juicio(LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }
    public Juicio(LocalDate fechaInicio, String estado) {
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }
    public Juicio(String estado) {
        this.estado = estado;
    }
    public Juicio() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Fecha de inicio = " + fechaInicio +
                ", fecha de fin = " + fechaFin +
                ", estado = " + estado;
    }
}
