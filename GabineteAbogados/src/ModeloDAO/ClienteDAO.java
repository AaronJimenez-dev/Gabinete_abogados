package ModeloDAO;

import Modelo.Caso;
import Modelo.Cliente;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ClienteDAO {
    private ArrayList<Cliente> clientes = new ArrayList<>();

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }
    public Cliente verCliente(String dni) {
        Cliente cliente;
        try {
            cliente = clientes.stream().filter(c -> c.getPersona().getDni().equals(dni)).findFirst().get();
        }catch (NoSuchElementException e){
            cliente = null;
        }
        return cliente;
    }
    public void insertarCliente(Cliente cliente){
        clientes.add(cliente);
    }
    public void eliminarClientes(Cliente cliente) {
        clientes.remove(cliente);
    }
    public void eliminarCaso(Cliente cliente, Caso caso) {
        cliente.getCasos().remove(caso);
    }
}
