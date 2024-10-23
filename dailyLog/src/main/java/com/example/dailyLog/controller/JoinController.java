package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JoinController {
    @Autowired
    private UserService userService;

    @GetMapping("/join")
    public String joinForm() {
        return "join"; // 회원가입 페이지 템플릿 이름
    }

    @PostMapping("/join")
    public String join(@RequestBody UserRequestInsertDto userRequestInsertDto) {
        userService.createUser(userRequestInsertDto);

        return "redirect:/login";
    }
}