import ModeloController.*;
import ModeloDAO.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static PersonaDAO personaDAO;
    public static PersonaController personaController;
    public static ClienteDAO clienteDAO;
    public static ClienteController clienteController;
    public static AbogadoDAO abogadoDAO;
    public static AbogadoController abogadoController;
    public static CasoDAO casoDAO;
    public static CasoController casoController;
    //public static CasoAbogadoDAO casoAbogadoDAO;
    //public static CasoAbogadoController casoAbogadoController;
    public static JuicioDAO juicioDAO;
    public static JuicioController juicioController;
    public static String[] objetos = new String[5];
    public static StringBuilder opciones1 = new StringBuilder();
    public static StringBuilder opciones2 = new StringBuilder();
    public static void main(String[] args) {
        declararVariables();
        declararObjetos();
        crearOpciones(0);
        opcion();
    }
    public static void declararVariables() {
        personaDAO = new PersonaDAO();
        clienteDAO = new ClienteDAO();
        abogadoDAO = new AbogadoDAO();
        casoDAO = new CasoDAO();
        //casoAbogadoDAO = new CasoAbogadoDAO();
        juicioDAO = new JuicioDAO();
        juicioController = new JuicioController(juicioDAO, null);
        personaController = new PersonaController(personaDAO, clienteDAO, abogadoDAO);
        clienteController = new ClienteController(personaController,clienteDAO);
        abogadoController = new AbogadoController(personaController,abogadoDAO,null,casoDAO);
        casoController = new CasoController(casoDAO,clienteController,juicioController,abogadoController,abogadoDAO,clienteDAO, juicioDAO);
        abogadoController.setCasoController(casoController);
        juicioController.setCasoController(casoController);
        //casoAbogadoController = new CasoAbogadoController();
    }
    public static void declararObjetos() {
        objetos[0] = "cliente";
        objetos[1] = "abogado";
        objetos[2] = "caso";
        objetos[3] = "juicio";
        objetos[4] = "casos y abogados";
    }
    public static void crearOpciones(int queOpcion) {
        if (queOpcion == 0) {
            opciones1.append("Seleccione una opcion:\n");
            for (int i = 0; i < objetos.length; i++) {
                opciones1.append(i+1);
                opciones1.append(". Operaciones con ");
                opciones1.append(objetos[i]);
                opciones1.append(".\n");
            }
        }
        else if (queOpcion != 5) {
            opciones2.setLength(0);
            opciones2.append("Seleccione una opcion:\n");
            opciones2.append("1. Ver la informacion un ");
            opciones2.append(objetos[queOpcion-1]);
            opciones2.append(".\n");
            opciones2.append("2. Crear un ");
            opciones2.append(objetos[queOpcion-1]);
            opciones2.append(".\n");
            opciones2.append("3. Modificar un ");
            opciones2.append(objetos[queOpcion-1]);
            opciones2.append(".\n");
            opciones2.append("4. Eliminar un ");
            opciones2.append(objetos[queOpcion-1]);
            opciones2.append(".\n");
            opciones2.append("5. Ver todos los ");
            opciones2.append(objetos[queOpcion-1]);
            opciones2.append(".\n");
        }else {
            opciones2.setLength(0);
            opciones2.append("Seleccione una opcion:\n");
            opciones2.append("1. Añadir casos a un abogado");
            opciones2.append(".\n");
            opciones2.append("2. Añadir abogados a un caso");
            opciones2.append(".\n");
            opciones2.append("3. Eliminar casos a un abogado");
            opciones2.append(".\n");
            opciones2.append("4. Eliminar abogados a un caso");
            opciones2.append(".\n");
        }
    }
    public static void opcion() {
        Scanner sc = new Scanner(System.in);
        boolean yes;
        do {
            opciones(sc);
            yes = continuar(sc);
        }while (yes);
    }
    public static void opciones(Scanner sc) {
        try {
            System.out.println(opciones1);
            int primeraOpcion = sc.nextInt();
            crearOpciones(primeraOpcion);
            System.out.println(opciones2);
            int segundaOpcion = sc.nextInt();
            switch (primeraOpcion) {
                case 1:
                    switch (segundaOpcion) {
                        case 1 -> clienteController.verCliente();
                        case 2 -> clienteController.insertarCliente();
                        case 3 -> clienteController.modificarCliente("editar");
                        case 4 -> clienteController.modificarCliente("eliminar");
                        case 5 -> clienteController.verTodos();
                    }
                    break;
                case 2:
                    switch (segundaOpcion) {
                        case 1 -> abogadoController.verAbogado();
                        case 2 -> abogadoController.insertarAbogado();
                        case 3 -> abogadoController.modificarAbogado("editar");
                        case 4 -> abogadoController.modificarAbogado("eliminar");
                        case 5 -> abogadoController.verTodos();
                    }
                    break;
                case 3:
                    switch (segundaOpcion) {
                        case 1 -> casoController.verCaso();
                        case 2 -> casoController.insertarCaso();
                        case 3 -> casoController.modificarCaso();
                        case 4 -> casoController.eliminarCaso();
                        case 5 -> casoController.verTodos();
                    }
                    break;
                case 4:
                    switch (segundaOpcion) {
                        case 1 -> juicioController.verJuicio();
                        case 2 -> System.out.println("No se pueden añadir juicios, primero debera crear su caso.");
                        case 3 -> juicioController.queJuicioModificar();
                        case 4 -> juicioController.eliminarJuicio();
                        case 5 -> juicioController.verTodos();
                    }
                    break;
                case 5:
                    switch (segundaOpcion) {
                        case 1 -> abogadoController.anadirCaso();
                        case 2 -> casoController.anadirAbogado();
                        case 3 -> abogadoController.eliminarCaso();
                        case 4 -> casoController.eliminarAbogado();
                    }
                    break;
                default:
                    System.out.println("No es una opción válida");
                    break;
            }
        }catch (InputMismatchException e){
            System.out.println("Debe escribir un numero.");
        }
    }
    public static boolean continuar(Scanner sc) {
        sc.nextLine();
        System.out.println("¿Desea hacer más operaciones?");
        return sc.nextLine().equalsIgnoreCase("si");
    }
    //TODO Añadir la base de datos
}