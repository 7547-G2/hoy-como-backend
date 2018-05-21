package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.api.service.menu.MenuDisplayer;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.queries.PedidoQuery;
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

    @Autowired
    public MobileUserService(MobileUserRepository mobileUserRepository, ComercioQuery comercioQuery, ModelMapper modelMapper, ObjectMapper objectMapper, MenuDisplayer menuDisplayer, PedidoQuery pedidoQuery, OrderDetailService orderDetailService) {
        this.mobileUserRepository = mobileUserRepository;
        this.comercioQuery = comercioQuery;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.menuDisplayer = menuDisplayer;
        this.pedidoQuery = pedidoQuery;
        this.orderDetailService = orderDetailService;
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
            MobileUserState mobileUserState = mobileUser.getState();
            mobileUserStateDto = new MobileUserStateDto(mobileUserState);
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
            MobileUserState mobileUserState = MobileUserState.getByStateCode(stateCode);
            mobileUser.setState(mobileUserState);

            mobileUserRepository.saveAndFlush(mobileUser);

            return ResponseEntity.ok("Mobile user with id: " + mobileUserFacebookId + " changed state successfully to: " + mobileUserState.name());
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
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(postPedidoDto.getStore_id());
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.findById(postPedidoDto.getFacebook_id());

        if (comercioOptional.isPresent() && mobileUserOptional.isPresent()) {
            try {
                Pedido pedido = modelMapper.map(postPedidoDto, Pedido.class);
                pedido.setStoreId(postPedidoDto.getStore_id());
                pedido.setFacebookId(postPedidoDto.getFacebook_id());
                pedido.getOrden().forEach(orden -> {
                    orden.setId(null);
                    orden.setPedido(pedido);
                });
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
            pedido.getOrden().forEach(orden -> {
                PedidoMobileUserDto pedidoMobileUserDto = new PedidoMobileUserDto();
                pedidoMobileUserDto.setOrder_id(orden.getId());
                Long comercioId = pedido.getStoreId();
                pedidoMobileUserDto.setStore_id(comercioId);
                String storeName = comercioQuery.getComercioById(comercioId).get().getNombre();
                pedidoMobileUserDto.setStore_name(storeName);
                pedidoMobileUserDto.setStatus("notYetImplemented");
                pedidoMobileUserDtoList.add(pedidoMobileUserDto);
            });
        });

        return pedidoMobileUserDtoList;
    }
}
