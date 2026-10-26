package mx.unam.heuristicas.jssp.wsa;

public record ParametrosWsa(
        int tamanoPoblacion,
        int numeroIteraciones,
        int numeroIteracionesGeneracionPoblacion,
        int rangoVisual,
        double probabilidadAmenaza,
        long semilla
) {

    public ParametrosWsa {

        if (tamanoPoblacion <= 0) {
            throw new IllegalArgumentException(
                    "El tamaño de la población debe ser mayor que cero."
            );
        }

        if (numeroIteraciones <= 0) {
            throw new IllegalArgumentException(
                    "El número de iteraciones debe ser mayor que cero."
            );
        }

        if (numeroIteracionesGeneracionPoblacion <= 0) {
            throw new IllegalArgumentException(
                    "El número de iteraciones de la población debe ser mayor que cero."
            );
        }

        if (rangoVisual <= 0) {
            throw new IllegalArgumentException(
                    "El rango visual debe ser mayor que cero."
            );
        }

        if (probabilidadAmenaza < 0.0
                || probabilidadAmenaza > 1.0) {

            throw new IllegalArgumentException(
                    "La probabilidad de amenaza debe estar entre 0 y 1."
            );
        }
    }
}