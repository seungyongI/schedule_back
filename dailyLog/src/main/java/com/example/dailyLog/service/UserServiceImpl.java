package com.example.dailyLog.service;

import com.example.dailyLog.constant.Theme;
import com.example.dailyLog.dto.request.DiaryRequestUpdateDto;
import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.constant.Provider;
import com.example.dailyLog.dto.request.UserRequestUpdateDto;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.exception.commonException.CommonErrorCode;
import com.example.dailyLog.exception.commonException.error.BizException;
import com.example.dailyLog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
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
            Calendars calendars = Calendars.builder()
                    .theme(Theme.LIGHT)
                    .build();

            User user = User.builder()
                    .email(userRequestInsertDto.getEmail())
                    .password(passwordEncoder.encode(userRequestInsertDto.getPassword()))
                    .userName(userRequestInsertDto.getUserName())
                    .provider(Provider.LOCAL)
                    .calendars(calendars)
                    .build();

            return userRepository.save(user);

        } catch (DuplicateKeyException e) { // 아이디 중복 예외 처리
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        } catch (Exception e) { // 기타 예외 처리
            throw new RuntimeException("회원 가입 중 오류 발생", e);
        }
    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public void updateUser(UserRequestUpdateDto userRequestUpdateDto, ProfileImage profileImage) {
        try {
            User updateUser = userRepository.findById(userRequestUpdateDto.getIdx())
                    .orElseThrow(() -> new BizException(CommonErrorCode.NOT_FOUND));

            if (userRequestUpdateDto.getEmail() != null) {
                updateUser.setEmail(userRequestUpdateDto.getEmail());
            }
            if (userRequestUpdateDto.getUserName() != null) {
                updateUser.setUserName(userRequestUpdateDto.getUserName());
            }
            if (userRequestUpdateDto.getPassword() != null) {
                updateUser.setPassword(passwordEncoder.encode(userRequestUpdateDto.getPassword()));
            }

            if (profileImage != null) {
                updateUser.setProfileImage(profileImage); // User 엔티티에 프로필 이미지 필드가 있다고 가정
            }
            userRepository.save(updateUser);

        } catch (Exception e) {
            throw new ServiceException("Failed to update user", e);
        }
    }

    @Override
    public void deleteUser(Long id) {

    }
}
