package ModeloController;

import Modelo.Abogado;
import Modelo.Caso;
import Modelo.CasoAbogado;
import ModeloDAO.CasoAbogadoDAO;

import java.util.ArrayList;
import java.util.Scanner;

public class CasoAbogadoController {
    /*
    private static CasoAbogadoDAO casoAbogadoDAO = new CasoAbogadoDAO();
    private static CasoController casoController = new CasoController();
    private static AbogadoController abogadoController = new AbogadoController();

    public void anadirCasosAbogado() {
        Abogado abogado = null;
        Caso caso = null;
        do{
            System.out.println("Digame el abogado al que quiere añadir un caso: ");
            abogado = abogadoController.conseguirAbogado();
            System.out.println("Digame el caso que quiere añadir al abogado " + abogado.getNombre());
            caso = casoController.conseguirCaso();
            if (casoAbogadoDAO.elAbogadoTieneCaso(abogado,caso))
                System.out.println("El abogado " + abogado.getNombre() + " ya tiene el caso " + caso.getNumExpediente() + " añadido.");
            else
                System.out.println("El abogado " + abogado.getNombre() + " ha sido relacionado correctamente con el caso " + caso.getNumExpediente() + ".");

        }while(mas("añadir"));
    }
    public void anadirAbogadoCasos() {
        Abogado abogado = null;
        Caso caso = null;
        do{
            caso = caso(casos,abogado,casoAbogados);
            abogado = abogado(abogados,caso,casoAbogados);
        }while(mas("añadir"));
        String casoAbogado = caso.getNumExpediente()+abogado.getDni();
        CasoAbogado casoAbogado1 = new CasoAbogado(casoAbogado);
        casoAbogados.add(casoAbogado1);
    }
    public void eliminarCasosAbogado() {}
    public void eliminarAbogadoCasos() {}

    private boolean mas(String mas) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Desea " + mas + " mas?");
        return sc.nextLine().equalsIgnoreCase("si");
    }

     */
}
