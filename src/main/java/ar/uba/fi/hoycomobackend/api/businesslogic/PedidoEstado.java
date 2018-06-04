package ar.uba.fi.hoycomobackend.api.businesslogic;

public enum PedidoEstado {
    INGRESADO("Ingresado"),
    EN_PREPARACION("EnPreparacion"),
    DESPACHADO("Despachado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String estado;

    PedidoEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return this.estado;
    }
}
