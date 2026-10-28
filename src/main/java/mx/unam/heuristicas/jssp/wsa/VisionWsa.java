package mx.unam.heuristicas.jssp.wsa;

import mx.unam.heuristicas.jssp.util.DoublePrecision;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class VisionWsa<S> {

    private final ProblemaHeuristico<S> problema;
    private final ParametrosWsa parametros;

    /**
     * Construye el comportamiento de visión utilizado por WSA.
     *
     * @param problema   problema heurístico sobre el que trabaja WSA
     * @param parametros parámetros del algoritmo WSA
     */
    public VisionWsa(
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
    }

    /**
     * Obtiene los lobos que se encuentran dentro del rango visual
     * de un lobo de referencia.
     *
     * Un lobo es visible si su distancia respecto al lobo de
     * referencia es menor o igual al rango visual R.
     *
     * El propio lobo de referencia no se incluye en el resultado.
     *
     * @param referencia lobo desde el cual se realiza la búsqueda
     * @param poblacion  población de lobos
     * @return lista de lobos visibles
     */
    public List<Lobo<S>> lobosVisibles(
            Lobo<S> referencia,
            PoblacionWsa<S> poblacion) {

        validarArgumentos(
                referencia,
                poblacion);

        ArrayList<Lobo<S>> visibles = new ArrayList<>();

        for (Lobo<S> candidato : poblacion.lobos()) {

            if (candidato == referencia) {
                continue;
            }

            double distancia = problema.distancia(
                    referencia.solucion(),
                    candidato.solucion());

            if (DoublePrecision.lessThanOrEqual(
                    distancia,
                    parametros.rangoVisual())) {

                visibles.add(candidato);
            }
        }

        return List.copyOf(visibles);
    }

    /**
     * Busca el mejor lobo visible cuyo costo sea estrictamente
     * menor que el costo del lobo de referencia.
     *
     * @param referencia lobo desde el cual se realiza la búsqueda
     * @param poblacion  población de lobos
     * @return mejor lobo visible, o vacío si no existe uno mejor
     */
    public Optional<Lobo<S>> mejorLoboVisible(
            Lobo<S> referencia,
            PoblacionWsa<S> poblacion) {

        validarArgumentos(
                referencia,
                poblacion);

        Lobo<S> mejor = null;

        for (Lobo<S> candidato : lobosVisibles(
                referencia,
                poblacion)) {

            if (!DoublePrecision.lessThan(
                    candidato.costo(),
                    referencia.costo())) {
                continue;
            }

            if (mejor == null
                    || DoublePrecision.lessThan(
                            candidato.costo(),
                            mejor.costo())) {

                mejor = candidato;
            }
        }

        return Optional.ofNullable(mejor);
    }

    private void validarArgumentos(
            Lobo<S> referencia,
            PoblacionWsa<S> poblacion) {

        if (referencia == null) {
            throw new IllegalArgumentException(
                    "El lobo de referencia no puede ser nulo.");
        }

        if (poblacion == null) {
            throw new IllegalArgumentException(
                    "La población no puede ser nula.");
        }
    }
}