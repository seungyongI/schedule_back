package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.KakaoUserRequestDto;
import com.example.dailyLog.dto.request.LoginRequestDto;
import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.dto.response.KakaoUserResponseDto;
import com.example.dailyLog.dto.response.LoginResponseDto;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.security.CustomUserDetails;
import com.example.dailyLog.security.providers.JwtTokenProvider;
import com.example.dailyLog.service.KakaoLoginService;
import com.example.dailyLog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService; // UserService 주입
    private final KakaoLoginService kakaoLoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.createToken(userDetails.getUserName());
        String refreshToken = jwtTokenProvider.createRefreshToken(userDetails.getUserName());

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .idx(userDetails.getIdx())
                .accessToken(token)
                .refreshToken(refreshToken)
                .email(userDetails.getEmail())
                .userName(userDetails.getUserName())
                .profileImage(userDetails.getProfileImage())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody UserRequestInsertDto userRequestInsertDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 오류 처리
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                // 오류 메시지 처리
                System.out.println(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body("비밀번호는 8자~20자 사이여야 하며, 영문, 숫자, 특수문자를 각 하나씩 사용하셔야 합니다.");
        }

        userService.createUser(userRequestInsertDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam("code") String code) {
        String accessToken = kakaoLoginService.getKakaoAccessToken(code);
        KakaoUserResponseDto kakaoUserInfo = kakaoLoginService.getKakaoUserInfo(accessToken);
        User user = kakaoLoginService.createKakaoUser(kakaoUserInfo);

        // JWT 생성
        String token = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        ProfileImage profileImage = user.getProfileImage();
        if (profileImage == null) {
            profileImage = new ProfileImage();
            profileImage.setImgUrl(null);
        }

        // LoginResponseDto 생성
        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .userName(user.getUserName())
                .profileImage(profileImage)
                .build();

        return ResponseEntity.ok(responseDto);
    }
}

