package com.mskara.todoapp.service;

import com.mskara.todoapp.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUserList();

    User createUser(User user);

}
