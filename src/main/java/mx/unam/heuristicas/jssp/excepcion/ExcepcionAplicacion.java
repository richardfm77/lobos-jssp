package mx.unam.heuristicas.jssp.excepcion;

/**
 * Excepción general para errores controlados de la aplicación.
 */
public class ExcepcionAplicacion extends RuntimeException {

    public ExcepcionAplicacion(String mensaje) {
        super(mensaje);
    }

    public ExcepcionAplicacion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}