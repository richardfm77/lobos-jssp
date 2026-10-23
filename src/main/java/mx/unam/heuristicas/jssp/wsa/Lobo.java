package mx.unam.heuristicas.jssp.wsa;

public final class Lobo<S> {

    private S solucion;
    private double costo;

    /**
     * Construye un lobo a partir de una solución y su costo.
     *
     * La solución representa la posición actual del lobo dentro
     * del espacio de búsqueda.
     *
     * @param solucion solución asociada a la posición del lobo
     * @param costo costo de la solución
     */
    public Lobo(
            S solucion,
            double costo
    ) {

        if (solucion == null) {
            throw new IllegalArgumentException(
                    "La solución del lobo no puede ser nula."
            );
        }

        if (!Double.isFinite(costo)) {
            throw new IllegalArgumentException(
                    "El costo del lobo debe ser un número finito."
            );
        }

        this.solucion = solucion;
        this.costo = costo;
    }

    /**
     * Regresa la solución que representa la posición actual del lobo.
     *
     * @return solución actual
     */
    public S solucion() {
        return solucion;
    }

    /**
     * Regresa el costo de la posición actual del lobo.
     *
     * @return costo actual
     */
    public double costo() {
        return costo;
    }

    /**
     * Actualiza simultáneamente la posición y el costo del lobo.
     *
     * @param nuevaSolucion nueva posición del lobo
     * @param nuevoCosto costo de la nueva posición
     */
    public void actualizar(
            S nuevaSolucion,
            double nuevoCosto
    ) {

        if (nuevaSolucion == null) {
            throw new IllegalArgumentException(
                    "La solución del lobo no puede ser nula."
            );
        }

        if (!Double.isFinite(nuevoCosto)) {
            throw new IllegalArgumentException(
                    "El costo del lobo debe ser un número finito."
            );
        }

        this.solucion = nuevaSolucion;
        this.costo = nuevoCosto;
    }
}