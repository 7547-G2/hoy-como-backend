package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.api.service.MobileUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = {"application/json"})
public class MobileUserRestController {

    private MobileUserService mobileUserService;

    @Autowired
    public MobileUserRestController(MobileUserService mobileUserService) {
        this.mobileUserService = mobileUserService;
    }

    @GetMapping(value = "/mobileUser")
    public List<MobileUserDto> getAllMobileUsers() {
        return mobileUserService.getMobileUserList();
    }

    @GetMapping(value = "/mobileUser/{id}")
    public MobileUserDto getMobileUserById(@PathVariable("id") Long id) {
        return mobileUserService.getMobileUserById(id);
    }

    @GetMapping("/mobileUser/{id}/authorized")
    public String getMobileUserAuthorized(@PathVariable("id") Long id) {
        return mobileUserService.getMobileUserAuthorizedById(id);
    }

    @PostMapping(value = "/mobileUser")
    public void addMobileUser(@RequestBody MobileUserDto mobileUser) {
        mobileUserService.addMobileUser(mobileUser);
    }

    @GetMapping(value = "/mobileUser/{id}/favorites")
    public String getMobileUserFavorites(@PathVariable("id") Long id) throws JsonProcessingException {
        return mobileUserService.getMobileUserFavorites(id);
    }

    @PostMapping(value = "/mobileUser/{mobileUserFacebookId}/favorite/{comercioId}")
    public String addFavoriteComercioToMobileUser(@PathVariable("mobileUserFacebookId") Long mobileUserFacebookId, @PathVariable("comercioId") Long comercioId) {
        return mobileUserService.addFavoriteComercioToMobileUser(mobileUserFacebookId, comercioId);
    }

    @PostMapping(value = "/mobileUser/{id}/address")
    public String addAddressToMobileUser(@PathVariable("id") Long id, @RequestBody AddressDto addressDto) {
        //TODO implement this method
        return "";
        //return mobileUserService.addAddressToMobileUser(id, addressDto);
    }
}
