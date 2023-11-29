package com.moodmate.moodmatebe.global.jwt;

import com.moodmate.moodmatebe.domain.user.exception.InvalidInputValueException;
import com.moodmate.moodmatebe.global.jwt.exception.ExpiredTokenException;
import com.moodmate.moodmatebe.global.jwt.exception.InvalidTokenException;
import com.moodmate.moodmatebe.global.oauth.domain.OAuthDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.access_secret}")
    private String accessSecret;

    @Value("${jwt.refresh_secret}")
    private String refreshSecret;

    public String generateToken(Long id, String email, AuthRole role, boolean isRefreshToken) {
        Instant accessDate = LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant();
        Instant refreshDate = LocalDateTime.now().plusDays(14).atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .claim("role", role.getRole())
                .claim("id", id)
                .claim("email", email)
                .setSubject(id.toString())
                .setExpiration(isRefreshToken ? Date.from(refreshDate) : Date.from(accessDate))
                .signWith(SignatureAlgorithm.HS256, isRefreshToken ? refreshSecret : accessSecret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token, false);
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get("role").toString()));
        OAuthDetails oAuthDetails = new OAuthDetails(Long.getLong(claims.get("id").toString()), claims.getSubject());
        return new UsernamePasswordAuthenticationToken(oAuthDetails, null, authorities);
    }

    public void validateToken(String token, boolean isRefreshToken) {
        try {
            parseClaims(token, isRefreshToken);
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        }
    }

    public Claims parseClaims(String accessToken, boolean isRefreshToken) {
        try {
            JwtParser parser = Jwts.parser().setSigningKey(isRefreshToken ? refreshSecret : accessSecret);
            return parser.parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(accessSecret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public String getTokenFromAuthorizationHeader(String authorizationHeader){
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidInputValueException();
        }

        return authorizationHeader.substring(7);
    }
}