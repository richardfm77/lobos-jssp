package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.ResultadoEvaluacionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EvaluadorGraficaJsspTest {

    @Test
    void calculaCorrectamenteMakespanDeUnaGraficaAciclica() {

        InstanciaJssp instancia =
                obtenerInstancia();

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

    private InstanciaJssp obtenerInstancia() {

        Path archivo =
                obtenerRutaRecurso(
                        "instancias/valida-3x3.txt"
                );

        return LectorInstanciaJssp.leer(
                archivo
        );
    }

    private Path obtenerRutaRecurso(
            String nombreRecurso
    ) {

        URL recurso = getClass()
                .getClassLoader()
                .getResource(nombreRecurso);

        assertNotNull(recurso);

        try {
            return Path.of(
                    recurso.toURI()
            );
        } catch (URISyntaxException e) {

            fail(
                    "No se pudo obtener la ruta del recurso: "
                            + nombreRecurso
            );

            return null;
        }
    }
}