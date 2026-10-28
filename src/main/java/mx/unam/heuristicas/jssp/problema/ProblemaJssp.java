package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.wsa.ProblemaHeuristico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;

import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.CotasJssp;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.ResultadoEvaluacionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.util.DoublePrecision;

public final class ProblemaJssp
                implements ProblemaHeuristico<SolucionJssp> {

        private final InstanciaJssp instancia;
        private final CotasJssp cotas;
        private final RandomGenerator generadorAleatorio;

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
                        InstanciaJssp instancia) {
                this(
                                instancia,
                                RandomGenerator.getDefault());
        }

        public ProblemaJssp(
                        InstanciaJssp instancia,
                        RandomGenerator generadorAleatorio) {

                if (instancia == null) {
                        throw new ExcepcionAplicacion(
                                        "La instancia JSSP no puede ser nula.");
                }

                if (generadorAleatorio == null) {
                        throw new ExcepcionAplicacion(
                                        "El generador aleatorio no puede ser nulo.");
                }

                this.instancia = instancia;
                this.cotas = CalculadoraCotasJssp.calcular(instancia);

                this.generadorAleatorio = generadorAleatorio;
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
                        SolucionJssp solucion) {

                if (solucion == null) {
                        throw new ExcepcionAplicacion(
                                        "La solución JSSP no puede ser nula.");
                }

                GraficaDirigida<Operacion> grafica = ConstructorGraficaJssp.construir(
                                instancia,
                                solucion);

                ResultadoEvaluacionJssp evaluacion = EvaluadorGraficaJssp.evaluar(
                                grafica);

                double costoNormalizado = ((double) evaluacion.makespan()
                                - cotas.cotaInferior())
                                /
                                (cotas.cotaSuperior()
                                                - cotas.cotaInferior());

                double penalizacion = (double) cotas.cotaInferior()
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
                        SolucionJssp solucion) {

                if (solucion == null) {
                        throw new ExcepcionAplicacion(
                                        "La solución JSSP no puede ser nula.");
                }

                ArrayList<SecuenciaMaquina> secuenciasElegibles = new ArrayList<>();

                for (SecuenciaMaquina secuencia : solucion.secuenciasMaquinas()) {

                        if (secuencia.operaciones().size() >= 2) {
                                secuenciasElegibles.add(secuencia);
                        }
                }

                if (secuenciasElegibles.isEmpty()) {
                        throw new ExcepcionAplicacion(
                                        "No existe una secuencia de máquina "
                                                        + "con al menos dos operaciones.");
                }

                SecuenciaMaquina secuenciaElegida = secuenciasElegibles.get(
                                generadorAleatorio.nextInt(
                                                secuenciasElegibles.size()));

                ArrayList<SecuenciaMaquina> nuevasSecuencias = new ArrayList<>();

                for (SecuenciaMaquina secuencia : solucion.secuenciasMaquinas()) {

                        ArrayList<Operacion> nuevasOperaciones = new ArrayList<>(
                                        secuencia.operaciones());

                        if (secuencia.idMaquina() == secuenciaElegida.idMaquina()) {

                                intercambiarDosOperaciones(
                                                nuevasOperaciones);
                        }

                        nuevasSecuencias.add(
                                        new SecuenciaMaquina(
                                                        secuencia.idMaquina(),
                                                        nuevasOperaciones));
                }

                return new SolucionJssp(
                                nuevasSecuencias);
        }

        @Override
        public SolucionJssp generarVecinoHacia(
                        SolucionJssp solucion,
                        SolucionJssp objetivo) {

                if (solucion == null || objetivo == null) {
                        throw new ExcepcionAplicacion(
                                        "Las soluciones JSSP no pueden ser nulas.");
                }

                double distanciaActual = distancia(
                                solucion,
                                objetivo);

                if (DoublePrecision.equals(
                                distanciaActual,
                                0.0)) {
                        throw new ExcepcionAplicacion(
                                        "No es posible acercarse a una solución idéntica.");
                }

                Map<Integer, SecuenciaMaquina> secuenciasObjetivo = new HashMap<>();

                for (SecuenciaMaquina secuencia : objetivo.secuenciasMaquinas()) {

                        secuenciasObjetivo.put(
                                        secuencia.idMaquina(),
                                        secuencia);
                }

                ArrayList<Integer> maquinasDiferentes = new ArrayList<>();

                for (SecuenciaMaquina secuencia : solucion.secuenciasMaquinas()) {

                        SecuenciaMaquina secuenciaObjetivo = secuenciasObjetivo.get(
                                        secuencia.idMaquina());

                        if (!secuencia.operaciones().equals(
                                        secuenciaObjetivo.operaciones())) {

                                maquinasDiferentes.add(
                                                secuencia.idMaquina());
                        }
                }

                int idMaquina = maquinasDiferentes.get(
                                generadorAleatorio.nextInt(
                                                maquinasDiferentes.size()));

                return acercarEnMaquina(
                                solucion,
                                objetivo,
                                idMaquina);
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
                        SolucionJssp segunda) {

                if (primera == null || segunda == null) {
                        throw new ExcepcionAplicacion(
                                        "Las soluciones JSSP no pueden ser nulas.");
                }

                Map<Integer, SecuenciaMaquina> secuenciasSegunda = new HashMap<>();

                for (SecuenciaMaquina secuencia : segunda.secuenciasMaquinas()) {

                        secuenciasSegunda.put(
                                        secuencia.idMaquina(),
                                        secuencia);
                }

                int distanciaTotal = 0;

                for (SecuenciaMaquina secuenciaPrimera : primera.secuenciasMaquinas()) {

                        SecuenciaMaquina secuenciaSegunda = secuenciasSegunda.get(
                                        secuenciaPrimera.idMaquina());

                        if (secuenciaSegunda == null) {
                                throw new ExcepcionAplicacion(
                                                "Las soluciones no contienen "
                                                                + "las mismas máquinas.");
                        }

                        distanciaTotal += distanciaSecuencias(
                                        secuenciaPrimera,
                                        secuenciaSegunda);
                }

                return distanciaTotal;
        }

        private void intercambiarDosOperaciones(
                        ArrayList<Operacion> operaciones) {

                int primeraPosicion = generadorAleatorio.nextInt(
                                operaciones.size());

                int segundaPosicion = generadorAleatorio.nextInt(
                                operaciones.size() - 1);

                if (segundaPosicion >= primeraPosicion) {
                        segundaPosicion++;
                }

                Collections.swap(
                                operaciones,
                                primeraPosicion,
                                segundaPosicion);
        }

        private int distanciaSecuencias(
                        SecuenciaMaquina primera,
                        SecuenciaMaquina segunda) {

                if (primera.operaciones().size() != segunda.operaciones().size()) {

                        throw new ExcepcionAplicacion(
                                        "Las secuencias de una misma máquina "
                                                        + "deben contener el mismo número de operaciones.");
                }

                Map<Operacion, Integer> posicionObjetivo = new HashMap<>();

                int posicion = 0;

                for (Operacion operacion : segunda.operaciones()) {

                        posicionObjetivo.put(
                                        operacion,
                                        posicion);

                        posicion++;
                }

                int[] permutacion = new int[primera.operaciones().size()];

                int indice = 0;

                for (Operacion operacion : primera.operaciones()) {

                        Integer posicionDestino = posicionObjetivo.get(operacion);

                        if (posicionDestino == null) {
                                throw new ExcepcionAplicacion(
                                                "Las secuencias no contienen "
                                                                + "las mismas operaciones.");
                        }

                        permutacion[indice] = posicionDestino;

                        indice++;
                }

                boolean[] visitado = new boolean[permutacion.length];

                int ciclos = 0;

                for (int i = 0; i < permutacion.length; i++) {

                        if (visitado[i]) {
                                continue;
                        }

                        ciclos++;

                        int actual = i;

                        while (!visitado[actual]) {

                                visitado[actual] = true;

                                actual = permutacion[actual];
                        }
                }

                return permutacion.length - ciclos;
        }

        private SolucionJssp acercarEnMaquina(
                        SolucionJssp solucion,
                        SolucionJssp objetivo,
                        int idMaquina) {

                SecuenciaMaquina secuenciaActual = null;
                SecuenciaMaquina secuenciaObjetivo = null;

                for (SecuenciaMaquina secuencia : solucion.secuenciasMaquinas()) {

                        if (secuencia.idMaquina() == idMaquina) {
                                secuenciaActual = secuencia;
                                break;
                        }
                }

                for (SecuenciaMaquina secuencia : objetivo.secuenciasMaquinas()) {

                        if (secuencia.idMaquina() == idMaquina) {
                                secuenciaObjetivo = secuencia;
                                break;
                        }
                }

                ArrayList<Operacion> operacionesActuales = new ArrayList<>(
                                secuenciaActual.operaciones());

                ArrayList<Operacion> operacionesObjetivo = new ArrayList<>(
                                secuenciaObjetivo.operaciones());

                ArrayList<Integer> posicionesDiferentes = new ArrayList<>();

                for (int posicion = 0; posicion < operacionesActuales.size(); posicion++) {

                        if (!operacionesActuales.get(posicion)
                                        .equals(
                                                        operacionesObjetivo.get(posicion))) {

                                posicionesDiferentes.add(
                                                posicion);
                        }
                }

                int primeraPosicion = posicionesDiferentes.get(
                                generadorAleatorio.nextInt(
                                                posicionesDiferentes.size()));

                Operacion operacionObjetivo = operacionesObjetivo.get(
                                primeraPosicion);

                Map<Operacion, Integer> posicionesActuales = new HashMap<>();

                for (int posicion = 0; posicion < operacionesActuales.size(); posicion++) {

                        posicionesActuales.put(
                                        operacionesActuales.get(posicion),
                                        posicion);
                }

                int segundaPosicion = posicionesActuales.get(
                                operacionObjetivo);

                Collections.swap(
                                operacionesActuales,
                                primeraPosicion,
                                segundaPosicion);

                ArrayList<SecuenciaMaquina> nuevasSecuencias = new ArrayList<>();

                for (SecuenciaMaquina secuencia : solucion.secuenciasMaquinas()) {

                        if (secuencia.idMaquina() == idMaquina) {

                                nuevasSecuencias.add(
                                                new SecuenciaMaquina(
                                                                idMaquina,
                                                                operacionesActuales));

                        } else {

                                nuevasSecuencias.add(
                                                new SecuenciaMaquina(
                                                                secuencia.idMaquina(),
                                                                new ArrayList<>(
                                                                                secuencia.operaciones())));
                        }
                }

                return new SolucionJssp(
                                nuevasSecuencias);
        }
}