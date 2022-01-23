package com.sycoldstorage.wms.security;

import com.sycoldstorage.wms.entity.Admin;
import com.sycoldstorage.wms.service.AdminService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    AdminService adminService;

    @Autowired
    HttpSession httpSession;

    @Autowired
    Environment env;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String id = (String) authentication.getPrincipal();
        Admin admin = adminService.getAdminById(id);

        String token = Jwts.builder()
                .setSubject(admin.getId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.valueOf(env.getProperty("token.access_token.expiration"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        response.setHeader("Authorization", token);
        response.addHeader("adminId", admin.getId());

    }
}
