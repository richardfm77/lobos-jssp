package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.wsa.ProblemaHeuristico;
import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.CotasJssp;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.ResultadoEvaluacionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;

public final class ProblemaJssp
        implements ProblemaHeuristico<SolucionJssp> {

    private final InstanciaJssp instancia;
    private final CotasJssp cotas;

    /**
     * Construye el problema JSSP asociado a una instancia.
     *
     * Las cotas inferior y superior se calculan una sola vez
     * al construir el problema, ya que dependen únicamente
     * de la instancia y no de las soluciones evaluadas.
     *
     * @param instancia instancia del problema JSSP
     */
    public ProblemaJssp(
            InstanciaJssp instancia
    ) {

        if (instancia == null) {
            throw new ExcepcionAplicacion(
                    "La instancia JSSP no puede ser nula."
            );
        }

        this.instancia = instancia;
        this.cotas =
                CalculadoraCotasJssp.calcular(instancia);
    }

    /**
     * Calcula el costo de una solución JSSP.
     *
     * El costo corresponde al makespan normalizado entre las
     * cotas inferior y superior de la instancia, más una
     * penalización por cada vértice que permanece asociado
     * a un ciclo después de aplicar el algoritmo de Kahn.
     *
     * @param solucion solución que será evaluada
     * @return costo de la solución
     */
    @Override
    public double funcionCosto(
            SolucionJssp solucion
    ) {

        if (solucion == null) {
            throw new ExcepcionAplicacion(
                    "La solución JSSP no puede ser nula."
            );
        }

        GraficaDirigida<Operacion> grafica =
                ConstructorGraficaJssp.construir(
                        instancia,
                        solucion
                );

        ResultadoEvaluacionJssp evaluacion =
                EvaluadorGraficaJssp.evaluar(
                        grafica
                );

        double costoNormalizado =
                ((double) evaluacion.makespan()
                        - cotas.cotaInferior())
                        /
                        (cotas.cotaSuperior()
                                - cotas.cotaInferior());

        double penalizacion =
                (double) cotas.cotaInferior()
                        * evaluacion.verticesEnCiclo();

        return costoNormalizado
                + penalizacion;
    }

    /**
     * Genera una solución vecina a partir de una solución.
     *
     * Pendiente de implementación.
     *
     * @param solucion solución de origen
     * @return solución vecina
     */
    @Override
    public SolucionJssp generarVecino(
            SolucionJssp solucion
    ) {

        throw new UnsupportedOperationException(
                "La generación de vecinos todavía no está implementada."
        );
    }

    /**
     * Calcula la distancia entre dos soluciones.
     *
     * Pendiente de implementación.
     *
     * @param primera primera solución
     * @param segunda segunda solución
     * @return distancia entre ambas soluciones
     */
    @Override
    public double distancia(
            SolucionJssp primera,
            SolucionJssp segunda
    ) {

        throw new UnsupportedOperationException(
                "La distancia entre soluciones todavía no está implementada."
        );
    }
}