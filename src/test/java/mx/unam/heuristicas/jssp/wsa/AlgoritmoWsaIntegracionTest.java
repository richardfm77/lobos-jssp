package mx.unam.heuristicas.jssp.wsa;

import mx.unam.heuristicas.jssp.UtilTest;
import mx.unam.heuristicas.jssp.problema.ConstructorGraficaJssp;
import mx.unam.heuristicas.jssp.problema.EvaluadorGraficaJssp;
import mx.unam.heuristicas.jssp.problema.GeneradorSolucionJssp;
import mx.unam.heuristicas.jssp.problema.ProblemaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.ResultadoEvaluacionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.util.DoublePrecision;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.Random;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlgoritmoWsaIntegracionTest {

    @Test
    void ejecutaWsaCompletoSobreJssp40x40()
            throws URISyntaxException {

        InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_40_40.txt");

        ParametrosWsa parametros = crearParametros();

        /*
         * Un único generador maestro para esta corrida.
         */
        RandomGenerator generadorAleatorio = new Random(
                parametros.semilla());

        /*
         * ProblemaJssp y AlgoritmoWsa reciben exactamente
         * la misma instancia del generador maestro.
         */
        ProblemaJssp problema = new ProblemaJssp(
                instancia,
                generadorAleatorio);

        AlgoritmoWsa<SolucionJssp> algoritmo = new AlgoritmoWsa<>(
                problema,
                parametros,
                generadorAleatorio);

        /*
         * Utilizamos como punto de partida la solución
         * construida mediante la cota superior.
         *
         * Para JSSP_40_40:
         *
         * makespan = 205
         * ciclos = 0
         * costo = 1
         */
        SolucionJssp solucionBase = GeneradorSolucionJssp
                .generarSolucionCotaSuperior(
                        instancia);

        double costoBase = problema.funcionCosto(
                solucionBase);

        ResultadoWsa<SolucionJssp> resultado = algoritmo.ejecutar(
                solucionBase);

        assertNotNull(
                resultado);

        assertNotNull(
                resultado.mejorSolucion());

        assertTrue(
                Double.isFinite(
                        resultado.mejorCosto()));

        System.out.println(
                "Mejor costo encontrado Test: " + resultado.mejorCosto()); 
              
        /*
         * Como la solución base pertenece a la población
         * inicial, el mejor histórico nunca debería terminar
         * siendo peor que ella.
         */
        assertTrue(
                DoublePrecision.lessThanOrEqual(
                        resultado.mejorCosto(),
                        costoBase));

        /*
         * El costo almacenado en ResultadoWsa debe corresponder
         * realmente con el costo de la solución almacenada.
         */
        double costoRecalculado = problema.funcionCosto(
                resultado.mejorSolucion());

        assertTrue(
                DoublePrecision.equals(
                        costoRecalculado,
                        resultado.mejorCosto()));

        /*
         * La iteración encontrada debe estar dentro
         * del intervalo:
         *
         * [0, numeroIteraciones]
         */
        assertTrue(
                resultado.iteracionEncontrada() >= 0);

        assertTrue(
                resultado.iteracionEncontrada() <= parametros.numeroIteraciones());

        /*
         * Finalmente verificamos directamente la solución
         * obtenida utilizando nuestra gráfica JSSP y Kahn.
         */
        ResultadoEvaluacionJssp evaluacion = EvaluadorGraficaJssp.evaluar(
                ConstructorGraficaJssp.construir(
                        instancia,
                        resultado.mejorSolucion()));

        /*
         * Como partimos de una solución factible de costo 1,
         * una solución con ciclos tendría una penalización muy
         * superior y no podría sustituirla como mejor global.
         */
        assertEquals(
                0,
                evaluacion.verticesEnCiclo());
    }

    @Test
    void mismaSemillaProduceExactamenteElMismoResultado()
            throws URISyntaxException {

        InstanciaJssp instancia = UtilTest.obtenerInstancia("JSSP_40_40.txt");

        ParametrosWsa parametros = crearParametros();

        /*
         * PRIMERA CORRIDA
         */
        RandomGenerator generadorPrimeraCorrida = new Random(
                parametros.semilla());

        ProblemaJssp primerProblema = new ProblemaJssp(
                instancia,
                generadorPrimeraCorrida);

        AlgoritmoWsa<SolucionJssp> primerAlgoritmo = new AlgoritmoWsa<>(
                primerProblema,
                parametros,
                generadorPrimeraCorrida);

        SolucionJssp primeraBase = GeneradorSolucionJssp
                .generarSolucionCotaSuperior(
                        instancia);

        ResultadoWsa<SolucionJssp> primerResultado = primerAlgoritmo.ejecutar(
                primeraBase);

        /*
         * SEGUNDA CORRIDA
         *
         * Creamos un NUEVO Random con la misma semilla.
         *
         * No reutilizamos el de la primera corrida porque
         * su estado interno ya fue consumido.
         */
        RandomGenerator generadorSegundaCorrida = new Random(
                parametros.semilla());

        ProblemaJssp segundoProblema = new ProblemaJssp(
                instancia,
                generadorSegundaCorrida);

        AlgoritmoWsa<SolucionJssp> segundoAlgoritmo = new AlgoritmoWsa<>(
                segundoProblema,
                parametros,
                generadorSegundaCorrida);

        SolucionJssp segundaBase = GeneradorSolucionJssp
                .generarSolucionCotaSuperior(
                        instancia);

        ResultadoWsa<SolucionJssp> segundoResultado = segundoAlgoritmo.ejecutar(
                segundaBase);

        /*
         * Al utilizar:
         *
         * - misma instancia JSSP,
         * - mismos parámetros,
         * - misma semilla,
         * - mismo orden secuencial de ejecución,
         *
         * debemos reproducir exactamente la corrida.
         */
        assertEquals(
                primerResultado.mejorSolucion(),
                segundoResultado.mejorSolucion());

        assertTrue(
                DoublePrecision.equals(
                        primerResultado.mejorCosto(),
                        segundoResultado.mejorCosto()));

        assertEquals(
                primerResultado.iteracionEncontrada(),
                segundoResultado.iteracionEncontrada());
    }

    private ParametrosWsa crearParametros() {

        return new ParametrosWsa(
                5,
                5,
                500,
                3,
                0.0,
                247L,
                100,
                5,
                50);
    }
}