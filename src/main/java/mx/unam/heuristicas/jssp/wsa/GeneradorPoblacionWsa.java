package mx.unam.heuristicas.jssp.wsa;

import mx.unam.heuristicas.jssp.util.DoublePrecision;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.random.RandomGenerator;

public final class GeneradorPoblacionWsa<S> {

        private final int MAX_INTENTOS;

        private final ProblemaHeuristico<S> problema;
        private final ParametrosWsa parametros;
        private final RandomGenerator generadorAleatorio;

        /**
         * Construye un generador de población utilizando la semilla
         * definida en los parámetros de WSA.
         *
         * @param problema     problema heurístico sobre el que trabajará WSA
         * @param parametros   parámetros del algoritmo WSA
         */
        public GeneradorPoblacionWsa(
                        ProblemaHeuristico<S> problema,
                        ParametrosWsa parametros) {

                if (problema == null) {
                        throw new IllegalArgumentException(
                                        "El problema heurístico no puede ser nulo.");
                }

                if (parametros == null) {
                        throw new IllegalArgumentException(
                                        "Los parámetros de WSA no pueden ser nulos.");
                }

                this.problema = problema;
                this.parametros = parametros;
                MAX_INTENTOS = this.parametros.numeroIteracionesGeneracionPoblacion();
                this.generadorAleatorio = new Random(parametros.semilla());
        }

        /**
         * Genera una población inicial alrededor de una solución base.
         *
         * La población contiene la solución base, soluciones cercanas
         * dentro del rango visual y soluciones lejanas fuera de dicho
         * rango. Las soluciones duplicadas son descartadas.
         *
         * @param solucionBase solución utilizada como punto de referencia
         * @return población inicial de WSA
         */
        public PoblacionWsa<S> generar(
                        S solucionBase) {

                if (solucionBase == null) {
                        throw new IllegalArgumentException(
                                        "La solución base no puede ser nula.");
                }

                ArrayList<Lobo<S>> lobos = new ArrayList<>();

                Set<S> solucionesUtilizadas = new HashSet<>();

                agregarLobo(
                                lobos,
                                solucionesUtilizadas,
                                solucionBase);

                int restantes = parametros.tamanoPoblacion() - 1;

                int cantidadCercanos = (restantes + 1) / 2;

                int cantidadLejanos = restantes - cantidadCercanos;

                generarCercanos(
                                solucionBase,
                                cantidadCercanos,
                                lobos,
                                solucionesUtilizadas);

                generarLejanos(
                                solucionBase,
                                cantidadLejanos,
                                lobos,
                                solucionesUtilizadas);

                return new PoblacionWsa<>(lobos);
        }

        private void generarCercanos(
                        S solucionBase,
                        int cantidad,
                        ArrayList<Lobo<S>> lobos,
                        Set<S> solucionesUtilizadas) {

                if (cantidad == 0) {
                        return;
                }

                /*
                 * El primer vecino garantiza que exista al menos
                 * otro lobo dentro del rango visual de la solución base.
                 */
                S vecino = problema.generarVecino(
                                solucionBase);

                double distancia = problema.distancia(
                                solucionBase,
                                vecino);

                if (!esDistanciaCercana(distancia)) {
                        throw new IllegalStateException(
                                        "El vecino generado no se encuentra "
                                                        + "dentro del rango visual.");
                }

                agregarLobo(
                                lobos,
                                solucionesUtilizadas,
                                vecino);

                for (int i = 1; i < cantidad; i++) {

                        S candidato = generarCandidatoCercano(
                                        solucionBase,
                                        solucionesUtilizadas);

                        agregarLobo(
                                        lobos,
                                        solucionesUtilizadas,
                                        candidato);
                }
        }

        private void generarLejanos(
                        S solucionBase,
                        int cantidad,
                        ArrayList<Lobo<S>> lobos,
                        Set<S> solucionesUtilizadas) {

                for (int i = 0; i < cantidad; i++) {

                        S candidato = generarCandidatoLejano(
                                        solucionBase,
                                        solucionesUtilizadas);

                        agregarLobo(
                                        lobos,
                                        solucionesUtilizadas,
                                        candidato);
                }
        }

        private S generarCandidatoCercano(
                        S solucionBase,
                        Set<S> solucionesUtilizadas) {

                for (int intento = 0; intento < MAX_INTENTOS; intento++) {

                        int pasos = 1 + generadorAleatorio.nextInt(
                                        parametros.rangoVisual());

                        S candidato = caminar(
                                        solucionBase,
                                        pasos);

                        double distancia = problema.distancia(
                                        solucionBase,
                                        candidato);

                        if (!solucionesUtilizadas.contains(candidato)
                                        && esDistanciaCercana(distancia)) {

                                return candidato;
                        }
                }

                throw new IllegalStateException(
                                "No fue posible generar una solución cercana distinta.");
        }

        private S generarCandidatoLejano(
                        S solucionBase,
                        Set<S> solucionesUtilizadas) {

                for (int intento = 0; intento < MAX_INTENTOS; intento++) {

                        int pasos = parametros.rangoVisual()
                                        + 1
                                        + generadorAleatorio.nextInt(
                                                        parametros.rangoVisual());

                        S candidato = caminar(
                                        solucionBase,
                                        pasos);

                        double distancia = problema.distancia(
                                        solucionBase,
                                        candidato);

                        if (!solucionesUtilizadas.contains(candidato)
                                        && DoublePrecision.greaterThan(
                                                        distancia,
                                                        parametros.rangoVisual())) {

                                return candidato;
                        }
                }

                throw new IllegalStateException(
                                "No fue posible generar una solución "
                                                + "fuera del rango visual.");
        }

        private S caminar(
                        S solucionInicial,
                        int numeroPasos) {

                S solucionActual = solucionInicial;

                for (int paso = 0; paso < numeroPasos; paso++) {

                        solucionActual = problema.generarVecino(
                                        solucionActual);
                }

                return solucionActual;
        }

        private boolean esDistanciaCercana(
                        double distancia) {

                return DoublePrecision.greaterThan(
                                distancia,
                                0.0)
                                && DoublePrecision.lessThanOrEqual(
                                                distancia,
                                                parametros.rangoVisual());
        }

        private void agregarLobo(
                        ArrayList<Lobo<S>> lobos,
                        Set<S> solucionesUtilizadas,
                        S solucion) {

                double costo = problema.funcionCosto(
                                solucion);

                lobos.add(new Lobo<>(solucion,costo,this.parametros.tamanoMemoria()));

                solucionesUtilizadas.add(solucion);
        }
}