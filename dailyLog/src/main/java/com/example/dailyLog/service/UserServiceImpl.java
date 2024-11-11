package com.example.dailyLog.service;

import com.example.dailyLog.constant.Provider;
import com.example.dailyLog.constant.Theme;
import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.dto.request.UserRequestUpdateDto;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.exception.commonException.CommonErrorCode;
import com.example.dailyLog.exception.commonException.error.BizException;
import com.example.dailyLog.exception.loginException.LoginErrorCode;
import com.example.dailyLog.exception.loginException.UserPKException;
import com.example.dailyLog.repository.CalendarRepository;
import com.example.dailyLog.repository.ProfileImageRepository;
import com.example.dailyLog.repository.UserRepository;
import com.example.dailyLog.security.CustomUserDetails;
import com.example.dailyLog.security.providers.JwtTokenProvider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.LoginException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final ProfileImageRepository profileImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final EntityManager entityManager;
    private final JwtTokenProvider jwtTokenProvider;


    // 회원가입
    @Override
    @Transactional
    public User createUser(UserRequestInsertDto userRequestInsertDto) {
        try {
            Calendars calendars = Calendars.builder().theme(Theme.LIGHT).build();
            log.info("calendar = {}", calendarRepository.save(calendars));

            // 기존 회원가입 로직
            User user = User.builder()
                    .email(userRequestInsertDto.getEmail())
                    .password(passwordEncoder.encode(userRequestInsertDto.getPassword()))
                    .userName(userRequestInsertDto.getUserName())
                    .provider(Provider.LOCAL)
                    .calendars(calendars)
                    .build();

            return userRepository.save(user);

        } catch (DuplicateKeyException e) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        } catch (Exception e) {
            throw new RuntimeException("회원 가입 중 오류 발생", e);
        }
    }


    // 회원 찾기
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BizException(CommonErrorCode.NOT_FOUND));
    }


    // 닉네임 수정
    @Override
    @Transactional
    public void updateUserName(@Valid UserRequestUpdateDto userRequestUpdateDto) {
        try {
            User updateUser = userRepository.findById(userRequestUpdateDto.getIdx())
                    .orElseThrow(() -> new BizException(CommonErrorCode.NOT_FOUND));

            if (userRequestUpdateDto.getUserName() != null) {
                updateUser.setUserName(userRequestUpdateDto.getUserName());
            }

            userRepository.save(updateUser);

        } catch (Exception e) {
            throw new RuntimeException("닉네임 업데이트 중 오류 발생", e);
        }
    }


    // 프로필 사진 수정
    @Override
    @Transactional
    public void updateProfileImage(Long idx, MultipartFile imageFile) {
        try {
            User updateUser = userRepository.findById(idx)
                    .orElseThrow(() -> new BizException(CommonErrorCode.NOT_FOUND));
            // 기존 이미지 삭제 로직 추가
            ProfileImage oldProfileImage = updateUser.getProfileImage();
            if (oldProfileImage != null) {
                updateUser.setProfileImage(null);
                entityManager.flush();
                profileImageRepository.delete(oldProfileImage);
            }

            if (imageFile != null) {
                ProfileImage profileImage = imageService.saveProfileImage(imageFile, updateUser);
                profileImage.setUser(updateUser);
                updateUser.setProfileImage(profileImage);
            }
        } catch (Exception e) {
            throw new RuntimeException("프로필 이미지 업데이트 중 오류 발생", e);
        }
    }


    // 회원탈퇴(작동X)
    @Transactional
    @Override
    public void deleteUser(Long idx, String token) {
        String userIdFromToken = jwtTokenProvider.getUserPk(token);

        if (!userIdFromToken.equals(String.valueOf(idx))) {
            throw new UserPKException(LoginErrorCode.USER_PK);
        }

        User user = userRepository.findById(idx).orElseThrow(
                () -> new BizException(CommonErrorCode.NOT_FOUND)
        );

        userRepository.deleteById(user.getIdx());
    }
}
