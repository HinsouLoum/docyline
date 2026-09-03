package com.laboacamedy.docyline.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service responsable de la generation et de la validation des tokens JWT.
 * Utilise pour l'authentification sans etat (stateless) de l'API REST.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
//    @Value("${app.jwt.secret}")
//    private String secretKey;
//
//    @Value("${app.jwt.expiration-ms}")
//    private long expirationMs;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    /** Genere un token jwt l'utilisateur authentifie, en y incluant son role */
    public String genererToken(UserDetails userDetails, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+jwtProperties.getExpirationMs()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraireEmail(String token){
        return extraireClaim(token, Claims::getSubject);
    }

    public boolean estTokenValide(String token,UserDetails userDetails){
        final String email = extraireEmail(token);
        return email.equals(userDetails.getUsername()) && !estTokenExpire(token);
    }

    public boolean estTokenExpire(String token){
        return extraireClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extraireClaim(String token, Function<Claims,T> resolver){
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }
}
