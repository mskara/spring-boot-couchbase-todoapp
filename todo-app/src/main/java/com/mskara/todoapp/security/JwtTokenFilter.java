package com.mskara.todoapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        try {

            //if request has bearer token extract and authenticate else clear context
            final Optional<String> token = extractTokenFromRequest(request);
            if (token.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(getAuthenticationFromToken(token.get()));
            } else {
                SecurityContextHolder.clearContext();
            }

            //after authentication continue dispatcher
            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }


    private Authentication getAuthenticationFromToken(String token) {

        //resolve jwt and find username
        final String username = tokenProvider.getUsernameFromToken(token);

        // generate authentication token for setting security context
        return new UsernamePasswordAuthenticationToken(username, null, null);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {

        final String bearerPrefix = "Bearer ";
        final String authHeaderKey = "Authorization";

        //if exists bearer token in Authorization header, extract and return else return empty
        return Optional.ofNullable(request.getHeader(authHeaderKey))
                .filter(authHeader -> authHeader.startsWith(bearerPrefix))
                .map(authHeader -> authHeader.substring(bearerPrefix.length()));
    }
}