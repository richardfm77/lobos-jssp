package mx.unam.heuristicas.jssp.wsa;

import mx.unam.heuristicas.jssp.util.DoublePrecision;

import java.util.random.RandomGenerator;

public final class AlgoritmoWsa<S> {

    private final ProblemaHeuristico<S> problema;
    private final ParametrosWsa parametros;
    private final RandomGenerator generadorAleatorio;

    /**
     * Construye una ejecución de WSA.
     *
     * Esta clase crea el generador aleatorio maestro de la
     * heurística utilizando la semilla definida en los parámetros.
     *
     * @param problema   problema heurístico
     * @param parametros parámetros de WSA
     * @param generadorAleatorio generador aleatorio maestro
     */
    public AlgoritmoWsa(
            ProblemaHeuristico<S> problema,
            ParametrosWsa parametros,
            RandomGenerator generadorAleatorio) {

        if (problema == null) {
            throw new IllegalArgumentException(
                    "El problema heurístico no puede ser nulo.");
        }

        if (parametros == null) {
            throw new IllegalArgumentException(
                    "Los parámetros de WSA no pueden ser nulos.");
        }

        if (generadorAleatorio == null) {
            throw new IllegalArgumentException(
                    "El generador aleatorio no puede ser nulo.");
        }

        this.problema = problema;
        this.parametros = parametros;
        this.generadorAleatorio = generadorAleatorio;
    }

    /**
     * Ejecuta WSA partiendo de una solución base.
     *
     * La población inicial corresponde a la iteración 0.
     *
     * @param solucionBase solución usada para generar la población inicial
     * @return mejor resultado encontrado durante toda la ejecución
     */
    public ResultadoWsa<S> ejecutar(S solucionBase) {

        if (solucionBase == null) {
            throw new IllegalArgumentException(
                    "La solución base no puede ser nula.");
        }

        GeneradorPoblacionWsa<S> generadorPoblacion = new GeneradorPoblacionWsa<>(
                problema,
                parametros,
                generadorAleatorio);

        PoblacionWsa<S> poblacion = generadorPoblacion.generar(
                solucionBase);

        /*
         * La población inicial es P_0.
         */
        ResultadoWsa<S> mejorResultado = obtenerMejorResultado(
                poblacion,
                0);

        IteracionWsa<S> iteracion = new IteracionWsa<>(
                problema,
                parametros,
                generadorAleatorio);

        for (int numeroIteracion = 1; numeroIteracion <= parametros.numeroIteraciones(); numeroIteracion++) {

            iteracion.ejecutar(
                    poblacion);

            ResultadoWsa<S> mejorIteracion = obtenerMejorResultado(
                    poblacion,
                    numeroIteracion);

            if (DoublePrecision.lessThan(
                    mejorIteracion.mejorCosto(),
                    mejorResultado.mejorCosto())) {

                mejorResultado = mejorIteracion;
            }
        }

        return mejorResultado;
    }

    /**
     * Obtiene el mejor lobo de la población y construye un
     * resultado correspondiente a la iteración indicada.
     */
    private ResultadoWsa<S> obtenerMejorResultado(
            PoblacionWsa<S> poblacion,
            int numeroIteracion) {

        Lobo<S> mejorLobo = poblacion.mejorLobo();

        return new ResultadoWsa<>(
                mejorLobo.solucion(),
                mejorLobo.costo(),
                numeroIteracion);
    }
}