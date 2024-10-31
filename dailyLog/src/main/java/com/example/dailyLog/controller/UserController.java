package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.UserRequestUpdateDto;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.service.ImageService;
import com.example.dailyLog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping(value = "/update")
    public ResponseEntity<String > updateUser(
            @RequestPart(name = "userUpdate" ,required = false) UserRequestUpdateDto userRequestUpdateDto,
            @RequestPart(name = "profileUpdate" ,required = false) MultipartFile multipartFile){
        try {
            if (userRequestUpdateDto == null || userRequestUpdateDto.getIdx() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Profile image update requires a valid user ID.");
            }
            ProfileImage profileImage = null;
            if (multipartFile != null && !multipartFile.isEmpty()) {
                try {
                    profileImage = imageService.saveProfileImage(multipartFile);
                } catch (Exception e) {
//                    imageService.deleteProfileImage(profileImage);
                    throw new RuntimeException("Failed to save profile image", e);
                }
            }

            userService.updateUser(userRequestUpdateDto, profileImage);

            return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create profile: " + e.getMessage());
        }

    }
}
