package ar.uba.fi.hoycomobackend.controller;

import ar.uba.fi.hoycomobackend.entity.usuario.Usuario;
import ar.uba.fi.hoycomobackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioRestController {

    private UsuarioService usuarioService;

    @Autowired
    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping(value = "/usuarios", produces = {"application/json"})
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping(value = "/usuarios/{id}", produces = {"application/json"})
    public Usuario getUsuarioById(@PathVariable("id") Long id) {
        return usuarioService.getUsuarioById(id);
    }

    @PostMapping(value = "/usuarios")
    public void addUsuario(@RequestBody Usuario usuario) {
        usuarioService.addUsuario(usuario);
    }
}
