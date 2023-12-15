package com.enigma.wms_spring.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.wms_spring.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${app.wms_spring.jwt.jwt-secret}")
    private String jwtSecret;
    @Value("${app.wms_spring.jwt.jwt-name}")
    private String appName;
    @Value("${app.wms_spring.jwt.jwtExpirationInSecond}")
    private long jwtExpirationInSecond;

    public String generateToken(AppUser appUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            String token = JWT.create()
                    .withIssuer(appName)
                    .withSubject(appUser.getId())
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationInSecond))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", appUser.getRole().name())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException();
        }
    }

    public boolean verifyJwtToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));//akan di ubah ke jwt byte
            JWTVerifier verifier = JWT.require(algorithm).build();//verivikasi algoritma
            DecodedJWT decodedJWT = verifier.verify(token); //verivikasi token
            return decodedJWT.getIssuer().equals(appName);
        }catch (JWTVerificationException e){
            throw new RuntimeException();
        }
    }

    public Map<String,String> getUserInfoByToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String, String> userinfo = new HashMap<>();//save user id dan role nya apa
            userinfo.put("userId",decodedJWT.getSubject());
            userinfo.put("role",decodedJWT.getClaim("role").asString());
            return userinfo;
        }catch (JWTVerificationException e){
            throw new RuntimeException();
        }
    }
}
