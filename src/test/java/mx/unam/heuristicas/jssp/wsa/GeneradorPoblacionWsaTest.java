package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneradorPoblacionWsaTest {

    @Test
    void generaPoblacionConCantidadCorrectaDeLobos() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        GeneradorPoblacionWsa<Integer> generador =
                new GeneradorPoblacionWsa<>(
                        problema,
                        parametros
                );

        PoblacionWsa<Integer> poblacion =
                generador.generar(0);

        assertEquals(
                parametros.tamanoPoblacion(),
                poblacion.tamano()
        );
    }

    @Test
    void generaPoblacionConSolucionesCercanasYLejanas() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        GeneradorPoblacionWsa<Integer> generador =
                new GeneradorPoblacionWsa<>(
                        problema,
                        parametros
                );

        PoblacionWsa<Integer> poblacion =
                generador.generar(0);

        int cercanos = 0;
        int lejanos = 0;

        for (Lobo<Integer> lobo :
                poblacion.lobos()) {

            double distancia =
                    problema.distancia(
                            0,
                            lobo.solucion()
                    );

            if (distancia > 0
                    && distancia <= parametros.rangoVisual()) {

                cercanos++;

            } else if (distancia
                    > parametros.rangoVisual()) {

                lejanos++;
            }
        }

        /*
         * Para una población de 5:
         *
         * 1 solución base
         * 2 soluciones cercanas
         * 2 soluciones lejanas
         */
        assertEquals(
                2,
                cercanos
        );

        assertEquals(
                2,
                lejanos
        );
    }

    @Test
    void conservaLaSolucionBaseEnLaPoblacion() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        GeneradorPoblacionWsa<Integer> generador =
                new GeneradorPoblacionWsa<>(
                        problema,
                        parametros
                );

        PoblacionWsa<Integer> poblacion =
                generador.generar(0);

        boolean contieneSolucionBase = false;

        for (Lobo<Integer> lobo :
                poblacion.lobos()) {

            if (lobo.solucion().equals(0)) {
                contieneSolucionBase = true;
                break;
            }
        }

        assertTrue(
                contieneSolucionBase
        );
    }

    @Test
    void noGeneraSolucionesDuplicadas() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        GeneradorPoblacionWsa<Integer> generador =
                new GeneradorPoblacionWsa<>(
                        problema,
                        parametros
                );

        PoblacionWsa<Integer> poblacion =
                generador.generar(0);

        Set<Integer> soluciones =
                new HashSet<>();

        for (Lobo<Integer> lobo :
                poblacion.lobos()) {

            soluciones.add(
                    lobo.solucion()
            );
        }

        assertEquals(
                poblacion.tamano(),
                soluciones.size()
        );
    }

    @Test
    void asignaCorrectamenteElCostoACadaLobo() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        GeneradorPoblacionWsa<Integer> generador =
                new GeneradorPoblacionWsa<>(
                        problema,
                        parametros
                );

        PoblacionWsa<Integer> poblacion =
                generador.generar(0);

        for (Lobo<Integer> lobo :
                poblacion.lobos()) {

            double costoEsperado =
                    problema.funcionCosto(
                            lobo.solucion()
                    );

            assertEquals(
                    costoEsperado,
                    lobo.costo()
            );
        }
    }

    @Test
    void lanzaExcepcionSiProblemaEsNulo() {

        ParametrosWsa parametros =
                crearParametros();

        assertThrows(
                IllegalArgumentException.class,
                () -> new GeneradorPoblacionWsa<Integer>(
                        null,
                        parametros
                )
        );
    }

    @Test
    void lanzaExcepcionSiParametrosSonNulos() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        assertThrows(
                IllegalArgumentException.class,
                () -> new GeneradorPoblacionWsa<>(
                        problema,
                        null
                )
        );
    }

    @Test
    void lanzaExcepcionSiSolucionBaseEsNula() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        GeneradorPoblacionWsa<Integer> generador =
                new GeneradorPoblacionWsa<>(
                        problema,
                        parametros
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> generador.generar(null)
        );
    }

    @Test
    void lanzaExcepcionSiNoPuedeGenerarSolucionLejana() {

        ProblemaHeuristico<Integer> problema =
                new ProblemaHeuristico<>() {

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
                        /*
                         * Sólo existen de manera efectiva
                         * las soluciones 0 y 1.
                         */
                        return solucion == 0
                                ? 1
                                : 0;
                    }

                    @Override
                    public Integer generarVecinoHacia(
                             Integer solucion,
                        Integer objetivo
                        ) {
                                return null;
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

        ParametrosWsa parametros =
                new ParametrosWsa(
                        3,
                        100,
                        10,
                        1,
                        0.1,
                        12345L,
                        1,
                        1,
                        1
                );

        GeneradorPoblacionWsa<Integer> generador =
                new GeneradorPoblacionWsa<>(
                        problema,
                        parametros
                );

        /*
         * Con rango visual 1 no existe ninguna
         * solución cuya distancia a 0 sea > 1.
         */
        assertThrows(
                IllegalStateException.class,
                () -> generador.generar(0)
        );
    }

    private ParametrosWsa crearParametros() {

        return new ParametrosWsa(
                5,
                100,
                100,
                3,
                0.1,
                12345L,
                1,
                1,
                1
        );
    }

    /**
     * Problema heurístico sencillo para probar el generador
     * independientemente de JSSP.
     *
     * Cada solución es un entero, un vecino se obtiene
     * incrementando una unidad y la distancia corresponde
     * a la diferencia absoluta entre dos soluciones.
     */
    private ProblemaHeuristico<Integer> crearProblemaPrueba() {

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
                return null;
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
}