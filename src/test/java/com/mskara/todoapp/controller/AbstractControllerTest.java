package com.mskara.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mskara.todoapp.model.dto.UserLoginRequestDto;
import com.mskara.todoapp.security.TokenProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserDetailsService userDetailsService;

    @MockBean
    protected TokenProvider tokenProvider;

    protected String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}
