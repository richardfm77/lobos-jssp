package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.UtilTest;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.util.DoublePrecision;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProblemaJsspTest {

    @Test
    void calculaCorrectamenteCostoSolucionCotaSuperior40x40() {

        InstanciaJssp instancia =
                UtilTest.obtenerInstancia(
                        "JSSP_40_40.txt"
                );

        SolucionJssp solucion =
                GeneradorSolucionJssp
                        .generarSolucionCotaSuperior(
                                instancia
                        );

        ProblemaJssp problema =
                new ProblemaJssp(instancia);

        double costo =
                problema.funcionCosto(
                        solucion
                );

        assertTrue(
                DoublePrecision.equals(
                        1.0,
                        costo
                )
        );
    }

    @Test
    void lanzaExcepcionSiLaInstanciaEsNula() {

        assertThrows(
                ExcepcionAplicacion.class,
                () -> new ProblemaJssp(null)
        );
    }

    @Test
    void lanzaExcepcionSiLaSolucionEsNula() {

        InstanciaJssp instancia =
                UtilTest.obtenerInstancia(
                        "JSSP_40_40.txt"
                );

        ProblemaJssp problema =
                new ProblemaJssp(instancia);

        assertThrows(
                ExcepcionAplicacion.class,
                () -> problema.funcionCosto(null)
        );
    }

    @Test
    void generarVecinoTodaviaNoEstaImplementado() {

        InstanciaJssp instancia =
                UtilTest.obtenerInstancia(
                        "JSSP_40_40.txt"
                );

        SolucionJssp solucion =
                GeneradorSolucionJssp
                        .generarInicial(instancia);

        ProblemaJssp problema =
                new ProblemaJssp(instancia);

        assertThrows(
                UnsupportedOperationException.class,
                () -> problema.generarVecino(
                        solucion
                )
        );
    }

    @Test
    void distanciaTodaviaNoEstaImplementada() {

        InstanciaJssp instancia =
                UtilTest.obtenerInstancia(
                        "JSSP_40_40.txt"
                );

        SolucionJssp solucion =
                GeneradorSolucionJssp
                        .generarInicial(instancia);

        ProblemaJssp problema =
                new ProblemaJssp(instancia);

        assertThrows(
                UnsupportedOperationException.class,
                () -> problema.distancia(
                        solucion,
                        solucion
                )
        );
    }
}