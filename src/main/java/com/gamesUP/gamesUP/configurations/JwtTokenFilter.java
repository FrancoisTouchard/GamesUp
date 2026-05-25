package com.gamesUP.gamesUP.configurations;

import com.gamesUP.gamesUP.service.AppUserService;
import com.gamesUP.gamesUP.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private AppUserService appUserService;

    public JwtTokenFilter(JwtService jwtService, AppUserService appUserService){
        this.jwtService = jwtService;
        this.appUserService = appUserService;
    }

    // extraction du token + authentification du user
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // extraction du header sous forme de string
        String header = request.getHeader(HttpHeaders.AUTHORIZATION.toLowerCase());
        // refuser header invalide (on sait que le format de Authorization attendu est "Bearer : token")
        if(header == null || header.isEmpty() || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.split(" ")[1].trim();

        if(!jwtService.validate(token)) {
            // le token a été refusé, on interrompt le filtre
            filterChain.doFilter(request, response);
            return;
        }

        // authentifier l'utilisateur dans le contexte de spring security si le token est valide
        // récupérer l'utilisateur
        UserDetails userDetails = appUserService.loadUserByUsername(jwtService.getUsername(token));

        // créer l'objet d'authent demandé par spring security contenant les userDetails et les rôles du user
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails == null ? List.of() : userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // on ajoute l'utilisateur authentifié au contexte de sécurité de spring et on sort du filtre qui a fait son job
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
