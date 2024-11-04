package com.example.dailyLog.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
public class KakaoUserRequestDto {
    private long id;

    @JsonProperty("connected_at")
    private String connectedAt;

    @JsonProperty("properties")
    private Properties properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    // 액세스 토큰 필드 추가
    private String accessToken;

    @Data
    @NoArgsConstructor
    public static class Properties {
        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("profile_image")
        private String profileImage;
    }

    @Data
    @NoArgsConstructor
    public static class KakaoAccount {
        @JsonProperty("profile")
        private Profile profile;

        @JsonProperty("email")
        private String email;

        @Data
        @NoArgsConstructor
        public static class Profile {
            @JsonProperty("nickname")
            private String nickname;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
}
