package mx.unam.heuristicas.jssp.wsa;

public interface ProblemaHeuristico<S> {

    /**
     * Calcula el valor de la función de costo de una solución.
     *
     * @param solucion solución que será evaluada
     * @return valor de la función de costo
     */
    double funcionCosto(S solucion);

    /**
     * Genera una solución vecina a partir de una solución dada.
     *
     * @param solucion solución de origen
     * @return solución vecina
     */
    S generarVecino(S solucion);

    /**
     * Calcula la distancia entre dos soluciones.
     *
     * @param primera primera solución
     * @param segunda segunda solución
     * @return distancia entre ambas soluciones
     */
    double distancia(S primera, S segunda);
}