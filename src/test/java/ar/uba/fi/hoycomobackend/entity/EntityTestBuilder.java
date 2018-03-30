package ar.uba.fi.hoycomobackend.entity;

public class EntityTestBuilder {

    public static Comercio createComercio(String nombre) {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);

        return comercio;
    }
}
