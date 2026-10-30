package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Trabajo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Genera una solución inicial recorriendo las operaciones por capas.
     *
     * Primero se considera la primera operación de cada trabajo, después
     * la segunda operación de cada trabajo y así sucesivamente hasta
     * procesar todas las operaciones de la instancia.
     *
     * Cada operación se agrega a la secuencia correspondiente a la máquina
     * que debe procesarla, conservando el orden definido por dicho recorrido.
     *
     * Esta estrategia permite construir una solución factible y generalmente
     * de mejor calidad que una agrupación directa por máquina, por lo que puede
     * utilizarse como solución base para inicializar la población de WSA.
     *
     * @param instancia instancia del problema JSSP
     * @return solución inicial construida mediante un recorrido por capas
     */
    public static SolucionJssp generarSolucionInicialPorCapas(
            InstanciaJssp instancia) {

        if (instancia == null) {
            throw new ExcepcionAplicacion(
                    "La instancia JSSP no puede ser nula.");
        }

        Map<Integer, List<Operacion>> operacionesPorMaquina = new LinkedHashMap<>();

        for (int idMaquina = 0; idMaquina < instancia.numeroMaquinas(); idMaquina++) {

            operacionesPorMaquina.put(
                    idMaquina,
                    new ArrayList<>());
        }

        List<Iterator<Operacion>> iteradoresTrabajos = new ArrayList<>();

        for (Trabajo trabajo : instancia.trabajos()) {
            iteradoresTrabajos.add(
                    trabajo.operaciones().iterator());
        }

        for (int indiceOperacion = 0; indiceOperacion < instancia.numeroMaquinas(); indiceOperacion++) {

            for (Iterator<Operacion> iterador : iteradoresTrabajos) {

                Operacion operacion = iterador.next();

                operacionesPorMaquina
                        .get(operacion.idMaquina())
                        .add(operacion);
            }
        }

        List<SecuenciaMaquina> secuencias = new ArrayList<>();

        for (Map.Entry<Integer, List<Operacion>> entrada : operacionesPorMaquina.entrySet()) {

            secuencias.add(
                    new SecuenciaMaquina(
                            entrada.getKey(),
                            entrada.getValue()));
        }

        return new SolucionJssp(secuencias);
    }
}