package mx.unam.heuristicas.jssp.problema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.SecuenciaMaquina;
import mx.unam.heuristicas.jssp.problema.modelo.SolucionJssp;

public final class EscritorSolucionJssp {

    private EscritorSolucionJssp() {
    }

    /**
     * Escribe una solución JSSP en un archivo.
     *
     * Cada línea representa la secuencia de operaciones
     * correspondiente a una máquina.
     *
     * Cada operación se representa mediante:
     *
     * [trabajo, operacion, tiempo]
     *
     * @param solucion    solución que se desea escribir
     * @param rutaArchivo ruta del archivo de salida
     */
    public static void escribir(
            SolucionJssp solucion,
            Path rutaArchivo) {

        if (solucion == null) {
            throw new ExcepcionAplicacion(
                    "La solución JSSP no puede ser nula.");
        }

        if (rutaArchivo == null) {
            throw new ExcepcionAplicacion(
                    "La ruta del archivo de salida no puede ser nula.");
        }

        String contenido = convertirATexto(
                solucion);

        try {
                Path rutaAbsoluta = rutaArchivo.toAbsolutePath();

                Path directorio = rutaAbsoluta.getParent();

                if (directorio != null
                                && Files.notExists(directorio)) {

                        Files.createDirectories(
                                        directorio);
                }

                Files.writeString(
                                rutaAbsoluta,
                                contenido);

        } catch (IOException e) {

                throw new ExcepcionAplicacion(
                                "No se pudo escribir el archivo de solución: "
                                                + rutaArchivo,
                                e);
        }
    }

    /**
     * Convierte una solución JSSP al formato textual
     * utilizado para los archivos de salida.
     *
     * @param solucion solución JSSP
     * @return representación textual de la solución
     */
    public static String convertirATexto(
            SolucionJssp solucion) {

        if (solucion == null) {
            throw new ExcepcionAplicacion(
                    "La solución JSSP no puede ser nula.");
        }

        StringBuilder resultado = new StringBuilder();

        boolean primeraMaquina = true;

        for (SecuenciaMaquina secuencia : solucion.secuenciasMaquinas()) {

            if (!primeraMaquina) {
                resultado.append(
                        System.lineSeparator());
            }

            resultado.append("[ ");

            for (Operacion operacion : secuencia.operaciones()) {

                resultado
                        .append("[")
                        .append(
                                operacion.idTrabajo())
                        .append(", ")
                        .append(
                                operacion.indiceOperacion())
                        .append(", ")
                        .append(
                                operacion.tiempoProceso())
                        .append("], ");
            }

            resultado.append("]");

            primeraMaquina = false;
        }

        return resultado.toString();
    }
}