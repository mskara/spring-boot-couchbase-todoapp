package com.mskara.todoapp.controller;

import com.mskara.todoapp.exception.UserAlreadyExistsException;
import com.mskara.todoapp.model.dto.UserLoginRequestDto;
import com.mskara.todoapp.model.entity.User;
import com.mskara.todoapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends AbstractControllerTest {

    @MockBean
    private UserService userService;

    @Test
    void whenRegisterWithValidInput_thenReturns201() throws Exception {

        User user = new User();
        user.setName("mehmet");
        user.setSurname("kara");
        user.setUsername("mkara");
        user.setPassword("123456");

        mockMvc.perform(post("/api/user/register")
                        .contentType("application/json")
                        .content(json(user)))
                .andExpect(status().isCreated()); //201

        verify(userService).register(user);

    }

    @Test
    void whenRegisterWithInvalidInput_thenReturns400() throws Exception {

        User user = new User();
        user.setName("mehmet");
        user.setName("kara");
        user.setUsername("mkara");
        user.setPassword("");

        mockMvc.perform(post("/api/user/register")
                        .contentType("application/json")
                        .content(json(user)))
                .andExpect(status().isBadRequest()) //400
                .andExpect(jsonPath("$.message").value("Invalid Parameters!"))
                .andExpect(jsonPath("$.path").value("/api/user/register"))
                .andExpect(jsonPath("$.details[0]").value("password : must not be empty"));
    }

    @Test
    void whenRegisterWithExistingUsername_thenReturns409() throws Exception {

        User user = new User();
        user.setName("mehmet");
        user.setName("kara");
        user.setUsername("mkara");
        user.setPassword("123456");

        when(userService.register(user)).thenThrow(new UserAlreadyExistsException(user.getUsername()));

        mockMvc.perform(post("/api/user/register")
                        .contentType("application/json")
                        .content(json(user)))
                .andExpect(status().isConflict()) //409
                .andExpect(jsonPath("$.message").value("User already exists with this username: mkara"))
                .andExpect(jsonPath("$.path").value("/api/user/register"));
    }

    @Test
    void whenLoginWithValidInput_thenReturns200() throws Exception {

        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setUsername("mkara");
        userLoginRequestDto.setPassword("123456");

        when(userService.login(userLoginRequestDto)).thenReturn("jwt_token");

        mockMvc.perform(post("/api/user/login")
                        .contentType("application/json")
                        .content(json(userLoginRequestDto)))
                .andExpect(status().isOk()) //200
                .andExpect(content().string("jwt_token"));

        verify(userService).login(userLoginRequestDto);

    }

    @Test
    void whenLoginWithInValidInput_thenReturns401() throws Exception {

        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setUsername("mkara");
        userLoginRequestDto.setPassword("1234567");

        when(userService.login(userLoginRequestDto)).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/api/user/login")
                        .contentType("application/json")
                        .content(json(userLoginRequestDto)))
                .andExpect(status().isUnauthorized());

    }


}