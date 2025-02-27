package Modelo;

import java.util.ArrayList;

public class Abogado{
    private Persona persona;
    private ArrayList<Caso> casos;

    public Abogado(Persona persona) {
        this.persona = persona;
    }
    public Abogado() {}

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(persona.toString());
        if (casos != null) {
            sb.append("\nCasos: ");
            for (Caso caso : casos) {
                sb.append(caso.getNumExpediente() + "\n");
            }
        }
        return sb.toString();
    }
}
