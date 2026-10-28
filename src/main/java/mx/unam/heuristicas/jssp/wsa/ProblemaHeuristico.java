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
     * Genera un vecino de una solución que se encuentra un paso
     * más cerca de una solución objetivo.
     *
     * @param solucion solución actual
     * @param objetivo solución hacia la cual se realiza el movimiento
     * @return vecino de la solución actual más cercano al objetivo
     */
    S generarVecinoHacia(S solucion, S objetivo);

    /**
     * Calcula la distancia entre dos soluciones.
     *
     * @param primera primera solución
     * @param segunda segunda solución
     * @return distancia entre ambas soluciones
     */
    double distancia(S primera, S segunda);
}