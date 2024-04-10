package com.moodmate.moodmatebe.global.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * 쿠키 생성 프로토콜
     * @param cookieName
     * @param cookieValue
     * @param response
     */
    public static void createCookie(String cookieName, String cookieValue, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        //쿠키 속성 설정
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(1000 * 60 * 60 * 24 * 7); // 7일
        response.addCookie(cookie);
    }

    /**
     * 쿠키 정보 가져오기
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 쿠키 삭제
     * @param cookieName
     * @param response
     */
    public static void deleteCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0); // 쿠키 만료 시간을 0으로 설정하여 삭제
        cookie.setPath("/"); // 쿠키의 유효 경로를 설정
        response.addCookie(cookie);
    }
}
