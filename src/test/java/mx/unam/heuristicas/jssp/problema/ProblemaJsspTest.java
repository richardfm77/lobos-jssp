package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.UtilTest;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.util.DoublePrecision;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Iterator;

class ProblemaJsspTest {

        @Test
        void calculaCorrectamenteCostoSolucionInicialPorCapas40x40() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                SolucionJssp solucion = GeneradorSolucionJssp
                                .generarSolucionInicialPorCapas(
                                                instancia);

                ProblemaJssp problema = new ProblemaJssp(instancia);

                double costo = problema.funcionCosto(
                                solucion);

                double costoEsperado = (205.0 - 103.0)
                                / (3251.0 - 103.0);

                assertTrue(
                                DoublePrecision.equals(
                                                costoEsperado,
                                                costo));
        }

        @Test
        void lanzaExcepcionSiLaInstanciaEsNula() {

                assertThrows(
                                ExcepcionAplicacion.class,
                                () -> new ProblemaJssp(null));
        }

        @Test
        void lanzaExcepcionSiLaSolucionEsNula() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                ProblemaJssp problema = new ProblemaJssp(instancia);

                assertThrows(
                                ExcepcionAplicacion.class,
                                () -> problema.funcionCosto(null));
        }

        @Test
        void generaVecinoConUnSoloIntercambio() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                SolucionJssp solucion = GeneradorSolucionJssp
                                .generarSolucionInicialPorCapas(
                                                instancia);

                ProblemaJssp problema = new ProblemaJssp(
                                instancia,
                                new Random(12345L));

                SolucionJssp vecino = problema.generarVecino(solucion);

                assertNotEquals(
                                solucion,
                                vecino);

                int maquinasModificadas = 0;

                Iterator<SecuenciaMaquina> original = solucion.secuenciasMaquinas()
                                .iterator();

                Iterator<SecuenciaMaquina> modificada = vecino.secuenciasMaquinas()
                                .iterator();

                while (original.hasNext()
                                && modificada.hasNext()) {

                        SecuenciaMaquina secuenciaOriginal = original.next();

                        SecuenciaMaquina secuenciaVecina = modificada.next();

                        assertEquals(
                                        secuenciaOriginal.idMaquina(),
                                        secuenciaVecina.idMaquina());

                        assertEquals(
                                        new HashSet<>(
                                                        secuenciaOriginal.operaciones()),
                                        new HashSet<>(
                                                        secuenciaVecina.operaciones()));

                        int posicionesDiferentes = 0;

                        Iterator<Operacion> operacionesOriginales = secuenciaOriginal.operaciones()
                                        .iterator();

                        Iterator<Operacion> operacionesVecinas = secuenciaVecina.operaciones()
                                        .iterator();

                        while (operacionesOriginales.hasNext()
                                        && operacionesVecinas.hasNext()) {

                                if (!operacionesOriginales.next()
                                                .equals(
                                                                operacionesVecinas.next())) {

                                        posicionesDiferentes++;
                                }
                        }

                        if (posicionesDiferentes > 0) {

                                maquinasModificadas++;

                                assertEquals(
                                                2,
                                                posicionesDiferentes);
                        }
                }

                assertEquals(
                                1,
                                maquinasModificadas);
        }

        @Test
        void lanzaExcepcionAlGenerarVecinoDeSolucionNula() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                ProblemaJssp problema = new ProblemaJssp(instancia);

                assertThrows(
                                ExcepcionAplicacion.class,
                                () -> problema.generarVecino(null));
        }

        @Test
        void vecinoHaciaObjetivoReduceDistanciaEnUno() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                ProblemaJssp problema = new ProblemaJssp(
                                instancia,
                                new Random(12345L));

                SolucionJssp actual = GeneradorSolucionJssp
                                .generarSolucionInicialPorCapas(
                                                instancia);

                SolucionJssp objetivo = actual;

                for (int i = 0; i < 10; i++) {
                        objetivo = problema.generarVecino(
                                        objetivo);
                }

                double distanciaAntes = problema.distancia(
                                actual,
                                objetivo);

                SolucionJssp vecino = problema.generarVecinoHacia(
                                actual,
                                objetivo);

                double distanciaDespues = problema.distancia(
                                vecino,
                                objetivo);

                assertTrue(
                                DoublePrecision.equals(
                                                distanciaAntes - 1.0,
                                                distanciaDespues));
        }

        @Test
        void distanciaDeUnaSolucionConsigoMismaEsCero() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                SolucionJssp solucion = GeneradorSolucionJssp
                                .generarSolucionInicialPorCapas(
                                                instancia);

                ProblemaJssp problema = new ProblemaJssp(instancia);

                double distancia = problema.distancia(
                                solucion,
                                solucion);

                assertTrue(
                                DoublePrecision.equals(
                                                0.0,
                                                distancia));
        }

        @Test
        void vecinoGeneradoTieneDistanciaUno() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                SolucionJssp solucion = GeneradorSolucionJssp
                                .generarSolucionInicialPorCapas(
                                                instancia);

                ProblemaJssp problema = new ProblemaJssp(
                                instancia,
                                new Random(12345L));

                SolucionJssp vecino = problema.generarVecino(solucion);

                double distancia = problema.distancia(
                                solucion,
                                vecino);

                assertTrue(
                                DoublePrecision.equals(
                                                1.0,
                                                distancia));
        }

        @Test
        void distanciaEsSimetrica() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                SolucionJssp primera = GeneradorSolucionJssp
                                .generarSolucionInicialPorCapas(
                                                instancia);

                ProblemaJssp problema = new ProblemaJssp(
                                instancia,
                                new Random(12345L));

                SolucionJssp segunda = problema.generarVecino(
                                primera);

                double distanciaPrimeraSegunda = problema.distancia(
                                primera,
                                segunda);

                double distanciaSegundaPrimera = problema.distancia(
                                segunda,
                                primera);

                assertTrue(
                                DoublePrecision.equals(
                                                distanciaPrimeraSegunda,
                                                distanciaSegundaPrimera));
        }
}