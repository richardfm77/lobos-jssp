package mx.unam.heuristicas.jssp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.net.URL;

import mx.unam.heuristicas.jssp.problema.LectorInstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;

public class UtilTest {

    public static InstanciaJssp obtenerInstancia(String nombreArchivo) {

        Path archivo = obtenerRutaRecurso(
                "instancias/" + nombreArchivo);

        return LectorInstanciaJssp.leer(
                archivo);
    }

    public static Path obtenerRutaRecurso(String nombreRecurso) {

        URL recurso = UtilTest.class
                .getClassLoader()
                .getResource(nombreRecurso);

        assertNotNull(recurso);

        try {
            return Path.of(
                    recurso.toURI());
        } catch (URISyntaxException e) {

            fail(
                    "No se pudo obtener la ruta del recurso: "
                            + nombreRecurso);

            return null;
        }
    }

}