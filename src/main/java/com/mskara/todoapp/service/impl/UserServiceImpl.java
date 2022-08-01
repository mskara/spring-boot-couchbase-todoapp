package com.mskara.todoapp.service.impl;

import com.mskara.todoapp.exception.UserNotFoundException;
import com.mskara.todoapp.model.dto.AccessTokenResponseDto;
import com.mskara.todoapp.model.dto.UserLoginRequestDto;
import com.mskara.todoapp.model.entity.User;
import com.mskara.todoapp.exception.UserAlreadyExistsException;
import com.mskara.todoapp.repository.UserRepository;
import com.mskara.todoapp.security.TokenProvider;
import com.mskara.todoapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public AccessTokenResponseDto register(User user) {
        checkUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return tokenProvider.generateToken(user.getUsername());
    }

    @Override
    public AccessTokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        final String username = userLoginRequestDto.getUsername();
        final String password = userLoginRequestDto.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generateToken(username);
    }

    @Override
    public User getCurrentUser() {
        String loggedInUsername = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new UserNotFoundException(loggedInUsername));
    }

    private void checkUser(User user) {
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
    }
}