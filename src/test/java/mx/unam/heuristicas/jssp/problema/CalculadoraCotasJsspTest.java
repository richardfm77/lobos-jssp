package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.UtilTest;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.CotasJssp;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraCotasJsspTest {

        @Test
        void calculaCorrectamenteLaCotaInferior() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("valida-3x3.txt");

                int cotaInferior = CalculadoraCotasJssp
                                .calcularCotaInferior(instancia);

                assertEquals(
                                10,
                                cotaInferior);
        }

        @Test
        void calculaCorrectamenteLaCotaSuperior() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("valida-3x3.txt");

                int cotaSuperior = CalculadoraCotasJssp
                                .calcularCotaSuperior(instancia);

                assertEquals(
                                24,
                                cotaSuperior);
        }

        @Test
        void calculaCorrectamenteAmbasCotas() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("valida-3x3.txt");

                CotasJssp cotas = CalculadoraCotasJssp
                                .calcular(instancia);

                assertEquals(
                                10,
                                cotas.cotaInferior());

                assertEquals(
                                24,
                                cotas.cotaSuperior());
        }

        @Test
        void lasCotasCalculadasCoincidenConLasDeLaInstancia() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("valida-3x3.txt");

                CotasJssp cotas = CalculadoraCotasJssp
                                .calcular(instancia);

                assertEquals(
                                instancia.cotaInferior(),
                                cotas.cotaInferior());

                assertEquals(
                                instancia.cotaSuperior(),
                                cotas.cotaSuperior());
        }

        @Test
        void lanzaExcepcionAlCalcularLasCotasConInstanciaNula() {

                assertThrows(
                                ExcepcionAplicacion.class,
                                () -> CalculadoraCotasJssp
                                                .calcular(null));
        }

        @Test
        void lanzaExcepcionAlCalcularCotaInferiorConInstanciaNula() {

                assertThrows(
                                ExcepcionAplicacion.class,
                                () -> CalculadoraCotasJssp
                                                .calcularCotaInferior(null));
        }

        @Test
        void lanzaExcepcionAlCalcularCotaSuperiorConInstanciaNula() {

                assertThrows(
                                ExcepcionAplicacion.class,
                                () -> CalculadoraCotasJssp
                                                .calcularCotaSuperior(null));
        }

        @Test
        void calculaCorrectamenteLaCotaInferior40x40() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_40_40.txt");

                int cotaInferior = CalculadoraCotasJssp
                                .calcularCotaInferior(instancia);

                assertEquals(
                                103,
                                cotaInferior);
        }

        @Test
        void calculaCorrectamenteLaCotaSuperior40x40() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_40_40.txt");

                int cotaSuperior = CalculadoraCotasJssp
                                .calcularCotaSuperior(instancia);

                assertEquals(
                                3251,
                                cotaSuperior);
        }

        @Test
        void calculaCorrectamenteAmbasCotas40x40() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_40_40.txt");

                CotasJssp cotas = CalculadoraCotasJssp
                                .calcular(instancia);

                assertEquals(
                                103,
                                cotas.cotaInferior());

                assertEquals(
                                3251,
                                cotas.cotaSuperior());
        }

        @Test
        void lasCotasCalculadasCoincidenConLasDeLaInstancia40x40() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_40_40.txt");

                CotasJssp cotas = CalculadoraCotasJssp
                                .calcular(instancia);

                assertEquals(
                                instancia.cotaInferior(),
                                cotas.cotaInferior());

                assertEquals(
                                instancia.cotaSuperior(),
                                cotas.cotaSuperior());
        }

        @Test
        void calculaCorrectamenteLaCotaInferior100x100() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_100_100.txt");

                int cotaInferior = CalculadoraCotasJssp
                                .calcularCotaInferior(instancia);

                assertEquals(
                                56381,
                                cotaInferior);
        }

        @Test
        void calculaCorrectamenteLaCotaSuperior100x100() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_100_100.txt");

                int cotaSuperior = CalculadoraCotasJssp
                                .calcularCotaSuperior(instancia);

                assertEquals(
                                5008631,
                                cotaSuperior);
        }

        @Test
        void calculaCorrectamenteAmbasCotas100x100() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_100_100.txt");

                CotasJssp cotas = CalculadoraCotasJssp
                                .calcular(instancia);

                assertEquals(
                                56381,
                                cotas.cotaInferior());

                assertEquals(
                                5008631,
                                cotas.cotaSuperior());
        }

        @Test
        void lasCotasCalculadasCoincidenConLasDeLaInstancia100x100() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_100_100.txt");

                CotasJssp cotas = CalculadoraCotasJssp
                                .calcular(instancia);

                assertEquals(
                                instancia.cotaInferior(),
                                cotas.cotaInferior());

                assertEquals(
                                instancia.cotaSuperior(),
                                cotas.cotaSuperior());
        }

        @Test
        void calculaCorrectamenteLaCotaInferior50x20() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_50_20.txt");

                int cotaInferior = CalculadoraCotasJssp
                                .calcularCotaInferior(
                                                instancia);

                assertEquals(
                                5621,
                                cotaInferior);
        }

        @Test
        void calculaCorrectamenteLaCotaSuperior50x20() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_50_20.txt");

                int cotaSuperior = CalculadoraCotasJssp
                                .calcularCotaSuperior(
                                                instancia);

                assertEquals(
                                100947,
                                cotaSuperior);
        }

        @Test
        void calculaCorrectamenteAmbasCotas50x20() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_50_20.txt");

                CotasJssp cotas = CalculadoraCotasJssp
                                .calcular(
                                                instancia);

                assertEquals(
                                5621,
                                cotas.cotaInferior());

                assertEquals(
                                100947,
                                cotas.cotaSuperior());
        }

        @Test
        void lasCotasCalculadasCoincidenConLasDeLaInstancia50x20() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_50_20.txt");

                CotasJssp cotas = CalculadoraCotasJssp
                                .calcular(
                                                instancia);

                assertEquals(
                                instancia.cotaInferior(),
                                cotas.cotaInferior());

                assertEquals(
                                instancia.cotaSuperior(),
                                cotas.cotaSuperior());
        }
}