package mx.unam.heuristicas.jssp.wsa;

import java.util.Optional;

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

                S candidato = null;

                if (mejorVisible.isPresent()) {

                        candidato = buscarMovimientoDirigido(
                                        lobo,
                                        mejorVisible.get());
                }

                /*
                 * Si no había un lobo mejor o todos los movimientos
                 * dirigidos estaban en memoria, exploramos aleatoriamente.
                 */
                if (candidato == null) {

                        candidato = buscarMovimientoAleatorio(
                                        lobo);
                }

                /*
                 * No encontramos una posición admisible.
                 * El lobo permanece donde está.
                 */
                if (candidato == null) {
                        return;
                }

                double nuevoCosto = problema.funcionCosto(
                                candidato);

                lobo.actualizar(
                                candidato,
                                nuevoCosto);
        }

        private S buscarMovimientoDirigido(
                        Lobo<S> lobo,
                        Lobo<S> objetivo) {

                for (int intento = 0; intento < parametros.maxIntentosMovimiento(); intento++) {

                        S candidato = problema.generarVecinoHacia(
                                        lobo.solucion(),
                                        objetivo.solucion());

                        if (!lobo.recuerda(candidato)) {
                                return candidato;
                        }
                }

                return null;
        }

        private S buscarMovimientoAleatorio(
                        Lobo<S> lobo) {

                for (int intento = 0; intento < parametros.maxIntentosMovimiento(); intento++) {

                        S candidato = problema.generarVecino(
                                        lobo.solucion());

                        if (!lobo.recuerda(candidato)) {
                                return candidato;
                        }
                }

                return null;
        }
}