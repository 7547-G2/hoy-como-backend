package ar.uba.fi.hoycomobackend.service;

import ar.uba.fi.hoycomobackend.entity.usuario.Usuario;
import ar.uba.fi.hoycomobackend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    public static Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getAllUsuarios() {
        LOGGER.info("Getting all Usuarios from database.");
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        LOGGER.info("Getting Usuario by id: {}", id);
        return usuarioRepository.getUsuarioById(id);
    }

    public void addUsuario(Usuario usuario) {
        LOGGER.info("Adding new Usuario: {}", usuario);
        usuarioRepository.saveAndFlush(usuario);
    }
}
