package mx.unam.heuristicas.jssp.wsa;

public final class Lobo<S> {

    private S solucion;
    private double costo;
    private final MemoriaEfimeraWsa<S> memoria;

    /**
     * Construye un lobo a partir de una solución y su costo.
     *
     * La solución representa la posición actual del lobo dentro
     * del espacio de búsqueda.
     *
     * @param solucion      solución asociada a la posición del lobo
     * @param costo         costo de la solución}
     * @param tamanoMemoria tamaño de la memoria efímera del lobo
     */
    public Lobo(
            S solucion,
            double costo,
            int tamanoMemoria) {

        if (solucion == null) {
            throw new IllegalArgumentException(
                    "La solución del lobo no puede ser nula.");
        }

        if (!Double.isFinite(costo)) {
            throw new IllegalArgumentException(
                    "El costo del lobo debe ser un número finito.");
        }

        this.solucion = solucion;
        this.costo = costo;
        this.memoria = new MemoriaEfimeraWsa<>(tamanoMemoria);
        this.memoria.registrar(
                solucion);
    }

    /**
     * Indica si el lobo ha ocupado recientemente
     * una solución determinada.
     */
    public boolean recuerda(
            S solucion
    ) {
        return memoria.contiene(
                solucion
        );
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
     * @param nuevoCosto    costo de la nueva posición
     */
    public void actualizar(
            S nuevaSolucion,
            double nuevoCosto) {

        if (nuevaSolucion == null) {
            throw new IllegalArgumentException(
                    "La solución del lobo no puede ser nula.");
        }

        if (!Double.isFinite(nuevoCosto)) {
            throw new IllegalArgumentException(
                    "El costo del lobo debe ser un número finito.");
        }

        this.solucion = nuevaSolucion;
        this.costo = nuevoCosto;

        memoria.registrar(
                nuevaSolucion
        );
    }
}