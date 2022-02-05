package com.sycoldstorage.wms;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;

import java.util.Date;

public class TestUtil {

    public static String createBearerToken(Environment env) {
        String token = Jwts.builder()
                .setSubject("hardline")
                .setExpiration(new Date(System.currentTimeMillis() + Long.valueOf(env.getProperty("token.access_token.expiration"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        return "Bearer " + token;
    }
}
