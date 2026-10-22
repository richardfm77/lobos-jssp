package mx.unam.heuristicas.jssp.problema.modelo;

import java.util.List;

/**
 * Representa un trabajo del JSSP.
 *
 * Las operaciones se almacenan en el orden en el que deben
 * ser procesadas.
 *
 * @param id identificador del trabajo
 * @param operaciones secuencia de operaciones del trabajo
 */
public record Trabajo(
        int id,
        List<Operacion> operaciones
) {
}