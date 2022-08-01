package com.mskara.todoapp.service;

import com.mskara.todoapp.model.dto.UserLoginRequestDto;
import com.mskara.todoapp.model.entity.User;

public interface UserService {

    String register(User user);

    String login(UserLoginRequestDto userLoginRequestDto);

}