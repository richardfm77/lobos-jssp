package mx.unam.heuristicas.jssp.wsa;

import java.util.Optional;

import mx.unam.heuristicas.jssp.util.DoublePrecision;

public final class MovimientoWsa<S> {

        private final ProblemaHeuristico<S> problema;
        private final ParametrosWsa parametros;
        private final VisionWsa<S> vision;

        public MovimientoWsa(
                        ProblemaHeuristico<S> problema,
                        ParametrosWsa parametros) {

                if (problema == null) {
                        throw new IllegalArgumentException(
                                        "El problema heurístico no puede ser nulo.");
                }

                if (parametros == null) {
                        throw new IllegalArgumentException(
                                        "Los parámetros de WSA no pueden ser nulos.");
                }

                this.problema = problema;
                this.parametros = parametros;
                this.vision = new VisionWsa<>(
                                problema,
                                parametros);
        }

        /**
         * Realiza el movimiento normal de un lobo teniendo en cuenta
         * su memoria efímera.
         *
         * Si existe un lobo visible mejor, primero se intenta realizar
         * un movimiento dirigido hacia él.
         *
         * Si todos los movimientos dirigidos generados pertenecen a la
         * memoria reciente, se intenta realizar exploración aleatoria.
         *
         * Si no existe un lobo visible mejor, se realiza directamente
         * exploración aleatoria.
         *
         * Si no se encuentra una solución no recordada después del
         * máximo número de intentos, el lobo permanece en su posición.
         */
        public void mover(
                        Lobo<S> lobo,
                        PoblacionWsa<S> poblacion) {

                if (lobo == null) {
                        throw new IllegalArgumentException(
                                        "El lobo no puede ser nulo.");
                }

                if (poblacion == null) {
                        throw new IllegalArgumentException(
                                        "La población no puede ser nula.");
                }

                Optional<Lobo<S>> mejorVisible = vision.mejorLoboVisible(
                                lobo,
                                poblacion);

                CandidatoEvaluado<S> candidato = null;

                /*
                 * Si existe un lobo visible mejor,
                 * buscamos el mejor movimiento dirigido
                 * entre varios candidatos.
                 */
                if (mejorVisible.isPresent()) {

                        candidato = buscarMovimientoDirigido(
                                        lobo,
                                        mejorVisible.get());
                }

                /*
                 * Si no existe un lobo visible mejor,
                 * o ningún movimiento dirigido produjo
                 * una mejora, realizamos exploración
                 * aleatoria.
                 */
                if (candidato == null) {

                        candidato = buscarMovimientoAleatorio(
                                        lobo);
                }

                /*
                 * No fue posible encontrar una posición
                 * admisible.
                 */
                if (candidato == null) {
                        return;
                }

                lobo.actualizar(
                                candidato.solucion(),
                                candidato.costo());
        }

        private CandidatoEvaluado<S> buscarMovimientoDirigido(
                        Lobo<S> lobo,
                        Lobo<S> objetivo) {

                CandidatoEvaluado<S> mejorCandidato = null;

                for (int intento = 0; intento < parametros.maxIntentosMovimiento(); intento++) {

                        S candidato = problema.generarVecinoHacia(
                                        lobo.solucion(),
                                        objetivo.solucion());

                        /*
                         * Las posiciones recientemente visitadas
                         * por el lobo no son consideradas.
                         */
                        if (lobo.recuerda(candidato)) {
                                continue;
                        }

                        double costo = problema.funcionCosto(
                                        candidato);

                        if (mejorCandidato == null
                                        || DoublePrecision.lessThan(
                                                        costo,
                                                        mejorCandidato.costo())) {

                                mejorCandidato = new CandidatoEvaluado<>(
                                                candidato,
                                                costo);
                        }
                }

                /*
                 * Seguir al otro lobo solamente tiene sentido
                 * si el paso producido representa una mejora
                 * respecto a nuestra posición actual.
                 */
                if (mejorCandidato != null
                                && DoublePrecision.lessThan(
                                                mejorCandidato.costo(),
                                                lobo.costo())) {

                        return mejorCandidato;
                }

                return null;
        }

        private CandidatoEvaluado<S> buscarMovimientoAleatorio(
                        Lobo<S> lobo) {

                CandidatoEvaluado<S> mejorCandidato = null;

                for (int intento = 0; intento < parametros.maxIntentosMovimiento(); intento++) {

                        S candidato = problema.generarVecino(
                                        lobo.solucion());

                        if (lobo.recuerda(candidato)) {
                                continue;
                        }

                        double costo = problema.funcionCosto(
                                        candidato);

                        if (mejorCandidato == null
                                        || DoublePrecision.lessThan(
                                                        costo,
                                                        mejorCandidato.costo())) {

                                mejorCandidato = new CandidatoEvaluado<>(
                                                candidato,
                                                costo);
                        }
                }

                return mejorCandidato;
        }

        private record CandidatoEvaluado<S>(
                        S solucion,
                        double costo) {
        }
}