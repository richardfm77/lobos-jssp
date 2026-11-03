package mx.unam.heuristicas.jssp;

import mx.unam.heuristicas.jssp.configuracion.LectorConfiguracion;
import mx.unam.heuristicas.jssp.estructura.GraficaDirigida;
import mx.unam.heuristicas.jssp.problema.ConstructorGraficaJssp;
import mx.unam.heuristicas.jssp.problema.EscritorSolucionJssp;
import mx.unam.heuristicas.jssp.problema.EvaluadorGraficaJssp;
import mx.unam.heuristicas.jssp.problema.GeneradorSolucionJssp;
import mx.unam.heuristicas.jssp.problema.LectorInstanciaJssp;
import mx.unam.heuristicas.jssp.problema.ProblemaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.ResultadoEvaluacionJssp;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;
import mx.unam.heuristicas.jssp.wsa.AlgoritmoWsa;
import mx.unam.heuristicas.jssp.wsa.ParametrosWsa;
import mx.unam.heuristicas.jssp.wsa.ResultadoWsa;

import java.nio.file.Path;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Punto de entrada de la aplicación.
 */
public final class App {

        private App() {
        }

        public static void main(String[] args) {

                if (args.length != 1) {

                        System.err.println(
                                        "Uso: java -jar jssp.jar <archivo.properties>");

                        return;
                }

                /*
                 * Lectura de configuración.
                 */
                Path rutaConfiguracion = Path.of(
                                args[0]);

                LectorConfiguracion configuracion = LectorConfiguracion.leer(
                                rutaConfiguracion);

                Path rutaInstancia = configuracion
                                .obtenerRutaInstancia();

                Path rutaSalida = configuracion
                                .obtenerRutaSalida();

                ParametrosWsa parametros = configuracion
                                .obtenerParametrosWsa();

                /*
                 * Lectura de la instancia.
                 */
                InstanciaJssp instancia = LectorInstanciaJssp.leer(
                                rutaInstancia);

                /*
                 * Un único generador aleatorio maestro
                 * para toda la corrida.
                 */
                RandomGenerator generadorAleatorio = new Random(
                                parametros.semilla());

                /*
                 * Construcción del problema.
                 */
                ProblemaJssp problema = new ProblemaJssp(
                                instancia,
                                generadorAleatorio);

                /*
                 * Construcción del algoritmo WSA.
                 */
                AlgoritmoWsa<SolucionJssp> algoritmo = new AlgoritmoWsa<>(
                                problema,
                                parametros,
                                generadorAleatorio);

                /*
                 * Generación de la solución inicial.
                 */
                SolucionJssp solucionInicial = GeneradorSolucionJssp
                                .generarSolucionInicialPorCapas(
                                                instancia);

                double costoInicial = problema.funcionCosto(
                                solucionInicial);

                ResultadoEvaluacionJssp evaluacionInicial = evaluarSolucion(
                                instancia,
                                solucionInicial);

                /*
                 * Información inicial de la corrida.
                 */
                imprimirConfiguracion(
                                rutaConfiguracion,
                                rutaInstancia,
                                rutaSalida,
                                instancia,
                                parametros);

                imprimirResultado(
                                "SOLUCIÓN INICIAL",
                                costoInicial,
                                evaluacionInicial);

                /*
                 * Ejecución de WSA.
                 */
                System.out.println();
                System.out.println(
                                "Ejecutando Wolf Search Algorithm...");
                System.out.println();

                long inicio = System.nanoTime();

                ResultadoWsa<SolucionJssp> resultado = algoritmo.ejecutar(
                                solucionInicial);

                long fin = System.nanoTime();

                double tiempoSegundos = (fin - inicio)
                                / 1_000_000_000.0;

                /*
                 * Mejor solución encontrada.
                 */
                SolucionJssp mejorSolucion = resultado.mejorSolucion();

                ResultadoEvaluacionJssp evaluacionFinal = evaluarSolucion(
                                instancia,
                                mejorSolucion);

                /*
                 * Escritura de la solución.
                 */
                EscritorSolucionJssp.escribir(
                                mejorSolucion,
                                rutaSalida);

                /*
                 * Resultados finales.
                 */
                imprimirResultado(
                                "MEJOR SOLUCIÓN ENCONTRADA",
                                resultado.mejorCosto(),
                                evaluacionFinal);

                System.out.printf(
                                "Tiempo de ejecución: %.3f segundos%n",
                                tiempoSegundos);

                System.out.println();

                System.out.println(
                                "Archivo de solución generado en:");

                System.out.println(
                                rutaSalida
                                                .toAbsolutePath()
                                                .normalize());

                System.out.println();

                System.out.println(
                                "============================================================");
        }

