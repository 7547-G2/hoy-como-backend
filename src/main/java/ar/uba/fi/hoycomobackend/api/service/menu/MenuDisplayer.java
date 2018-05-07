package ar.uba.fi.hoycomobackend.api.service.menu;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.MenuDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MenuDisplayer {
    private static Logger LOGGER = LoggerFactory.getLogger(MenuDisplayer.class);
    private CategoriaComidaRepository categoriaComidaRepository;

    @Autowired
    public MenuDisplayer(CategoriaComidaRepository categoriaComidaRepository) {
        this.categoriaComidaRepository = categoriaComidaRepository;
    }

    public ResponseEntity getMenuFromComercio(Comercio comercio) {
        LOGGER.info("Trying to get menu from comercio");
        Set<Plato> platoSet = comercio.getPlatos();
        if (platoSet.isEmpty()) {
            LOGGER.warn("No platos found for given comercio");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("El comercio seleccionado no cuenta con platos habilitados."));
        }
        Map<Long, List<Plato>> platoCategoryMap = getPlatoCategoryMap(platoSet);

        List<MenuDto> menuList = getMenuListFromPlatoCategoryMap(platoCategoryMap);

        return ResponseEntity.ok(menuList);
    }

    private List<MenuDto> getMenuListFromPlatoCategoryMap(Map<Long, List<Plato>> platoCategoryMap) {
        return platoCategoryMap.keySet().stream().map(categoria -> {
            List<Plato> platoList = platoCategoryMap.get(categoria);
            String categoriaName = categoriaComidaRepository.findById(categoria).get().getTipo();
            return new MenuDto(platoList, categoriaName);
        }).collect(Collectors.toList());
    }

    private Map<Long, List<Plato>> getPlatoCategoryMap(Set<Plato> platoSet) {
        Map<Long, List<Plato>> platoCategoryMap = new HashMap<>();

        for (Plato plato : platoSet) {
            LOGGER.info("Mapping plato {}", plato.getId());
            platoCategoryMap = updatePlatoCategoryMap(platoCategoryMap, plato);
        }
        LOGGER.info("Map succesfully built");
        return platoCategoryMap;
    }

    private Map<Long, List<Plato>> updatePlatoCategoryMap(final Map platoCategoryMap, Plato plato) {
        Long categoria = plato.getCategoria();
        List<Plato> platoList;
        if (platoCategoryMap.containsKey(categoria)) {
            platoList = ((List<Plato>) platoCategoryMap.get(categoria));
            platoList.add(plato);
        } else {
            platoList = new ArrayList<>();
            platoList.add(plato);
        }
        platoCategoryMap.put(categoria, platoList);

        return platoCategoryMap;
    }


}