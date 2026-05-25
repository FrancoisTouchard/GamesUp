package com.gamesUP.gamesUP.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String generatedKey;

    @Value("${jwt.issuer}")
    private String issuer;

    public boolean validate(String token) {

        try {
            Key key = Keys.hmacShaKeyFor(this.generatedKey.getBytes(StandardCharsets.UTF_8));

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer(issuer)
                    .build()
                    .parseClaimsJws(token); // remplace parse()

            return true;

        } catch ( JwtException | IllegalArgumentException e) {
            if (e instanceof MalformedJwtException) System.out.println("token malformé");
            else if (e instanceof UnsupportedJwtException) System.out.println("token non supporté par la lib");
            else if (e instanceof ExpiredJwtException) System.out.println("token expiré");
            else if (e instanceof IllegalArgumentException) System.out.println("Paramètre non valide");
            else System.out.println(e);
            return false;
        }
    }

    public String getUsername(String token) {
        Key key = Keys.hmacShaKeyFor(this.generatedKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return claims.getSubject().split(",")[0];
    }


    public String generateToken(Authentication authentication) {

        Key key = Keys.hmacShaKeyFor(this.generatedKey.getBytes(StandardCharsets.UTF_8));

        Instant now = Instant.now();

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(authentication.getName()) // username
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(60 * 60 * 24))) // 24h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

