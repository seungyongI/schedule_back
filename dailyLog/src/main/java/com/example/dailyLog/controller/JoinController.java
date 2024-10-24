package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JoinController {
    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserRequestInsertDto userRequestInsertDto) {
        userService.createUser(userRequestInsertDto);
        return ResponseEntity.ok("sucess");
    }
}