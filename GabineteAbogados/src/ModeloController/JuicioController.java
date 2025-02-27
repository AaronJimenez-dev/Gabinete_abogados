package ModeloController;

import Modelo.Caso;
import Modelo.Cliente;
import Modelo.Juicio;
import ModeloDAO.CasoDAO;
import ModeloDAO.JuicioDAO;

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
        Juicio juicio;
        LocalDate fechaInicio = validarFecha("inicio", LocalDate.now());
        String tieneFecha = tieneFecha();
        if (tieneFecha.equalsIgnoreCase("no")) {
            String estado = calcularEstado(tieneFecha);
            juicio = new Juicio(fechaInicio, estado);
        }
        else {
            LocalDate fechaFin = validarFecha("finalizacion", fechaInicio);
            String estado = calcularEstado("");
            juicio = new Juicio(fechaInicio, fechaFin, estado);
        }
        juicio.setCaso(caso);
        juicio.setId(generarId());
        juicioDAO.insertarJuicio(juicio);
        return juicio;
    }
    public void verJuicio() {
        Juicio juicio = conseguirJuicio();
        if (juicio != null)
            System.out.println(juicio.toString());
    }
    public void modificarJuicio(Juicio juicio) {
        Scanner sc = new Scanner(System.in);
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
                        System.out.println("Muchas gracias, ahora el estado del juicio es: " + juicio.getEstado());
                    }

                }else {//Si tiene fecha de finalizacion se cambia es estado automaticamente a finalizado.
                    LocalDate fechaFin = validarFecha("finalizacion", juicio.getFechaInicio());
                    juicio.setFechaFin(fechaFin);
                    juicio.setEstado(calcularEstado(""));
                    System.out.println("Muchas gracias, ahora el estado del juicio es: " + juicio.getEstado());
                }
            }
            System.out.println("Se ha modificado el juicio sin problemas");
        }
    }
    public void eliminarJuicio() {
        Juicio juicio = conseguirJuicio();
        if (juicio != null) {
            juicioDAO.eliminarJuicio(juicio);
            casoController.eliminarTodoDeCaso(juicio.getCaso());
        }
    }
    public void verTodos(){
        ArrayList<Juicio> juicios = juicioDAO.getJuicios();
        for (Juicio juicio : juicios)
            System.out.println(juicio.getId());
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
    private String calcularEstado(String fechaFin) {
        String estado;
        if (fechaFin.isEmpty())
            estado = "finalizado";
        else
            estado = "tramite";
        return estado;
    }
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
