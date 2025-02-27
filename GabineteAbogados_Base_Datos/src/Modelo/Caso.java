package Modelo;

import java.util.ArrayList;

public class Caso {
    private int numExpediente;
    private Cliente cliente;
    private Juicio juicio;
    private ArrayList<Abogado> abogados;

    public Caso(int numExpediente, Cliente cliente, Juicio juicio) {
        this.numExpediente = numExpediente;
        this.cliente = cliente;
        this.juicio = juicio;
    }
    public Caso(Cliente cliente) {
        this.numExpediente = numExpediente;
        this.cliente = cliente;
    }
    public Caso(){}

    public int getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(int numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Juicio getJuicio() {
        return juicio;
    }

    public void setJuicio(Juicio juicio) {
        this.juicio = juicio;
    }

    public ArrayList<Abogado> getAbogados() {
        return abogados;
    }

    public void setAbogados(ArrayList<Abogado> abogados) {
        this.abogados = abogados;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Numero de expediente: " + numExpediente +
                "\nCliente = " + cliente.getPersona().getDni() +
                "\nJuicio = " + juicio.getId());
        if (abogados != null) {
            sb.append("\nAbogados: ");
            for (Abogado abogado : abogados) {
                sb.append(abogado.getPersona().getDni() + "\n");
            }
        }
        return sb.toString();
    }
}
