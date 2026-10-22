package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Trabajo;

import java.util.List;

/**
 * Construye la gráfica dirigida asociada a una solución del JSSP.
 *
 * La gráfica contiene:
 *
 * - aristas de precedencia entre operaciones de un mismo trabajo;
 * - aristas de precedencia determinadas por el orden de cada máquina.
 */
public final class ConstructorGraficaJssp {

    private ConstructorGraficaJssp() {
    }

    /**
     * Construye la gráfica dirigida correspondiente a una solución.
     *
     * @param instancia instancia del JSSP
     * @param solucion  solución representada mediante cadenas de máquinas
     * @return gráfica dirigida de operaciones
     */
    public static GraficaDirigida<Operacion> construir(
            InstanciaJssp instancia,
            SolucionJssp solucion) {

        if (instancia == null) {
            throw new ExcepcionAplicacion(
                    "La instancia JSSP no puede ser nula.");
        }

        if (solucion == null) {
            throw new ExcepcionAplicacion(
                    "La solución JSSP no puede ser nula.");
        }

        GraficaDirigida<Operacion> grafica = new GraficaDirigida<>();

        agregarVertices(
                grafica,
                instancia);

        agregarPrecedenciasTrabajos(
                grafica,
                instancia);

        agregarPrecedenciasMaquinas(
                grafica,
                solucion);

        return grafica;
    }

    private static void agregarVertices(
            GraficaDirigida<Operacion> grafica,
            InstanciaJssp instancia) {

        for (Trabajo trabajo : instancia.trabajos()) {

            for (Operacion operacion : trabajo.operaciones()) {
                grafica.agrega(operacion);
            }
        }
    }

    private static void agregarPrecedenciasTrabajos(
            GraficaDirigida<Operacion> grafica,
            InstanciaJssp instancia) {

        for (Trabajo trabajo : instancia.trabajos()) {

            List<Operacion> operaciones = trabajo.operaciones();

            conectarConsecutivas(
                    grafica,
                    operaciones);
        }
    }

    private static void agregarPrecedenciasMaquinas(
            GraficaDirigida<Operacion> grafica,
            SolucionJssp solucion) {

        for (SecuenciaMaquina secuencia : solucion.secuenciasMaquinas()) {

            conectarConsecutivas(
                    grafica,
                    secuencia.operaciones());
        }
    }

    private static void conectarConsecutivas(
            GraficaDirigida<Operacion> grafica,
            List<Operacion> operaciones) {

        Operacion anterior = null;

        for (Operacion actual : operaciones) {

            if (anterior != null
                    && !grafica.hayArista(anterior, actual)) {

                grafica.conecta(
                        anterior,
                        actual);
            }

            anterior = actual;
        }
    }
}