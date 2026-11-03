package mx.unam.heuristicas.jssp.wsa;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class RegistroEjecucionWsa
        implements AutoCloseable {

    private final BufferedWriter escritor;
    private final boolean mostrarConsola;
    private final int frecuenciaIteraciones;

    public RegistroEjecucionWsa(
            String nombreReporte,
            Path directorioSalida,
            boolean mostrarConsola,
            int frecuenciaIteraciones) {

        if (nombreReporte == null || nombreReporte.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del reporte no puede ser nulo o vacío.");
        }

        if (directorioSalida == null) {
            throw new IllegalArgumentException(
                    "El directorio de salida no puede ser nulo.");
        }

        if (frecuenciaIteraciones <= 0) {
            throw new IllegalArgumentException(
                    "La frecuencia de registro debe ser mayor que cero.");
        }

        this.mostrarConsola = mostrarConsola;

        this.frecuenciaIteraciones = frecuenciaIteraciones;

        try {

            Files.createDirectories(
                    directorioSalida);

            Path archivoReporte = directorioSalida.resolve(
                    nombreReporte);

            this.escritor = Files.newBufferedWriter(
                    archivoReporte,
                    StandardCharsets.UTF_8);

        } catch (IOException e) {

            throw new ExcepcionAplicacion(
                    "No se pudo crear el reporte de ejecución.",
                    e);
        }
    }

    /**
     * Registra el costo de la solución inicial.
     */
    public void registrarInicial(
            double costo) {

        registrarEvaluacion(
                costo);

        if (mostrarConsola) {

            System.out.printf(
                    Locale.US,
                    "[INICIAL] Costo: %.10f%n",
                    costo);
        }
    }

    /**
     * Registra una solución que fue aceptada por un lobo.
     */
    public void registrarAceptacion(
            int iteracion,
            int indiceLobo,
            String accion,
            double costoAnterior,
            double costoNuevo) {

        registrarEvaluacion(
                costoNuevo);

        if (!mostrarConsola) {
            return;
        }

        System.out.printf(
                Locale.US,
                "[Iteración %d][Lobo %d][%s] "
                        + "%.10f -> %.10f ACEPTADA%n",
                iteracion,
                indiceLobo,
                accion,
                costoAnterior,
                costoNuevo);
    }

    /**
     * Informa periódicamente el avance de la heurística.
     */
    public void registrarProgreso(
            int iteracion,
            int totalIteraciones,
            double mejorCosto) {

        if (!mostrarConsola) {
            return;
        }

        if (iteracion % frecuenciaIteraciones != 0
                && iteracion != totalIteraciones) {

            return;
        }

        System.out.printf(
                Locale.US,
                "[Iteración %d/%d] Mejor costo: %.10f%n",
                iteracion,
                totalIteraciones,
                mejorCosto);
    }

    /**
     * Informa cuando se encuentra un nuevo mejor global.
     */
    public void registrarNuevoMejor(
            int iteracion,
            double costo) {

        if (!mostrarConsola) {
            return;
        }

        System.out.printf(
                Locale.US,
                "[NUEVO MEJOR][Iteración %d] %.10f%n",
                iteracion,
                costo);
    }

    private void registrarEvaluacion(
            double costo) {

        try {

            escritor.write(
                    String.format(
                            Locale.US,
                            "E:%.10f",
                            costo));

            escritor.newLine();

        } catch (IOException e) {

            throw new ExcepcionAplicacion(
                    "No se pudo escribir en el reporte WSA.",
                    e);
        }
    }

    @Override
    public void close() {

        try {

            escritor.flush();
            escritor.close();

        } catch (IOException e) {

            throw new ExcepcionAplicacion(
                    "No se pudo cerrar el reporte WSA.",
                    e);
        }
    }
}