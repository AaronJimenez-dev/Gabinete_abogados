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
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
            return null;
        }
    }
    public Caso conseguirCasoPorJuicio(int id) {
        try{
            Caso caso = casoDAO.verCasoPorJuicio(id);
            return caso;
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
            return null;
        }
    }
    public void insertarCaso() {
        if (verificarCrearCaso()) {
            Caso caso = crearCaso();
            if (caso != null) {
                try{
                    if(casoDAO.insertarCaso(caso) > 0)
                        System.out.println("Caso correctamente añadido a la base de datos.");
                    else
                        System.out.println("El caso no se pudo añadir a la base de datos.");
                }catch (SQLException e) {
                    System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
                }
            }
        }else
            System.out.println("Sin clientes no puede existir ningun caso.");
    }
    public void modificarCaso() {
        Scanner sc = new Scanner(System.in);
        Caso caso = conseguirCaso();
        int modificado = 0;

        if (caso != null){
            System.out.println("¿Desea modificar el cliente?");
            String client = sc.nextLine();

            if (client.equalsIgnoreCase("si")) {//Si se desea modificar el usuario
                Cliente cliente = validarCliente(caso, "modificar");
                if (cliente.getPersona().getDni().isEmpty())
                    System.out.println("El cliente no existe.");
                else
                    caso.setCliente(cliente);
                modificado++;
            }

            System.out.println("¿Desea modificar el juicio?");
            String juici = sc.nextLine();

            if (juici.equalsIgnoreCase("si")) {//Si se desea modificar el juicio
                juicioController.modificarJuicio(caso.getJuicio());
                modificado++;
            }

            if (modificado > 0) {
                int size = 0;
                try {
                    size = casoDAO.modificarCaso(caso);
                    if (size > 0)
                        System.out.println("Se ha modificado el caso sin problemas");
                    else
                        System.out.println("No se han podido aplicar los cambios.");

                }catch (SQLException e){
                    System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
                }

            }else
                System.out.println("No ha habido cambios");
        }
    }
    public void eliminarCaso() {
        Caso caso = conseguirCaso();
        eliminacionCosasCaso(caso);
    }
    public void verTodos(){
        try {
            ArrayList<Caso> casos = casoDAO.getCasos();
            for (Caso caso : casos)
                System.out.println(caso.getNumExpediente());
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
        }
    }

    public void eliminacionCosasCaso(Caso caso){
        eliminarTodoDeCaso(caso);
        try {
            juicioDAO.eliminarJuicio(caso.getJuicio());
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
    }
    public void eliminarTodoDeCaso(Caso caso){
        try {
            int size = 0;
            if (caso != null) {
                size = casoDAO.eliminarCaso(caso);
                try {
                    for (Abogado abogado : caso.getAbogados()) {
                        abogadoDAO.eliminarCaso(abogado, caso);
                    }
                }catch (Exception e){}
            }
            if (size > 0)
                System.out.println("El caso se ha eliminado correctamente.");
            else
                System.out.println("El caso no se ha podido eliminar.");
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
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
            }
            return caso;
        }catch (InputMismatchException e){
            System.out.println("Debe escribir un numero.");
            return null;
        }
    }
    /*
    private int generarNumExpediente() {
        try{
            ArrayList<Caso> casos = casoDAO.getCasos();
            int numExpediente;
            try {
                numExpediente = casos.getLast().getNumExpediente() + 1;
            }catch (NoSuchElementException e) {
                numExpediente = 1;
            }
            return numExpediente;
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
            return 0;
        }
    }

     */
    private Cliente validarCliente(Caso caso, String tipo) {
        Cliente cliente = clienteController.conseguirCliente();
        if (cliente != null) {
            if (tipo.equalsIgnoreCase("modificar"))
                cambiarCliente(cliente, caso);
        }
        return cliente;
    }
    private void cambiarCliente(Cliente cliente, Caso caso) {

    }
    private Juicio validarJuicio(Caso caso){
        return juicioController.validarJuicio(caso);
    }

    public void verAbogadosCaso(){
        try {
            ArrayList<Abogado> abogados = casoDAO.verAbogadosCaso(conseguirCaso());
            for (Abogado abogado : abogados){
                System.out.println(abogado.toString());
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }catch (NullPointerException e){
            System.out.println("Todavia no hay ninguna relacion.");
        }
    }
    public void anadirAbogado() {
        try {
            Caso caso = conseguirCaso();
            if (caso != null) {
                System.out.println("Dime que abogado quieres añadir:");
                Abogado abogado = abogadoController.conseguirAbogado();
                if (abogado != null) {
                    if (casoDAO.buscarAbogado(abogado, caso))
                        System.out.println("El abogado ya está relacionado al abogado.");
                    else {
                        if(casoDAO.anadirAbogado(abogado, caso) > 0)
                            System.out.println("El abogado y el caso se han relacionado exitosamente.");
                        else
                            System.out.println("No se ha podido relacionar el caso y el abogado.");
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
    }
    public void eliminarAbogado(){
        try {
            Caso caso = conseguirCaso();
            if (caso != null) {
                System.out.println("Dime que abogado quieres eliminar:");
                Abogado abogado = abogadoController.conseguirAbogado();
                if (abogado != null) {
                    if (casoDAO.buscarAbogado(abogado, caso)) {
                        if(casoDAO.eliminarAbogado(abogado, caso) > 0)
                            System.out.println("El abogado se ha dejado de relacionar con el caso exitosamente.");
                        else
                            System.out.println("El abogado no se ha podido dejar de relacionar con el caso.");
                    } else
                        System.out.println("El abogado no está relacionado al abogado");
                }
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
    }
}
