package com.mskara.todoapp.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface TokenProvider {

    String generateToken(String username);

    String getUsernameFromToken(String token);

    List<SimpleGrantedAuthority> getRolesFromToken(String token);

}