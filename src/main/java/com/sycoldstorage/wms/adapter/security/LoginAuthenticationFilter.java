package com.sycoldstorage.wms.adapter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sycoldstorage.wms.adapter.presentation.web.login.LoginDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 로그인 처리 필터
 */
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public LoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login"));
    }

    /**
     * 로그인 시도
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDto loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
            if (StringUtils.isEmpty(loginRequest.getId()) || StringUtils.isEmpty(loginRequest.getPassword())) {
                throw new IllegalArgumentException("Admin ID or Password is empty.");
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword(), new ArrayList<>());
            return super.getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
