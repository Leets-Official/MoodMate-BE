//package com.moodmate.moodmatebe.global.jwt;
//
//import com.moodmate.moodmatebe.global.jwt.exception.ExpiredTokenException;
//import com.moodmate.moodmatebe.global.jwt.exception.InvalidTokenException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            String token = resolveToken(request);
//            if (token != null) {
//               // jwtTokenProvider.validateToken(token, false);
//                jwtTokenProvider.validateToken(token);
//                Authentication authentication = this.jwtTokenProvider.getAuthentication(token);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                Cookie cookie = new Cookie("jwtToken", token);
//                cookie.setHttpOnly(false);
//                cookie.setMaxAge(60 * 60 * 12); // 쿠키 12시간
//                cookie.setPath("/");
//                response.addCookie(cookie);
//            }
//            filterChain.doFilter(request, response);
//        } catch (ExpiredTokenException e) {
//            throw e;
//        } catch (InvalidTokenException e) {
//            throw e;
//        }
//    }
//
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
//            return bearerToken.split(" ")[1];
//        }
//        return null;
//    }
//}