package mx.unam.heuristicas.jssp.problema.modelo;

import java.util.List;

/**
 * Representa una solución candidata del JSSP
 * mediante una cadena de máquinas.
 *
 * @param secuenciasMaquinas orden de procesamiento
 *                           de cada máquina
 */
public record SolucionJssp(
        List<SecuenciaMaquina> secuenciasMaquinas
) {
}