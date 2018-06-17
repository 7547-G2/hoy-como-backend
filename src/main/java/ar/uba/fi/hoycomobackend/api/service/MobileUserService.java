package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.api.service.distancetime.TimeTakenCalculator;
import ar.uba.fi.hoycomobackend.api.service.menu.MenuDisplayer;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.queries.PedidoQuery;
import ar.uba.fi.hoycomobackend.database.repository.CommentRepository;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MobileUserService {

    private static Logger LOGGER = LoggerFactory.getLogger(MobileUserService.class);
    private static String ADDRESS_ADDED_SUCCESSFUL = "Se agregó dirección exitosamente";
    private static String MOBILE_USER_ADDED_SUCCESSFUL = "Se agregó usuario exitosamente";
    private static String MOBILE_USER_ADD_UNSUCCESSFUL = "Datos incorrectos al cargar usuario";

    private MobileUserRepository mobileUserRepository;
    private ComercioQuery comercioQuery;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;
    private MenuDisplayer menuDisplayer;
    private PedidoQuery pedidoQuery;
    private OrderDetailService orderDetailService;
    private TimeTakenCalculator timeTakenCalculator;
    private CommentRepository commentRepository;

    @Autowired
    public MobileUserService(MobileUserRepository mobileUserRepository, ComercioQuery comercioQuery, ModelMapper modelMapper, ObjectMapper objectMapper, MenuDisplayer menuDisplayer, PedidoQuery pedidoQuery, OrderDetailService orderDetailService, TimeTakenCalculator timeTakenCalculator, CommentRepository commentRepository) {
        this.mobileUserRepository = mobileUserRepository;
        this.comercioQuery = comercioQuery;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.menuDisplayer = menuDisplayer;
        this.pedidoQuery = pedidoQuery;
        this.orderDetailService = orderDetailService;
        this.timeTakenCalculator = timeTakenCalculator;
        this.commentRepository = commentRepository;
    }

    public ResponseEntity getMobileUserList() {
        LOGGER.info("Getting all Usuarios from database.");
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        List<MobileUserDto> mobileUserDtoList = new ArrayList<>();
        for (MobileUser mobileUser : mobileUserList) {
            Address address = mobileUser.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            MobileUserDto mobileUserDto = modelMapper.map(mobileUser, MobileUserDto.class);
            mobileUserDto.setAddressDto(addressDto);
            mobileUserDto.removeNulls();
            mobileUserDto.setMobileUserState(mobileUser.getState());
            mobileUserDtoList.add(mobileUserDto);
        }

        return ResponseEntity.ok(mobileUserDtoList);
    }

    public ResponseEntity getMobileUserById(Long id) {
        LOGGER.info("Getting MobileUser by id: {}", id);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);
        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Address address = mobileUser.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            MobileUserDto mobileUserDto = modelMapper.map(mobileUser, MobileUserDto.class);
            mobileUserDto.setAddressDto(addressDto);
            mobileUserDto.removeNulls();

            return ResponseEntity.ok(mobileUserDto);
        } else
            return ResponseEntity.ok(new MobileUserDto());
    }

    public ResponseEntity addMobileUser(MobileUserAddDto mobileUserAddDto) {
        LOGGER.info("Adding new MobileUser: {}", mobileUserAddDto);
        AddressDto addressDto = mobileUserAddDto.getAddressDto();
        Address address = modelMapper.map(addressDto, Address.class);
        MobileUser mobileUser = modelMapper.map(mobileUserAddDto, MobileUser.class);
        mobileUser.setAddress(address);
        mobileUser.setTelephone(mobileUserAddDto.getPhone());
        try {
            mobileUserRepository.saveAndFlush(mobileUser);
            return ResponseEntity.ok(MOBILE_USER_ADDED_SUCCESSFUL);
        } catch (RuntimeException e) {
            LOGGER.info("Error while adding new mobile user: {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(MOBILE_USER_ADD_UNSUCCESSFUL));
    }

    public ResponseEntity getMobileUserAuthorizedById(Long id) throws JsonProcessingException {
        LOGGER.info("Checking if MobileUser is authorized by id: {}", id);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);
        MobileUserStateDto mobileUserStateDto;
        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            String mobileUserState = mobileUser.getState();

            if ("deshabilitado".equalsIgnoreCase(mobileUserState)) {
                mobileUserStateDto = new MobileUserStateDto(MobileUserState.UNAUTHORIZED);
                mobileUserStateDto.setDescription(mobileUser.getMotivoDeshabilitacion());
            } else {
                mobileUserStateDto = new MobileUserStateDto(MobileUserState.AUTHORIZED);
            }
        } else
            mobileUserStateDto = new MobileUserStateDto(MobileUserState.NOT_FOUND);

        String mobileUserStateDtoJson = objectMapper.writeValueAsString(mobileUserStateDto);
        return ResponseEntity.ok(mobileUserStateDtoJson);
    }

    public ResponseEntity addFavoriteComercioToMobileUser(Long mobileUserFacebookId, Long comercioId) {
        LOGGER.info("Trying to add Comercio with id: {}, to mobile user with id: {}", comercioId, mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (mobileUserOptional.isPresent() && comercioOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Comercio comercio = comercioOptional.get();
            mobileUser.getFavoriteComercios().add(comercio);
            comercio.getMobileUserList().add(mobileUser);

            mobileUserRepository.saveAndFlush(mobileUser);
            return ResponseEntity.ok("Comercio con id: " + comercioId + "agregado a favoritos al usuario con id: " + mobileUserFacebookId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se pudo dar de alta debido a que el usuario o el comercio no fueron encontrados"));
        }
    }

    public ResponseEntity getMobileUserFavorites(Long id) throws JsonProcessingException {
        LOGGER.info("Trying to get all mobile user's favorites with id: {}", id);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);

        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            MobileUserFavoritesDto mobileUserFavoritesDto = getMobileUserFavoritesDtoFromMobileUser(mobileUser);
            mobileUserFavoritesDto.removeNulls();

            return ResponseEntity.ok(objectMapper.writeValueAsString(mobileUserFavoritesDto));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No existe el usuario con id: " + id));
        }
    }

    private MobileUserFavoritesDto getMobileUserFavoritesDtoFromMobileUser(MobileUser mobileUser) {
        MobileUserFavoritesDto mobileUserFavoritesDto = new MobileUserFavoritesDto();
        Set<Long> favorites = new LinkedHashSet<>();
        List<Comercio> comercioList = mobileUser.getFavoriteComercios();

        for (Comercio comercio : comercioList) {
            favorites.add(comercio.getId());
        }
        mobileUserFavoritesDto.setFavorites(favorites);

        return mobileUserFavoritesDto;
    }

    public ResponseEntity addAddressToMobileUser(Long mobileUserFacebookId, AddressDto addressDto) {
        LOGGER.info("Getting mobile user with id: {}", mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);

        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Address address = modelMapper.map(addressDto, Address.class);
            mobileUser.setAddress(address);
            if (addressDto.getPhone() != null)
                mobileUser.setTelephone(addressDto.getPhone());

            mobileUserRepository.saveAndFlush(mobileUser);
            return ResponseEntity.ok(ADDRESS_ADDED_SUCCESSFUL);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No existe el usuario con id: " + mobileUserFacebookId));
    }

    public ResponseEntity getComercioMobileUserDtoSet(String search) {
        List<Comercio> comercioList = comercioQuery.findBySearchQuery(search);
        List<ComercioMobileUserDto> comercioMobileUserDtoList = getComercioDtos(comercioList);
        comercioMobileUserDtoList = comercioMobileUserDtoList.stream().filter(comercio -> "habilitado".equals(comercio.getEstado())).collect(Collectors.toList());
        comercioMobileUserDtoList.forEach(ComercioMobileUserDto::removeNulls);
        comercioMobileUserDtoList.forEach(ComercioMobileUserDto::transformPrices);

        return ResponseEntity.ok(comercioMobileUserDtoList);
    }

    private List<ComercioMobileUserDto> getComercioDtos(List<Comercio> comercioList) {
        List<ComercioMobileUserDto> comercioMobileUserDtoList = new ArrayList<>();
        for (Comercio comercio : comercioList) {
            Address address = comercio.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            ComercioMobileUserDto comercioMobileUserDto = modelMapper.map(comercio, ComercioMobileUserDto.class);
            comercioMobileUserDto.setAddressDto(addressDto);

            comercioMobileUserDtoList.add(comercioMobileUserDto);
        }
        return comercioMobileUserDtoList;
    }

    public ResponseEntity changeStateToMobileUser(Long mobileUserFacebookId, Integer stateCode) {
        LOGGER.info("Getting mobile user with id: {}", mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);

        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();

            String mobileUserState = (0 == stateCode) ? "habilitado" : "deshabilitado";
            mobileUser.setState(mobileUserState);

            mobileUserRepository.saveAndFlush(mobileUser);

            return ResponseEntity.ok("Mobile user with id: " + mobileUserFacebookId + " changed state successfully to: " + mobileUserState);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No existe el usuario con id: " + mobileUserFacebookId));
    }

    public ResponseEntity getMenuFromComercio(Long comercioId) {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);
        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();

            return menuDisplayer.getMenuFromComercio(comercio);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No existe el comercio con id: " + comercioId));
    }

    public ResponseEntity postPedido(PostPedidoDto postPedidoDto) {
        LOGGER.info("PostPedido data {}, latitude: {}, longitude: {}", postPedidoDto, postPedidoDto.getLat(), postPedidoDto.getLng());
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(postPedidoDto.getStore_id());
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.findById(postPedidoDto.getFacebook_id());

        if (comercioOptional.isPresent() && mobileUserOptional.isPresent()) {
            try {
                Comercio comercio = comercioOptional.get();
                Pedido pedido = modelMapper.map(postPedidoDto, Pedido.class);
                pedido.setLongitud(postPedidoDto.getLng());
                pedido.setLatitud(postPedidoDto.getLat());
                pedido.setStoreId(postPedidoDto.getStore_id());
                pedido.setFacebookId(postPedidoDto.getFacebook_id());
                pedido.getOrden().forEach(orden -> {
                    orden.setId(null);
                });
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = new Date();
                pedido.setFecha(dateFormat.format(date));
                pedido.setFechaInicioFacturacion(new java.sql.Date(System.currentTimeMillis()));
                Integer timeTakenPedido = timeTakenCalculator.timeTakenFromOriginToDestination(postPedidoDto.getLat(), postPedidoDto.getLng(), comercio.getLatitud(), comercio.getLongitud());
                pedido.setTimeAccordingToDistance(timeTakenPedido);
                Pedido savedPedido = pedidoQuery.savePedido(pedido);
                orderDetailService.creation(savedPedido, comercioOptional.get().getNombre());
                return ResponseEntity.ok().build();
            } catch (Throwable e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Problema al intentar salvar el pedido. Razón: " + e.getMessage()));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No existe el comercio con id: " + postPedidoDto.getStore_id() + " O el usuario con id: " + postPedidoDto.getFacebook_id()));
    }

    public ResponseEntity getPedidosOfUser(Long facebookId) {
        try {
            List<Pedido> pedidoList = pedidoQuery.getpedidosOfUser(facebookId);
            List<PedidoMobileUserDto> pedidoMobileUserDtoList = transformPedidoToPedidoMobileUserDto(pedidoList);

            return ResponseEntity.ok(pedidoMobileUserDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Problema al interno intentando obtener pedidos. Razón: " + e));
        }
    }

    private List<PedidoMobileUserDto> transformPedidoToPedidoMobileUserDto(List<Pedido> pedidoList) {
        List<PedidoMobileUserDto> pedidoMobileUserDtoList = new ArrayList<>();
        pedidoList.forEach(pedido -> {
            PedidoMobileUserDto pedidoMobileUserDto = new PedidoMobileUserDto();
            pedidoMobileUserDto.setStore_id(pedido.getStoreId());
            Long comercioId = pedido.getStoreId();
            String storeName = comercioQuery.getComercioById(comercioId).get().getNombre();
            pedidoMobileUserDto.setStore_name(storeName);
            String estadoPedido = transformEstadoPedidoToCorrectValue(pedido.getEstado());
            pedidoMobileUserDto.setStatus(estadoPedido);
            pedidoMobileUserDto.setOrder_id(pedido.getId());
            pedidoMobileUserDto.setFecha(pedido.getLastModified());

            pedidoMobileUserDtoList.add(pedidoMobileUserDto);
        });

        return pedidoMobileUserDtoList;
    }

    private String transformEstadoPedidoToCorrectValue(String estado) {
        if (estado != null) {
            if ("EnPreparacion".equalsIgnoreCase(estado))
                return "En Preparación";
            else
                return estado;
        }
        return null;
    }

    public ResponseEntity cancelPedido(Long pedidoId) {
        try {
            Pedido pedido = pedidoQuery.getPedidoById(pedidoId).get();
            pedido.setEstado("Cancelado");
            pedido = pedidoQuery.savePedido(pedido);
            orderDetailService.update(pedido);

            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se encontró pedido"));
        }
    }

    public ResponseEntity postCommentInPedido(Long mobileUserFacebookId, Long pedidoId, ComentarioDto comentarioDto) {
        try {
            Pedido pedido = pedidoQuery.getPedidoById(pedidoId).get();
            Comment comment = new Comment();
            comment.setMobileUserFacebookId(mobileUserFacebookId);
            comment.setComercioId(pedido.getStoreId());
            comment.setPedidoId(pedidoId);
            comment.setStars(comentarioDto.getEstrellas());
            comment.setUserComment(comentarioDto.getComentario());
            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
            comment.setUserCommentDate(sqlDate);
            pedido.setEstado("Calificado");
            updateRatingOfCommerce(pedido.getStoreId(), comentarioDto.getEstrellas());

            pedidoQuery.savePedido(pedido);
            orderDetailService.update(pedido);
            comment = commentRepository.saveAndFlush(comment);

            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Error al postear comentario: " + e.getMessage()));
        }
    }

    private void updateRatingOfCommerce(Long comercioId, Integer estrellas) {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);
        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            if (comercio.getTotalRatings() == null)
                comercio.setTotalRatings(0L);

            Float newRating = ( estrellas + (comercio.getTotalRatings()*comercio.getRating()) ) / (comercio.getTotalRatings() + 1.0f);
            comercio.setRating(newRating);

            comercioQuery.saveAndFlush(comercio);
        } else
            LOGGER.warn("No commerce found by that ID");
    }
}
