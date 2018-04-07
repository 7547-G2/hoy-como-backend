package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.api.service.BackofficeComercioSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api")
public class BackofficeComercioSessionController {

    private BackofficeComercioSessionService backofficeComercioSessionService;
    private ObjectMapper objectMapper;

    @Autowired
    public BackofficeComercioSessionController(BackofficeComercioSessionService backofficeComercioSessionService, ObjectMapper objectMapper) {
        this.backofficeComercioSessionService = backofficeComercioSessionService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/backofficeComercio/session", produces = {"application/json"})
    public String session(@RequestBody BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        String response;
        Optional<TokenDto> tokenOptional = backofficeComercioSessionService.getTokenFromSession(backofficeComercioSessionDto);

        if (tokenOptional.isPresent()) {
            TokenDto tokenDto = tokenOptional.get();
            response = objectMapper.writeValueAsString(tokenDto);
        } else {
            response = objectMapper.writeValueAsString("Datos incorrectos");
        }

        return response;
    }

}
