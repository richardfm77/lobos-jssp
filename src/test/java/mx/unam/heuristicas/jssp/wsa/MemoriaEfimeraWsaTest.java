package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoriaEfimeraWsaTest {

    @Test
    void memoriaComienzaVacia() {

        MemoriaEfimeraWsa<Integer> memoria =
                new MemoriaEfimeraWsa<>(
                        3
                );

        assertEquals(
                0,
                memoria.tamano()
        );

        assertEquals(
                3,
                memoria.capacidad()
        );
    }

    @Test
    void registraSoluciones() {

        MemoriaEfimeraWsa<Integer> memoria =
                new MemoriaEfimeraWsa<>(
                        3
                );

        memoria.registrar(10);
        memoria.registrar(20);

        assertEquals(
                2,
                memoria.tamano()
        );

        assertTrue(
                memoria.contiene(10)
        );

        assertTrue(
                memoria.contiene(20)
        );
    }

    @Test
    void eliminaLaSolucionMasAntiguaCuandoSeLlena() {

        MemoriaEfimeraWsa<Integer> memoria =
                new MemoriaEfimeraWsa<>(
                        3
                );

        memoria.registrar(10);
        memoria.registrar(20);
        memoria.registrar(30);

        assertTrue(
                memoria.contiene(10)
        );

        memoria.registrar(40);

        /*
         * La memoria pasa de:
         *
         * [10, 20, 30]
         *
         * a:
         *
         * [20, 30, 40]
         */
        assertFalse(
                memoria.contiene(10)
        );

        assertTrue(
                memoria.contiene(20)
        );

        assertTrue(
                memoria.contiene(30)
        );

        assertTrue(
                memoria.contiene(40)
        );

        assertEquals(
                3,
                memoria.tamano()
        );
    }

    @Test
    void nuncaSuperaSuCapacidad() {

        MemoriaEfimeraWsa<Integer> memoria =
                new MemoriaEfimeraWsa<>(
                        2
                );

        memoria.registrar(1);
        memoria.registrar(2);
        memoria.registrar(3);
        memoria.registrar(4);
        memoria.registrar(5);

        assertEquals(
                2,
                memoria.tamano()
        );

        assertTrue(
                memoria.contiene(4)
        );

        assertTrue(
                memoria.contiene(5)
        );

        assertFalse(
                memoria.contiene(3)
        );
    }

    @Test
    void rechazaCapacidadNoPositiva() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new MemoriaEfimeraWsa<Integer>(
                        0
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new MemoriaEfimeraWsa<Integer>(
                        -1
                )
        );
    }

    @Test
    void rechazaSolucionNulaAlRegistrar() {

        MemoriaEfimeraWsa<Integer> memoria =
                new MemoriaEfimeraWsa<>(
                        3
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> memoria.registrar(
                        null
                )
        );
    }

    @Test
    void rechazaSolucionNulaAlConsultar() {

        MemoriaEfimeraWsa<Integer> memoria =
                new MemoriaEfimeraWsa<>(
                        3
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> memoria.contiene(
                        null
                )
        );
    }
}