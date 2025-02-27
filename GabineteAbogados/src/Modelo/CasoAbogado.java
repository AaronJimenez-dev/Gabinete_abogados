package Modelo;

import java.util.ArrayList;

public class CasoAbogado {
    private String id;
    private ArrayList<Caso> casos;
    private ArrayList<Abogado> abogados;

    public CasoAbogado(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Caso> getCasos() {
        return casos;
    }

    public void setCasos(ArrayList<Caso> casos) {
        this.casos = casos;
    }

    public ArrayList<Abogado> getAbogados() {
        return abogados;
    }

    public void setAbogado(ArrayList<Abogado> abogados) {
        this.abogados = abogados;
    }
}
