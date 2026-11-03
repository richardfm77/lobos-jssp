package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.UtilTest;
import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.ResultadoEvaluacionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.util.DoublePrecision;
import mx.unam.heuristicas.jssp.wsa.EnumTipoSolucion;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorSolucionJsspTest {

        @Test
        void generaCorrectamenteUnaSolucionInicial() {

                Path archivo = UtilTest.obtenerRutaRecurso(
                                "instancias/valida-3x3.txt");

                InstanciaJssp instancia = LectorInstanciaJssp.leer(archivo);

                SolucionJssp solucion = GeneradorSolucionJssp.generarInicial(instancia);

                assertEquals(
                                3,
                                solucion.secuenciasMaquinas().size());

                SecuenciaMaquina maquina0 = solucion.secuenciasMaquinas().get(0);

                assertEquals(
                                0,
                                maquina0.idMaquina());

                assertEquals(
                                3,
                                maquina0.operaciones().size());

                assertEquals(
                                0,
                                maquina0.operaciones()
                                                .get(0)
                                                .idTrabajo());

                assertEquals(
                                1,
                                maquina0.operaciones()
                                                .get(1)
                                                .idTrabajo());

                assertEquals(
                                2,
                                maquina0.operaciones()
                                                .get(2)
                                                .idTrabajo());
        }

        @Test
        void generaUnaSolucionNoFactible() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_50_20.txt");

                ProblemaJssp problema = new ProblemaJssp(instancia);

                SolucionJssp solucion = GeneradorSolucionJssp
                                .generarNoFactible(
                                                instancia);

                GraficaDirigida<Operacion> grafica = ConstructorGraficaJssp.construir(
                                instancia,
                                solucion);

                ResultadoEvaluacionJssp evaluacion = EvaluadorGraficaJssp.evaluar(
                                grafica);

                assertTrue(
                                evaluacion.verticesEnCiclo() > 0);
                assertTrue(
                                DoublePrecision.greaterThanOrEqual(
                                                problema.funcionCosto(solucion),
                                                1.0));
        }

        @Test
        void lanzaExcepcionSiLaInstanciaEsNula() {

                ExcepcionAplicacion excepcion = assertThrows(
                                ExcepcionAplicacion.class,
                                () -> GeneradorSolucionJssp
                                                .generarInicial(null));

                assertEquals(
                                "La instancia JSSP no puede ser nula.",
                                excepcion.getMessage());
        }

        @Test
        void generaSolucionNormal() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                SolucionJssp esperada = GeneradorSolucionJssp.generarInicial(
                                instancia);

                SolucionJssp obtenida = GeneradorSolucionJssp.generaTipoSolucion(
                                instancia,
                                EnumTipoSolucion.NORMAL);

                assertNotNull(
                                obtenida);

                assertEquals(
                                esperada,
                                obtenida);
        }

        @Test
        void generaSolucionFactible() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                SolucionJssp solucion = GeneradorSolucionJssp.generaTipoSolucion(
                                instancia,
                                EnumTipoSolucion.FACTIBLE);

                assertNotNull(
                                solucion);

                GraficaDirigida<Operacion> grafica = ConstructorGraficaJssp.construir(
                                instancia,
                                solucion);

                ResultadoEvaluacionJssp evaluacion = EvaluadorGraficaJssp.evaluar(
                                grafica);

                assertEquals(
                                0,
                                evaluacion.verticesEnCiclo());
        }

        @Test
        void generaSolucionNoFactible() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                SolucionJssp solucion = GeneradorSolucionJssp.generaTipoSolucion(
                                instancia,
                                EnumTipoSolucion.NO_FACTIBLE);

                assertNotNull(
                                solucion);

                GraficaDirigida<Operacion> grafica = ConstructorGraficaJssp.construir(
                                instancia,
                                solucion);

                ResultadoEvaluacionJssp evaluacion = EvaluadorGraficaJssp.evaluar(
                                grafica);

                assertTrue(
                                evaluacion.verticesEnCiclo() > 0);
        }

        @Test
        void lanzaExcepcionSiTipoEsNulo() {

                InstanciaJssp instancia = UtilTest.obtenerInstancia(
                                "JSSP_40_40.txt");

                ExcepcionAplicacion excepcion = assertThrows(
                                ExcepcionAplicacion.class,
                                () -> GeneradorSolucionJssp
                                                .generaTipoSolucion(
                                                                instancia,
                                                                null));

                assertEquals(
                                "El tipo de solución no puede ser nulo.",
                                excepcion.getMessage());
        }

}