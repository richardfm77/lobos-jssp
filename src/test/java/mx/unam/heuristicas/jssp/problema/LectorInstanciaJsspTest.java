package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.UtilTest;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.Trabajo;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LectorInstanciaJsspTest {

    @Test
    void leeCorrectamenteUnaInstanciaValida() {

        Path archivo = UtilTest.obtenerRutaRecurso(
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
    void leeCorrectamenteUnaInstanciaValida40x40() {

        Path archivo = UtilTest.obtenerRutaRecurso(
                "instancias/JSSP_40_40.txt"
        );

        InstanciaJssp instancia =
                LectorInstanciaJssp.leer(archivo);

        assertEquals(
                40,
                instancia.numeroTrabajos()
        );

        assertEquals(
                40,
                instancia.numeroMaquinas()
        );

        assertEquals(
                3251,
                instancia.cotaSuperior()
        );

        assertEquals(
                103,
                instancia.cotaInferior()
        );

        assertEquals(
                40,
                instancia.trabajos().size()
        );

        Trabajo primerTrabajo =
                instancia.trabajos().get(0);

        assertEquals(
                0,
                primerTrabajo.id()
        );

        assertEquals(
                40,
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
                23,
                primeraOperacion.idMaquina()
        );

        assertEquals(
                8,
                primeraOperacion.tiempoProceso()
        );
    }

    @Test
    void leeCorrectamenteUnaInstanciaValida100x100() {

        Path archivo = UtilTest.obtenerRutaRecurso(
                "instancias/JSSP_100_100.txt"
        );

        InstanciaJssp instancia =
                LectorInstanciaJssp.leer(archivo);

        assertEquals(
                100,
                instancia.numeroTrabajos()
        );

        assertEquals(
                100,
                instancia.numeroMaquinas()
        );

        assertEquals(
                5008631,
                instancia.cotaSuperior()
        );

        assertEquals(
                56381,
                instancia.cotaInferior()
        );

        assertEquals(
                100,
                instancia.trabajos().size()
        );

        Trabajo primerTrabajo =
                instancia.trabajos().get(0);

        assertEquals(
                0,
                primerTrabajo.id()
        );

        assertEquals(
                100,
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
                20,
                primeraOperacion.idMaquina()
        );

        assertEquals(
                172,
                primeraOperacion.tiempoProceso()
        );
    }

        @Test
        void leeCorrectamenteUnaInstanciaValida50x20() {

        Path archivo = UtilTest.obtenerRutaRecurso(
                "instancias/JSSP_50_20.txt"
        );

        InstanciaJssp instancia =
                LectorInstanciaJssp.leer(archivo);

        assertEquals(
                50,
                instancia.numeroTrabajos()
        );

        assertEquals(
                20,
                instancia.numeroMaquinas()
        );

        assertEquals(
                100947,
                instancia.cotaSuperior()
        );

        assertEquals(
                5621,
                instancia.cotaInferior()
        );

        assertEquals(
                50,
                instancia.trabajos().size()
        );

        Trabajo primerTrabajo =
                instancia.trabajos().get(0);

        assertEquals(
                0,
                primerTrabajo.id()
        );

        assertEquals(
                20,
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
                16,
                primeraOperacion.idMaquina()
        );

        assertEquals(
                80,
                primeraOperacion.tiempoProceso()
        );
        }

    @Test
    void lanzaExcepcionSiElArchivoNoExiste() {

        Path archivo = Path.of(
                "instancias/archivo-que-no-existe.txt"
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

        Path archivo = UtilTest.obtenerRutaRecurso(
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

        Path archivo = UtilTest.obtenerRutaRecurso(
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

        Path archivo = UtilTest.obtenerRutaRecurso(
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

        Path archivo = UtilTest.obtenerRutaRecurso(
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

        Path archivo = UtilTest.obtenerRutaRecurso(
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

        Path archivo = UtilTest.obtenerRutaRecurso(
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

        Path archivo = UtilTest.obtenerRutaRecurso(
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

}