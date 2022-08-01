package com.mskara.todoapp.service;

import com.mskara.todoapp.model.dto.AccessTokenResponseDto;
import com.mskara.todoapp.model.dto.UserLoginRequestDto;
import com.mskara.todoapp.model.entity.User;

public interface UserService {

    AccessTokenResponseDto register(User user);

    AccessTokenResponseDto login(UserLoginRequestDto userLoginRequestDto);

    User getCurrentUser();

}
