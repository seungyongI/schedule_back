package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.entity.User;

public interface UserService {
    User createUser(UserRequestInsertDto userRequestInsertDto);

    User findUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long id);
}
