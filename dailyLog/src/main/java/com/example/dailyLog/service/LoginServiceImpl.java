package com.example.dailyLog.service;

import com.example.dailyLog.details.LoginDetails;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;


    @Override
    public LoginDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new LoginDetails(user);
    }
}