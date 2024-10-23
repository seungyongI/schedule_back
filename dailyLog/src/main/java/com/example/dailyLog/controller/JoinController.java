package com.example.dailyLog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class JoinController {

    @GetMapping("/join")
    public String joinForm() {
        return "join"; // 회원가입 페이지 템플릿 이름
    }
}