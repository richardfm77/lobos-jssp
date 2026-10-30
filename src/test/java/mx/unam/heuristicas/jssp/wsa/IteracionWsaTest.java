package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IteracionWsaTest {

    @Test
    void realizaMovimientoNormalCuandoNoHayAmenaza() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros(
                        0.0
                );

        RandomGenerator generadorAleatorio =
                new Random(
                        parametros.semilla()
                );

        Lobo<Integer> lobo =
                crearLobo(
                        0,
                        0.0,
                        parametros
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                lobo
                        )
                );

        IteracionWsa<Integer> iteracion =
                new IteracionWsa<>(
                        problema,
                        parametros,
                        generadorAleatorio
                );

        iteracion.ejecutar(
                poblacion
        );

        assertEquals(
                1,
                lobo.solucion()
        );

        assertEquals(
                1.0,
                lobo.costo()
        );

        assertTrue(
                lobo.recuerda(1)
        );
    }

    @Test
    void realizaEscapeCuandoHayAmenaza() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros(
                        1.0
                );

        RandomGenerator generadorAleatorio =
                new Random(
                        parametros.semilla()
                );

        Lobo<Integer> lobo =
                crearLobo(
                        0,
                        0.0,
                        parametros
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                lobo
                        )
                );

        IteracionWsa<Integer> iteracion =
                new IteracionWsa<>(
                        problema,
                        parametros,
                        generadorAleatorio
                );

        iteracion.ejecutar(
                poblacion
        );

        /*
         * R = 2:
         *
         * 0 -> 1 -> 2 -> 3
         *
         * d(0,3) = 3 > 2.
         */
        assertEquals(
                3,
                lobo.solucion()
        );

        assertEquals(
                3.0,
                lobo.costo()
        );

        assertTrue(
                lobo.recuerda(3)
        );
    }

    @Test
    void procesaTodosLosLobosSecuencialmente() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros(
                        0.0
                );

        RandomGenerator generadorAleatorio =
                new Random(
                        parametros.semilla()
                );

        Lobo<Integer> primero =
                crearLobo(
                        0,
                        0.0,
                        parametros
                );

        Lobo<Integer> segundo =
                crearLobo(
                        10,
                        10.0,
                        parametros
                );

        Lobo<Integer> tercero =
                crearLobo(
                        20,
                        20.0,
                        parametros
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                primero,
                                segundo,
                                tercero
                        )
                );

        IteracionWsa<Integer> iteracion =
                new IteracionWsa<>(
                        problema,
                        parametros,
                        generadorAleatorio
                );

        iteracion.ejecutar(
                poblacion
        );

        assertEquals(
                1,
                primero.solucion()
        );

        assertEquals(
                11,
                segundo.solucion()
        );

        assertEquals(
                21,
                tercero.solucion()
        );
    }

    @Test
    void rechazaPoblacionNula() {

        ParametrosWsa parametros =
                crearParametros(
                        0.0
                );

        IteracionWsa<Integer> iteracion =
                new IteracionWsa<>(
                        crearProblemaPrueba(),
                        parametros,
                        new Random(
                                parametros.semilla()
                        )
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> iteracion.ejecutar(
                        null
                )
        );
    }

    @Test
    void rechazaProblemaNulo() {

        ParametrosWsa parametros =
                crearParametros(
                        0.0
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new IteracionWsa<Integer>(
                        null,
                        parametros,
                        new Random(
                                parametros.semilla()
                        )
                )
        );
    }

    @Test
    void rechazaParametrosNulos() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new IteracionWsa<>(
                        crearProblemaPrueba(),
                        null,
                        new Random(12345L)
                )
        );
    }

    @Test
    void rechazaGeneradorAleatorioNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new IteracionWsa<>(
                        crearProblemaPrueba(),
                        crearParametros(0.0),
                        null
                )
        );
    }

    private ProblemaHeuristico<Integer>
    crearProblemaPrueba() {

        return new ProblemaHeuristico<>() {

            @Override
            public double funcionCosto(
                    Integer solucion
            ) {
                return solucion;
            }

            @Override
            public Integer generarVecino(
                    Integer solucion
            ) {
                return solucion + 1;
            }

            @Override
            public Integer generarVecinoHacia(
                    Integer solucion,
                    Integer objetivo
            ) {

                if (solucion < objetivo) {
                    return solucion + 1;
                }

                if (solucion > objetivo) {
                    return solucion - 1;
                }

                return solucion;
            }

            @Override
            public double distancia(
                    Integer primera,
                    Integer segunda
            ) {
                return Math.abs(
                        primera - segunda
                );
            }
        };
    }

    private Lobo<Integer> crearLobo(
            int solucion,
            double costo,
            ParametrosWsa parametros
    ) {

        return new Lobo<>(
                solucion,
                costo,
                parametros.tamanoMemoria()
        );
    }

    private ParametrosWsa crearParametros(
            double probabilidadAmenaza
    ) {

        return new ParametrosWsa(
                3,
                100,
                100,
                2,
                probabilidadAmenaza,
                12345L,
                100,
                3,
                10
        );
    }
}