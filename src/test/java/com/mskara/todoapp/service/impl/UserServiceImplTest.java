package com.mskara.todoapp.service.impl;

import com.mskara.todoapp.exception.UserAlreadyExistsException;
import com.mskara.todoapp.model.dto.UserLoginRequestDto;
import com.mskara.todoapp.model.entity.User;
import com.mskara.todoapp.repository.UserRepository;
import com.mskara.todoapp.security.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;


    @Test
    void whenRegisterWithNewUserInfo_thenCreateUserAndReturnToken() {

        //prepare
        when(userRepository.existsByUsername("skara")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded_123456");
        when(tokenProvider.generateToken("skara")).thenReturn("jwt_token");

        //execute
        User user = new User();
        user.setName("serhat");
        user.setSurname("kara");
        user.setUsername("skara");
        user.setPassword("123456");

        String token = userService.register(user);

        //check
        verify(userRepository).existsByUsername("skara");
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(user);
        verify(tokenProvider).generateToken("skara");
        assertEquals("jwt_token", token);

    }

    @Test
    void whenRegisterWithExistingUserInfo_thenThrowException() {

        when(userRepository.existsByUsername("skara")).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> {
                    User user = new User();
                    user.setName("serhat");
                    user.setSurname("kara");
                    user.setUsername("skara");
                    user.setPassword("123456");
                    userService.register(user);
                });

        verify(userRepository).existsByUsername("skara");
        assertEquals("User already exists with this username: skara", exception.getMessage());

    }

    @Test
    void whenLogin_thenReturnToken() {

        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setUsername("skara");
        userLoginRequestDto.setPassword("123456");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(any());
        when(tokenProvider.generateToken("skara")).thenReturn("jwt_token");

        String token = userService.login(userLoginRequestDto);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken("skara");
        assertEquals("jwt_token", token);

    }

}