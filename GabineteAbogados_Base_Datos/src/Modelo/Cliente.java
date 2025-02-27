package Modelo;

import java.util.ArrayList;

public class Cliente{
    private Persona persona;
    private int telefono;
    private String correo;
    private ArrayList<Caso> casos;

    public Cliente(Persona persona, int telefono, String correo) {
        this.persona = persona;
        this.telefono = telefono;
        this.correo = correo;
    }
    public Cliente(Persona persona) {
        this.persona = persona;
    }
    public Cliente() {}

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public ArrayList<Caso> getCasos() {
        return casos;
    }

    public void setCasos(ArrayList<Caso> casos) {
        this.casos = casos;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.persona.toString() +
                "\nTelefono = " + getTelefono() +
                "\nCorreo = '" + getCorreo());
        if (casos != null) {
            sb.append("\nCasos: ");
            for (Caso caso : casos) {
                sb.append(caso.getNumExpediente() + "\n");
            }
        }
        return sb.toString();
    }
}
