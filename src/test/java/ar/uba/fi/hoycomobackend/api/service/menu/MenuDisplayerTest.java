package ar.uba.fi.hoycomobackend.api.service.menu;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.entity.PlatoState;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class MenuDisplayerTest {

    private MenuDisplayer menuDisplayer = new MenuDisplayer();

    @Test
    public void getMenuFromComercio() {
        Comercio comercio = createTestComercio();
        ResponseEntity response = menuDisplayer.getMenuFromComercio(comercio);
        List<Menu> menuList = (List<Menu>) response.getBody();

        assertThat(menuList).hasSize(2);
    }

    private Comercio createTestComercio() {
        Comercio comercio = new Comercio();
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