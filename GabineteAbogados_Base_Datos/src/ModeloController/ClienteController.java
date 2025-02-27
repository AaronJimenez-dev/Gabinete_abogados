package ModeloController;

import Modelo.Cliente;
import Modelo.Persona;
import ModeloDAO.ClienteDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClienteController {
    private PersonaController personaController;
    private ClienteDAO clienteDAO;

    public ClienteController(PersonaController personaController, ClienteDAO clienteDAO) {
        this.personaController = personaController;
        this.clienteDAO = clienteDAO;
    }

    public void verCliente() {
        Cliente cliente = conseguirCliente();
        if (cliente != null)
            System.out.println(cliente.toString());
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
    public void modificarCliente(String queHacer) {
        try {
            ArrayList<Cliente> clientes = clienteDAO.getClientes();
            System.out.println("Digame el dni del cliente a " + queHacer + ".");

            Cliente clienteBorrado = conseguirCliente();
            if (clienteBorrado != null) {
                int size = eliminarCliente(clienteBorrado);

                if (queHacer.equals("eliminar")) {
                    if (size > 0) {
                        System.out.println("Cliente eliminado");
                        personaController.eliminarPersona(clienteBorrado.getPersona());
                    }else{
                        System.out.println("No se ha podido eliminar el cliente");
                    }

                } else if (queHacer.equals("editar")) {
                    if(size > 0) {
                        System.out.println("Digame la informacion del nuevo cliente a " + queHacer + ".");
                        size = editarCliente(clienteBorrado);

                        if (size > 0) //Si el tamaño de clientes recupera el tamaño original se ha modificado correctamente.
                            System.out.println("El cliente se ha editado correctamente");
                        else {
                            System.out.println("El cliente no se ha podido editar");
                            clienteDAO.insertarCliente(clienteBorrado);
                        }
                    }else
                        System.out.println("No se ha podido continuar.");
                }
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
        }
    }
    private int editarCliente(Cliente clienteBorrado) {
        int size = 0;
        try {
            Cliente cliente = new Cliente(personaController.modificarPersona(clienteBorrado.getPersona()));
            cliente.setTelefono(validarTelefono());
            cliente.setCorreo(validarCorreo());
            cliente.setCasos(clienteBorrado.getCasos());
            size = clienteDAO.insertarCliente(cliente);
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
        return size;
    }
    public int eliminarCliente(Cliente cliente) {
        int size = 0;
        try {
            size = clienteDAO.eliminarClientes(cliente);
            personaController.eliminarPersona(cliente.getPersona());
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
        return size;
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
