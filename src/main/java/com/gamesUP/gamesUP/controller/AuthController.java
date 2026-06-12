package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.AppUserDTO;
import com.gamesUP.gamesUP.service.AppUserService;
import com.gamesUP.gamesUP.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthController {

    private final AppUserService appUserService;
    private final JwtService jwtService;

    public AuthController(JwtService jwtService, AppUserService appUserService){
        this.jwtService = jwtService;
        this.appUserService = appUserService;
    }

    @PostMapping("/public/login")
    // fournir username + pwd en basic auth
    public String login(Authentication authentication){
        return jwtService.generateToken(authentication);
    }

    @PostMapping("/public/signup")
    public ResponseEntity<AppUserDTO> signup(@Valid @RequestBody AppUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appUserService.register(dto));
    }

    @PostMapping("/private/user/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            jwtService.blacklist(header.substring(7));
        }
        return ResponseEntity.noContent().build();
    }
}
