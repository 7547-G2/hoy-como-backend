package ar.uba.fi.hoycomobackend.api.service.menu;

import ar.uba.fi.hoycomobackend.api.dto.CommentsCommerceDto;
import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.MenuDto;
import ar.uba.fi.hoycomobackend.api.dto.MenuWithLogoDto;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import ar.uba.fi.hoycomobackend.database.repository.CommentRepository;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MenuDisplayer {
    private static Logger LOGGER = LoggerFactory.getLogger(MenuDisplayer.class);
    private CategoriaComidaRepository categoriaComidaRepository;
    private CommentRepository commentRepository;
    private MobileUserRepository mobileUserRepository;

    @Autowired
    public MenuDisplayer(CategoriaComidaRepository categoriaComidaRepository, CommentRepository commentRepository, MobileUserRepository mobileUserRepository) {
        this.categoriaComidaRepository = categoriaComidaRepository;
        this.commentRepository = commentRepository;
        this.mobileUserRepository = mobileUserRepository;
    }

    public ResponseEntity getMenuFromComercio(Comercio comercio) {
        LOGGER.info("Trying to get menu from comercio");
        Set<Plato> platoSet = comercio.getPlatos();
        if (platoSet.isEmpty()) {
            LOGGER.warn("No platos found for given comercio");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("El comercio seleccionado no cuenta con platos habilitados."));
        }
        platoSet = platoSet.stream().filter(plato -> PlatoState.ACTIVO.equals(plato.getState())).collect(Collectors.toSet());
        Map<Long, List<Plato>> platoCategoryMap = getPlatoCategoryMap(platoSet);

        List<MenuDto> menuList = getMenuListFromPlatoCategoryMap(platoCategoryMap);
        menuList.forEach(MenuDto::removeNulls);
        MenuWithLogoDto menuWithLogoDto = new MenuWithLogoDto();
        menuWithLogoDto.setImagen_comercio(comercio.getImagenComercio());
        menuWithLogoDto.setMenu(menuList);
        menuWithLogoDto.setDescuentoGlobal(comercio.getDescuento());
        menuWithLogoDto = menuWithComments(comercio.getId(), menuWithLogoDto);

        return ResponseEntity.ok(menuWithLogoDto);
    }

    private MenuWithLogoDto menuWithComments(Long comercioId, MenuWithLogoDto menuWithLogoDto) {
        List<Comment> commentList = commentRepository.findCommentsByComercioId(comercioId);
        List<CommentsCommerceDto> commentsCommerceDtoList = new ArrayList<>();
        commentList.forEach(comment -> {
            CommentsCommerceDto commentsCommerceDto = new CommentsCommerceDto();
            commentsCommerceDto.setComment(comment.getUserComment());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            if (comment.getUserCommentTimestamp() != null) {
                commentsCommerceDto.setDateComment(formatter.format(comment.getUserCommentTimestamp()));
            }
            if (comment.getCommerceReplyTimestamp() != null) {
                commentsCommerceDto.setDateReplica(formatter.format(comment.getCommerceReplyTimestamp()));
            }
            commentsCommerceDto.setRating(comment.getStars());
            commentsCommerceDto.setReplica(comment.getCommerceReply());
            MobileUser mobileUser = mobileUserRepository.getMobileUserByFacebookId(comment.getMobileUserFacebookId()).get();
            commentsCommerceDto.setUser(mobileUser.getFirstName() + " " + mobileUser.getLastName());

            commentsCommerceDtoList.add(commentsCommerceDto);
        });
        menuWithLogoDto.setComentarios(commentsCommerceDtoList);

        return menuWithLogoDto;
    }

    private List<MenuDto> getMenuListFromPlatoCategoryMap(Map<Long, List<Plato>> platoCategoryMap) {
        return platoCategoryMap.keySet().stream().filter(categoria -> categoriaComidaRepository.findById(categoria).get().getActive()).map(categoria -> {
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