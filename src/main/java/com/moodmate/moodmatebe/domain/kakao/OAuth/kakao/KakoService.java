package com.moodmate.moodmatebe.domain.kakao.OAuth.kakao;//package semicolon.MeetOn.domain.admin.OAuth.kakao;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.client.RestTemplate;
//import semicolon.MeetOn.domain.admin.domain.Admin;
//import semicolon.MeetOn.domain.admin.dao.AdminRepository;
//
//import java.util.Optional;
//
//import static semicolon.MeetOn.domain.admin.dto.AdminDto.*;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class KakoService {
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String KAKAO_CLIENT_ID;
//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//    private String REDIRECT_URI;
//    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
//    private String TOKEN_URI;
//    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
//    private String USER_INFO_URI;
//    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
//    private String AUTHORIZATION_GRANT_TYPE;
//
//    private final RestTemplate restTemplate;
//
//    public AdminSaveRequestDto getKakoUserInfo(String code) throws JsonProcessingException {
//        String accessToken = getAccessToken(code);
//        return makeAdminSaveRequestDto(accessToken);
//    }
//
//    private String getAccessToken(String code) throws JsonProcessingException {
//        //HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        //headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Accept", "application/json");
//
//        //HTTP Body 생성
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", AUTHORIZATION_GRANT_TYPE);
//        params.add("client_id", KAKAO_CLIENT_ID);
//        params.add("redirect_uri", REDIRECT_URI);
//        params.add("code", code);
//
//        //HTTP(카카오) 요청보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                TOKEN_URI,
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        return jsonNode.get("access_token").asText();
//    }
//
//    private AdminSaveRequestDto makeAdminSaveRequestDto(String accessToken) throws JsonProcessingException {
//        JsonNode jsonNode = getKakaoUserInfo(accessToken);
//        log.info("user_info={}", jsonNode);
//        String kakaoNickname = jsonNode.path("properties").path("nickname").asText();
//        String kakaoEmail = jsonNode.path("kakao_account").path("email").asText();
//        return AdminSaveRequestDto.of(kakaoEmail, kakaoEmail);
//    }
//
//    private JsonNode getKakaoUserInfo(String accessToken) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                USER_INFO_URI,
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readTree(responseBody);
//    }
//}
