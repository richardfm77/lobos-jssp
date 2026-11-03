package mx.unam.heuristicas.jssp.wsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumTipoSolucionTest {

    @Test
    void convierteFactibleCorrectamente() {

        EnumTipoSolucion resultado = EnumTipoSolucion.fromString(
                "FACTIBLE");

        assertEquals(
                EnumTipoSolucion.FACTIBLE,
                resultado);
    }

    @Test
    void convierteNormalCorrectamente() {

        EnumTipoSolucion resultado = EnumTipoSolucion.fromString(
                "NORMAL");

        assertEquals(
                EnumTipoSolucion.NORMAL,
                resultado);
    }

    @Test
    void convierteNoFactibleCorrectamente() {

        EnumTipoSolucion resultado = EnumTipoSolucion.fromString(
                "NO_FACTIBLE");

        assertEquals(
                EnumTipoSolucion.NO_FACTIBLE,
                resultado);
    }

    @Test
    void aceptaMinusculas() {

        assertEquals(
                EnumTipoSolucion.FACTIBLE,
                EnumTipoSolucion.fromString(
                        "factible"));

        assertEquals(
                EnumTipoSolucion.NORMAL,
                EnumTipoSolucion.fromString(
                        "normal"));

        assertEquals(
                EnumTipoSolucion.NO_FACTIBLE,
                EnumTipoSolucion.fromString(
                        "no_factible"));
    }

    @Test
    void aceptaCombinacionDeMayusculasYMinusculas() {

        EnumTipoSolucion resultado = EnumTipoSolucion.fromString(
                "No_FaCtIbLe");

        assertEquals(
                EnumTipoSolucion.NO_FACTIBLE,
                resultado);
    }

    @Test
    void lanzaExcepcionSiTextoEsNulo() {

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> EnumTipoSolucion
                        .fromString(
                                null));

        assertEquals(
                "El tipo de solución no puede ser nulo.",
                excepcion.getMessage());
    }

    @Test
    void lanzaExcepcionSiTipoEsDesconocido() {

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> EnumTipoSolucion
                        .fromString(
                                "ALEATORIA"));

        assertEquals(
                "Tipo de solución desconocido: ALEATORIA",
                excepcion.getMessage());
    }

    @Test
    void ignoraEspaciosAlInicioYAlFinal() {

        assertEquals(
                EnumTipoSolucion.FACTIBLE,
                EnumTipoSolucion.fromString(
                        "  factible  "));
    }
}