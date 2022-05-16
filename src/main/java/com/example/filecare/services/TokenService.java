package com.example.filecare.services;

import com.example.filecare.db.entities.AuthorityImpl;
import com.example.filecare.db.entities.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * Service for operating with tokens (create/validate)
 */

@Service
public class TokenService {

    // TODO: change hardcoded expiration time
    private static final Long EXPIRATION_PERIOD = 1000 * 60 * 60L;
    private final String key;

    public TokenService(@Value("${runtime_variables.secret_key}") String key) {
        this.key = key;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_PERIOD))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> claims =
                Collections.singletonMap("authority", userDetails.getAuthorities().get(0));
        return createToken(claims, userDetails.getUsername());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public AuthorityImpl extractAuthorities(String token) {
        return extractClaim(token, claim -> claim.get("authorities", AuthorityImpl.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && isTokenExpired(token).equals(false);
    }
}
