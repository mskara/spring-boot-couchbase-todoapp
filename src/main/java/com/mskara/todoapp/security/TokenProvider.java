package com.mskara.todoapp.security;

import com.mskara.todoapp.model.dto.AccessTokenResponseDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface TokenProvider {

    AccessTokenResponseDto generateToken(String username);

    String getUsernameFromToken(String token);

    List<SimpleGrantedAuthority> getRolesFromToken(String token);

}