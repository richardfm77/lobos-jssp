package mx.unam.heuristicas.jssp.wsa;

import java.util.random.RandomGenerator;

public final class IteracionWsa<S> {

    private final MovimientoWsa<S> movimiento;
    private final AmenazaWsa<S> amenaza;

    /**
     * Construye el comportamiento de una iteración completa de WSA.
     *
     * Tanto el movimiento como la amenaza utilizan la misma instancia
     * del generador aleatorio maestro.
     *
     * @param problema           problema heurístico
     * @param parametros         parámetros de WSA
     * @param generadorAleatorio generador aleatorio maestro de la ejecución
     */
    public IteracionWsa(
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

        this.movimiento = new MovimientoWsa<>(
                problema,
                parametros);

        this.amenaza = new AmenazaWsa<>(
                problema,
                parametros,
                generadorAleatorio);
    }

    /**
     * Ejecuta una iteración completa de WSA.
     *
     * Los lobos son procesados secuencialmente siguiendo el
     * orden en el que aparecen dentro de la población.
     *
     * Cada modificación se aplica inmediatamente, por lo que
     * los lobos procesados posteriormente pueden observar las
     * nuevas posiciones de los lobos procesados anteriormente.
     *
     * @param poblacion población sobre la que se ejecuta la iteración
     */
    public void ejecutar(
            PoblacionWsa<S> poblacion) {

        if (poblacion == null) {
            throw new IllegalArgumentException(
                    "La población no puede ser nula.");
        }

        for (Lobo<S> lobo : poblacion.lobos()) {

            boolean ocurrioAmenaza = amenaza.procesar(
                    lobo);

            if (!ocurrioAmenaza) {

                movimiento.mover(
                        lobo,
                        poblacion);
            }
        }
    }
}