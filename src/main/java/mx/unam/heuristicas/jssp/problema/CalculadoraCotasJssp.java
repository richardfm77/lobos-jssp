package mx.unam.heuristicas.jssp.problema;

import mx.unam.heuristicas.jssp.excepcion.ExcepcionAplicacion;
import mx.unam.heuristicas.jssp.problema.modelo.CotasJssp;
import mx.unam.heuristicas.jssp.problema.modelo.InstanciaJssp;
import mx.unam.heuristicas.jssp.problema.modelo.Operacion;
import mx.unam.heuristicas.jssp.problema.modelo.Trabajo;

public final class CalculadoraCotasJssp {

    private CalculadoraCotasJssp() {
    }

    public static CotasJssp calcular(
            InstanciaJssp instancia) {

        if (instancia == null) {
            throw new ExcepcionAplicacion(
                    "La instancia JSSP no puede ser nula.");
        }

        return new CotasJssp(
                calcularCotaSuperior(instancia),
                calcularCotaInferior(instancia));
    }

    public static int calcularCotaInferior(
            InstanciaJssp instancia) {

        if (instancia == null) {
            throw new ExcepcionAplicacion(
                    "La instancia JSSP no puede ser nula.");
        }

        int mayorTiempoTrabajo = 0;

        int[] cargasMaquinas = new int[instancia.numeroMaquinas()];

        for (Trabajo trabajo : instancia.trabajos()) {

            int tiempoTrabajo = 0;

            for (Operacion operacion : trabajo.operaciones()) {

                tiempoTrabajo += operacion.tiempoProceso();

                cargasMaquinas[operacion.idMaquina()] += operacion.tiempoProceso();
            }

            mayorTiempoTrabajo = Math.max(
                    mayorTiempoTrabajo,
                    tiempoTrabajo);
        }

        int mayorCargaMaquina = 0;

        for (int carga : cargasMaquinas) {
            mayorCargaMaquina = Math.max(
                    mayorCargaMaquina,
                    carga);
        }

        return Math.max(
                mayorTiempoTrabajo,
                mayorCargaMaquina);
    }

    public static int calcularCotaSuperior(
            InstanciaJssp instancia) {

        if (instancia == null) {
            throw new ExcepcionAplicacion(
                    "La instancia JSSP no puede ser nula.");
        }

        int cotaSuperior = 0;

        for (Trabajo trabajo : instancia.trabajos()) {

            for (Operacion operacion : trabajo.operaciones()) {

                cotaSuperior += operacion.tiempoProceso();
            }
        }

        return cotaSuperior;
    }
}