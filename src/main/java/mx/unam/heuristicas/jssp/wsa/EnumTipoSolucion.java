package mx.unam.heuristicas.jssp.wsa;

public enum EnumTipoSolucion {
    FACTIBLE,
    NORMAL,
    NO_FACTIBLE;

    public static EnumTipoSolucion fromString(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de solución no puede ser nulo.");
        }
        switch (tipo.trim().toUpperCase()) {
            case "FACTIBLE":
                return FACTIBLE;
            case "NORMAL":
                return NORMAL;
            case "NO_FACTIBLE":
                return NO_FACTIBLE;
            default:
                throw new IllegalArgumentException("Tipo de solución desconocido: " + tipo);
        }
    }
}