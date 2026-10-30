package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VisionWsaTest {

    @Test
    void encuentraLobosDentroDelRangoVisual() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        Lobo<Integer> referencia =
                new Lobo<>(10, 10.0,1);

        Lobo<Integer> cercanoIzquierda =
                new Lobo<>(8, 8.0,1);

        Lobo<Integer> cercanoDerecha =
                new Lobo<>(13, 13.0,1);

        Lobo<Integer> lejano =
                new Lobo<>(20, 20.0,1);

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                referencia,
                                cercanoIzquierda,
                                cercanoDerecha,
                                lejano
                        )
                );

        VisionWsa<Integer> vision =
                new VisionWsa<>(
                        problema,
                        parametros
                );

        List<Lobo<Integer>> visibles =
                vision.lobosVisibles(
                        referencia,
                        poblacion
                );

        assertEquals(
                2,
                visibles.size()
        );

        assertTrue(
                visibles.contains(
                        cercanoIzquierda
                )
        );

        assertTrue(
                visibles.contains(
                        cercanoDerecha
                )
        );

        assertFalse(
                visibles.contains(
                        lejano
                )
        );
    }

    @Test
    void consideraVisibleUnLoboExactamenteEnElLimiteDelRango() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        Lobo<Integer> referencia =
                new Lobo<>(10, 10.0, 1);

        Lobo<Integer> limite =
                new Lobo<>(13, 13.0, 1);

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                referencia,
                                limite
                        )
                );

        VisionWsa<Integer> vision =
                new VisionWsa<>(
                        problema,
                        parametros
                );

        List<Lobo<Integer>> visibles =
                vision.lobosVisibles(
                        referencia,
                        poblacion
                );

        assertEquals(
                1,
                visibles.size()
        );

        assertTrue(
                visibles.contains(limite)
        );
    }

    @Test
    void encuentraElMejorLoboVisible() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        Lobo<Integer> referencia =
                new Lobo<>(10, 10.0, 1);

        Lobo<Integer> mejorVisible =
                new Lobo<>(8, 8.0, 1);

        Lobo<Integer> peorVisible =
                new Lobo<>(12, 12.0, 1);

        Lobo<Integer> mejorPeroLejano =
                new Lobo<>(0, 0.0, 1);

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                referencia,
                                mejorVisible,
                                peorVisible,
                                mejorPeroLejano
                        )
                );

        VisionWsa<Integer> vision =
                new VisionWsa<>(
                        problema,
                        parametros
                );

        Optional<Lobo<Integer>> mejor =
                vision.mejorLoboVisible(
                        referencia,
                        poblacion
                );

        assertTrue(
                mejor.isPresent()
        );

        assertEquals(
                mejorVisible,
                mejor.get()
        );
    }

    @Test
    void noEncuentraMejorLoboCuandoTodosLosVisiblesSonPeores() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        Lobo<Integer> referencia =
                new Lobo<>(10, 10.0, 1);

        Lobo<Integer> primero =
                new Lobo<>(11, 11.0, 1);

        Lobo<Integer> segundo =
                new Lobo<>(12, 12.0, 1);

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                referencia,
                                primero,
                                segundo
                        )
                );

        VisionWsa<Integer> vision =
                new VisionWsa<>(
                        problema,
                        parametros
                );

        Optional<Lobo<Integer>> mejor =
                vision.mejorLoboVisible(
                        referencia,
                        poblacion
                );

        assertTrue(
                mejor.isEmpty()
        );
    }

    @Test
    void noIncluyeAlPropioLoboEntreLosVisibles() {

        ProblemaHeuristico<Integer> problema =
                crearProblemaPrueba();

        ParametrosWsa parametros =
                crearParametros();

        Lobo<Integer> referencia =
                new Lobo<>(10, 10.0, 1);

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                referencia
                        )
                );

        VisionWsa<Integer> vision =
                new VisionWsa<>(
                        problema,
                        parametros
                );

        List<Lobo<Integer>> visibles =
                vision.lobosVisibles(
                        referencia,
                        poblacion
                );

        assertTrue(
                visibles.isEmpty()
        );
    }

    @Test
    void rechazaLoboReferenciaNulo() {

        VisionWsa<Integer> vision =
                new VisionWsa<>(
                        crearProblemaPrueba(),
                        crearParametros()
                );

        PoblacionWsa<Integer> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                new Lobo<>(0, 0.0, 1)
                        )
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> vision.lobosVisibles(
                        null,
                        poblacion
                )
        );
    }

    @Test
    void rechazaPoblacionNula() {

        VisionWsa<Integer> vision =
                new VisionWsa<>(
                        crearProblemaPrueba(),
                        crearParametros()
                );

        Lobo<Integer> lobo =
                new Lobo<>(0, 0.0, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> vision.lobosVisibles(
                        lobo,
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
            public Integer generarVecinoHacia(Integer solucion, Integer objetivo) {
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
}