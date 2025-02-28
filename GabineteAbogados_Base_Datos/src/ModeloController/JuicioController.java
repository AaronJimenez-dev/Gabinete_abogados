package ModeloController;

import Modelo.Caso;
import Modelo.Cliente;
import Modelo.Juicio;
import ModeloDAO.CasoDAO;
import ModeloDAO.JuicioDAO;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JuicioController {
    private JuicioDAO juicioDAO;
    private CasoController casoController;

    public JuicioController(JuicioDAO juicioDAO, CasoController casoController) {
        this.juicioDAO = juicioDAO;
        this.casoController = casoController;
    }

    public void setCasoController(CasoController casoController) {
        this.casoController = casoController;
    }

    public Juicio validarJuicio(Caso caso) throws DateTimeException {
        try {
            Juicio juicio;
            LocalDate fechaInicio = validarFecha("inicio", LocalDate.now());
            String tieneFecha = tieneFecha();
            if (tieneFecha.equalsIgnoreCase("no")) {
                String estado = calcularEstado("");
                juicio = new Juicio(fechaInicio, estado);
            } else {
                LocalDate fechaFin = validarFecha("finalizacion", fechaInicio);
                String estado = calcularEstado(fechaFin.toString());
                juicio = new Juicio(fechaInicio, fechaFin, estado);
            }
            //juicio.setId(generarId());
            juicioDAO.insertarJuicio(juicio);
            return juicio;
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
            return null;
        }
    }
    public void verJuicio() {
        Juicio juicio = conseguirJuicio();
        if (juicio != null)
            System.out.println(juicio.toString());
    }
    //CAMBIARLO TODO
    public void modificarJuicio(Juicio juicio) {
        Scanner sc = new Scanner(System.in);
        int modificado = 0;
        if (juicio.getEstado().equals("finalizado"))
            System.out.println("El juicio ya ha finalizado, no puede hacer cambios.");
        else {
            if (juicio.getFechaFin() == null) {
                String tieneFecha = tieneFecha();

                if (tieneFecha.equalsIgnoreCase("no")) {//Si no tiene fecha damos opciona a cambiar el estado
                    System.out.println("¿Desea modificar el estado del juicio?, actualmente es: " + juicio.getEstado());
                    String respuesta = sc.nextLine();
                    if (respuesta.equalsIgnoreCase("si")) { //El juicio cambia de estado entre tramite y anulado
                        juicio.setEstado(cambiarEstado(juicio.getEstado()));
                        modificado++;
                    }

                } else {//Si tiene fecha de finalizacion se cambia es estado automaticamente a finalizado.
                    LocalDate fechaFin = validarFecha("finalizacion", juicio.getFechaInicio());
                    juicio.setFechaFin(fechaFin);
                    juicio.setEstado(calcularEstado(fechaFin.toString()));
                    modificado++;
                }
            }

            if (modificado > 0) {
                int size = 0;
                try {
                    size = juicioDAO.modificarJuicio(juicio);
                    if (size > 0)
                        System.out.println("Muchas gracias, se ha modificado el juicio sin problemas, ahora el estado del juicio es: " + juicio.getEstado());
                    else
                        System.out.println("No se han podido aplicar los cambios.");

                } catch (SQLException e) {
                    System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
                }
            } else
                System.out.println("No ha habido cambios");
        }
    }
    public void eliminarJuicio() {
        int size = 0;
        try {
            Juicio juicio = conseguirJuicio();
            if (juicio != null) {
                casoController.eliminarTodoDeCaso(casoController.conseguirCasoPorJuicio(juicio.getId()));
                size = juicioDAO.eliminarJuicio(juicio);
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
        if(size > 0)
            System.out.println("Se ha eliminado el juicio sin problemas.");
        else
            System.out.println("No se ha podido eliminar el juicio");
    }
    public void verTodos(){
        try {
            ArrayList<Juicio> juicios = juicioDAO.getJuicios();
            for (Juicio juicio : juicios)
                System.out.println(juicio.getId());
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
        }
    }

    public Juicio conseguirJuicio() {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Dime el id del juicio.");
            int id = sc.nextInt();
            Juicio juicio = juicioDAO.verJuicio(id);
            if (juicio == null)
                System.out.println("El juicio no existe.");
            return juicio;
        }catch (InputMismatchException e){
            System.out.println("Debe escribir un numero.");
            return null;
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
            return null;
        }
    }

    private String tieneFecha() {
        Scanner sc = new Scanner(System.in);
        boolean yes;
        String respuesta;
        do {
            System.out.println("¿Desea añadir una fecha de finalizacion?");
            respuesta = sc.nextLine();
            if (respuesta.equalsIgnoreCase("si"))
                yes = false;
            else if (respuesta.equalsIgnoreCase("no"))
                yes = false;
            else {
                System.out.println("Tienes que decir 'si' o 'no' para avanzar");
                yes = true;
            }
        }while (yes);
        return respuesta;
    }
    private LocalDate validarFecha(String fechaPreguntar, LocalDate fechaComparar) {
        Scanner sc = new Scanner(System.in);
        boolean yes;
        LocalDate fecha;
        do{
            System.out.println("¿Cual es la fecha de " + fechaPreguntar + "?");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fecha = LocalDate.parse(sc.nextLine(), formatter);
            if(fechaPreguntar.equals("inicio") && fecha.isAfter(fechaComparar)) {
                System.out.println("La fecha de inicio no puede ser posterior a hoy.");
                yes = true;
            }else if (fechaPreguntar.equals("finalizacion") && fecha.isBefore(fechaComparar)) {
                System.out.println("La fecha no puede ser previa a la fecha de inicio.");
                yes = true;
            }else
                yes = false;
        }while(yes);
        return fecha;
    }
    private String calcularEstado(String fecha) {
        String estado = "tramite";
        if (!fecha.isEmpty()){
            LocalDate fechaFin = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (fechaFin.isBefore(LocalDate.now()))
                estado = "finalizado";
        }
        return estado;
    }
    /*
    private int generarId() {
        ArrayList<Juicio> juicios = juicioDAO.getJuicios();
        int id;
        try {
            id = juicios.getLast().getId() + 1;
        }catch (NoSuchElementException e) {
            id = 1;
        }
        return id;
    }
     */
    public void queJuicioModificar(){
        Juicio juicio = conseguirJuicio();
        if (juicio != null)
            modificarJuicio(juicio);
    }
    private String cambiarEstado(String estado) {
        if (estado.equals("tramite"))
            return "anulado";
        else
            return "tramite";
    }
}
