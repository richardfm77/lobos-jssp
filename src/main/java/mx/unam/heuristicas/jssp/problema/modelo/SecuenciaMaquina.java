package mx.unam.heuristicas.jssp.problema.modelo;

import java.util.List;

/**
 * Representa el orden en que una máquina procesará
 * las operaciones asignadas a ella.
 *
 * @param idMaquina identificador de la máquina
 * @param operaciones operaciones en orden de procesamiento
 */
public record SecuenciaMaquina(
        int idMaquina,
        List<Operacion> operaciones
) {
}