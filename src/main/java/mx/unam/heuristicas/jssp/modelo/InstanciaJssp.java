package mx.unam.heuristicas.jssp.modelo;

import java.util.List;

/**
 * Representa una instancia completa del problema JSSP.
 *
 * @param numeroTrabajos número total de trabajos
 * @param numeroMaquinas número total de máquinas
 * @param cotaSuperior cota superior proporcionada por la instancia
 * @param cotaInferior cota inferior proporcionada por la instancia
 * @param trabajos trabajos que forman la instancia
 */
public record InstanciaJssp(
        int numeroTrabajos,
        int numeroMaquinas,
        int cotaSuperior,
        int cotaInferior,
        List<Trabajo> trabajos
) {
}