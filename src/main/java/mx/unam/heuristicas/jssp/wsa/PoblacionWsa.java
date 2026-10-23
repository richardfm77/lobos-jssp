package mx.unam.heuristicas.jssp.wsa;

import java.util.Collections;
import java.util.List;

public final class PoblacionWsa<S> {

    private final List<Lobo<S>> lobos;

    /**
     * Construye una población a partir de una lista de lobos.
     *
     * @param lobos lobos que forman la población
     */
    public PoblacionWsa(
            List<Lobo<S>> lobos
    ) {

        if (lobos == null || lobos.isEmpty()) {
            throw new IllegalArgumentException(
                    "La población debe contener al menos un lobo."
            );
        }

        if (lobos.stream().anyMatch(lobo -> lobo == null)) {
            throw new IllegalArgumentException(
                    "La población no puede contener lobos nulos."
            );
        }

        this.lobos = List.copyOf(lobos);
    }

    /**
     * Regresa los lobos que forman la población.
     *
     * @return lista inmodificable de lobos
     */
    public List<Lobo<S>> lobos() {
        return Collections.unmodifiableList(lobos);
    }

    /**
     * Regresa el número de lobos de la población.
     *
     * @return tamaño de la población
     */
    public int tamano() {
        return lobos.size();
    }

    /**
     * Obtiene el lobo con menor costo dentro de la población.
     *
     * @return lobo con mejor función de costo
     */
    public Lobo<S> mejorLobo() {

        Lobo<S> mejor = null;

        for (Lobo<S> lobo : lobos) {

            if (mejor == null
                    || lobo.costo() < mejor.costo()) {

                mejor = lobo;
            }
        }

        return mejor;
    }
}