package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.UtilTest;
import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.ResultadoEvaluacionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluadorGraficaJsspTest {

    @Test
    void calculaCorrectamenteMakespanDeUnaGraficaAciclica() {

        InstanciaJssp instancia =
                UtilTest.obtenerInstancia("valida-3x3.txt");

        SolucionJssp solucion =
                GeneradorSolucionJssp
                        .generarInicial(instancia);

        GraficaDirigida<Operacion> grafica =
                ConstructorGraficaJssp.construir(
                        instancia,
                        solucion
                );

        ResultadoEvaluacionJssp resultado =
                EvaluadorGraficaJssp.evaluar(grafica);

        assertEquals(
                22,
                resultado.makespan()
        );

        assertEquals(
                0,
                resultado.verticesEnCiclo()
        );
    }

    @Test
    void solucionCotaSuperior40x40TieneMakespanEsperado() {

        InstanciaJssp instancia =
                UtilTest.obtenerInstancia("JSSP_40_40.txt");

        SolucionJssp solucion =
                GeneradorSolucionJssp
                        .generarSolucionCotaSuperior(instancia);

        GraficaDirigida<Operacion> grafica =
                ConstructorGraficaJssp.construir(
                        instancia,
                        solucion
                );

        ResultadoEvaluacionJssp resultado =
                EvaluadorGraficaJssp.evaluar(grafica);

        assertEquals(
                205,
                resultado.makespan()
        );

        assertEquals(
                0,
                resultado.verticesEnCiclo()
        );
    }

    @Test
    void detectaVerticesEnCiclo() {

        Operacion a =
                new Operacion(0, 0, 0, 2);

        Operacion b =
                new Operacion(1, 0, 1, 3);

        Operacion c =
                new Operacion(2, 0, 2, 4);

        GraficaDirigida<Operacion> grafica =
                new GraficaDirigida<>();

        grafica.agrega(a);
        grafica.agrega(b);
        grafica.agrega(c);

        grafica.conecta(a, b);
        grafica.conecta(b, c);
        grafica.conecta(c, a);

        ResultadoEvaluacionJssp resultado =
                EvaluadorGraficaJssp.evaluar(grafica);

        assertEquals(
                3,
                resultado.verticesEnCiclo()
        );
    }

    @Test
    void lanzaExcepcionSiLaGraficaEsNula() {

        assertThrows(
                ExcepcionAplicacion.class,
                () -> EvaluadorGraficaJssp
                        .evaluar(null)
        );
    }

}