package com.example.dailyLog.service;

import com.example.dailyLog.entity.User;
import com.example.dailyLog.repository.UserRepository;
import com.example.dailyLog.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // CustomUserDetails 객체 생성
        return new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                user.getUserName(),
                user.getProfile() // profile 설정
        );
    }
}