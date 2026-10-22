package mx.unam.heuristicas.jssp.estructura;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraficaDirigidaTest {

    @Test
    void creaUnaGraficaVacia() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        assertTrue(grafica.esVacia());
        assertEquals(0, grafica.numeroVertices());
        assertEquals(0, grafica.numeroAristas());
    }

    @Test
    void agregaVerticesCorrectamente() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");
        grafica.agrega("B");

        assertFalse(grafica.esVacia());
        assertEquals(2, grafica.numeroVertices());

        assertTrue(grafica.contiene("A"));
        assertTrue(grafica.contiene("B"));
    }

    @Test
    void noPermiteAgregarUnElementoNulo() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> grafica.agrega(null)
        );
    }

    @Test
    void noPermiteVerticesRepetidos() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");

        assertThrows(
                IllegalArgumentException.class,
                () -> grafica.agrega("A")
        );
    }

    @Test
    void conectaVerticesEnUnaSolaDireccion() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");
        grafica.agrega("B");

        grafica.conecta("A", "B");

        assertTrue(
                grafica.hayArista("A", "B")
        );

        assertFalse(
                grafica.hayArista("B", "A")
        );

        assertEquals(
                1,
                grafica.numeroAristas()
        );
    }

    @Test
    void calculaCorrectamenteLosGrados() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");
        grafica.agrega("B");
        grafica.agrega("C");

        grafica.conecta("A", "C");
        grafica.conecta("B", "C");

        assertEquals(
                0,
                grafica.gradoEntrada("A")
        );

        assertEquals(
                0,
                grafica.gradoEntrada("B")
        );

        assertEquals(
                2,
                grafica.gradoEntrada("C")
        );

        assertEquals(
                1,
                grafica.gradoSalida("A")
        );

        assertEquals(
                1,
                grafica.gradoSalida("B")
        );

        assertEquals(
                0,
                grafica.gradoSalida("C")
        );
    }

    @Test
    void obtieneCorrectamenteLosVecinosSalientes() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");
        grafica.agrega("B");
        grafica.agrega("C");

        grafica.conecta("A", "B");
        grafica.conecta("A", "C");

        Set<String> vecinos =
                grafica.vecinosSalientes("A");

        assertEquals(
                Set.of("B", "C"),
                vecinos
        );
    }

    @Test
    void noPermiteModificarLosVecinosDesdeElExterior() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");
        grafica.agrega("B");

        grafica.conecta("A", "B");

        Set<String> vecinos =
                grafica.vecinosSalientes("A");

        assertThrows(
                UnsupportedOperationException.class,
                () -> vecinos.add("C")
        );
    }

    @Test
    void noPermiteLazos() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");

        assertThrows(
                IllegalArgumentException.class,
                () -> grafica.conecta("A", "A")
        );
    }

    @Test
    void noPermiteAristasRepetidas() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");
        grafica.agrega("B");

        grafica.conecta("A", "B");

        assertThrows(
                IllegalArgumentException.class,
                () -> grafica.conecta("A", "B")
        );
    }

    @Test
    void lanzaExcepcionSiElOrigenNoExiste() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("B");

        assertThrows(
                NoSuchElementException.class,
                () -> grafica.conecta("A", "B")
        );
    }

    @Test
    void lanzaExcepcionSiElDestinoNoExiste() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");

        assertThrows(
                NoSuchElementException.class,
                () -> grafica.conecta("A", "B")
        );
    }

    @Test
    void obtieneTodosLosVertices() {

        GraficaDirigida<String> grafica =
                new GraficaDirigida<>();

        grafica.agrega("A");
        grafica.agrega("B");
        grafica.agrega("C");

        assertEquals(
                Set.of("A", "B", "C"),
                grafica.vertices()
        );
    }
}