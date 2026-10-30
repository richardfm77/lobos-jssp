package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import java.util.random.RandomGenerator;

class AlgoritmoWsaTest {

    @Test
    void conservaMejorResultadoDeLaPoblacionInicial() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros(
                        5
                );

        AlgoritmoWsa<Integer> algoritmo =
                new AlgoritmoWsa<>(
                        problema,
                        parametros,
                        crearGeneradorAleatorio()
                );

        ResultadoWsa<Integer> resultado =
                algoritmo.ejecutar(
                        0
                );

        /*
         * El costo es f(x) = x.
         *
         * La solución base 0 pertenece a la población
         * inicial y es la mejor posible entre las
         * soluciones generadas.
         *
         * Posteriormente los lobos se desplazan hacia
         * valores mayores, pero el mejor histórico
         * debe conservarse.
         */
        assertEquals(
                0,
                resultado.mejorSolucion()
        );

        assertEquals(
                0.0,
                resultado.mejorCosto()
        );

        assertEquals(
                0,
                resultado.iteracionEncontrada()
        );
    }

    @Test
    void encuentraMejorResultadoDuranteLasIteraciones() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaDondeMayorEsMejor();

        ParametrosWsa parametros =
                crearParametros(
                        5
                );

        AlgoritmoWsa<Integer> algoritmo =
                new AlgoritmoWsa<>(
                        problema,
                        parametros,
                        crearGeneradorAleatorio()
                );

        ResultadoWsa<Integer> resultado =
                algoritmo.ejecutar(
                        0
                );

        /*
         * f(x) = -x.
         *
         * Por tanto, aumentar x mejora el costo.
         *
         * Como generarVecino(x) = x + 1,
         * durante las iteraciones deben aparecer
         * soluciones mejores que las iniciales.
         */
        double mejorCostoInicialMaximo =
                0.0;

        assertEquals(
                true,
                resultado.mejorCosto()
                        < mejorCostoInicialMaximo
        );
    }

    @Test
    void mismaSemillaProduceMismoResultado() {

        ParametrosWsa parametros =
                crearParametros(
                        10
                );

        /*
         * Cada corrida debe tener su propia instancia
         * del problema si el problema mantiene estado
         * interno relacionado con su generación de vecinos.
         */
        AlgoritmoWsa<Integer> primeraCorrida =
                new AlgoritmoWsa<>(
                        crearProblemaPrueba(),
                        parametros,
                        crearGeneradorAleatorio()
                );

        AlgoritmoWsa<Integer> segundaCorrida =
                new AlgoritmoWsa<>(
                        crearProblemaPrueba(),
                        parametros,
                        crearGeneradorAleatorio()
                );

        ResultadoWsa<Integer> primero =
                primeraCorrida.ejecutar(
                        0
                );

        ResultadoWsa<Integer> segundo =
                segundaCorrida.ejecutar(
                        0
                );

        assertEquals(
                primero.mejorSolucion(),
                segundo.mejorSolucion()
        );

        assertEquals(
                primero.mejorCosto(),
                segundo.mejorCosto()
        );

        assertEquals(
                primero.iteracionEncontrada(),
                segundo.iteracionEncontrada()
        );
    }

    @Test
    void rechazaSolucionBaseNula() {

        AlgoritmoWsa<Integer> algoritmo =
                new AlgoritmoWsa<>(
                        crearProblemaPrueba(),
                        crearParametros(5),
                        crearGeneradorAleatorio()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> algoritmo.ejecutar(
                        null
                )
        );
    }

    @Test
    void rechazaProblemaNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new AlgoritmoWsa<Integer>(
                        null,
                        crearParametros(5),
                        null
                )
        );
    }

    @Test
    void rechazaParametrosNulos() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new AlgoritmoWsa<>(
                        crearProblemaPrueba(),
                        null,
                        null
                )
        );
    }

    /**
     * Problema donde los valores pequeños son mejores.
     *
     * generarVecino siempre incrementa la solución,
     * de modo que la solución base 0 termina siendo
     * el mejor histórico.
     */
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

    /**
     * Problema donde los valores grandes son mejores,
     * ya que el costo es el negativo de la solución.
     */
    private ProblemaHeuristico<Integer>
    crearProblemaDondeMayorEsMejor() {

        return new ProblemaHeuristico<>() {

            @Override
            public double funcionCosto(
                    Integer solucion
            ) {
                return -solucion;
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
            int numeroIteraciones
    ) {

        return new ParametrosWsa(
                3,                  
                numeroIteraciones,  
                100,               
                2,                
                0.0,      
                12345L,             
                100,      
                3,            
                10      
        );
    }

    private RandomGenerator crearGeneradorAleatorio() {
        return new Random(12345L);
    }
}