package mx.unam.heuristicas.jssp.wsa;

import java.util.ArrayDeque;
import java.util.Deque;

public final class MemoriaEfimeraWsa<S> {

    private final int capacidad;
    private final Deque<S> soluciones;

    public MemoriaEfimeraWsa(
            int capacidad
    ) {

        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad de la memoria debe ser mayor que cero."
            );
        }

        this.capacidad = capacidad;
        this.soluciones = new ArrayDeque<>();
    }

    /**
     * Registra una solución como una posición recientemente
     * visitada por el lobo.
     *
     * Si la memoria está llena, se elimina primero la
     * solución más antigua.
     *
     * @param solucion solución visitada
     */
    public void registrar(
            S solucion
    ) {

        if (solucion == null) {
            throw new IllegalArgumentException(
                    "La solución no puede ser nula."
            );
        }

        if (soluciones.size() == capacidad) {
            soluciones.removeFirst();
        }

        soluciones.addLast(
                solucion
        );
    }

    /**
     * Indica si una solución pertenece a la memoria reciente.
     */
    public boolean contiene(
            S solucion
    ) {

        if (solucion == null) {
            throw new IllegalArgumentException(
                    "La solución no puede ser nula."
            );
        }

        return soluciones.contains(
                solucion
        );
    }

    public int tamano() {
        return soluciones.size();
    }

    public int capacidad() {
        return capacidad;
    }
}