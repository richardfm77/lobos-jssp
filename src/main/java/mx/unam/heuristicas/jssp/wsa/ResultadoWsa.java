package mx.unam.heuristicas.jssp.wsa;

public record ResultadoWsa<S>(
        S mejorSolucion,
        double mejorCosto,
        int iteracionEncontrada
) {

    public ResultadoWsa {

        if (mejorSolucion == null) {
            throw new IllegalArgumentException(
                    "La mejor solución no puede ser nula."
            );
        }

        if (!Double.isFinite(mejorCosto)) {
            throw new IllegalArgumentException(
                    "El mejor costo debe ser un número finito."
            );
        }

        if (iteracionEncontrada < 0) {
            throw new IllegalArgumentException(
                    "La iteración encontrada no puede ser negativa."
            );
        }
    }
}