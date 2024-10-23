package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.LoginRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@CrossOrigin
public class LoginController {
    @GetMapping("/login")
    public String loginForm() {

        return "login";
    }

    @PostMapping("/login")
    public void login(@ModelAttribute @Valid LoginRequestDto loginRequest, BindingResult bindingResult
            , HttpServletResponse response) throws IOException {
        if (bindingResult.hasErrors()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
