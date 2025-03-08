package com.example.naejeonhajab.security;

import com.example.naejeonhajab.common.exception.UserException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.example.naejeonhajab.common.response.enums.UserApiResponse.*;

@Slf4j(topic = "JwtManager")
@Component
public class JwtManager {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateJwt(JwtDto jwtDto) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(jwtDto.getUserId()))
                .claim("email", jwtDto.getEmail())
                .claim("nickname", jwtDto.getNickname())
                .claim("userRole", jwtDto.getUserRole())
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public Claims toClaims(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new UserException(JWT_INVALID,e);
        } catch (ExpiredJwtException e) {
            throw new UserException(JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new UserException(JWT_UNSUPPORTED);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new UserException(JWT_NOT_SIGNATURED);
        }
    }
}