package ModeloController;

import Modelo.Abogado;
import Modelo.Caso;
import Modelo.Cliente;
import Modelo.Juicio;
import ModeloDAO.AbogadoDAO;
import ModeloDAO.CasoDAO;
import ModeloDAO.ClienteDAO;
import ModeloDAO.JuicioDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CasoController {
    private CasoDAO casoDAO;
    private ClienteController clienteController;
    private JuicioController juicioController;
    private AbogadoController abogadoController;
    private AbogadoDAO abogadoDAO;
    private ClienteDAO clienteDAO;
    private JuicioDAO juicioDAO;

    public CasoController(CasoDAO casoDAO, ClienteController clienteController, JuicioController juicioController, AbogadoController abogadoController, AbogadoDAO abogadoDAO, ClienteDAO clienteDAO, JuicioDAO juicioDAO) {
        this.casoDAO = casoDAO;
        this.clienteController = clienteController;
        this.juicioController = juicioController;
        this.abogadoController = abogadoController;
        this.abogadoDAO = abogadoDAO;
        this.clienteDAO = clienteDAO;
        this.juicioDAO = juicioDAO;
    }

    public void verCaso() {
        Caso caso = conseguirCaso();
        if (caso != null)
            System.out.println(caso.toString());
    }
    public Caso conseguirCaso() {
        Scanner sc = new Scanner(System.in);
        try{
            System.out.println("Dime el numero de expediente del caso.");
            int numExpediente = sc.nextInt();
            Caso caso = new Caso();
            if (numExpediente <= 0) {
                System.out.println("El numero de expediente no puede ser menor o igual a 0.");
                caso = null;
            }else {
                caso = casoDAO.verCaso(numExpediente);
                if (caso == null)
                    System.out.println("El caso no existe.");
            }
            return caso;
        }catch (InputMismatchException e){
            System.out.println("Debe escribir un numero.");
            return null;
        }
    }
    public void insertarCaso() {
        if (verificarCrearCaso()) {
            Caso caso = crearCaso();
            if (caso != null) {
                if (caso == null)
                    System.out.println("El cliente no puede estar vacio");
                else
                    casoDAO.insertarCaso(caso);
            }
        }else
            System.out.println("Sin clientes no puede existir ningun caso.");
    }
    public void modificarCaso() {
        Scanner sc = new Scanner(System.in);
        Caso caso = conseguirCaso();
        if (caso != null){
            System.out.println("¿Desea modificar el cliente?");
            String client = sc.nextLine();

            if (client.equalsIgnoreCase("si")) {//Si se desea modificar el usuario
                Cliente cliente = validarCliente(caso, "modificar");
                if (cliente.getPersona().getDni().isEmpty())
                    System.out.println("El cliente no existe.");
                else
                    caso.setCliente(cliente);
            }

            System.out.println("¿Desea modificar el juicio?");
            String juici = sc.nextLine();

            if (!juici.equalsIgnoreCase("no")) {//Si se desea modificar el juicio
                juicioController.modificarJuicio(caso.getJuicio());
            }

            System.out.println("Se ha modificado el caso sin problemas");
        }
    }
    public void eliminarCaso() {
        Caso caso = conseguirCaso();
        eliminarTodoDeCaso(caso);
    }
    public void verTodos(){
        ArrayList<Caso> casos = casoDAO.getCasos();
        for (Caso caso : casos)
            System.out.println(caso.getNumExpediente());
    }

    public void eliminarTodoDeCaso(Caso caso){
        if (caso != null){
            casoDAO.eliminarCaso(caso);
            for (Abogado abogado : caso.getAbogados()) {
                abogadoDAO.eliminarCaso(abogado,caso);
            }
            clienteDAO.eliminarCaso(caso.getCliente(),caso);
            juicioDAO.eliminarJuicio(caso.getJuicio());
        }
    }
    private boolean verificarCrearCaso() {
        try {
            if (clienteDAO.getClientes().isEmpty())
                return false;
            else
                return true;
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
            return false;
        }
    }
    public Caso crearCaso(){
        try{
            ArrayList<Caso> casos = casoDAO.getCasos();
            Caso caso = new Caso();
            System.out.println("El numero de expediente se crea automáticamente");
            System.out.println("Inserte el cliente:");
            Cliente cliente = validarCliente(caso, "crear");
            if (cliente == null)
                System.out.println("El cliente no existe.");
            else {
                caso.setCliente(cliente);
                System.out.println("Ahora vamos a crear el juicio para este caso:");
                caso.setJuicio(validarJuicio(caso));
                caso.setNumExpediente(generarNumExpediente());
            }
            return caso;
        }catch (InputMismatchException e){
            System.out.println("Debe escribir un numero.");
            return null;
        }
    }
    private int generarNumExpediente() {
        ArrayList<Caso> casos = casoDAO.getCasos();
        int numExpediente;
        try {
            numExpediente = casos.getLast().getNumExpediente() + 1;
        }catch (NoSuchElementException e) {
            numExpediente = 1;
        }
        return numExpediente;
    }
    private Cliente validarCliente(Caso caso, String tipo) {
        Cliente cliente = clienteController.conseguirCliente();
        if (cliente != null) {
            if (tipo.equalsIgnoreCase("modificar"))
                cambiarCliente(cliente, caso);
            else {
                try {
                    cliente.getCasos().add(caso);
                }catch (NullPointerException e){
                    cliente.setCasos(new ArrayList<>());
                    cliente.getCasos().add(caso);
                }
            }
        }
        return cliente;
    }
    private void cambiarCliente(Cliente cliente, Caso caso) {
        caso.getCliente().getCasos().remove(caso);//borramos al cliente anterior el caso
        cliente.getCasos().add(caso);//le añadimos al nuevo cliente el caso nuevo
    }
    private Juicio validarJuicio(Caso caso){
        return juicioController.validarJuicio(caso);
    }

    public void anadirAbogado() {
        Caso caso = conseguirCaso();
        if (caso != null){
            System.out.println("Dime que abogado quieres añadir:");
            Abogado abogado = abogadoController.conseguirAbogado();
            if (abogado != null){
                if (casoDAO.buscarAbogado(abogado, caso))
                    System.out.println("El abogado ya está relacionado al abogado.");
                else {
                    casoDAO.anadirAbogado(abogado, caso);
                    abogadoDAO.anadirCaso(abogado,caso);
                }
            }
        }
    }
    public void eliminarAbogado(){
        Caso caso = conseguirCaso();
        if (caso != null){
            System.out.println("Dime que abogado quieres eliminar:");
            Abogado abogado = abogadoController.conseguirAbogado();
            if (abogado != null){
                if (casoDAO.buscarAbogado(abogado, caso)) {
                    casoDAO.eliminarAbogado(abogado, caso);
                    abogadoDAO.eliminarCaso(abogado,caso);
                }
                else
                    System.out.println("El abogado no está relacionado al abogado");
            }
        }
    }
}
