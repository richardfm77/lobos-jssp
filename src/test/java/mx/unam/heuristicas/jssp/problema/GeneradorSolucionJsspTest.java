package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorSolucionJsspTest {

    @Test
    void generaCorrectamenteUnaSolucionInicial() {

        Path archivo = obtenerRutaRecurso(
                "instancias/valida-3x3.txt"
        );

        InstanciaJssp instancia =
                LectorInstanciaJssp.leer(archivo);

        SolucionJssp solucion =
                GeneradorSolucionJssp.generarInicial(instancia);

        assertEquals(
                3,
                solucion.secuenciasMaquinas().size()
        );

        SecuenciaMaquina maquina0 =
                solucion.secuenciasMaquinas().get(0);

        assertEquals(
                0,
                maquina0.idMaquina()
        );

        assertEquals(
                3,
                maquina0.operaciones().size()
        );

        assertEquals(
                0,
                maquina0.operaciones()
                        .get(0)
                        .idTrabajo()
        );

        assertEquals(
                1,
                maquina0.operaciones()
                        .get(1)
                        .idTrabajo()
        );

        assertEquals(
                2,
                maquina0.operaciones()
                        .get(2)
                        .idTrabajo()
        );
    }

    @Test
    void lanzaExcepcionSiLaInstanciaEsNula() {

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () ->
                                GeneradorSolucionJssp
                                        .generarInicial(null)
                );

        assertEquals(
                "La instancia JSSP no puede ser nula.",
                excepcion.getMessage()
        );
    }

    private Path obtenerRutaRecurso(
            String nombreRecurso
    ) {

        URL recurso = getClass()
                .getClassLoader()
                .getResource(nombreRecurso);

        assertNotNull(
                recurso,
                "No se encontró el recurso de prueba: "
                        + nombreRecurso
        );

        try {
            return Path.of(recurso.toURI());

        } catch (URISyntaxException e) {
            fail(
                    "No se pudo obtener la ruta del recurso: "
                            + nombreRecurso
            );

            return null;
        }
    }
}