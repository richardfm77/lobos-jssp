package mx.unam.heuristicas.jssp.problema.modelo;

/**
 * Representa una operación perteneciente a un trabajo del JSSP.
 *
 * @param idTrabajo       identificador del trabajo al que pertenece
 * @param indiceOperacion posición de la operación dentro de su trabajo
 * @param idMaquina       identificador de la máquina que debe procesarla
 * @param tiempoProceso   tiempo requerido para completar la operación
 */
public record Operacion(
        int idTrabajo,
        int indiceOperacion,
        int idMaquina,
        int tiempoProceso
) {
}