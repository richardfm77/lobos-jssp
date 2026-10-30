package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.CotasJssp;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.Trabajo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Se encarga de leer archivos que representan instancias del
 * Problema de Programación de Trabajos en Taller (JSSP).
 */
public final class LectorInstanciaJssp {

    private LectorInstanciaJssp() {
    }

    /**
     * Lee una instancia JSSP desde un archivo.
     *
     * El formato esperado es:
     *
     * n m
     * maquina tiempo maquina tiempo ...
     *
     * @param rutaArchivo ruta del archivo que contiene la instancia
     * @return instancia JSSP construida a partir del archivo
     * @throws ExcepcionAplicacion si el archivo no puede leerse
     *                             o contiene una instancia inválida
     */
    public static InstanciaJssp leer(Path rutaArchivo) throws ExcepcionAplicacion {

        if (rutaArchivo == null) {
            throw new ExcepcionAplicacion(
                    "La ruta del archivo no puede ser nula.");
        }

        final List<String> lineas;

        try {
            lineas = Files.readAllLines(rutaArchivo)
                    .stream()
                    .filter(linea -> !linea.isBlank())
                    .toList();

        } catch (IOException e) {
            throw new ExcepcionAplicacion(
                    "No se pudo leer el archivo de la instancia: "
                            + rutaArchivo,
                    e);
        }

        if (lineas.isEmpty()) {
            throw new ExcepcionAplicacion(
                    "El archivo de la instancia está vacío.");
        }

        String[] encabezado = lineas.get(0)
                .trim()
                .split("\\s+");

        if (encabezado.length != 2) {
            throw new ExcepcionAplicacion(
                    "El encabezado debe contener exactamente: n m.");
        }

        final int numeroTrabajos;
        final int numeroMaquinas;

        try {
            numeroTrabajos = Integer.parseInt(encabezado[0]);
            numeroMaquinas = Integer.parseInt(encabezado[1]);
        } catch (NumberFormatException e) {
            throw new ExcepcionAplicacion(
                    "El encabezado contiene valores que no son enteros.",
                    e);
        }

        if (numeroTrabajos <= 0) {
            throw new ExcepcionAplicacion(
                    "El número de trabajos debe ser mayor que cero.");
        }

        if (numeroMaquinas <= 0) {
            throw new ExcepcionAplicacion(
                    "El número de máquinas debe ser mayor que cero.");
        }

        if (lineas.size() - 1 != numeroTrabajos) {
            throw new ExcepcionAplicacion(
                    "El número de trabajos indicado en el encabezado "
                            + "no coincide con el número de filas del archivo.");
        }

        List<Trabajo> trabajos = new ArrayList<>();

        for (int idTrabajo = 0; idTrabajo < numeroTrabajos; idTrabajo++) {

            String[] valores = lineas.get(idTrabajo + 1)
                    .trim()
                    .split("\\s+");

            int valoresEsperados = numeroMaquinas * 2;

            if (valores.length != valoresEsperados) {
                throw new ExcepcionAplicacion(
                        "El trabajo " + idTrabajo
                                + " debe contener exactamente "
                                + numeroMaquinas
                                + " operaciones.");
            }

            List<Operacion> operaciones = new ArrayList<>();

            for (int indiceOperacion = 0; indiceOperacion < numeroMaquinas; indiceOperacion++) {

                int posicion = indiceOperacion * 2;

                final int idMaquina;
                final int tiempoProceso;

                try {
                    idMaquina = Integer.parseInt(valores[posicion]);

                    tiempoProceso = Integer.parseInt(valores[posicion + 1]);

                } catch (NumberFormatException e) {
                    throw new ExcepcionAplicacion(
                            "El trabajo " + idTrabajo
                                    + " contiene un valor que no es entero.",
                            e);
                }

                if (idMaquina < 0
                        || idMaquina >= numeroMaquinas) {

                    throw new ExcepcionAplicacion(
                            "La máquina " + idMaquina
                                    + " del trabajo " + idTrabajo
                                    + " está fuera del rango permitido.");
                }

                if (tiempoProceso <= 0) {
                    throw new ExcepcionAplicacion(
                            "El tiempo de procesamiento de la operación "
                                    + indiceOperacion
                                    + " del trabajo "
                                    + idTrabajo
                                    + " debe ser mayor que cero.");
                }

                operaciones.add(
                        new Operacion(
                                idTrabajo,
                                indiceOperacion,
                                idMaquina,
                                tiempoProceso));
            }

            trabajos.add(
                    new Trabajo(
                            idTrabajo,
                            operaciones));
        }
        
        InstanciaJssp instancia = new InstanciaJssp(
                numeroTrabajos,
                numeroMaquinas,
                0,
                0,
                trabajos);

        CotasJssp cotas = CalculadoraCotasJssp.calcular(instancia);

        return new InstanciaJssp(
                numeroTrabajos,
                numeroMaquinas,
                cotas.cotaSuperior(),
                cotas.cotaInferior(),
                trabajos);
    }
}