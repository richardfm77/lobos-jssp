package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.Trabajo;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LectorInstanciaJsspTest {

    @Test
    void leeCorrectamenteUnaInstanciaValida() {

        Path archivo = obtenerRutaRecurso(
                "instancias/valida-3x3.txt"
        );

        InstanciaJssp instancia =
                LectorInstanciaJssp.leer(archivo);

        assertEquals(
                3,
                instancia.numeroTrabajos()
        );

        assertEquals(
                3,
                instancia.numeroMaquinas()
        );

        assertEquals(
                24,
                instancia.cotaSuperior()
        );

        assertEquals(
                10,
                instancia.cotaInferior()
        );

        assertEquals(
                3,
                instancia.trabajos().size()
        );

        Trabajo primerTrabajo =
                instancia.trabajos().get(0);

        assertEquals(
                0,
                primerTrabajo.id()
        );

        assertEquals(
                3,
                primerTrabajo.operaciones().size()
        );

        Operacion primeraOperacion =
                primerTrabajo.operaciones().get(0);

        assertEquals(
                0,
                primeraOperacion.idTrabajo()
        );

        assertEquals(
                0,
                primeraOperacion.indiceOperacion()
        );

        assertEquals(
                0,
                primeraOperacion.idMaquina()
        );

        assertEquals(
                3,
                primeraOperacion.tiempoProceso()
        );
    }

    @Test
    void lanzaExcepcionSiElArchivoNoExiste() {

        Path archivo = Path.of(
                "archivo-que-no-existe.txt"
        );

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () -> LectorInstanciaJssp.leer(archivo)
                );

        assertTrue(
                excepcion.getMessage()
                        .contains("No se pudo leer")
        );
    }

    @Test
    void lanzaExcepcionSiElArchivoEstaVacio() {

        Path archivo = obtenerRutaRecurso(
                "instancias/vacia.txt"
        );

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () -> LectorInstanciaJssp.leer(archivo)
                );

        assertEquals(
                "El archivo de la instancia está vacío.",
                excepcion.getMessage()
        );
    }

    @Test
    void lanzaExcepcionSiElEncabezadoEsInvalido() {

        Path archivo = obtenerRutaRecurso(
                "instancias/encabezado-invalido.txt"
        );

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () -> LectorInstanciaJssp.leer(archivo)
                );

        assertTrue(
                excepcion.getMessage()
                        .contains("encabezado")
        );
    }

    @Test
    void lanzaExcepcionSiElNumeroDeTrabajosNoCoincide() {

        Path archivo = obtenerRutaRecurso(
                "instancias/numero-trabajos-invalido.txt"
        );

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () -> LectorInstanciaJssp.leer(archivo)
                );

        assertTrue(
                excepcion.getMessage()
                        .contains("número de trabajos")
        );
    }

    @Test
    void lanzaExcepcionSiUnTrabajoNoTieneElNumeroCorrectoDeOperaciones() {

        Path archivo = obtenerRutaRecurso(
                "instancias/numero-operaciones-invalido.txt"
        );

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () -> LectorInstanciaJssp.leer(archivo)
                );

        assertTrue(
                excepcion.getMessage()
                        .contains(
                                "debe contener exactamente 3 operaciones"
                        )
        );
    }

    @Test
    void lanzaExcepcionSiLaMaquinaEstaFueraDeRango() {

        Path archivo = obtenerRutaRecurso(
                "instancias/maquina-invalida.txt"
        );

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () -> LectorInstanciaJssp.leer(archivo)
                );

        assertTrue(
                excepcion.getMessage()
                        .contains("fuera del rango")
        );
    }

    @Test
    void lanzaExcepcionSiElTiempoDeProcesamientoNoEsPositivo() {

        Path archivo = obtenerRutaRecurso(
                "instancias/tiempo-invalido.txt"
        );

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () -> LectorInstanciaJssp.leer(archivo)
                );

        assertTrue(
                excepcion.getMessage()
                        .contains("debe ser mayor que cero")
        );
    }

    @Test
    void lanzaExcepcionSiExisteUnValorNoNumerico() {

        Path archivo = obtenerRutaRecurso(
                "instancias/valor-no-numerico.txt"
        );

        ExcepcionAplicacion excepcion =
                assertThrows(
                        ExcepcionAplicacion.class,
                        () -> LectorInstanciaJssp.leer(archivo)
                );

        assertTrue(
                excepcion.getMessage()
                        .contains("no es entero")
        );
    }

    private Path obtenerRutaRecurso(String nombreRecurso) {

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