package com.pifrans.auth.controllers;

import com.pifrans.auth.constants.HeadersKeys;
import com.pifrans.auth.constants.HeadersValues;
import com.pifrans.auth.models.User;
import com.pifrans.auth.securities.TokenJWTSecurity;
import com.pifrans.auth.securities.UserDetailsSecurity;
import com.pifrans.auth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenJWTSecurity tokenJWTSecurity;
    private final UserService userService;


    public AuthController(TokenJWTSecurity tokenJWTSecurity, UserService userService) {
        this.tokenJWTSecurity = tokenJWTSecurity;
        this.userService = userService;
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserDetailsSecurity userDetailsSecurity = userService.userLogged();

            if (userDetailsSecurity != null) {
                String token = tokenJWTSecurity.generateToken(userDetailsSecurity.getUsername());
                Long id = userDetailsSecurity.getId();
                User user = userService.updateToken(token, id);

                response.addHeader(HeadersKeys.X_AUTHORIZATION.getDescription(), HeadersValues.BEARER.getDescription() + user.getToken());
                return ResponseEntity.noContent().build();
            }
            throw new RuntimeException("UserDetailsSecurity NULL em: " + request.getRequestURI());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
