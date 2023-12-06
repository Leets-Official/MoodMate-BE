package com.moodmate.moodmatebe.global.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;
import com.moodmate.moodmatebe.global.jwt.AuthRole;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import com.moodmate.moodmatebe.global.oauth.domain.OAuthDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${social-login.redirect}")
    private String redirectUrl;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("CustomAuthenticationSuccessHandler - onAuthenticationSuccess method is called");

        try {
            OAuthDetails oAuthDetails = (OAuthDetails) authentication.getPrincipal();
            Long id = oAuthDetails.getId();
            String email = oAuthDetails.getEmail();
            String authority = ((SimpleGrantedAuthority) oAuthDetails.getAuthorities().toArray()[0]).getAuthority();
            AuthRole role = AuthRole.valueOf(authority);

            String accessToken = jwtProvider.generateToken(id, email, role, false);
            String refreshToken = jwtProvider.generateToken(id, email, role, true);

            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
            accessTokenCookie.setHttpOnly(false);
            accessTokenCookie.setSecure(false);
            accessTokenCookie.setMaxAge(60 * 60 * 2);
            accessTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(false);
            refreshTokenCookie.setSecure(false);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(60 * 60 * 24 * 14);
            response.addCookie(refreshTokenCookie);

//            String redirectUrlWithToken = buildRedirectUrlWithToken(redirectUrl, accessToken);
//            getRedirectStrategy().sendRedirect(request, response, redirectUrlWithToken);

            response.setHeader("accessToken", accessToken);
            response.setHeader("refreshToken", refreshToken);

//            Map<String, String> tokenMap = new HashMap<>();
//            tokenMap.put("accessToken", accessToken);
//            tokenMap.put("refreshToken", refreshToken);
//
//            String tokenJson = objectMapper.writeValueAsString(tokenMap);
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(tokenJson);

//            getRedirectStrategy().sendRedirect(request, response, redirectUrl + "?accessToekn=" + accessToken + "?refreshToekn=" + refreshToken);

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);
            System.out.println("CustomAuthenticationSuccessHandler - Successfully completed the onAuthenticationSuccess method");
        } catch (Exception e) {
            System.out.println("Exception in loadUser: " + e.getMessage());
            e.printStackTrace();

            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String buildRedirectUrlWithToken(String redirectUrl, String accessToken) throws UnsupportedEncodingException {
        // Add the access token as a query parameter to the redirect URL
        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("accessToken", URLEncoder.encode(accessToken, StandardCharsets.UTF_8.toString()))
                .build().toUriString();
    }
}