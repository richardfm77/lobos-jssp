package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParametrosWsaTest {

    @Test
    void creaParametrosValidos() {

        ParametrosWsa parametros =
                new ParametrosWsa(
                        30,
                        1000,
                        5,
                        0.1,
                        12345L
                );

        assertEquals(
                30,
                parametros.tamanoPoblacion()
        );

        assertEquals(
                1000,
                parametros.numeroIteraciones()
        );

        assertEquals(
                5,
                parametros.rangoVisual()
        );

        assertEquals(
                0.1,
                parametros.probabilidadAmenaza()
        );

        assertEquals(
                12345L,
                parametros.semilla()
        );
    }

    @Test
    void rechazaTamanoPoblacionInvalido() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ParametrosWsa(
                        0,
                        100,
                        5,
                        0.1,
                        123L
                )
        );
    }

    @Test
    void rechazaNumeroIteracionesInvalido() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ParametrosWsa(
                        30,
                        0,
                        5,
                        0.1,
                        123L
                )
        );
    }

    @Test
    void rechazaRangoVisualInvalido() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ParametrosWsa(
                        30,
                        100,
                        0,
                        0.1,
                        123L
                )
        );
    }

    @Test
    void rechazaProbabilidadAmenazaMenorQueCero() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ParametrosWsa(
                        30,
                        100,
                        5,
                        -0.1,
                        123L
                )
        );
    }

    @Test
    void rechazaProbabilidadAmenazaMayorQueUno() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ParametrosWsa(
                        30,
                        100,
                        5,
                        1.1,
                        123L
                )
        );
    }
}