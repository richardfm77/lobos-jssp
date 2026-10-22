package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Trabajo;

import java.util.ArrayList;
import java.util.List;

public final class GeneradorSolucionJssp {

    private GeneradorSolucionJssp() {
    }

    /**
     * Genera una solución inicial agrupando las operaciones
     * por la máquina que debe procesarlas.
     *
     * El orden inicial dentro de cada máquina corresponde
     * al orden en que las operaciones son encontradas al
     * recorrer los trabajos de la instancia.
     *
     * @param instancia instancia del problema
     * @return solución inicial
     */
    public static SolucionJssp generarInicial(
            InstanciaJssp instancia) {

        if (instancia == null) {
            throw new ExcepcionAplicacion(
                    "La instancia JSSP no puede ser nula.");
        }

        List<List<Operacion>> operacionesPorMaquina = new ArrayList<>();

        for (int idMaquina = 0; idMaquina < instancia.numeroMaquinas(); idMaquina++) {

            operacionesPorMaquina.add(
                    new ArrayList<>());
        }

        for (Trabajo trabajo : instancia.trabajos()) {

            for (Operacion operacion : trabajo.operaciones()) {

                operacionesPorMaquina
                        .get(operacion.idMaquina())
                        .add(operacion);
            }
        }

        List<SecuenciaMaquina> secuenciasMaquinas = new ArrayList<>();

        for (int idMaquina = 0; idMaquina < instancia.numeroMaquinas(); idMaquina++) {

            secuenciasMaquinas.add(
                    new SecuenciaMaquina(
                            idMaquina,
                            operacionesPorMaquina.get(idMaquina)));
        }

        return new SolucionJssp(
                secuenciasMaquinas);
    }
}