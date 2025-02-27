package ModeloDAO;

import Modelo.Abogado;
import Modelo.Caso;
import Modelo.CasoAbogado;

import java.util.ArrayList;

public class CasoAbogadoDAO {
    /*

    private static CasoDAO casoDAO = new CasoDAO();
    private static AbogadoDAO abogadoDAO = new AbogadoDAO();
    private static ArrayList<CasoAbogado> casoAbogados = new ArrayList<>();

    public static boolean elAbogadoTieneCaso(Abogado abogadoBuscar, Caso casoBuscar) {
        boolean existe = false;
        Abogado abogadoActual = null;
        CasoAbogado casoAbogadoActual = null;
        for (int i = 0; i < casoAbogados.size(); i++) {
            ArrayList<Abogado> abogados = casoAbogados.get(i).getAbogados();
            ArrayList<Caso> casos = casoAbogados.get(i).getCasos();
            for (int j = 0; j < abogados.size(); j++) {
                if (abogados.get(j).getDni().equals(abogadoBuscar.getDni())) {
                    //Si el abogado ya existe le busca el caso
                    for (int o = 0; o < casos.size(); o++) {
                        if (casos.get(i).getNumExpediente() == casoBuscar.getNumExpediente()) {
                            //Si el abogado ya tiene el caso añadido
                            existe = true;
                            o = casos.size();
                            j = abogados.size();
                            i = casoAbogados.size();
                        }
                    }
                    abogadoActual = abogados.get(j);
                    casoAbogadoActual = casoAbogados.get(i);
                }
            }
        }
        if (!existe) {
            if (casoAbogadoActual != null) {
                for (int i = 0; i < casoAbogados.size(); i++) {
                    if (casoAbogados.get(i).getId() == casoAbogadoActual.getId()){
                        casoAbogados.get(i).getCasos().add(casoBuscar);
                    }
                }
            }
        }
        return existe;
    }


 */
}
