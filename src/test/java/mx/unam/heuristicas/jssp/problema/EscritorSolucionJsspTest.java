package mx.unam.heuristicas.jssp.problema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;

class EscritorSolucionJsspTest {

    @TempDir
    Path directorioTemporal;

    @Test
    void convierteCorrectamenteSolucionATexto() {

        SolucionJssp solucion = crearSolucionPrueba();

        String resultado = EscritorSolucionJssp
                .convertirATexto(
                        solucion);

        String esperado = "[ [0, 0, 3], [1, 0, 2], ]"
                + System.lineSeparator()
                + "[ [0, 1, 4], [1, 1, 5], ]";

        assertEquals(
                esperado,
                resultado);
    }

    @Test
    void escribeCorrectamenteSolucionEnArchivo()
            throws IOException {

        SolucionJssp solucion = crearSolucionPrueba();

        Path archivo = directorioTemporal.resolve(
                "solucion.txt");

        EscritorSolucionJssp.escribir(
                solucion,
                archivo);

        assertTrue(
                Files.exists(
                        archivo));

        String contenido = Files.readString(
                archivo);

        String esperado = "[ [0, 0, 3], [1, 0, 2], ]"
                + System.lineSeparator()
                + "[ [0, 1, 4], [1, 1, 5], ]";

        assertEquals(
                esperado,
                contenido);
    }

    @Test
    void lanzaExcepcionSiLaSolucionEsNula() {

        Path archivo = directorioTemporal.resolve(
                "solucion.txt");

        assertThrows(
                ExcepcionAplicacion.class,
                () -> EscritorSolucionJssp.escribir(
                        null,
                        archivo));
    }

    @Test
    void lanzaExcepcionSiLaRutaEsNula() {

        SolucionJssp solucion = crearSolucionPrueba();

        assertThrows(
                ExcepcionAplicacion.class,
                () -> EscritorSolucionJssp.escribir(
                        solucion,
                        null));
    }

    private SolucionJssp crearSolucionPrueba() {

        Operacion operacion00 = new Operacion(
                0,
                0,
                0,
                3);

        Operacion operacion01 = new Operacion(
                0,
                1,
                1,
                4);

        Operacion operacion10 = new Operacion(
                1,
                0,
                0,
                2);

        Operacion operacion11 = new Operacion(
                1,
                1,
                1,
                5);

        SecuenciaMaquina maquina0 = new SecuenciaMaquina(
                0,
                List.of(
                        operacion00,
                        operacion10));

        SecuenciaMaquina maquina1 = new SecuenciaMaquina(
                1,
                List.of(
                        operacion01,
                        operacion11));

        return new SolucionJssp(
                List.of(
                        maquina0,
                        maquina1));
    }
}