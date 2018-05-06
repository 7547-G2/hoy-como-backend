package ar.uba.fi.hoycomobackend.api.service.menu;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.service.MobileUserService;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MenuDisplayer {
    private static Logger LOGGER = LoggerFactory.getLogger(MenuDisplayer.class);

    public ResponseEntity getMenuFromComercio(Comercio comercio) {
        LOGGER.info("Trying to get menu from comercio");
        Set<Plato> platoSet = comercio.getPlatos();
        if (platoSet.isEmpty()) {
            LOGGER.warn("No platos found for given comercio");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("El comercio seleccionado no cuenta con platos habilitados."));
        }
        Map<Long, List<Plato>> platoCategoryMap = getPlatoCategoryMap(platoSet);

        List<Menu> menuList = getMenuListFromPlatoCategoryMap(platoCategoryMap);

        return ResponseEntity.ok(menuList);
    }

    private List<Menu> getMenuListFromPlatoCategoryMap(Map<Long, List<Plato>> platoCategoryMap) {
        return platoCategoryMap.keySet().stream().map(categoria -> {
            List<Plato> platoList = platoCategoryMap.get(categoria);
            return new Menu(platoList);
        }).collect(Collectors.toList());
    }

    private Map<Long, List<Plato>> getPlatoCategoryMap(Set<Plato> platoSet) {
        Map<Long, List<Plato>> platoCategoryMap = new HashMap<>();

        for (Plato plato : platoSet) {
            LOGGER.info("Mapping plato {}", plato.getId());
            platoCategoryMap = updatePlatoCategoryMap(platoCategoryMap, plato);
        }
        LOGGER.info("Map succesfully built {}", platoCategoryMap.toString());
        return platoCategoryMap;
    }

    private Map<Long, List<Plato>> updatePlatoCategoryMap(final Map platoCategoryMap, Plato plato) {
        LOGGER.info("Current platoCategoryMap {}", platoCategoryMap);
        LOGGER.info("Mapping plato {}", plato);
        Long categoria = plato.getCategoria();
        List<Plato> platoList;
        if (platoCategoryMap.containsKey(categoria)) {
            platoList = ((List<Plato>) platoCategoryMap.get(categoria));
            platoList.add(plato);
        } else {
            platoList = Arrays.asList(plato);
        }
        platoCategoryMap.put(categoria, platoList);

        return platoCategoryMap;
    }


}


class Menu {
    private Long id_categ;
    private String nombre_categ;
    private Integer orden_categ;
    private List<PlatoMenu> platos;

    public Menu(List<Plato> platoList) {
        Plato firstPlato = platoList.get(0);
        this.id_categ = firstPlato.getCategoria();
        this.nombre_categ = ""; //TODO ver como consigo el nombre de la categoria
        this.orden_categ = firstPlato.getOrden();
        this.platos = platoList.stream().map(plato -> new PlatoMenu(plato)).collect(Collectors.toList());
    }

    class PlatoMenu {
        private Long id_plato;
        private String nombre_plato;
        private String imagen;
        private Float precio;
        private Integer orden_plato;

        public PlatoMenu(Plato plato) {
            this.id_plato = plato.getId();
            this.nombre_plato = plato.getNombre();
            this.imagen = plato.getImagen();
            this.precio = plato.getPrecio();
            this.orden_plato = plato.getOrden();
        }
    }

}