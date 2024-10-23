//package com.example.dailyLog.conf;
//
//
//import org.apache.catalina.filters.CorsFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:5173");  // 클라이언트 도메인 허용
//        config.addAllowedHeader("*");  // 모든 헤더 허용
//        config.addAllowedMethod("*");  // 모든 HTTP 메서드 허용
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}