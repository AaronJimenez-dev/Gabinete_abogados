package ModeloController;

import Modelo.Caso;
import Modelo.Cliente;
import Modelo.Persona;
import ModeloDAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClienteController {
    private PersonaController personaController;
    private ClienteDAO clienteDAO;
    private CasoController casoController;

    public ClienteController(PersonaController personaController, ClienteDAO clienteDAO) {
        this.personaController = personaController;
        this.clienteDAO = clienteDAO;
    }

    public void setCasoController(CasoController casoController) {
        this.casoController = casoController;
    }

    public void verCliente() {
        Cliente cliente = conseguirCliente();
        if (cliente != null) {
            try {
                cliente = clienteDAO.verCasoCliente(cliente);
            } catch (SQLException e) {
                System.out.println("Ha ocurrido un error en la base de datos." + e.getMessage());
            }
            System.out.println(cliente.toString());
        }
    }
    public Cliente conseguirCliente() {
        String dni = personaController.validar("DNI", "^[0-9]{8}[A-Z]");
        try {
            Cliente cliente = clienteDAO.verCliente(dni);
            if (cliente == null)
                System.out.println("El cliente no existe.");
            return cliente;
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
            return null;
        }
    }
    public void insertarCliente(){
        Cliente cliente = crearCliente();
        if (cliente == null)
            System.out.println("El cliente ya existe.");
        else{
            try {
                clienteDAO.insertarCliente(cliente);
            }catch (SQLException e) {
                System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
            }
        }
    }
    public void modificarCliente() {
        System.out.println("Digame el dni del cliente a modificar.");
        Cliente cliente = conseguirCliente();
        if (cliente != null) {
            int size = editarCliente(cliente);
            if (size > 0) //Si el tamaño de clientes recupera el tamaño original se ha modificado correctamente.
                System.out.println("El cliente se ha editado correctamente");
            else
                System.out.println("El cliente no se ha podido editar");
        }else
            System.out.println("No se ha podido continuar.");
    }
    private int editarCliente(Cliente cliente) {
        int size = 0;
        try {
            cliente.setPersona(personaController.modificarPersona(cliente.getPersona()));
            cliente.setTelefono(validarTelefono());
            cliente.setCorreo(validarCorreo());
            size = clienteDAO.modificarCliente(cliente);
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
        return size;
    }
    public void eliminarCliente() {
        System.out.println("Digame el dni del cliente a modificar.");
        Cliente cliente = conseguirCliente();
        int size = 0;

        try {
            for (Caso caso : clienteDAO.verCasoCliente(cliente).getCasos()) {
                casoController.eliminacionCosasCaso(caso);
            }
            size = clienteDAO.eliminarClientes(cliente);
            personaController.eliminarPersona(cliente.getPersona());
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }

        if (size > 0) {
            System.out.println("Cliente eliminado");
            personaController.eliminarPersona(cliente.getPersona());
        }else{
            System.out.println("No se ha podido eliminar el cliente");
        }
    }
    public void verTodos(){
        try {
            ArrayList<Cliente> clientes = clienteDAO.getClientes();
            for (Cliente cliente : clientes)
                System.out.println(cliente.getPersona().getDni());
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
        }
    }

    public Cliente crearCliente(){
        Persona persona = personaController.validarPersona("cliente");
        try {
            if (clienteDAO.verCliente(persona.getDni()) == null) {
                int telefono = validarTelefono();
                String correo = validarCorreo();
                return new Cliente(persona, telefono, correo);
            } else
                return null;
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
            return null;
        }
    }
    public int validarTelefono(){
        Scanner sc = new Scanner(System.in);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[0-9]{9}");
        boolean invalido;
        String numero;
        do{
            invalido = false;
            System.out.println("Introduzca el numero de telefono: ");
            numero = sc.nextLine();
            java.util.regex.Matcher matcher = pattern.matcher(numero);
            if (!matcher.matches()) {
                System.out.println("Numero de telefono con formato incorrecto");
                invalido = true;
            }
        }while (invalido);
        return Integer.parseInt(numero);
    }
    public String validarCorreo(){
        Scanner sc = new Scanner(System.in);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[a-z.-]+@[a-z]+.[a-z]{3}$");
        boolean invalido;
        String correo;
        do{
            invalido = false;
            System.out.println("Introduzca el correo: ");
            correo = sc.nextLine();
            java.util.regex.Matcher matcher = pattern.matcher(correo);
            if (!matcher.matches()) {
                System.out.println("Correo con formato incorrecto");
                invalido = true;
            }
        }while (invalido);
        return correo;
    }
}
