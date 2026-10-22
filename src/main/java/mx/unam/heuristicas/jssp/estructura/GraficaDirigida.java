package mx.unam.heuristicas.jssp.estructura;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Representa una gráfica dirigida genérica.
 *
 * Cada vértice mantiene el conjunto de sus vecinos salientes.
 * Además, se almacena explícitamente el grado de entrada
 * de cada vértice.
 *
 * @param <T> tipo de los elementos almacenados en la gráfica
 */
public final class GraficaDirigida<T> {

    private final Map<T, Set<T>> adyacencias;
    private final Map<T, Integer> gradosEntrada;

    private int numeroAristas;

    public GraficaDirigida() {
        adyacencias = new LinkedHashMap<>();
        gradosEntrada = new LinkedHashMap<>();
        numeroAristas = 0;
    }

    /**
     * Agrega un vértice a la gráfica.
     *
     * @param elemento elemento a agregar
     */
    public void agrega(T elemento) {

        if (elemento == null) {
            throw new IllegalArgumentException(
                    "El elemento no puede ser nulo.");
        }

        if (contiene(elemento)) {
            throw new IllegalArgumentException(
                    "El elemento ya pertenece a la gráfica.");
        }

        adyacencias.put(
                elemento,
                new LinkedHashSet<>());

        gradosEntrada.put(
                elemento,
                0);
    }

    /**
     * Agrega la arista dirigida origen -> destino.
     *
     * @param origen  vértice origen
     * @param destino vértice destino
     */
    public void conecta(
            T origen,
            T destino) {

        verificaVertice(origen);
        verificaVertice(destino);

        if (origen.equals(destino)) {
            throw new IllegalArgumentException(
                    "No se permiten lazos.");
        }

        if (hayArista(origen, destino)) {
            throw new IllegalArgumentException(
                    "La arista ya existe.");
        }

        adyacencias
                .get(origen)
                .add(destino);

        gradosEntrada.put(
                destino,
                gradosEntrada.get(destino) + 1);

        numeroAristas++;
    }

    /**
     * Nos dice si un elemento pertenece a la gráfica.
     *
     * @param elemento elemento a consultar
     * @return true si pertenece a la gráfica
     */
    public boolean contiene(T elemento) {
        return adyacencias.containsKey(elemento);
    }

    /**
     * Nos dice si existe la arista origen -> destino.
     *
     * @param origen  vértice origen
     * @param destino vértice destino
     * @return true si existe la arista
     */
    public boolean hayArista(
            T origen,
            T destino) {

        verificaVertice(origen);
        verificaVertice(destino);

        return adyacencias
                .get(origen)
                .contains(destino);
    }

    /**
     * Regresa los vecinos salientes de un vértice.
     *
     * @param elemento vértice
     * @return vecinos alcanzables mediante una arista saliente
     */
    public Set<T> vecinosSalientes(T elemento) {

        verificaVertice(elemento);

        return Collections.unmodifiableSet(
                adyacencias.get(elemento));
    }

    /**
     * Regresa el grado de entrada de un vértice.
     *
     * @param elemento vértice
     * @return grado de entrada
     */
    public int gradoEntrada(T elemento) {

        verificaVertice(elemento);

        return gradosEntrada.get(elemento);
    }

    /**
     * Regresa el grado de salida de un vértice.
     *
     * @param elemento vértice
     * @return grado de salida
     */
    public int gradoSalida(T elemento) {

        verificaVertice(elemento);

        return adyacencias
                .get(elemento)
                .size();
    }

    /**
     * Regresa todos los vértices de la gráfica.
     *
     * @return conjunto de vértices
     */
    public Set<T> vertices() {

        return Collections.unmodifiableSet(
                adyacencias.keySet());
    }

    /**
     * Regresa el número de vértices.
     *
     * @return número de vértices
     */
    public int numeroVertices() {
        return adyacencias.size();
    }

    /**
     * Regresa el número de aristas.
     *
     * @return número de aristas
     */
    public int numeroAristas() {
        return numeroAristas;
    }

    /**
     * Nos dice si la gráfica no contiene vértices.
     *
     * @return true si la gráfica está vacía
     */
    public boolean esVacia() {
        return adyacencias.isEmpty();
    }

    private void verificaVertice(T elemento) {

        if (!contiene(elemento)) {
            throw new NoSuchElementException(
                    "El elemento no pertenece a la gráfica.");
        }
    }
}