package ar.uba.fi.hoycomobackend.entity;

import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.entity.usuario.Usuario;

public class EntityTestBuilder {

    public static Comercio createComercio(String nombre) {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);

        return comercio;
    }

    public static Usuario createUsuario(String nombre, String apellido, String mail) {
        Usuario usuario = new Usuario();
        usuario.setDeactivated(false);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(mail);

        return usuario;
    }
}
