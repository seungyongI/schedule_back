package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.DiaryRequestDeleteDto;
import com.example.dailyLog.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteImage(@RequestBody DiaryRequestDeleteDto diaryRequestDeleteDto) {
        imageService.deleteImages(diaryRequestDeleteDto);
        return ResponseEntity.status(HttpStatus.OK).body("Image deleted successfully");
    }
}
