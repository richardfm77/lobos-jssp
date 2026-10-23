package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PoblacionWsaTest {

    @Test
    void obtieneCorrectamenteElMejorLobo() {

        Lobo<String> primero =
                new Lobo<>("A", 5.0);

        Lobo<String> segundo =
                new Lobo<>("B", 2.0);

        Lobo<String> tercero =
                new Lobo<>("C", 8.0);

        PoblacionWsa<String> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                primero,
                                segundo,
                                tercero
                        )
                );

        assertEquals(
                segundo,
                poblacion.mejorLobo()
        );
    }

    @Test
    void regresaCorrectamenteElTamano() {

        PoblacionWsa<String> poblacion =
                new PoblacionWsa<>(
                        List.of(
                                new Lobo<>("A", 1.0),
                                new Lobo<>("B", 2.0)
                        )
                );

        assertEquals(
                2,
                poblacion.tamano()
        );
    }

    @Test
    void rechazaPoblacionVacia() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PoblacionWsa<>(
                        List.of()
                )
        );
    }
}