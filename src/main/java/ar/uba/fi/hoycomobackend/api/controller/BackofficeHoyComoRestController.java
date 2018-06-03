package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.ChangeStateDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoAddDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.api.service.BackofficeHoyComoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BackofficeHoyComoRestController {

    private BackofficeHoyComoService backofficeHoyComoService;

    @Autowired
    public BackofficeHoyComoRestController(BackofficeHoyComoService backofficeHoyComoService) {
        this.backofficeHoyComoService = backofficeHoyComoService;
    }

    @GetMapping(value = {"/comercios"}, produces = {"application/json"})
    public List<ComercioHoyComoDto> getComercios(@RequestParam(value = "search", required = false) String search) {
        return backofficeHoyComoService.getComercios(search);
    }

    @PostMapping(value = "/comercios", consumes = {"application/json"})
    public ResponseEntity addComercio(@RequestBody ComercioHoyComoAddDto comercioHoyComoAddDto) {
        return backofficeHoyComoService.addComercio(comercioHoyComoAddDto);
    }

    @PutMapping(value = "/comercios/{comercioId}", consumes = {"application/json"})
    public ResponseEntity updateComercio(@PathVariable("comercioId") Long comercioId, @RequestBody ComercioHoyComoAddDto comercioHoyComoAddDto) {
        return backofficeHoyComoService.updateComercio(comercioId, comercioHoyComoAddDto);
    }

    @GetMapping(value = {"/comercios/mobileUsers"}, produces = {"application/json"})
    public ResponseEntity getMobileUsers(@RequestParam(value = "search", required = false) String search) {
        // Similar ?search=firstName:string,lastName:string, state:0
        //state 0 activado, 1 desactivado
        return backofficeHoyComoService.getUsuarios(search);
    }

    @PutMapping(value = {"/comercios/mobileUser/{mobileUserId}"})
    public ResponseEntity modifyStateOfUser(@PathVariable("mobileUserId") Long mobileUserId, @RequestBody ChangeStateDto changeStateDto) {
        return backofficeHoyComoService.changeStateOfUserById(mobileUserId, changeStateDto);
    }

    @PostMapping(value = "/comercios/tipoComida")
    public ResponseEntity createTipoComidaForComercio(@RequestBody String tipoComida) {
        return backofficeHoyComoService.createTipoComida(tipoComida);
    }

    @PostMapping(value = "/test")
    public ResponseEntity abc() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=-34.617499,-58.368442&destinations=-34.609370,-58.374888&key=AIzaSyC1_Pl3mGUTHBCGPY-HdHV2hg-OZfb3pWg";
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.getBody());
        String value = root.path("rows").get(0).path("elements").get(0).path("duration").get("text").asText();
        Integer totalTime = Integer.parseInt(value.split(" ")[0]);
        return ResponseEntity.ok(totalTime);
    }
}
