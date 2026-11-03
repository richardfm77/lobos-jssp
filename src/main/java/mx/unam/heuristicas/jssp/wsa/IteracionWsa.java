package mx.unam.heuristicas.jssp.wsa;

import java.util.Objects;
import java.util.random.RandomGenerator;

public final class IteracionWsa<S> {

    private final MovimientoWsa<S> movimiento;
    private final AmenazaWsa<S> amenaza;
    private final RegistroEjecucionWsa registro;

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

        this(problema, parametros, generadorAleatorio, null);
    }

    /**
     * Construye el comportamiento de una iteración completa de WSA.
     *
     * Tanto el movimiento como la amenaza utilizan la misma instancia
     * del generador aleatorio maestro.
     *
     * @param problema           problema heurístico
     * @param parametros         parámetros de WSA
     * @param generadorAleatorio generador aleatorio maestro de la ejecución
     * @param registro           registro de ejecución de WSA
     */
    public IteracionWsa(
            ProblemaHeuristico<S> problema,
            ParametrosWsa parametros,
            RandomGenerator generadorAleatorio,
            RegistroEjecucionWsa registro) {

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

        this.registro = registro;
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

        ejecutar(
                poblacion,
                0);
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
     * @param poblacion       población sobre la que se ejecuta la iteración
     * @param numeroIteracion número de la iteración para el registro de ejecución.
     */
    public void ejecutar(
            PoblacionWsa<S> poblacion,
            int numeroIteracion) {

        if (poblacion == null) {
            throw new IllegalArgumentException(
                    "La población no puede ser nula.");
        }

        int indiceLobo = 0;

        for (Lobo<S> lobo : poblacion.lobos()) {

            S solucionAnterior = lobo.solucion();

            double costoAnterior = lobo.costo();

            /*
             * Primero se verifica si el lobo
             * debe escapar de una amenaza.
             */
            boolean huboAmenaza = amenaza.procesar(
                    lobo);

            if (huboAmenaza) {
                registrarCambio(
                        numeroIteracion,
                        indiceLobo,
                        "AMENAZA",
                        solucionAnterior,
                        costoAnterior,
                        lobo);
            } else {
                /*
                 * Si no hubo amenaza, el lobo realiza
                 * su movimiento normal.
                 */
                movimiento.mover(
                        lobo,
                        poblacion);

                registrarCambio(
                        numeroIteracion,
                        indiceLobo,
                        "MOVIMIENTO",
                        solucionAnterior,
                        costoAnterior,
                        lobo);
            }

            indiceLobo++;
        }
    }

    /**
     * Registra una solución únicamente si el lobo
     * cambió efectivamente de posición.
     */
    private void registrarCambio(
            int numeroIteracion,
            int indiceLobo,
            String accion,
            S solucionAnterior,
            double costoAnterior,
            Lobo<S> lobo) {

        if (registro == null) {
            return;
        }

        S solucionNueva = lobo.solucion();

        if (Objects.equals(
                solucionAnterior,
                solucionNueva)) {
            return;
        }

        registro.registrarAceptacion(
                numeroIteracion,
                indiceLobo,
                accion,
                costoAnterior,
                lobo.costo());
    }
}