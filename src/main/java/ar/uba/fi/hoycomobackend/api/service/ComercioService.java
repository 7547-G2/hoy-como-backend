package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.Message;
import ar.uba.fi.hoycomobackend.api.dto.MessageWithId;
import ar.uba.fi.hoycomobackend.api.dto.PasswordUpdateDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ComercioService {

    private static final String PENDIENTE_MENU = "pendiente menu";
    private ComercioQuery comercioQuery;

    @Autowired
    public ComercioService(ComercioQuery comercioQuery) {
        this.comercioQuery = comercioQuery;
    }

    public ResponseEntity updatePassword(PasswordUpdateDto passwordUpdateDto) {
        String email = passwordUpdateDto.getEmail();
        Optional<Comercio> comercioOptional = comercioQuery.getComercioByEmail(email);
        if (passwordsMatch(comercioOptional, passwordUpdateDto)) {
            Comercio comercio = comercioOptional.get();
            String newPassword = passwordUpdateDto.getNewPassword();
            comercio.setPassword(newPassword);
            comercio.setEstado(PENDIENTE_MENU);
            comercio = comercioQuery.saveAndFlush(comercio);

            return ResponseEntity.ok(new MessageWithId("Contraseña actualizada correctamente.", comercio.getId()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Email no encontrado o Hash no matchea."));
    }

    private boolean passwordsMatch(Optional<Comercio> comercioOptional, PasswordUpdateDto passwordUpdateDto) {
        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            return comercio.getPassword().equals(passwordUpdateDto.getOldPassword());
        }
        return false;
    }
}
