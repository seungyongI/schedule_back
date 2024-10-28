package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.dto.request.UserRequestUpdateDto;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.entity.User;

public interface UserService {
    User createUser(UserRequestInsertDto userRequestInsertDto);

    User findUserById(Long id);

    public void updateUser(UserRequestUpdateDto userRequestUpdateDto, ProfileImage profileImage);

    void deleteUser(Long id);
}
