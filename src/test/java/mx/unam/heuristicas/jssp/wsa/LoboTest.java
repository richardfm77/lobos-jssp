package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoboTest {

        @Test
        void construyeLoboCorrectamente() {

                String solucion = "solucion-1";

                Lobo<String> lobo = new Lobo<>(
                                solucion,
                                1.5, 1);

                assertEquals(
                                solucion,
                                lobo.solucion());

                assertEquals(
                                1.5,
                                lobo.costo());
        }

        @Test
        void actualizaSolucionYCosto() {

                Lobo<String> lobo = new Lobo<>(
                                "solucion-1",
                                2.0,
                                1);

                lobo.actualizar(
                                "solucion-2",
                                1.0);

                assertEquals(
                                "solucion-2",
                                lobo.solucion());

                assertEquals(
                                1.0,
                                lobo.costo());
        }

        @Test
        void rechazaSolucionNulaAlConstruir() {

                assertThrows(
                                IllegalArgumentException.class,
                                () -> new Lobo<>(
                                                null,
                                                1.0,
                                                1));
        }

        @Test
        void rechazaCostoNoFinitoAlConstruir() {

                assertThrows(
                                IllegalArgumentException.class,
                                () -> new Lobo<>(
                                                "solucion",
                                                Double.NaN,
                                                1));

                assertThrows(
                                IllegalArgumentException.class,
                                () -> new Lobo<>(
                                                "solucion",
                                                Double.POSITIVE_INFINITY, 1));
        }

        @Test
        void rechazaSolucionNulaAlActualizar() {

                Lobo<String> lobo = new Lobo<>(
                                "solucion",
                                1.0,
                                1);

                assertThrows(
                                IllegalArgumentException.class,
                                () -> lobo.actualizar(
                                                null,
                                                2.0));
        }

        @Test
        void rechazaCostoNoFinitoAlActualizar() {

                Lobo<String> lobo = new Lobo<>(
                                "solucion",
                                1.0,
                                1);

                assertThrows(
                                IllegalArgumentException.class,
                                () -> lobo.actualizar(
                                                "otra-solucion",
                                                Double.NaN));
        }

        @Test
        void recuerdaSuSolucionInicial() {

                Lobo<Integer> lobo = new Lobo<>(
                                10,
                                10.0,
                                3);

                assertTrue(
                                lobo.recuerda(10));
        }

        @Test
        void recuerdaSolucionesDespuesDeActualizarse() {

                Lobo<Integer> lobo = new Lobo<>(
                                10,
                                10.0,
                                3);

                lobo.actualizar(
                                20,
                                20.0);

                assertTrue(
                                lobo.recuerda(10));

                assertTrue(
                                lobo.recuerda(20));
        }

        @Test
        void olvidaSolucionesAntiguas() {

                Lobo<Integer> lobo = new Lobo<>(
                                10,
                                10.0,
                                3);

                lobo.actualizar(20, 20.0);
                lobo.actualizar(30, 30.0);
                lobo.actualizar(40, 40.0);

                /*
                 * Capacidad = 3.
                 *
                 * Historia:
                 *
                 * inicialmente: [10]
                 * -> [10,20]
                 * -> [10,20,30]
                 * -> [20,30,40]
                 */
                assertFalse(
                                lobo.recuerda(10));

                assertTrue(
                                lobo.recuerda(20));

                assertTrue(
                                lobo.recuerda(30));

                assertTrue(
                                lobo.recuerda(40));
        }
}