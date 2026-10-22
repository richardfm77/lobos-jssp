package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConstructorGraficaJsspTest {

    @Test
    void construyeCorrectamenteLaGrafica() {

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

        assertEquals(
                9,
                grafica.numeroVertices()
        );

        assertEquals(
                12,
                grafica.numeroAristas()
        );
    }

    @Test
    void agregaLasPrecedenciasDeLosTrabajos() {

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

        Operacion o00 =
                instancia.trabajos()
                        .get(0)
                        .operaciones()
                        .get(0);

        Operacion o01 =
                instancia.trabajos()
                        .get(0)
                        .operaciones()
                        .get(1);

        Operacion o02 =
                instancia.trabajos()
                        .get(0)
                        .operaciones()
                        .get(2);

        assertTrue(
                grafica.hayArista(o00, o01)
        );

        assertTrue(
                grafica.hayArista(o01, o02)
        );

        assertFalse(
                grafica.hayArista(o02, o01)
        );
    }

    @Test
    void agregaLasPrecedenciasDeLasMaquinas() {

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

        /*
         * Máquina 0:
         *
         * O00 -> O10 -> O21
         */

        Operacion o00 =
                instancia.trabajos()
                        .get(0)
                        .operaciones()
                        .get(0);

        Operacion o10 =
                instancia.trabajos()
                        .get(1)
                        .operaciones()
                        .get(0);

        Operacion o21 =
                instancia.trabajos()
                        .get(2)
                        .operaciones()
                        .get(1);

        assertTrue(
                grafica.hayArista(o00, o10)
        );

        assertTrue(
                grafica.hayArista(o10, o21)
        );

        assertFalse(
                grafica.hayArista(o10, o00)
        );
    }

    @Test
    void respetaElOrdenIndicadoPorLaSolucion() {

        InstanciaJssp instancia =
                obtenerInstancia();

        SolucionJssp solucion =
                GeneradorSolucionJssp
                        .generarInicial(instancia);

        List<SecuenciaMaquina> secuencias =
                new ArrayList<>(
                        solucion.secuenciasMaquinas()
                );

        SecuenciaMaquina maquina0 =
                secuencias.get(0);

        List<Operacion> operaciones =
                new ArrayList<>(
                        maquina0.operaciones()
                );

        Collections.swap(
                operaciones,
                0,
                1
        );

        secuencias.set(
                0,
                new SecuenciaMaquina(
                        0,
                        operaciones
                )
        );

        SolucionJssp solucionModificada =
                new SolucionJssp(secuencias);

        GraficaDirigida<Operacion> grafica =
                ConstructorGraficaJssp.construir(
                        instancia,
                        solucionModificada
                );

        Operacion o00 =
                instancia.trabajos()
                        .get(0)
                        .operaciones()
                        .get(0);

        Operacion o10 =
                instancia.trabajos()
                        .get(1)
                        .operaciones()
                        .get(0);

        assertTrue(
                grafica.hayArista(o10, o00)
        );

        assertFalse(
                grafica.hayArista(o00, o10)
        );
    }

    @Test
    void lanzaExcepcionSiLaInstanciaEsNula() {

        InstanciaJssp instancia =
                obtenerInstancia();

        SolucionJssp solucion =
                GeneradorSolucionJssp
                        .generarInicial(instancia);

        assertThrows(
                ExcepcionAplicacion.class,
                () -> ConstructorGraficaJssp.construir(
                        null,
                        solucion
                )
        );
    }

    @Test
    void lanzaExcepcionSiLaSolucionEsNula() {

        InstanciaJssp instancia =
                obtenerInstancia();

        assertThrows(
                ExcepcionAplicacion.class,
                () -> ConstructorGraficaJssp.construir(
                        instancia,
                        null
                )
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
                    "No se pudo obtener el recurso."
            );

            return null;
        }
    }
}