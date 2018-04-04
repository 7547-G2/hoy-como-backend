package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.api.service.MobileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MobileUserRestController {

    private MobileUserService mobileUserService;

    @Autowired
    public MobileUserRestController(MobileUserService mobileUserService) {
        this.mobileUserService = mobileUserService;
    }

    @GetMapping(value = "/mobileUser", produces = {"application/json"})
    public List<MobileUserDto> getAllMobileUsers() {
        return mobileUserService.getMobileUserList();
    }

    @GetMapping(value = "/mobileUser/{id}", produces = {"application/json"})
    public MobileUserDto getMobileUserById(@PathVariable("id") Long id) {
        return mobileUserService.getMobileUserById(id);
    }

    @GetMapping("/mobileUser/{id}/authorized")
    public ResponseEntity getMobileUserAuthorized(@PathVariable("id") Long id) {
        return mobileUserService.getMobileUserAuthorizedById(id);
    }

    @PostMapping(value = "/mobileUser")
    public void addMobileUser(@RequestBody MobileUserDto mobileUser) {
        mobileUserService.addMobileUser(mobileUser);
    }
}
