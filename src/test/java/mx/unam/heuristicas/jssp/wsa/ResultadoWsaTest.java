package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResultadoWsaTest {

    @Test
    void creaResultadoValido() {

        ResultadoWsa<String> resultado =
                new ResultadoWsa<>(
                        "mejor-solucion",
                        0.75,
                        42
                );

        assertEquals(
                "mejor-solucion",
                resultado.mejorSolucion()
        );

        assertEquals(
                0.75,
                resultado.mejorCosto()
        );

        assertEquals(
                42,
                resultado.iteracionEncontrada()
        );
    }

    @Test
    void permiteIteracionCero() {

        ResultadoWsa<Integer> resultado =
                new ResultadoWsa<>(
                        10,
                        1.0,
                        0
                );

        assertEquals(
                0,
                resultado.iteracionEncontrada()
        );
    }

    @Test
    void rechazaSolucionNula() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ResultadoWsa<>(
                        null,
                        1.0,
                        0
                )
        );
    }

    @Test
    void rechazaCostoNoFinito() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ResultadoWsa<>(
                        "solucion",
                        Double.NaN,
                        0
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ResultadoWsa<>(
                        "solucion",
                        Double.POSITIVE_INFINITY,
                        0
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ResultadoWsa<>(
                        "solucion",
                        Double.NEGATIVE_INFINITY,
                        0
                )
        );
    }

    @Test
    void rechazaIteracionNegativa() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ResultadoWsa<>(
                        "solucion",
                        1.0,
                        -1
                )
        );
    }
}