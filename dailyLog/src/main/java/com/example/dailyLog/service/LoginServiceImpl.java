package com.example.dailyLog.service;

import com.example.dailyLog.entity.User;
import com.example.dailyLog.exception.loginException.EmailNotFoundException;
import com.example.dailyLog.exception.loginException.LoginErrorCode;
import com.example.dailyLog.repository.UserRepository;
import com.example.dailyLog.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(LoginErrorCode.EMAIL_NOT_FOUND));

        // CustomUserDetails 객체 직접 생성
        return new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                user.getUserName(),
                user.getProfileImage()
        );
    }
}