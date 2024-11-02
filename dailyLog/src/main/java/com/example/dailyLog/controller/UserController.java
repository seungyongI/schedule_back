package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.UserRequestUpdateDto;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.service.ImageService;
import com.example.dailyLog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ImageService imageService;
    private final UserService userService;

    //유저 프로필 수정
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUser(@RequestPart(name = "userRequest") UserRequestUpdateDto userRequestUpdateDto,
                                             @RequestPart(name = "imageFiles", required = false) MultipartFile imageFile) {
        userService.updateUser(userRequestUpdateDto, imageFile);
        return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");
    }
}

