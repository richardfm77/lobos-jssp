package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoboTest {

    @Test
    void construyeLoboCorrectamente() {

        String solucion = "solucion-1";

        Lobo<String> lobo =
                new Lobo<>(
                        solucion,
                        1.5
                );

        assertEquals(
                solucion,
                lobo.solucion()
        );

        assertEquals(
                1.5,
                lobo.costo()
        );
    }

    @Test
    void actualizaSolucionYCosto() {

        Lobo<String> lobo =
                new Lobo<>(
                        "solucion-1",
                        2.0
                );

        lobo.actualizar(
                "solucion-2",
                1.0
        );

        assertEquals(
                "solucion-2",
                lobo.solucion()
        );

        assertEquals(
                1.0,
                lobo.costo()
        );
    }

    @Test
    void rechazaSolucionNulaAlConstruir() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Lobo<>(
                        null,
                        1.0
                )
        );
    }

    @Test
    void rechazaCostoNoFinitoAlConstruir() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Lobo<>(
                        "solucion",
                        Double.NaN
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Lobo<>(
                        "solucion",
                        Double.POSITIVE_INFINITY
                )
        );
    }

    @Test
    void rechazaSolucionNulaAlActualizar() {

        Lobo<String> lobo =
                new Lobo<>(
                        "solucion",
                        1.0
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> lobo.actualizar(
                        null,
                        2.0
                )
        );
    }

    @Test
    void rechazaCostoNoFinitoAlActualizar() {

        Lobo<String> lobo =
                new Lobo<>(
                        "solucion",
                        1.0
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> lobo.actualizar(
                        "otra-solucion",
                        Double.NaN
                )
        );
    }
}