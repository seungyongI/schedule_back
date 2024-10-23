package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.entity.Provider;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(UserRequestInsertDto userRequestInsertDto) {
        try{
            User user = User.builder()
                    .email(userRequestInsertDto.getEmail())
                    .password(passwordEncoder.encode(userRequestInsertDto.getPassword()))
                    .userName(userRequestInsertDto.getUserName())
                    .provider(Provider.LOCAL)
                    .build();
            userRepository.save(user);
        } catch (DuplicateKeyException e) { // 아이디 중복 예외 처리
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        } catch (Exception e) { // 기타 예외 처리
            throw new RuntimeException("회원 가입 중 오류 발생", e);
        }
        return null;
    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
