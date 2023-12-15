package com.moodmate.moodmatebe.global.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;
import com.moodmate.moodmatebe.global.jwt.AuthRole;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import com.moodmate.moodmatebe.global.oauth.domain.OAuthDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${social-login.redirect}")
    private String redirectUrl;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            OAuthDetails oAuthDetails = (OAuthDetails) authentication.getPrincipal();
            Long id = oAuthDetails.getId();
            String email = oAuthDetails.getEmail();
            String authority = ((SimpleGrantedAuthority) oAuthDetails.getAuthorities().toArray()[0]).getAuthority();
            AuthRole role = AuthRole.valueOf(authority);

            String accessToken = jwtProvider.generateToken(id, email, role, false);
            String refreshToken = jwtProvider.generateToken(id, email, role, true);

            getRedirectStrategy().sendRedirect(request, response, redirectUrl + "?accessToken=" + accessToken + "&refreshToken=" + refreshToken);

        } catch (Exception e) {
            e.printStackTrace();

            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}