        /**
         * Evalúa una solución JSSP mediante su gráfica dirigida.
         *
         * @param instancia instancia del problema
         * @param solucion  solución a evaluar
         * @return resultado de la evaluación
         */
        private static ResultadoEvaluacionJssp evaluarSolucion(
                        InstanciaJssp instancia,
                        SolucionJssp solucion) {

                GraficaDirigida<Operacion> grafica = ConstructorGraficaJssp.construir(
                                instancia,
                                solucion);

                return EvaluadorGraficaJssp.evaluar(
                                grafica);
        }

        /**
         * Imprime la configuración utilizada para la corrida.
         */
        private static void imprimirConfiguracion(
                        Path rutaConfiguracion,
                        Path rutaInstancia,
                        Path rutaSalida,
                        InstanciaJssp instancia,
                        ParametrosWsa parametros) {

                System.out.println();
                System.out.println(
                                "============================================================");

                System.out.println(
                                "                  WSA - JSSP");

                System.out.println(
                                "============================================================");

                System.out.println();

                System.out.println(
                                "CONFIGURACIÓN");

                System.out.println(
                                "------------------------------------------------------------");

                System.out.println(
                                "Archivo properties: "
                                                + rutaConfiguracion
                                                                .toAbsolutePath()
                                                                .normalize());

                System.out.println(
                                "Instancia: "
                                                + rutaInstancia
                                                                .toAbsolutePath()
                                                                .normalize());

                System.out.println(
                                "Archivo de salida: "
                                                + rutaSalida
                                                                .toAbsolutePath()
                                                                .normalize());

                System.out.println();

                System.out.println(
                                "Trabajos: "
                                                + instancia.numeroTrabajos());

                System.out.println(
                                "Máquinas: "
                                                + instancia.numeroMaquinas());

                System.out.println(
                                "Cota inferior: "
                                                + instancia.cotaInferior());

                System.out.println(
                                "Cota superior: "
                                                + instancia.cotaSuperior());

                System.out.println();

                System.out.println(
                                "PARÁMETROS WSA");

                System.out.println(
                                "------------------------------------------------------------");

                System.out.println(
                                "Tamaño de población: "
                                                + parametros.tamanoPoblacion());

                System.out.println(
                                "Número de iteraciones: "
                                                + parametros.numeroIteraciones());

                System.out.println(
                                "Iteraciones generación población: "
                                                + parametros
                                                                .numeroIteracionesGeneracionPoblacion());

                System.out.println(
                                "Rango visual: "
                                                + parametros.rangoVisual());

                System.out.println(
                                "Probabilidad de amenaza: "
                                                + parametros.probabilidadAmenaza());

                System.out.println(
                                "Semilla: "
                                                + parametros.semilla());

                System.out.println(
                                "Máximos intentos de escape: "
                                                + parametros.maxIntentosEscape());

                System.out.println(
                                "Tamaño de memoria efímera: "
                                                + parametros.tamanoMemoria());

                System.out.println(
                                "Máximos intentos de movimiento: "
                                                + parametros.maxIntentosMovimiento());
        }

        /**
         * Imprime el resultado de evaluar una solución.
         */
        private static void imprimirResultado(
                        String titulo,
                        double costo,
                        ResultadoEvaluacionJssp evaluacion) {

                System.out.println();
                System.out.println(
                                "------------------------------------------------------------");

                System.out.println(
                                titulo);

                System.out.println(
                                "------------------------------------------------------------");

                System.out.println(
                                "Costo: "
                                                + costo);

                System.out.println(
                                "Makespan: "
                                                + evaluacion.makespan());

                System.out.println(
                                "Vértices en ciclo: "
                                                + evaluacion.verticesEnCiclo());
        }
}