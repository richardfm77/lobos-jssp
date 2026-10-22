package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.ResultadoEvaluacionJssp;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

public final class EvaluadorGraficaJssp {

    private EvaluadorGraficaJssp() {
    }

    public static ResultadoEvaluacionJssp evaluar(
            GraficaDirigida<Operacion> grafica) {

        if (grafica == null) {
            throw new ExcepcionAplicacion(
                    "La gráfica JSSP no puede ser nula.");
        }

        Map<Operacion, Integer> gradosEntrada = new LinkedHashMap<>();

        Map<Operacion, Integer> tiemposFin = new LinkedHashMap<>();

        Queue<Operacion> cola = new ArrayDeque<>();

        for (Operacion operacion : grafica.vertices()) {

            int grado = grafica.gradoEntrada(operacion);

            gradosEntrada.put(
                    operacion,
                    grado);

            tiemposFin.put(
                    operacion,
                    0);

            if (grado == 0) {

                cola.add(operacion);

                tiemposFin.put(
                        operacion,
                        operacion.tiempoProceso());
            }
        }

        while (!cola.isEmpty()) {

            Operacion actual = cola.remove();

            for (Operacion vecino : grafica.vecinosSalientes(actual)) {

                int tiempoCandidato = tiemposFin.get(actual)
                        + vecino.tiempoProceso();

                int tiempoActual = tiemposFin.get(vecino);

                tiemposFin.put(
                        vecino,
                        Math.max(
                                tiempoActual,
                                tiempoCandidato));

                int nuevoGrado = gradosEntrada.get(vecino) - 1;

                gradosEntrada.put(
                        vecino,
                        nuevoGrado);

                if (nuevoGrado == 0) {
                    cola.add(vecino);
                }
            }
        }

        int makespan = 0;

        for (int tiempoFin : tiemposFin.values()) {

            makespan = Math.max(
                    makespan,
                    tiempoFin);
        }

        int verticesEnCiclo = 0;

        for (int grado : gradosEntrada.values()) {

            if (grado > 0) {
                verticesEnCiclo++;
            }
        }

        return new ResultadoEvaluacionJssp(
                makespan,
                verticesEnCiclo);
    }
}