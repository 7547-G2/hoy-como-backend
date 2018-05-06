package ar.uba.fi.hoycomobackend.api.businesslogic;

public enum ComercioEstado {
    PENDIENTE_ACTIVACION("pendiente activacion"),
    PENDIENTE_MENU("pendiente menu"),
    HABILITADO("habilitado"),
    DESHABILITADO("deshabilitado");

    private final String estado;

    ComercioEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return this.estado;
    }
}
