package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.PasswordUpdateDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.database.repository.TipoComidaRepository;
import ar.uba.fi.hoycomobackend.entity.DatabaseFiller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultPasswordUpdateDto;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("localprod")
public class ComercioServiceTest {

    @Autowired
    private ComercioRepository comercioRepository;
    @Autowired
    private TipoComidaRepository tipoComidaRepository;
    @Autowired
    private ComercioService comercioService;

    @Test
    public void updatePassword() {
        DatabaseFiller.createDefaultComercioInDatabase(comercioRepository, tipoComidaRepository);
        PasswordUpdateDto passwordUpdateDto = createDefaultPasswordUpdateDto();
        comercioService.updatePassword(passwordUpdateDto);
        Comercio comercio = comercioRepository.findAll().get(0);

        assertThat(comercio.getPassword()).isEqualTo("newPassword");
        assertThat(comercio.getEstado()).isEqualTo("pendiente menu");
    }
}