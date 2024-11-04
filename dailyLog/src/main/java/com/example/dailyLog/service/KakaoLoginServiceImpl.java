package com.example.dailyLog.service;

import com.example.dailyLog.constant.Provider;
import com.example.dailyLog.dto.request.KakaoUserRequestDto;
import com.example.dailyLog.dto.response.KakaoUserResponseDto;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.repository.UserRepository;
import com.example.dailyLog.security.providers.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoLoginServiceImpl implements KakaoLoginService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Override
    @Cacheable(value = "kakaoAccessToken", key = "#code")
    public String getKakaoAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoUserRequestDto> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, KakaoUserRequestDto.class);

        return response.getBody().getAccessToken();
    }

    @Override
    @Cacheable(value = "kakaoUserInfo", key = "#accessToken")
    public KakaoUserResponseDto getKakaoUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<KakaoUserRequestDto> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, KakaoUserRequestDto.class);

        KakaoUserRequestDto kakaoUserRequestDto = response.getBody();
        KakaoUserResponseDto kakaoUserResponseDto = modelMapper.map(kakaoUserRequestDto, KakaoUserResponseDto.class);
        return kakaoUserResponseDto;
    }

    @Override
    public User createKakaoUser(KakaoUserResponseDto kakaoUserResponseDto) {
        User user = userRepository.findByEmail(kakaoUserResponseDto.getEmail()).orElse(null);

        if (user == null) {
            ProfileImage profileImage = new ProfileImage();
            profileImage.setImgUrl(kakaoUserResponseDto.getProfileImageUrl());

            user = User.builder()
                    .email(kakaoUserResponseDto.getEmail())
                    .userName(kakaoUserResponseDto.getNickname())
                    .profileImage(profileImage)
                    .provider(Provider.KAKAO)
                    .build();

            userRepository.save(user);
        }

        return user;
    }

    @Override
    public String createJwtToken(User user) {
        return jwtTokenProvider.createToken(user.getEmail());
    }

}
