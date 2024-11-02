package com.example.dailyLog.service;

import com.example.dailyLog.constant.Theme;
import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.constant.Provider;
import com.example.dailyLog.dto.request.UserRequestUpdateDto;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.exception.commonException.CommonErrorCode;
import com.example.dailyLog.exception.commonException.error.BizException;
import com.example.dailyLog.exception.imageException.FileUploadError;
import com.example.dailyLog.exception.imageException.ImageErrorCode;
import com.example.dailyLog.repository.ProfileImageRepository;
import com.example.dailyLog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;


    @Override
    @Transactional
    public User createUser(UserRequestInsertDto userRequestInsertDto) {
        try {
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

    // 유저 프로필 수정
    @Override
    @Transactional
    public void updateUser(UserRequestUpdateDto userRequestUpdateDto, MultipartFile imageFile) {
        try {
            User updateUser = userRepository.findById(userRequestUpdateDto.getIdx())
                    .orElseThrow(() -> new BizException(CommonErrorCode.NOT_FOUND));


            if (userRequestUpdateDto.getUserName() != null) {
                updateUser.setUserName(userRequestUpdateDto.getUserName());
            }
            userRepository.save(updateUser);

            ProfileImage profileImage = imageService.saveProfileImage(imageFile, updateUser);
            profileImage.setUser(updateUser);
            profileImageRepository.save(profileImage);
        } catch (Exception e) {
            throw new FileUploadError(ImageErrorCode.FILE_UPLOAD_ERROR);
        }
    }
}


