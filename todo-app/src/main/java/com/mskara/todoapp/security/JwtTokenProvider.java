package com.mskara.todoapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

    @Value("${jwt.tokenValidity}")
    private int tokenValidity;

    @Value("${jwt.signingKey}")
    private String signingKey;

    @Override
    public String generateToken(String username) {

        final long now = System.currentTimeMillis();
        final long expiryTime = now + tokenValidity;
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(expiryTime))
                .sign(Algorithm.HMAC256(signingKey.getBytes()));
    }

    @Override
    public String getUsernameFromToken(String token) {
        return decodeToken(token).getSubject();
    }

    @Override
    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {

        return decodeToken(token)
                .getClaim("roles")
                .asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(signingKey))
                .build()
                .verify(token);
    }
}