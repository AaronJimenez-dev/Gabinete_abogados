package ModeloController;

import Modelo.*;
import ModeloDAO.AbogadoDAO;
import ModeloDAO.CasoDAO;

import java.util.ArrayList;

public class AbogadoController {
    private PersonaController personaController;
    private AbogadoDAO abogadoDAO;
    private CasoController casoController;
    private CasoDAO casoDAO;

    public AbogadoController(PersonaController personaController,AbogadoDAO abogadoDAO, CasoController casoController, CasoDAO casoDAO) {
        this.personaController = personaController;
        this.abogadoDAO = abogadoDAO;
        this.casoController = casoController;
        this.casoDAO = casoDAO;
    }

    public void setCasoController(CasoController casoController) {
        this.casoController = casoController;
    }

    public void verAbogado() {
        Abogado abogado = conseguirAbogado();
        if (abogado != null)
            System.out.println(abogado.toString());
    }
    public Abogado conseguirAbogado() {
        String dni = personaController.validar("DNI","^[0-9]{8}[A-Z]");
        Abogado abogado = abogadoDAO.verAbogado(dni);
        if (abogado == null)
            System.out.println("El abogado no existe.");
        return abogado;
    }
    public void insertarAbogado(){
        Abogado abogado = crearAbogado();
        if (abogado == null)
            System.out.println("El abogado ya existe.");
        else {
            abogadoDAO.insertarAbogados(abogado);
        }
    }
    public void modificarAbogado(String queHacer) {
        ArrayList<Abogado> abogados = abogadoDAO.getAbogados();
        final int size = abogados.size();
        System.out.println("Digame el dni del abogado a " + queHacer + ".");

        Abogado abogadoBorrado = conseguirAbogado();
        if (abogadoBorrado != null) {
            eliminarAbogado(abogadoBorrado);

            if (abogados.size() == size) { //Si el tamaño de abogados no cambia significa que no se ha eliminado.
                System.out.println("No se puede continuar con la operación.");

            } else if (queHacer.equals("eliminar")) {
                System.out.println("Abogado eliminado");
                personaController.eliminarPersona(abogadoBorrado.getPersona());

            } else if (queHacer.equals("editar")) {
                System.out.println("Digame la informacion del nuevo abogado a " + queHacer + ".");
                editarAbogado(abogadoBorrado);

                if (size == abogados.size()) //Si el tamaño de abogados recupera el tamaño original se ha modificado correctamente.
                    System.out.println("El abogado se ha editado correctamente");
                else {
                    System.out.println("El abogado no se ha podido editar");
                    abogadoDAO.insertarAbogados(abogadoBorrado);
                }
            }
        }
    }
    private void editarAbogado(Abogado abogadoBorrado) {
        Abogado abogado = new Abogado(personaController.modificarPersona(abogadoBorrado.getPersona()));
        abogado.setCasos(abogadoBorrado.getCasos());
        abogadoDAO.insertarAbogados(abogado);
    }
    public void eliminarAbogado(Abogado abogado) {
        abogadoDAO.eliminarAbogado(abogado);
        for (Caso caso : abogado.getCasos()) {
            casoDAO.eliminarAbogado(abogado, caso);
        }
    }
    public void verTodos(){
        ArrayList<Abogado> abogados = abogadoDAO.getAbogados();
        for (Abogado abogado : abogados)
            System.out.println(abogado.getPersona().getDni());
    }

    public Abogado crearAbogado() {
        Persona persona = personaController.validarPersona("abogado");
        if (abogadoDAO.verAbogado(persona.getDni()) == null)
            return new Abogado(persona);
        else
            return null;
    }

    public void anadirCaso(){
        Abogado abogado = conseguirAbogado();
        if (abogado != null){
            System.out.println("Dime que caso quieres añadir:");
            Caso caso = casoController.conseguirCaso();
            if (caso != null){
                if (abogadoDAO.buscarCaso(abogado, caso))
                    System.out.println("El caso ya está relacionado al abogado");
                else {
                    abogadoDAO.anadirCaso(abogado, caso);
                    casoDAO.anadirAbogado(abogado, caso);
                }
            }
        }
    }
    public void eliminarCaso(){
        Abogado abogado = conseguirAbogado();
        if (abogado != null){
            System.out.println("Dime que caso quieres eliminar:");
            Caso caso = casoController.conseguirCaso();
            if (caso != null){
                if (abogadoDAO.buscarCaso(abogado, caso)) {
                    abogadoDAO.eliminarCaso(abogado, caso);
                    casoDAO.eliminarAbogado(abogado, caso);
                }
                else
                    System.out.println("El caso no está relacionado al abogado");
            }
        }
    }
}
