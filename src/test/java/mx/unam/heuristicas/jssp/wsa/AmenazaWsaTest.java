package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmenazaWsaTest {

    @Test
    void noModificaLoboCuandoNoOcurreAmenaza() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        AmenazaWsa<Integer> amenaza =
                new AmenazaWsa<>(
                        problema,
                        crearParametros(0.0),
                        new java.util.Random(12345L)
                );

        Lobo<Integer> lobo =
                crearLobo(
                        0,
                        0.0
                );

        boolean ocurrio =
                amenaza.procesar(
                        lobo
                );

        assertFalse(
                ocurrio
        );

        assertEquals(
                0,
                lobo.solucion()
        );

        assertEquals(
                0.0,
                lobo.costo()
        );
    }

    @Test
    void escapaCuandoOcurreAmenaza() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        AmenazaWsa<Integer> amenaza =
                new AmenazaWsa<>(
                        problema,
                        crearParametros(1.0),
                        new java.util.Random(12345L)
                );

        Lobo<Integer> lobo =
                crearLobo(
                        0,
                        0.0
                );

        boolean ocurrio =
                amenaza.procesar(
                        lobo
                );

        assertTrue(
                ocurrio
        );

        /*
         * R = 3:
         *
         * 0 -> 1 -> 2 -> 3 -> 4
         *
         * d(0,4) = 4 > 3.
         */
        assertEquals(
                4,
                lobo.solucion()
        );

        assertEquals(
                4.0,
                lobo.costo()
        );
    }

    @Test
    void evitaSolucionesGuardadasEnMemoriaDuranteEscape() {

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
                         * Primer candidato: 1.
                         * Está almacenado en memoria.
                         */
                        if (llamadas[0] == 1) {
                            return 1;
                        }

                        /*
                         * Después avanzamos:
                         *
                         * 0 -> 2 -> 4
                         */
                        return solucion + 2;
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
                        0,
                        0.0
                );

        /*
         * Hacemos que 1 forme parte de la memoria:
         *
         * [0, 1, 0]
         */
        lobo.actualizar(
                1,
                1.0
        );

        lobo.actualizar(
                0,
                0.0
        );

        AmenazaWsa<Integer> amenaza =
                new AmenazaWsa<>(
                        problema,
                        crearParametros(1.0),
                        new java.util.Random(12345L)
                );

        amenaza.procesar(
                lobo
        );

        /*
         * El primer candidato fue 1,
         * pero fue rechazado.
         *
         * Después:
         *
         * 0 -> 2 -> 4
         *
         * d(0,4) = 4 > R.
         */
        assertEquals(
                4,
                lobo.solucion()
        );

        assertEquals(
                4.0,
                lobo.costo()
        );

        assertEquals(
                3,
                llamadas[0]
        );
    }

    @Test
    void registraPosicionDeEscapeEnMemoria() {

        AmenazaWsa<Integer> amenaza =
                new AmenazaWsa<>(
                        crearProblemaPrueba(),
                        crearParametros(1.0),
                        new java.util.Random(12345L)
                );

        Lobo<Integer> lobo =
                crearLobo(
                        0,
                        0.0
                );

        amenaza.procesar(
                lobo
        );

        assertEquals(
                4,
                lobo.solucion()
        );

        assertTrue(
                lobo.recuerda(4)
        );
    }

    @Test
    void noModificaLoboSiNoLograEscaparPorLaMemoria() {

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
                         * Siempre propone regresar a 1.
                         */
                        return 1;
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
                        0,
                        0.0
                );

        /*
         * Guardamos 1 como posición reciente.
         */
        lobo.actualizar(
                1,
                1.0
        );

        lobo.actualizar(
                0,
                0.0
        );

        AmenazaWsa<Integer> amenaza =
                new AmenazaWsa<>(
                        problema,
                        crearParametros(1.0),
                        new java.util.Random(12345L)    
                );

        assertThrows(
                IllegalStateException.class,
                () -> amenaza.procesar(
                        lobo
                )
        );

        /*
         * El escape nunca se completó,
         * por lo que el estado real del lobo
         * permanece igual.
         */
        assertEquals(
                0,
                lobo.solucion()
        );

        assertEquals(
                0.0,
                lobo.costo()
        );
    }

    @Test
    void noConsideraEscapeDistanciaIgualAlRangoVisual() {

        AmenazaWsa<Integer> amenaza =
                new AmenazaWsa<>(
                        crearProblemaPrueba(),
                        crearParametros(1.0),
                        new java.util.Random(12345L)
                );

        Lobo<Integer> lobo =
                crearLobo(
                        10,
                        10.0
                );

        amenaza.procesar(
                lobo
        );

        /*
         * R = 3:
         *
         * 10 -> 11 : d = 1
         * 10 -> 12 : d = 2
         * 10 -> 13 : d = 3
         * 10 -> 14 : d = 4
         */
        assertEquals(
                14,
                lobo.solucion()
        );
    }

    @Test
    void rechazaLoboNulo() {

        AmenazaWsa<Integer> amenaza =
                new AmenazaWsa<>(
                        crearProblemaPrueba(),
                        crearParametros(1.0),
                        new java.util.Random(12345L)
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> amenaza.procesar(
                        null
                )
        );
    }

    @Test
    void rechazaProblemaNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new AmenazaWsa<Integer>(
                        null,
                        crearParametros(1.0),
                        new java.util.Random(12345L)
                )
        );
    }

    @Test
    void rechazaParametrosNulos() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new AmenazaWsa<>(
                        crearProblemaPrueba(),
                        null,
                        new java.util.Random(12345L)
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
                crearParametros(1.0)
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

    private ParametrosWsa crearParametros(
            double probabilidadAmenaza
    ) {

        return new ParametrosWsa(
                5,                      
                100,                  
                100,
                3,                      
                probabilidadAmenaza,
                12345L,     
                100,
                3,      
                10
        );
    }
}