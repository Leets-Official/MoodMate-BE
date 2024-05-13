package com.moodmate.moodmatebe.global.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void createCookie(String cookieName, String cookieValue, HttpServletResponse response, int maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        //쿠키 속성 설정
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge); // 만료 시간 설정
        response.addCookie(cookie);
    }

    public static void deleteCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0); // 쿠키 만료 시간을 0으로 설정하여 삭제
        cookie.setPath("/"); // 쿠키의 유효 경로를 설정
        response.addCookie(cookie);
    }
}
