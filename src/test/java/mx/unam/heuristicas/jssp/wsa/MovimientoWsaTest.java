package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovimientoWsaTest {

    @Test
    void seMueveHaciaUnLoboVisibleMejor() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        Lobo<Integer> actual =
                crearLobo(
                        10,
                        10.0
                );

        Lobo<Integer> mejor =
                crearLobo(
                        8,
                        8.0
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                actual,
                                mejor
                        )
                );

        MovimientoWsa<Integer> movimiento =
                new MovimientoWsa<>(
                        problema,
                        parametros
                );

        movimiento.mover(
                actual,
                poblacion
        );

        assertEquals(
                9,
                actual.solucion()
        );

        assertEquals(
                9.0,
                actual.costo()
        );
    }

    @Test
    void realizaMovimientoAleatorioSiNoHayLoboVisibleMejor() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        Lobo<Integer> actual =
                crearLobo(
                        10,
                        10.0
                );

        Lobo<Integer> peor =
                crearLobo(
                        12,
                        12.0
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                actual,
                                peor
                        )
                );

        MovimientoWsa<Integer> movimiento =
                new MovimientoWsa<>(
                        problema,
                        parametros
                );

        movimiento.mover(
                actual,
                poblacion
        );

        assertEquals(
                110,
                actual.solucion()
        );

        assertEquals(
                110.0,
                actual.costo()
        );
    }

    @Test
    void ignoraLoboMejorSiEstaFueraDelRangoVisual() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        Lobo<Integer> actual =
                crearLobo(
                        10,
                        10.0
                );

        Lobo<Integer> mejorPeroLejano =
                crearLobo(
                        0,
                        0.0
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                actual,
                                mejorPeroLejano
                        )
                );

        MovimientoWsa<Integer> movimiento =
                new MovimientoWsa<>(
                        problema,
                        parametros
                );

        movimiento.mover(
                actual,
                poblacion
        );

        assertEquals(
                110,
                actual.solucion()
        );
    }

    @Test
    void evitaUnaSolucionRecordadaDuranteExploracion() {

        int[] llamadas =
                {0};

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

                        llamadas[0]++;

                        /*
                         * Primer intento:
                         * devuelve 9, que está en memoria.
                         *
                         * Segundo intento:
                         * devuelve 11, que no está en memoria.
                         */
                        if (llamadas[0] == 1) {
                            return 9;
                        }

                        return 11;
                    }

                    @Override
                    public Integer generarVecinoHacia(
                            Integer solucion,
                            Integer objetivo
                    ) {
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

        Lobo<Integer> lobo =
                crearLobo(
                        10,
                        10.0
                );

        /*
         * La memoria queda conceptualmente:
         *
         * [10, 9, 10]
         *
         * Por tanto 9 es una posición reciente.
         */
        lobo.actualizar(
                9,
                9.0
        );

        lobo.actualizar(
                10,
                10.0
        );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                lobo
                        )
                );

        MovimientoWsa<Integer> movimiento =
                new MovimientoWsa<>(
                        problema,
                        crearParametros()
                );

        movimiento.mover(
                lobo,
                poblacion
        );

        /*
         * 9 fue rechazado por memoria.
         * Se aceptó 11.
         */
        assertEquals(
                11,
                lobo.solucion()
        );

        assertEquals(
                11.0,
                lobo.costo()
        );

        assertEquals(
                2,
                llamadas[0]
        );
    }

    @Test
    void permaneceQuietoSiTodosLosMovimientosEstanEnMemoria() {

        int[] llamadas =
                {0};

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

                        llamadas[0]++;

                        /*
                         * Siempre intenta regresar a 9.
                         */
                        return 9;
                    }

                    @Override
                    public Integer generarVecinoHacia(
                            Integer solucion,
                            Integer objetivo
                    ) {
                        return 9;
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

        Lobo<Integer> lobo =
                crearLobo(
                        10,
                        10.0
                );

        lobo.actualizar(
                9,
                9.0
        );

        lobo.actualizar(
                10,
                10.0
        );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                lobo
                        )
                );

        MovimientoWsa<Integer> movimiento =
                new MovimientoWsa<>(
                        problema,
                        crearParametros()
                );

        movimiento.mover(
                lobo,
                poblacion
        );

        /*
         * Todos los candidatos fueron rechazados.
         * El lobo conserva su posición.
         */
        assertEquals(
                10,
                lobo.solucion()
        );

        assertEquals(
                10.0,
                lobo.costo()
        );

        assertEquals(
                crearParametros()
                        .maxIntentosMovimiento(),
                llamadas[0]
        );
    }

    @Test
    void registraNuevaPosicionEnLaMemoriaDespuesDeMoverse() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        Lobo<Integer> lobo =
                crearLobo(
                        10,
                        10.0
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                lobo
                        )
                );

        MovimientoWsa<Integer> movimiento =
                new MovimientoWsa<>(
                        problema,
                        crearParametros()
                );

        movimiento.mover(
                lobo,
                poblacion
        );

        assertEquals(
                110,
                lobo.solucion()
        );

        /*
         * actualizar() debe haber registrado
         * automáticamente la nueva solución.
         */
        assertEquals(
                true,
                lobo.recuerda(110)
        );
    }

    @Test
    void rechazaLoboNulo() {

        MovimientoWsa<Integer> movimiento =
                new MovimientoWsa<>(
                        crearProblemaPrueba(),
                        crearParametros()
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                crearLobo(
                                        0,
                                        0.0
                                )
                        )
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> movimiento.mover(
                        null,
                        poblacion
                )
        );
    }

    @Test
    void rechazaPoblacionNula() {

        MovimientoWsa<Integer> movimiento =
                new MovimientoWsa<>(
                        crearProblemaPrueba(),
                        crearParametros()
                );

        Lobo<Integer> lobo =
                crearLobo(
                        0,
                        0.0
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> movimiento.mover(
                        lobo,
                        null
                )
        );
    }

    private Lobo<Integer> crearLobo(
            int solucion,
            double costo
    ) {

        return new Lobo<>(
                solucion,
                costo,
                crearParametros()
                        .tamanoMemoria()
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
                return solucion + 100;
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

    private ParametrosWsa crearParametros() {

        return new ParametrosWsa(
                5,          
                100,      
                100,
                3,        
                0.1,
                12345L,     
                100,
                3,      
                10
        );
    }
}