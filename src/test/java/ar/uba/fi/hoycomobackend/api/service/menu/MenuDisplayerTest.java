package ar.uba.fi.hoycomobackend.api.service.menu;

import ar.uba.fi.hoycomobackend.api.dto.MenuDto;
import ar.uba.fi.hoycomobackend.api.dto.MenuWithLogoDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.entity.PlatoState;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import ar.uba.fi.hoycomobackend.database.repository.CommentRepository;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultCategoriaComida;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


public class MenuDisplayerTest {

    private CategoriaComidaRepository categoriaComidaRepository = Mockito.mock(CategoriaComidaRepository.class);
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private MobileUserRepository mobileUserRepository = Mockito.mock(MobileUserRepository.class);
    private MenuDisplayer menuDisplayer = new MenuDisplayer(categoriaComidaRepository, commentRepository, mobileUserRepository);

    @Test
    public void getMenuFromComercio() {
        Mockito.when(categoriaComidaRepository.findById(any())).thenReturn(Optional.of(createDefaultCategoriaComida()));
        Comercio comercio = createTestComercio();
        ResponseEntity response = menuDisplayer.getMenuFromComercio(comercio);
        MenuWithLogoDto menuWithLogoDto = (MenuWithLogoDto) response.getBody();

        assertThat(menuWithLogoDto.getImagen_comercio()).isEqualTo("imagenComercio");
        List<MenuDto> menu = menuWithLogoDto.getMenu();
        assertThat(menu).hasSize(2);
        assertThat(menu.get(0).getNombre_categ()).isEqualTo("tipo");
    }

    private Comercio createTestComercio() {
        Comercio comercio = new Comercio();
        comercio.setImagenLogo("imagenLogo");
        comercio.setImagenComercio("imagenComercio");
        Set<Plato> platoSet = createTestPlatoSet();
        comercio.setPlatos(platoSet);

        return comercio;
    }

    private Set<Plato> createTestPlatoSet() {
        Set<Plato> platoSet = new HashSet<>();
        Plato firstPlato = new Plato();
        firstPlato.setState(PlatoState.ACTIVO);
        firstPlato.setPrecio(1.0f);
        firstPlato.setNombre("nombre");
        firstPlato.setImagen("imagen");
        firstPlato.setCategoria(1L);
        firstPlato.setOrden(1);
        Plato secondPlato = new Plato();
        secondPlato.setState(PlatoState.ACTIVO);
        secondPlato.setPrecio(2.0f);
        secondPlato.setNombre("nombreSecond");
        secondPlato.setImagen("imagenSecond");
        secondPlato.setCategoria(2L);
        secondPlato.setOrden(1);
        platoSet.add(firstPlato);
        platoSet.add(secondPlato);

        return platoSet;
    }
}