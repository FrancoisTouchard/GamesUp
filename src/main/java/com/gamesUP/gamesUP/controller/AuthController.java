package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.AppUserDTO;
import com.gamesUP.gamesUP.service.AppUserService;
import com.gamesUP.gamesUP.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class AuthController {

    private final AppUserService appUserService;
    private JwtService jwtService;

    public AuthController(JwtService jwtService, AppUserService appUserService){
        this.jwtService = jwtService;
        this.appUserService = appUserService;
    }

    @GetMapping("/hello")
    public String getHelloWorld() {
        return "Hello exo";
    }

    @PostMapping("/login")
    // fournir username + pwd en basic auth
    public String login(Authentication authentication){
        return jwtService.generateToken(authentication);
    }

    @PostMapping("/signup")
    public ResponseEntity<AppUserDTO> signup(@Valid @RequestBody AppUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appUserService.register(dto));
    }

    @GetMapping("/clients")
    public List<AppUserDTO> findAll(){
        return appUserService.findAll();
    }
}
