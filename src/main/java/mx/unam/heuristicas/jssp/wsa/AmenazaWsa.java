package mx.unam.heuristicas.jssp.wsa;

import mx.unam.heuristicas.jssp.util.DoublePrecision;

import java.util.random.RandomGenerator;

public final class AmenazaWsa<S> {

    private final ProblemaHeuristico<S> problema;
    private final ParametrosWsa parametros;
    private final RandomGenerator generadorAleatorio;

    /**
     * Construye el comportamiento de amenaza y escape de WSA.
     *
     * @param problema   problema heurístico sobre el que trabaja WSA
     * @param parametros parámetros del algoritmo WSA
     */
    public AmenazaWsa(
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

        this.problema = problema;
        this.parametros = parametros;
        this.generadorAleatorio = generadorAleatorio;
    }

    /**
     * Determina si ocurre una amenaza y, en ese caso,
     * hace escapar al lobo.
     *
     * @param lobo lobo sobre el cual puede ocurrir la amenaza
     * @return true si ocurrió una amenaza; false en otro caso
     */
    public boolean procesar(
            Lobo<S> lobo) {

        if (lobo == null) {
            throw new IllegalArgumentException(
                    "El lobo no puede ser nulo.");
        }

        if (!ocurreAmenaza()) {
            return false;
        }

        escapar(lobo);

        return true;
    }

    private boolean ocurreAmenaza() {

        return generadorAleatorio.nextDouble() < parametros.probabilidadAmenaza();
    }

    /**
     * Realiza una caminata aleatoria hasta abandonar el
     * rango visual de la posición donde surgió la amenaza.
     */
    private void escapar(
            Lobo<S> lobo) {

        S solucionOrigen = lobo.solucion();

        S solucionActual = solucionOrigen;

        for (int intento = 0; intento < parametros.maxIntentosEscape(); intento++) {

            
            S candidato = problema.generarVecino(
                            solucionActual
                        );

            if (lobo.recuerda(candidato)) {
                continue;
            }
            
            solucionActual =
                    candidato;

            double distancia = problema.distancia(
                    solucionOrigen,
                    solucionActual);

            if (DoublePrecision.greaterThan(
                    distancia,
                    parametros.rangoVisual())) {

                double nuevoCosto = problema.funcionCosto(
                        solucionActual);

                lobo.actualizar(
                        solucionActual,
                        nuevoCosto);

                return;
            }
        }

        throw new IllegalStateException(
                "No fue posible escapar fuera del rango visual.");
    }
}