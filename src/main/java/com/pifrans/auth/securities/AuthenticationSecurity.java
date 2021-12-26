package com.pifrans.auth.securities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pifrans.auth.constants.HeadersKeys;
import com.pifrans.auth.constants.HeadersValues;
import com.pifrans.auth.dtos.users.UserCredentialDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

public class AuthenticationSecurity extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOG = Logger.getLogger(AuthenticationSecurity.class.getName());
    private final AuthenticationManager authenticationManager;
    private final TokenJWTSecurity jwtSecurity;

    public AuthenticationSecurity(AuthenticationManager authenticationManager, TokenJWTSecurity jwtSecurity) {
        this.authenticationManager = authenticationManager;
        this.jwtSecurity = jwtSecurity;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserCredentialDTO credenciaisDTO = new ObjectMapper().readValue(request.getInputStream(), UserCredentialDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credenciaisDTO.getEmail(), credenciaisDTO.getPassword(), new ArrayList<>());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = ((UserDetailsSecurity) authResult.getPrincipal()).getUsername();
        String token = jwtSecurity.generateToken(username);
        response.addHeader(HeadersKeys.X_AUTHORIZATION.getDescription(), HeadersValues.BEARER.getDescription() + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        LOG.severe(String.format("Falha na autenticação: %s", failed.getMessage()));
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().append(json());
    }

    private String json() {
        long date = new Date().getTime();
        return "{\"timestamp\": " + date + ", " + "\"status\": 401, " + "\"error\": \"Não autorizado\", " + "\"message\": \"Email ou senha inválidos\", " + "\"path\": \"/login\"}";
    }
}
