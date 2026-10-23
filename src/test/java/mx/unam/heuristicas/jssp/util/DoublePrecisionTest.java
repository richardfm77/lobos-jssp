package mx.unam.heuristicas.jssp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoublePrecisionTest {

    @Test
    void consideraIgualesValoresDentroDeLaTolerancia() {

        assertTrue(
                DoublePrecision.equals(
                        1.0,
                        1.0 + 1e-8
                )
        );
    }

    @Test
    void consideraDistintosValoresFueraDeLaTolerancia() {

        assertFalse(
                DoublePrecision.equals(
                        1.0,
                        1.0 + 1e-5
                )
        );
    }

    @Test
    void comparaMenorCorrectamente() {

        assertTrue(
                DoublePrecision.lessThan(
                        1.0,
                        2.0
                )
        );
    }

    @Test
    void comparaMayorCorrectamente() {

        assertTrue(
                DoublePrecision.greaterThan(
                        2.0,
                        1.0
                )
        );
    }

    @Test
    void comparaMenorOIgualConsiderandoTolerancia() {

        assertTrue(
                DoublePrecision.lessThanOrEqual(
                        1.0 + 1e-8,
                        1.0
                )
        );
    }

    @Test
    void comparaMayorOIgualConsiderandoTolerancia() {

        assertTrue(
                DoublePrecision.greaterThanOrEqual(
                        1.0 - 1e-8,
                        1.0
                )
        );
    }
}