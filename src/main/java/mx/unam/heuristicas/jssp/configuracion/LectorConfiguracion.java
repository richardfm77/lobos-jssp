package mx.unam.heuristicas.jssp.configuracion;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.wsa.ParametrosWsa;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Se encarga de leer la configuración de la aplicación
 * desde un archivo properties externo.
 */
public final class LectorConfiguracion {

    private final Properties propiedades;

    private LectorConfiguracion(
            Properties propiedades
    ) {
        this.propiedades = propiedades;
    }

    /**
     * Lee un archivo properties de configuración.
     *
     * @param rutaArchivo ruta del archivo properties
     * @return lector de configuración
     * @throws ExcepcionAplicacion si el archivo no puede leerse
     */
    public static LectorConfiguracion leer(
            Path rutaArchivo
    ) {

        if (rutaArchivo == null) {
            throw new ExcepcionAplicacion(
                    "La ruta del archivo de configuración no puede ser nula."
            );
        }

        Properties propiedades =
                new Properties();

        try (InputStream entrada =
                     Files.newInputStream(
                             rutaArchivo
                     )) {

            propiedades.load(
                    entrada
            );

        } catch (IOException e) {

            throw new ExcepcionAplicacion(
                    "No se pudo leer el archivo de configuración: "
                            + rutaArchivo,
                    e
            );
        }

        return new LectorConfiguracion(
                propiedades
        );
    }

    /**
     * Obtiene la ruta completa del archivo que contiene
     * la instancia JSSP.
     *
     * @return ruta completa de la instancia
     */
    public Path obtenerRutaInstancia() {

        String archivo =
                obtenerPropiedad(
                        "instancia.archivo"
                );

        return Path.of(
                archivo
        );
    }

    /**
     * Construye los parámetros del algoritmo WSA a partir
     * de las propiedades configuradas.
     *
     * @return parámetros WSA
     */
    public ParametrosWsa obtenerParametrosWsa() {

        return new ParametrosWsa(
                obtenerEntero(
                        "wsa.tamanoPoblacion"
                ),
                obtenerEntero(
                        "wsa.numeroIteraciones"
                ),
                obtenerEntero(
                        "wsa.numeroIteracionesGeneracionPoblacion"
                ),
                obtenerEntero(
                        "wsa.rangoVisual"
                ),
                obtenerDouble(
                        "wsa.probabilidadAmenaza"
                ),
                obtenerLong(
                        "wsa.semilla"
                ),
                obtenerEntero(
                        "wsa.maxIntentosEscape"
                ),
                obtenerEntero(
                        "wsa.tamanoMemoria"
                ),
                obtenerEntero(
                        "wsa.maxIntentosMovimiento"
                )
        );
    }

    private String obtenerPropiedad(
            String nombre
    ) {

        String valor =
                propiedades.getProperty(
                        nombre
                );

        if (valor == null
                || valor.isBlank()) {

            throw new ExcepcionAplicacion(
                    "No se encontró la propiedad requerida: "
                            + nombre
            );
        }

        return valor.trim();
    }

    private int obtenerEntero(
            String nombre
    ) {

        String valor =
                obtenerPropiedad(
                        nombre
                );

        try {

            return Integer.parseInt(
                    valor
            );

        } catch (NumberFormatException e) {

            throw new ExcepcionAplicacion(
                    "La propiedad "
                            + nombre
                            + " debe contener un número entero.",
                    e
            );
        }
    }

    private long obtenerLong(
            String nombre
    ) {

        String valor =
                obtenerPropiedad(
                        nombre
                );

        try {

            return Long.parseLong(
                    valor
            );

        } catch (NumberFormatException e) {

            throw new ExcepcionAplicacion(
                    "La propiedad "
                            + nombre
                            + " debe contener un número entero largo.",
                    e
            );
        }
    }

    private double obtenerDouble(
            String nombre
    ) {

        String valor =
                obtenerPropiedad(
                        nombre
                );

        try {

            return Double.parseDouble(
                    valor
            );

        } catch (NumberFormatException e) {

            throw new ExcepcionAplicacion(
                    "La propiedad "
                            + nombre
                            + " debe contener un número decimal.",
                    e
            );
        }
    }
}