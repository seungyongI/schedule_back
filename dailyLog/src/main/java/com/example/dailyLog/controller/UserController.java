package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.UserRequestUpdateDto;
import com.example.dailyLog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //유저 닉네임 수정
    @PutMapping(value = "/updateUserName")
    public ResponseEntity<String> updateUserName(
                                                 @RequestBody UserRequestUpdateDto userRequestUpdateDto){
        userService.updateUserName(userRequestUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("UserNickname updated successfully");
    }

    //유저 프로필 이미지 수정
    @PostMapping(value = "/updateProfileImage/{idx}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfileImage(@PathVariable(name = "idx") Long idx,
                                                     @RequestPart(name = "imageFiles") MultipartFile imageFile) {
        userService.updateProfileImage(idx, imageFile);
        return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");
    }
}

